package superapp.dal;

import org.springframework.data.repository.ListCrudRepository;

import superapp.data.MiniAppCommandEntity;
import superapp.data.MiniAppCommandPrimaryKeyId;


public interface MiniAppCommandCrud extends ListCrudRepository<MiniAppCommandEntity, MiniAppCommandPrimaryKeyId>{

	}

