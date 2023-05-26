package superapp.converters;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import superapp.boundaries.object.*;
import superapp.boundaries.user.UserId;
import superapp.data.ObjectEntity;
import superapp.data.ObjectPrimaryKeyId;
import superapp.data.PointOfInterest;

import java.util.HashMap;
import java.util.Map;


@Component
public class OMSObjectConvertor {

	public ObjectBoundary toObjectBoundary(OSMObjectBoundary osm, String category, boolean active, UserId userId) {
		{
			ObjectBoundary boundary = new ObjectBoundary();
			boundary.setType("point");
			boundary.setAlias(osm.getTags().getOrDefault("name", "unknown").toString());
			boundary.setActive(active);

			Location location = new Location(osm.getLat(), osm.getLon());
			boundary.setLocation(location);

			PointOfInterest poi = new PointOfInterest();
			poi.setType(osm.getTags().getOrDefault(category, "unknown").toString());
			poi.setPrivateOrPublic("public");




			CreatedBy cb = new CreatedBy(userId);
			boundary.setCreatedBy(cb);

			boundary.setObjectDetails(osm.getTags());
			boundary.getObjectDetails().put("type", osm.getTags().get(category).toString());
			boundary.getObjectDetails().put("private or public", "public");

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
	



