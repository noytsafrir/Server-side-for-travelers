package superapp.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;

public interface ObjectCrud extends ListCrudRepository<ObjectEntity, ObjectPrimaryKeyId>,
		PagingAndSortingRepository<ObjectEntity, ObjectPrimaryKeyId> {
	
	public List<ObjectEntity> findAllByActive(
			@Param("active") boolean active,
			Pageable pageable);
	
    public List<ObjectEntity> findParentsByObjectId(
            @Param("objectId") ObjectPrimaryKeyId objectId,
            Pageable pageable);
	
    public List<ObjectEntity> findChildrenByObjectId(
            @Param("objectId") ObjectPrimaryKeyId objectId,
            Pageable pageable);
    
    public List<ObjectEntity> findByObjectIdAndParents_Active(
            @Param("objectId") ObjectPrimaryKeyId objectId, 
            @Param("active") boolean active,
            Pageable pageable);
    
    public List<ObjectEntity> findByObjectIdAndChildren_Active(
            @Param("objectId") ObjectPrimaryKeyId objectId, 
            @Param("active") boolean active,
            Pageable pageable);
    
    //findAllByParentsObjects_objectId (many to many--> so have to be findAll)
}
