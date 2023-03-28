package superapp.converters;

import org.springframework.stereotype.Component;

import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserId;
import superapp.data.UserEntity;


@Component
public class UserConverter {
	public UserBoundary toBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();
		boundary.setUserId(new UserId(entity.getSuperapp(),entity.getEmail()));
		boundary.setRole(entity.getRole());
		boundary.setUsername(entity.getUsername());
		boundary.setAvatar(entity.getAvatar());
		return boundary;
	}
	
	public UserEntity toEntity(UserBoundary boundary) {
		UserEntity entity = new UserEntity();
		entity.setEmail(boundary.getUserId().getEmail());
		entity.setSuperapp(boundary.getUserId().getSuperapp());
		entity.setRole(boundary.getRole());
		entity.setUsername(boundary.getUsername());
		entity.setAvatar(boundary.getAvatar());
		return entity;
	}
}
