package com.gray.Entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;
	
	@Column(name = "Post_Title", nullable = false)
	private String title;
	 
	@Column(name = "Post_Content",nullable = false,length = 100000)
	private String content;
	
	@Column(name = "PostImage",nullable = false)
	private String imageName;
	
	@Column(name="Date",nullable = false)
	private Date addedDate;
	
	@ManyToOne
	@JoinColumn(nullable = false,name = "CategorieId")
	private Category category;
	
	@ManyToOne
	@JoinColumn(nullable = false,name = "UserId")
	private User user; 
	
	@OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
	private Set<Comment> comments=new HashSet<>();
}
