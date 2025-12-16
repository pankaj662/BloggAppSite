package com.gray.Payloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostResponce {

	private List<PostDto> content;
	
	private int pageNumber;
	
	private int pageSize;
	
	private int curentPagePost;
	
	private long totalPosts;
	
	private long totalPage;
	
	private boolean lastPage;
	
	
}
