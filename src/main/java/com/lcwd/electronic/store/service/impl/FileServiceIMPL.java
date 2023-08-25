package com.lcwd.electronic.store.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.service.FileService;

@Service
public class FileServiceIMPL implements FileService {
 
	private Logger logger=LoggerFactory.getLogger(FileServiceIMPL.class);
	
	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		
		String originalFileName = file.getOriginalFilename();
		logger.info("fileName: {} "+originalFileName);
		String fileName = UUID.randomUUID().toString();
		String extension=originalFileName.substring(originalFileName.lastIndexOf("."));
		String fileNameWithExtension=fileName+extension;
		String fullPathwithFileName=path+File.separator+fileNameWithExtension;
		
		if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
			
			File folder= new File(path);
			if(!folder.exists()) {
			  folder.mkdirs();
			}
			
			Files.copy(file.getInputStream(), Paths.get(fullPathwithFileName));
			
			return fileNameWithExtension;
		}else {
			throw new BadApiRequest("File With this"+extension+" Not allowed");
		}
		
	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		String fullpath=path+File.separator+name;
		InputStream inputStream= new FileInputStream(fullpath);
		
		return inputStream;
	}

}
