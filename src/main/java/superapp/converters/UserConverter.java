package superapp.converters;

import org.springframework.stereotype.Component;

import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.data.UserEntity;
import superapp.data.UserPrimaryKeyId;


@Component //create an instance from this class 
public class UserConverter {
	public UserBoundary toBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();
	
		UserId userId = new UserId();
		userId.setSuperapp(entity.getUserId().getSuperapp());
		userId.setEmail(entity.getUserId().getEmail());
		
		boundary.setUserId(userId);
		boundary.setRole(entity.getRole());
		boundary.setUsername(entity.getUsername());
		boundary.setAvatar(entity.getAvatar());
		return boundary;
	}
	
	public UserEntity toEntity(UserBoundary boundary) {
		UserEntity entity = new UserEntity();

		UserPrimaryKeyId userPrimaryKeyId = new UserPrimaryKeyId();
		userPrimaryKeyId.setSuperapp(boundary.getUserId().getSuperapp());
		userPrimaryKeyId.setEmail(boundary.getUserId().getEmail());
		
		entity.setUserId(userPrimaryKeyId);
		entity.setRole(boundary.getRole());
		entity.setUsername(boundary.getUsername());
		entity.setAvatar(boundary.getAvatar());
		return entity;
	}
}
