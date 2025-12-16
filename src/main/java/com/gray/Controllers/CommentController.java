package com.gray.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gray.Config.SenitizerUtils;
import com.gray.Payloads.ApiResponse;
import com.gray.Payloads.CommentDto;
import com.gray.Services.CommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/veri-fied/blogg-app/user")
@Tag(name = "Comment Manegment",description = "This apis use manage comments")
public class CommentController
{

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private SenitizerUtils senitize;
	
	@PostMapping("/Comment/post/{postId}/user/{userId}")
	@PreAuthorize("hasAnyAuthority('ADMIN_CREATE','USER_CREATE','MANEGER_CREATE')")
	public ResponseEntity<CommentDto> addCommentInPostWithUser(@Valid
			                                                   @RequestBody CommentDto commentDto,
			                                                   @PathVariable Integer postId,
	                                                           @PathVariable Integer userId){
		commentDto.setContent(senitize.clean(commentDto.getContent()));
		CommentDto dto=this.commentService.addComment(commentDto, postId, userId);
		return new ResponseEntity<CommentDto>(dto,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN_DELETE','USER_DELETE','MANEGER_DELETE')")
	@DeleteMapping("/CommentDelete/CommentId/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId)
	{
		this.commentService.deleteComment(commentId);
		ApiResponse apiResponse=new ApiResponse("Comment deleted",true);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
}
