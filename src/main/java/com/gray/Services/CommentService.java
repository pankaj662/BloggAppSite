package com.gray.Services;

import com.gray.Payloads.CommentDto;

public interface CommentService {

	 CommentDto addComment(CommentDto commentDto,Integer postId,Integer userId); 
	 
	 void deleteComment(Integer commentId);
}
