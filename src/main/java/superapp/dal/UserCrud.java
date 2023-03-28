package superapp.dal;

import org.springframework.data.repository.CrudRepository;

import superapp.data.UserEntity;

public interface UserCrud extends CrudRepository<UserEntity, String>{
	//TODO: check what should we do if we have a primary key that contain 2 variables

}
