package com.gray.Services.Impl;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gray.Entity.User;
import com.gray.Enum.Role;
import com.gray.Exceptions.ResourcesNotFoundException;
import com.gray.Exceptions.ResourcesNotFoundExceptionwithString;
import com.gray.Repositories.UserRepositorie;
import com.gray.Services.BlockSystemService;

@Service
public class BlockedSystemServiceImpl implements BlockSystemService{

	@Autowired
	private UserRepositorie repositorie;
	
	@Override
	public void blockedUserTemporary(Integer userid, Duration duration) {
		User user=this.repositorie.findById(userid).orElseThrow(()->
		                      new ResourcesNotFoundException("User", "userId", userid));
		user.setTeamporaryBlock(LocalDateTime.now().plus(duration));
		user.setJwtTokenVersion(1.10);
		repositorie.save(user);
		
	}

	@Override
	public void blockedUserParmanently(Integer userId) {
		User user=this.repositorie.findById(userId).orElseThrow(
				()-> new ResourcesNotFoundException("User", "userId",userId));
		user.setParmanentBlock(true);
		user.setJwtTokenVersion(1.10);
		repositorie.save(user);
		
	}

	@Override
	public void unblockUserManualy(Integer userId) {
		User user=this.repositorie.findById(userId).orElseThrow(
				()-> new ResourcesNotFoundException("User", "userId",userId));
		
		user.setTeamporaryBlock(null);
		user.setParmanentBlock(false);
		user.setActive(true);
		user.setJwtTokenVersion(1.09);
		repositorie.save(user);
		
	}

	@Override
	public boolean isUserBlocked(User user) {
		if(user.isParmanentBlock())
		{
			return true;
		}
		else if(user.getTeamporaryBlock()!=null&&LocalDateTime.now().isBefore(user.getTeamporaryBlock()))
		{
		return true;
		}
		
		return false;
	}

	@Override
	public void userProfileDeactive(Integer id) {
		
		User user=this.repositorie.findById(id).orElseThrow(
				()-> new ResourcesNotFoundException("User", "UserId", id)
				);
		user.setActive(false);
		user.setJwtTokenVersion(1.10);
		repositorie.save(user);
	}

	@Override
	public void activeUserProfile(Integer id) {
		User user=this.repositorie.findById(id).orElseThrow(
				()->new ResourcesNotFoundException("User", "userId", id)
				);
		user.setActive(true);
		user.setJwtTokenVersion(1.09);
		repositorie.save(user);
	}

	@Override
	public void allUserProfileDeActive(Role role) {
		this.repositorie.allUserProfileUpdate(false, role);
	}

	@Override
	public void allUserProfileActive(Role role) {
		this.repositorie.allUserProfileUpdate(true,role );
	}
}