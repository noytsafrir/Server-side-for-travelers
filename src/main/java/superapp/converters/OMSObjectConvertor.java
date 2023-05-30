package superapp.converters;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import superapp.boundaries.object.*;
import superapp.boundaries.user.UserId;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;
import superapp.data.PointOfInterest;
import superapp.logic.init.DataInitializerOSM;

import java.util.HashMap;
import java.util.Map;


@Component
public class OMSObjectConvertor {

	private ObjectMapper objectMapper;

	private String typeOSM;
	private String detailsOSM;

	private String defaultImage;

	private Log logger = LogFactory.getLog(OMSObjectConvertor.class);

	@PostConstruct
	public void init() {
		this.objectMapper = new ObjectMapper();
	}

	@Value("${osm.object.type:NA}")
	public void setTypeOSM(String typeOSM) {
		this.typeOSM = typeOSM;
	}

	@Value("${osm.object.details}")
	public void setDetailsOSM(String detailsOSM) {
		this.detailsOSM = detailsOSM;
	}

	@Value("${osm.object.defaultImage}")
	public void setDefaultImage(String defaultImage) {
		this.defaultImage = defaultImage;
	}

	public ObjectBoundary toObjectBoundary(OSMObjectBoundary osm, String category, boolean active, UserId userId) {
		{
			ObjectBoundary boundary = new ObjectBoundary();

			boundary.setType(this.typeOSM);
			boundary.setAlias(osm.getTags().getOrDefault("name", "unknown").toString());
			boundary.setActive(active);
			boundary.setCreatedBy(new CreatedBy(userId));
			boundary.setLocation(new Location(osm.getLat(), osm.getLon()));

			PointOfInterest poi = new PointOfInterest();
			poi.setType(osm.getTags().getOrDefault(category, "NA").toString());
			poi.setDescription(osm.getTags().getOrDefault("description", "NA").toString());
			poi.setImage(osm.getTags().getOrDefault("image", defaultImage).toString());

			Map<String, Object> poiMap = objectMapper.convertValue(poi, Map.class);
			boundary.setObjectDetails(new HashMap<>(poiMap));
			boundary.getObjectDetails().put(this.detailsOSM, osm.getTags());

			logger.trace("ObjectBoundary: " + boundary.toString());
			return boundary;
		}

	}
	public Map<String, Object> jsonToMap (String str) {
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();

		try {
			map = (HashMap<String, Object>) mapper.readValue(str, Map.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return map;
	}
}
	



