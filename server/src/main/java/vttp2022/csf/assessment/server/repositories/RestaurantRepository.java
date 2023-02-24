package vttp2022.csf.assessment.server.repositories;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.BucketNameUtils;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;

@Repository
public class RestaurantRepository {

	@Autowired
	private AmazonS3 s3;

    @Autowired
    private MongoTemplate template;
	
	@Value("${spaces.bucket}")
	private String spacesBucket;

	@Value("${spaces.endpoint.url}")
	private String spacesEndpointUrl;

	private Logger logger = Logger.getLogger(RestaurantRepository.class.getName());

	// TODO Task 2
	// Use this method to retrive a list of cuisines from the restaurant collection
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	/**
     * 
     * db.getCollection("restaurants").find({})
     */
	public Optional<List<Restaurant>> getCuisines() {
		// Implmementation in here
        System.out.println("HERE");
        
        List<Document> lsDoc = template.find(new Query().limit(100), Document.class, "restaurants");

        if (lsDoc.isEmpty())
            return Optional.empty();

        Document doc = lsDoc.get(0);
        System.out.println(doc.get("address", Document.class));

        List<Restaurant> lsRes = lsDoc.stream()
            .map(d -> {
                LatLng latLng = new LatLng();
                latLng.setLongitude(d.get("address", Document.class).getList("coord", Double.class).get(0).floatValue());
                latLng.setLatitude(d.get("address", Document.class).getList("coord", Double.class).get(1).floatValue());
                
                // System.out.println("long: " + latLng.getLongitude());
                // System.out.println("lat: " + latLng.getLatitude());

                Restaurant r = new Restaurant();
                r.setRestaurantId(d.getString("restaurant_id"));
                r.setName(d.getString("name"));
                r.setCuisine(d.getString("cuisine").replace("/", "_"));
                r.setAddress("%s, %s, %s".formatted(d.get("address", Document.class).getString("building"), d.get("address", Document.class).getString("street"), d.get("address", Document.class).getString("zipcode")));
                r.setCoordinates(latLng);
                // r.setMapURL(null);
                return r;
            })
            .toList();

        return Optional.of(lsRes);
	}

	// TODO Task 3
	// Use this method to retrive a all restaurants for a particular cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method

	//  db.getCollection("restaurants").find({cuisine: 'Bakery'})
	public Optional<List<Restaurant>> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		Criteria c = Criteria.where("cuisine").is(cuisine);
		Query query = Query.query(c);

		List<Document> lsDoc = template.find(query, Document.class, "restaurants");

		if (lsDoc.isEmpty())
            return Optional.empty();

		List<Restaurant> lsRes = lsDoc.stream()
            .map(d -> {
                LatLng latLng = new LatLng();
                latLng.setLongitude(d.get("address", Document.class).getList("coord", Double.class).get(0).floatValue());
                latLng.setLatitude(d.get("address", Document.class).getList("coord", Double.class).get(1).floatValue());
                
                // System.out.println("long: " + latLng.getLongitude());
                // System.out.println("lat: " + latLng.getLatitude());

                Restaurant r = new Restaurant();
                r.setRestaurantId(d.getString("restaurant_id"));
                r.setName(d.getString("name"));
                r.setCuisine(d.getString("cuisine").replace("/", "_"));
                r.setAddress("%s, %s, %s".formatted(d.get("address", Document.class).getString("building"), d.get("address", Document.class).getString("street"), d.get("address", Document.class).getString("zipcode")));
                r.setCoordinates(latLng);
                // r.setMapURL(null);
                return r;
            })
            .toList();

			return Optional.of(lsRes);
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method

	//  db.getCollection("restaurants").find({name: 'Aliotta Bake Shop'})
	public Optional<Restaurant> getRestaurant(String restaurantName) {
		// Implmementation in here
		
		MatchOperation matchOp = Aggregation.match(Criteria.where("name").is(restaurantName));
		ProjectionOperation proj = Aggregation
					.project("restaurant_id", "name", "cuisine")
					.and("address.coord").as("coordinates")
					.and(StringOperators.Concat.valueOf("address.building").concat(", ").concatValueOf("address.street").concat(", ").concatValueOf("address.zipcode").concat(", ").concatValueOf("borough")).as("address")
					.andExclude("_id");
		Aggregation pipeline = Aggregation.newAggregation(matchOp, proj);
		//AggregationResults<Document> res =  template.aggregate(pipeline, "???", Document.class);

		Criteria c = Criteria.where("name").is(restaurantName);
		Query query = Query.query(c);

		Document doc = template.findOne(query, Document.class, "restaurants");

		if (doc.isEmpty())
			return Optional.empty();

		Document d = new Document();
		d.put("restaurant_id", doc.getString("restaurant_id"));
		d.put("name", doc.getString("name"));
		d.put("cuisine", doc.getString("cuisine").replace("/", "_"));
		d.put("address", "%s, %s, %s".formatted(doc.get("address", Document.class).getString("building"), doc.get("address", Document.class).getString("street"), doc.get("address", Document.class).getString("zipcode")));
		d.put("coordinates", doc.get("address", Document.class).getList("coord", Double.class));

		System.out.println(d);

		String url = UriComponentsBuilder.fromUriString("http://map.chuklee.com/map")
				.queryParam("lat", doc.get("address", Document.class).getList("coord", Double.class).get(1).floatValue())
                .queryParam("lng", doc.get("address", Document.class).getList("coord", Double.class).get(0).floatValue())
                .toUriString();
		
		RequestEntity<Void> req = RequestEntity.get(url).build();

		ResponseEntity<byte[]> resp;
		RestTemplate template = new RestTemplate();

		resp = template.exchange(req, byte[].class);

		byte[] payload = resp.getBody();
		System.out.println(payload);

		Map<String, String> userData = new HashMap<>();
		userData.put("name", doc.getString("name"));

		// Metadata of the file
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setUserMetadata(userData);
		metadata.setContentType("image/jpeg");
		metadata.setContentLength(payload.length);

		try {
			PutObjectRequest putReq = new PutObjectRequest(spacesBucket, "%s.jpeg".formatted(doc.getString("restaurant_id")), new ByteArrayInputStream(payload), metadata);
			putReq.withCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject(putReq);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Put S3", ex);
		}
		
		LatLng latLng = new LatLng();
		latLng.setLongitude(doc.get("address", Document.class).getList("coord", Double.class).get(0).floatValue());
        latLng.setLatitude(doc.get("address", Document.class).getList("coord", Double.class).get(1).floatValue());

		Restaurant r = new Restaurant();
                r.setRestaurantId(d.getString("restaurant_id"));
                r.setName(d.getString("name"));
                r.setCuisine(d.getString("cuisine").replace("/", "_"));
                r.setAddress(d.getString("address"));
                r.setCoordinates(latLng);
                

		String imageUrl = "https://%s.%s/%s".formatted(spacesBucket, spacesEndpointUrl, "%s.jpeg".formatted(doc.getString("restaurant_id")));
		r.setMapURL(imageUrl);
		return Optional.of(r);
	}

	// // TODO Task 5
	// // Use this method to insert a comment into the restaurant database
	// // DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// // Write the Mongo native query above for this method
	// //  
	// public void addComment(Comment comment) {
	// 	// Implmementation in here
		
	// }
	
	// You may add other methods to this class

}
