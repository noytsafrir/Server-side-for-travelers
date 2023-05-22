package superapp.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import superapp.data.MiniAppCommandEntity;
import superapp.data.MiniAppCommandPrimaryKeyId;
import superapp.data.ObjectEntity;

public interface MiniAppCommandCrud extends ListCrudRepository<MiniAppCommandEntity, MiniAppCommandPrimaryKeyId>,
		PagingAndSortingRepository<MiniAppCommandEntity, MiniAppCommandPrimaryKeyId> {
	
	public List<MiniAppCommandEntity> findAllByCommandID_Miniapp(
			@Param("miniapp") String miniapp,
			Pageable pageable);

}
