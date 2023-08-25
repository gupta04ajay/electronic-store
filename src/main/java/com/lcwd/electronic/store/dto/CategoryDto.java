package com.lcwd.electronic.store.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
  
	private String categoryId;
	
	@NotBlank
	
	private String title;
	
	@NotBlank(message = " description required")
	private String description;
	
	@NotBlank
	private String coverImage;
}
