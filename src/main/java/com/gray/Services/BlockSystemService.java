package com.gray.Services;

import java.time.Duration;

import com.gray.Entity.User;
import com.gray.Enum.Role;

public interface BlockSystemService {

	void blockedUserTemporary(Integer userid,Duration duration);
	
	void blockedUserParmanently(Integer userId);
	
	void unblockUserManualy(Integer userId);
	
	boolean isUserBlocked(User user);
	
	void userProfileDeactive(Integer id);
	
	void activeUserProfile(Integer id);
	
	void allUserProfileDeActive(Role role);
	
	void allUserProfileActive(Role role);
}
