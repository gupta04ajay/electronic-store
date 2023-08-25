package com.lcwd.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dto.ApiResponceMessage;
import com.lcwd.electronic.store.dto.ImageResponse;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.ProductDto;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	 @Autowired
	 private FileService fileService;
	 
	 @Value("${product.image.path}")
	 private String imagePath;

	 @PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
		ProductDto productDto = productService.create(dto);
		return new ResponseEntity<>(productDto, HttpStatus.CREATED);
	}
    
	 @PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> update(@RequestBody ProductDto dto, @PathVariable("productId") String productId) {
		ProductDto productDto = productService.update(dto, productId);
		return new ResponseEntity<>(productDto, HttpStatus.CREATED);
	}
    
	 @PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponceMessage> delete(@PathVariable("productId") String productId) throws IOException {
		productService.delete(productId);
		ApiResponceMessage responseMessage = ApiResponceMessage.builder().message("Product is deleted successfully !!")
				.status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
		ProductDto productDto = productService.get(productId);
		return new ResponseEntity<>(productDto, HttpStatus.OK);
	}

	// get all
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAll(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

	) {
		PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}

	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

	) {
		PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}

	// search
	@GetMapping("/search/{query}")
	public ResponseEntity<PageableResponse<ProductDto>> searchProduct(@PathVariable String query,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

	) {
		PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(query, pageNumber, pageSize,
				sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	//upload image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
    ) throws IOException {
        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.get(productId);
        productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName()).message("Product image is successfully uploaded !!").status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    //serve image
    @GetMapping(value = "/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.get(productId);
        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }
}
