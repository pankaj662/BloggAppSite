package com.gray.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gray.Enum.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public  class  User  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "user_name", nullable = false)
	private String name;
	
	@Column(name = "user_email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "user_password", nullable = false)
	private String password;
	
	@Column(name = "user_about", nullable = false)
	private String about;
	
	@Column(name = "profile_image")
	private String image;
	
	@CreatedDate
	@Column(name = "created_at",nullable = false,updatable = false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(name = "updated_at",nullable = false)
	private LocalDateTime updatedAt;
	
	@Column(name = "Last_Login")
	private LocalDateTime lastLogin;
	
	@CreatedBy
	@Column(name="Created_By",nullable = false)
	private String createdBy;
	
	@LastModifiedBy
	@Column(name = "Update_By",nullable = false)
	private String updatedBy;
	
	@Column(name="User_Profile",nullable = false)
	private boolean active=true;
	
	@Column(name="Temporary_Block")
	private LocalDateTime teamporaryBlock;
	
	@Column(name="Parmanent_Block",nullable = false)
	private boolean parmanentBlock=false;
	
	@Column(name = "JwtToken_Version",nullable=false)
	private double jwtTokenVersion=1.09;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Post> posts = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<>();

//	@JsonManagedReference
//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
//	private Set<Role> roles = new HashSet<>();

	@Enumerated(EnumType.STRING)
	private Role roles;
	
	//@JsonManagedReference
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		
//		return roles.getAuthorities();
//	}
//
//	@Override
//	public String getPassword() {
//		return this.password;
//	}
//
//	@Override
//	public String getUsername() {
//		return this.email;
//	}
//	
//	public double getJwtTokenVersion() {
//		return this.jwtTokenVersion;
//	}
}
