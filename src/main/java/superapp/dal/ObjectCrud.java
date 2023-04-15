package superapp.dal;

import org.springframework.data.repository.ListCrudRepository;

import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;

public interface ObjectCrud extends ListCrudRepository<ObjectEntity, ObjectPrimaryKeyId>{

}
