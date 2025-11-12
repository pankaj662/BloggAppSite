package com.gray.Services.Impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gray.Entity.Comment;
import com.gray.Entity.Post;
import com.gray.Entity.User;
import com.gray.Exceptions.ResourcesNotFoundException;
import com.gray.Payloads.CommentDto;
import com.gray.Repositories.CommentRepository;
import com.gray.Repositories.PostsRepositorie;
import com.gray.Repositories.UserRepositorie;
import com.gray.Services.CommentService;

@Service
public class CommentServiceClass implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PostsRepositorie postsRepositorie;
	
	@Autowired
	private UserRepositorie userRepositorie;
	@Override
	public CommentDto addComment(CommentDto commentDto,Integer postId,Integer userId) {
		Post post=this.postsRepositorie.findById(postId).orElseThrow(()->
                                          new ResourcesNotFoundException("Post", "PostId", postId));
        User user=this.userRepositorie.findById(userId).orElseThrow(()->
                                           new ResourcesNotFoundException("User", "userId",userId)); 
		Comment comment=this.commentDtoToComment(commentDto);
		comment.setPost(post);
		comment.setUser(user);
		this.commentRepository.save(comment);
		CommentDto commentDto2=this.commentToCommentDto(comment);
		return commentDto2;
	}

	@Override
	public void deleteComment(Integer commentId) {		
		Comment comment=this.commentRepository.findById(commentId).orElseThrow(()->
		                                       new ResourcesNotFoundException("Comment", "CommentId", commentId));
		this.commentRepository.delete(comment);
		
	}
	private Comment commentDtoToComment(CommentDto commentDto) {
		Comment comment=this.mapper.map(commentDto, Comment.class);
		return comment;
	}
	
	private CommentDto commentToCommentDto(Comment comment)
	{
		CommentDto commentDto=this.mapper.map(comment, CommentDto.class);
		return commentDto;
	}

}
