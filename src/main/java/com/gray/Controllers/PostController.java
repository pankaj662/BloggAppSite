package com.gray.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;

import com.gray.Config.AppConstants;
import com.gray.Exceptions.EmptyException;
import com.gray.Extra.AdvanceValidation;
import com.gray.Extra.BasicValidation;
import com.gray.Extra.NextLevleValidation;
import com.gray.Payloads.ApiResponse;
import com.gray.Payloads.PostDto;
import com.gray.Payloads.PostResponce;
import com.gray.Services.PostService;
import com.gray.Services.Impl.FileUplodeStstemClass;

import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@Tag(name = "Post Managment",description = "This apis using maneging post")
@RequestMapping("/api/v1/user")
public class PostController {
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileUplodeStstemClass uplodeSystem;

	@Value("${project.image}")
	private String path;
	
	@PreAuthorize("hasAnyAuthority('ADMIN_CREATE','MANEGER_CREATE','USER_CREATE')")
	@PostMapping("/addPost/user/{userId}/categorie/{categorieId}")
	public ResponseEntity<PostDto> addPost(
			@Validated({BasicValidation.class,BasicValidation.class, AdvanceValidation.class,
					NextLevleValidation.class })
			@PathVariable Integer userId, @PathVariable Integer categorieId,
			@RequestParam("image") MultipartFile[] files,
			@RequestParam("title") String title,
			@RequestParam("content") String content) 
	        throws Exception {
		   
		PostDto postDto=new PostDto();
		postDto.setTitle(title);
		postDto.setContent(content);
		PostDto post = this.postService.addPost(postDto, userId, categorieId,path,files);
		return new ResponseEntity<PostDto>(post, HttpStatus.CREATED);

	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ','USER_READ')")
	@GetMapping("/getPost/byCategorie/{categorieId}")
	public ResponseEntity<List<PostDto>> getPostByCategorie(@PathVariable Integer categorieId)
	{
		List<PostDto> list=this.postService.getAllPostCategorie(categorieId);
		if(list.isEmpty())
		{
			throw new EmptyException("Categorie is empty");
		}
		return new ResponseEntity<List<PostDto>>(list,HttpStatus.FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ')")
	@GetMapping("/getPost/byUser/{userId}")
	public ResponseEntity<PostResponce> getPostByUser(@PathVariable Integer userId,
			                                           @RequestParam(value = "pageNumber",defaultValue =AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
			                                           @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
			                                           @RequestParam(value = "ancOrder",defaultValue = AppConstants.PAGE_DIR,required = false) String dsnOrder,
			                                           @RequestParam(value = "sortDir",defaultValue = AppConstants.PAGE_SHORT,required = false) String sortDirectry )
	{
		PostResponce responce=this.postService.getAllPostUser(userId,pageNumber,pageSize,dsnOrder,sortDirectry);
		return new ResponseEntity<PostResponce>(responce,HttpStatus.FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ','USER_READ')")
	@GetMapping("/getPosts/allPosts")
	@Cacheable(value = "posts")
	public ResponseEntity<PostResponce> getAllPosts(@RequestParam(value = "pageNumber",defaultValue =AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
			                                        @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
			                                        @RequestParam(value = "pageSortA",defaultValue = AppConstants.PAGE_SHORT,required = false) String pageSortA,
	                                                @RequestParam(value = "pageSortDir",defaultValue =AppConstants.PAGE_DIR,required = false) String pageSortDir)
	{
		PostResponce list=this.postService.getAllPost(pageNumber,pageSize,pageSortA,pageSortDir);
		return new ResponseEntity<PostResponce>(list,HttpStatus.FOUND);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_RAED')")
	@GetMapping("/getPost/byId/{postId}")
	@Cacheable(value = "post",key = "#postId")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId)
	{
		PostDto postDto=this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN_UPDATE','MANEGER_UPDATE','USER_UPDATE')")
	@PutMapping("/updatePost/byId/{postId}")
	public ResponseEntity<PostDto> updatePost(@Validated({BasicValidation.class,AdvanceValidation.class,NextLevleValidation.class})
	                                          @RequestBody PostDto postDto,
	                                          @PathVariable Integer postId)
	{
		PostDto postDto2=this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(postDto2,HttpStatus.UPGRADE_REQUIRED);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN_DELETE','MANEGER_DELETE','USER_DELETE')")
	@DeleteMapping("/deletePost/byId/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId)
	{
		this.postService.deletePost(postId);
		ApiResponse apiResponse=new ApiResponse("Post delete succesfuly", true);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ','USER_READ')")
	@GetMapping("/getPost/byTitleName/{keywored}")
	public ResponseEntity<List<PostDto>> getPostByTitleName(@PathVariable String keywored)
	{
		List<PostDto> list=this.postService.getAllPostSearch(keywored);
		if(list.isEmpty()){throw new EmptyException("This title not available");}
		return new ResponseEntity<List<PostDto>>(list,HttpStatus.FOUND);
	}
	
//	@PreAuthorize("hasRole('ADMIN')('USER')")
//	@PostMapping("/addPhoto/{postId}")
//	public ResponseEntity<ApiResponse> addPhotoToPost(@PathVariable Integer postId,
//			                                          @RequestParam("image") MultipartFile file)
//	                                                   throws Exception 
//	{
//		this.uplodeSystem.uplodeImage(postId, path, file);
//		ApiResponse apiResponse=new ApiResponse("Image Uplode SuccessFuly",true);
//		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
//	}
}
