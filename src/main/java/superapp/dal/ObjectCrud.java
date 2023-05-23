package superapp.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
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

	public List<ObjectEntity> findAllByParents_objectId(
			@Param("objectId") ObjectEntity object,
			Pageable pageable);

	public List<ObjectEntity> findAllByChildren_objectId(
			@Param("objectId") ObjectEntity object,
			Pageable pageable);

	public List<ObjectEntity> findAllByActiveIsTrueAndParents_objectId(
			@Param("objectId") ObjectEntity object,
			Pageable pageable);

	public List<ObjectEntity> findAllByActiveIsTrueAndChildren_objectId(
			@Param("objectId") ObjectEntity object,
			Pageable pageable);

	List<ObjectEntity> findAllByType(
			@Param("type") String type,
			Pageable pageable);

	List<ObjectEntity> findAllByActiveIsTrueAndType(
			@Param("type") String type,
			Pageable pageable);

	List<ObjectEntity> findAllByAlias(
			@Param("alias") String alias,
			Pageable pageable);

	List<ObjectEntity> findAllByActiveIsTrueAndAlias(
			String alias,
			Pageable pageable);

	List<ObjectEntity> findByLocationNear(
			@Param("location") Point location,
			@Param("maxDistance") Distance maxDistance,
			Pageable pageable);
	
	List<ObjectEntity> findByActiveIsTrueAndLocationNear(
			@Param("location") Point location,
			@Param("maxDistance") Distance maxDistance,
			Pageable pageable);
}
