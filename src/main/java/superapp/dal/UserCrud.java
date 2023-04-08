package superapp.dal;

import org.springframework.data.repository.CrudRepository;

import superapp.data.UserEntity;
import superapp.data.UserPrimaryKeyId;

public interface UserCrud extends CrudRepository<UserEntity, UserPrimaryKeyId>{

}
