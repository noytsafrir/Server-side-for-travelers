package superapp.logic.actualServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import superapp.boundaries.object.OSMObjectBoundary;
import superapp.boundaries.object.ObjectBoundary;
import superapp.boundaries.user.UserId;
import superapp.converters.OMSObjectConvertor;
import superapp.logic.ObjectServiceWithPagination;
import superapp.logic.OpenStreetMapService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenStreetMapServiceDB implements OpenStreetMapService {
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private OMSObjectConvertor convertor;

    private ObjectServiceWithPagination objectService;

    private Log logger = LogFactory.getLog(MiniAppCommandServiceDB.class);

    private String baseUrl;

    public OpenStreetMapServiceDB() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
    }

    @Autowired
    public void setConvertor(OMSObjectConvertor convertor) {
        this.convertor = convertor;
    }
    @Autowired
    public void setObjectService(ObjectServiceWithPagination objectService) {
        this.objectService = objectService;
    }

    @Value("${osm.api.url}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void createPOIsFromOSM(double latitude, double longitude, double radiusInMeters, String category, String type, UserId userId) {
        List<ObjectBoundary> objectBoundaries = retrievePOIs(latitude, longitude, radiusInMeters, category, type)
                .stream()
                .map(osmObject -> convertor.toObjectBoundary(osmObject, category, true, userId))
                .toList();

        objectBoundaries.forEach(objectBoundary -> {
            objectService.createObject(objectBoundary);
        });
    }

    public List<OSMObjectBoundary> retrievePOIs(double latitude, double longitude, double radiusInMeters, String category, String type) {
        String encodedCategoryValue = null;
        String encodedTypeValue = null;
        ArrayList<OSMObjectBoundary> rv = new ArrayList<>();

        try {
            encodedCategoryValue = URLEncoder.encode(category, StandardCharsets.UTF_8.toString());
            encodedTypeValue = URLEncoder.encode(type, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (encodedCategoryValue != null && encodedTypeValue != null) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://www.overpass-api.de/api/interpreter")
                    .queryParam("data", "[out:json];node[\"" + encodedCategoryValue +"\"=\"" + encodedTypeValue + "\"](around:" + radiusInMeters + "," + latitude + "," + longitude + ");out;");

            String encodedUrl = builder.build().toUriString();
            logger.trace("Start retrieving POIs from OpenStreetMap, URI = " + encodedUrl);
            String response = restTemplate.getForObject(encodedUrl, String.class);

            try {
                JsonNode json = objectMapper.readTree(response);
                json.get("elements").forEach(element -> {
                    OSMObjectBoundary osmObject = null;
                    try {
                        rv.add(objectMapper.readValue(element.toString(), OSMObjectBoundary.class));
                    } catch (JsonProcessingException e) {
                        logger.warn("Failed to parse response from OpenStreetMap: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                logger.warn("Failed to parse response from OpenStreetMap: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        logger.info("Retrieved " + rv.size() + " POIs from OpenStreetMap with tag " + category + "=" + type);
        return rv;
    }
}
