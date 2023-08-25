package com.lcwd.electronic.store.entites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User implements UserDetails {
	
	@Id
	private String userId;
	
	@Column(name = "user_name")
	private String name;
	
	@Column(name="user_email", unique = true)
	private String email;
	
	@Column(name = "user_password")
	private String password;
	
	private String gender;
	
	@Column(length = 1000)
	private String about;
	
	@Column(name="user_image_name")
	private String imageName;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	private List<Order> orders= new ArrayList<>();
	
	 @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	    private Set<Role> roles = new HashSet<>();
	 
	 @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
	    private  Cart cart;

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			Set<SimpleGrantedAuthority> authorities = this.roles.stream()
					.map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
			return authorities;
		}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	

}
