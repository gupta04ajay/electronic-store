package com.lcwd.electronic.store.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.lcwd.electronic.store.entites.Role;
import com.lcwd.electronic.store.validate.ImageNameValid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

	private String userId;

	@Size(min = 3, max = 25,message = "Invalid name !!")
	private String name;
	
	
    //@Email(message = "Invalid Email !!")
	@Pattern(regexp ="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid email")
    @NotBlank
	private String email;
    
    @NotBlank(message = "Password is required")
	private String password;

    @Size(min = 4,max = 6,message = "Invalid gender")
	private String gender;

    @NotBlank(message="About is required")
	private String about;

    @ImageNameValid
	private String imageName;
    
   
    private Set<RoleDto> roles = new HashSet<>();

}
