package com.gray.Services.Impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gray.Entity.Category;
import com.gray.Entity.Post;
import com.gray.Entity.User;
import com.gray.Exceptions.EmptyException;
import com.gray.Exceptions.ResourcesNotFoundException;
import com.gray.Payloads.PostDto;
import com.gray.Payloads.PostResponce;
import com.gray.Repositories.CategorieRepository;
import com.gray.Repositories.PostsRepositorie;
import com.gray.Repositories.UserRepositorie;
import com.gray.Services.PostService;

@Service
public class PostServiceClass implements PostService {

	@Autowired
	private PostsRepositorie postsRepositorie;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepositorie userRepositorie;

	@Autowired
	private CategorieRepository categorieRepository;

	@Autowired
	private FileUplodeStstemClass fileUplodeSystem;
	
	@Override
	public PostResponce getAllPost(Integer pageNumber, Integer pageSize, String pageSortA, String pageSortDir) {

		Sort sort = (pageSortDir.equalsIgnoreCase("dsc")) ? Sort.by(pageSortA).descending()
				: Sort.by(pageSortA).ascending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pageList = this.postsRepositorie.findAll(pageable);

		List<Post> lists = pageList.getContent();
		List<PostDto> listDtos = lists.stream().map(list -> this.postToPostDto(list)).collect(Collectors.toList());
		if (listDtos.isEmpty()) {
			throw new EmptyException("NO Any Post");
		}
		PostResponce postResponce = new PostResponce();
		postResponce.setContent(listDtos);
		postResponce.setPageNumber(pageList.getNumber());
		postResponce.setPageSize(pageList.getSize());
		postResponce.setTotalPosts(pageList.getTotalElements());
		postResponce.setTotalPage(pageList.getTotalPages());
		postResponce.setLastPage(pageList.isLast());
		postResponce.setCurentPagePost(pageList.getNumberOfElements());

		return postResponce;
	}

	@Override
	public List<PostDto> getAllPostCategorie(Integer categorieId) {
		Category category = this.categorieRepository.findById(categorieId)
				.orElseThrow(() -> new ResourcesNotFoundException("Categoie", "CategorieId", categorieId));

		List<Post> lists = this.postsRepositorie.findByCategory(category);
		List<PostDto> listDto = lists.stream().map(list -> this.postToPostDto(list)).collect(Collectors.toList());
		return listDto;
	}

	@Override
	public PostResponce getAllPostUser(Integer userId, Integer pageNumber, Integer pageSize, String dsnOrder,
			String shortDir) {
		User user = this.userRepositorie.findById(userId)
				.orElseThrow(() -> new ResourcesNotFoundException("User", "UserId", userId));

		Sort sort=(dsnOrder.equalsIgnoreCase("dsc"))?Sort.by(shortDir).descending():Sort.by(shortDir).ascending();
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Post> page=this.postsRepositorie.findByUser(pageable,user);
		List<Post> lists = page.getContent();
		List<PostDto> listDtos = lists.stream().map(list -> this.postToPostDto(list)).collect(Collectors.toList());
		if(listDtos.isEmpty())
		{
			throw new EmptyException("This User no any post");
		}
		PostResponce postResponce=new PostResponce();
		postResponce.setContent(listDtos);
		postResponce.setPageSize(page.getSize());
		postResponce.setPageNumber(page.getNumber());
		postResponce.setCurentPagePost(page.getNumberOfElements());
		postResponce.setLastPage(page.isLast());
		postResponce.setTotalPage(page.getTotalPages());
		postResponce.setTotalPosts(page.getTotalElements());
		return postResponce;
	}

	@Override
	public List<PostDto> getAllPostSearch(String keyword) {
		List<Post> posts = this.postsRepositorie.findByTitleContaining(keyword);
		List<PostDto> postDtos = posts.stream().map(post -> this.postToPostDto(post)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public PostDto addPost(PostDto postDto, Integer userId, Integer categorieId, String path, MultipartFile[] files)
			throws Exception {
		User user = this.userRepositorie.findById(userId)
				.orElseThrow(() -> new ResourcesNotFoundException("User", "UserId", userId));

		Category category = this.categorieRepository.findById(categorieId)
				.orElseThrow(() -> new ResourcesNotFoundException("Categorie", "ctaegorieId", categorieId));
	Post post = this.postDtoToPost(postDto);
	post.setImageName(this.fileUplodeSystem.uploadImageInPost(path, files));
	post.setAddedDate(new Date());
	post.setCategory(category);
	post.setUser(user);
	this.postsRepositorie.save(post);
	PostDto postDto2 = this.postToPostDto(post);
	return postDto2;
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer id) {
		Post post = this.postsRepositorie.findById(id)
				.orElseThrow(() -> new ResourcesNotFoundException("Post", "Id", id));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		PostDto postDto2 = this.postToPostDto(post);

		return postDto2;
	}

	@Override
	public void deletePost(Integer id) {
		Post post = this.postsRepositorie.findById(id)
				.orElseThrow(() -> new ResourcesNotFoundException("Post", "Id", id));
		this.postsRepositorie.delete(post);
	}

	@Override
	public PostDto getPostById(Integer id) {
		Post post = this.postsRepositorie.findById(id)
				.orElseThrow(() -> new ResourcesNotFoundException("Post", "Id", id));
		PostDto postDto = this.postToPostDto(post);
		return postDto;
	}

	private Post postDtoToPost(PostDto postDto) {
		Post post = this.modelMapper.map(postDto, Post.class);
		return post;
	}

	private PostDto postToPostDto(Post post) {
		PostDto postDto = this.modelMapper.map(post, PostDto.class);
		return postDto;
	}

}
