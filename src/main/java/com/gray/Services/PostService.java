package com.gray.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gray.Payloads.PostDto;
import com.gray.Payloads.PostResponce;

public interface PostService
{
	//Post Add
 PostDto addPost(PostDto postDto,Integer userId,Integer categorieId,String path,MultipartFile[] files) throws Exception;
  //Post Update
 PostDto updatePost(PostDto postDto,Integer id);
 //Post delete
 void deletePost(Integer id);
 //Post get by id
 PostDto getPostById(Integer id);
 //Post all
 PostResponce getAllPost(Integer pageNumber,Integer pageSize,String pageSortA,String pageSortDir);
 //All categorie post get
 List<PostDto> getAllPostCategorie(Integer categorieId);
 //User all post get
 PostResponce getAllPostUser(Integer userId,Integer pageNumber,Integer pageSize,String dsnOrder,String shortDir);
 //post search system
 List<PostDto> getAllPostSearch(String keyword);
}
