package superapp.dal;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import superapp.data.UserEntity;
import superapp.data.UserPrimaryKeyId;

public interface UserCrud extends ListCrudRepository<UserEntity, UserPrimaryKeyId>, 
								PagingAndSortingRepository<UserEntity, UserPrimaryKeyId>{
}
