package superapp.logic.actualServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import superapp.boundaries.object.OSMObjectBoundary;
import superapp.boundaries.object.ObjectBoundary;
import superapp.converters.OMSObjectConvertor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenStreetMapService implements CommandLineRunner {
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    private OMSObjectConvertor convertor;

    private String baseUrl = "https://www.overpass-api.de/api/interpreter";

    public OpenStreetMapService() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
    }

    @Autowired
    public void setConvertor(OMSObjectConvertor convertor) {
        this.convertor = convertor;
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
            String response = restTemplate.getForObject(encodedUrl, String.class);

            try {
                JsonNode json = objectMapper.readTree(response);
                json.get("elements").forEach(element -> {
                    OSMObjectBoundary osmObject = null;
                    try {
                        rv.add(objectMapper.readValue(element.toString(), OSMObjectBoundary.class));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return rv;
    }



    @Override
    public void run(String... args) throws Exception {
        List<OSMObjectBoundary> osmObjects = new ArrayList<>();
        List<ObjectBoundary> boundaries = new ArrayList<>();

        System.err.println("openstreetmap start");
        osmObjects = retrievePOIs(32.109333, 34.855499, 5000, "tourism", "viewpoint");



        System.err.println("openstreetmap end");
    }

}
