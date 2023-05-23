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

	List<ObjectEntity> findAllByTypeAndActiveTrue(
			@Param("type") String type,
			Pageable pageable);

	List<ObjectEntity> findAllByAlias(
			@Param("alias") String alias,
			Pageable pageable);

	List<ObjectEntity> findAllByAliasAndActiveTrue(
			String alias,
			Pageable pageable);

	List<ObjectEntity> findAllByLatBetweenAndLngBetween(
			@Param("latMin") Double latMin,
			@Param("latMax") Double latMax,
			@Param("lngMin") Double lngMin,
			@Param("lngMax") Double lngMax,
			Pageable pageable);

	List<ObjectEntity> findAllByLatBetweenAndLngBetweenAndActive(
			@Param("latMin") Double latMin,
			@Param("latMax") Double latMax,
			@Param("lngMin") Double lngMin,
			@Param("lngMax") Double lngMax,
			@Param("active") boolean active,
			Pageable pageable);
}
