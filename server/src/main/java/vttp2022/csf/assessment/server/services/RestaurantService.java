package vttp2022.csf.assessment.server.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;
// import vttp2022.csf.assessment.server.repositories.MapCache;
import vttp2022.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepo;

	// @Autowired
	// private MapCache mapCache;

	// TODO Task 2 
	// Use the following method to get a list of cuisines 
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public List<Restaurant> getCuisines() {
		// Implmementation in here
		Optional<List<Restaurant>> lsRes = restaurantRepo.getCuisines();

        System.out.println("List of Restaurant is empty: " + lsRes.isEmpty());

        return lsRes.get();
	}

	// // TODO Task 3 
	// // Use the following method to get a list of restaurants by cuisine
	// // You can add any parameters (if any) and the return type 
	// // DO NOT CHNAGE THE METHOD'S NAME
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		Optional<List<Restaurant>> lsRes = restaurantRepo.getRestaurantsByCuisine(cuisine);
		return lsRes.get();
	}

	// // TODO Task 4
	// // Use this method to find a specific restaurant
	// // You can add any parameters (if any) 
	// // DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public Optional<Restaurant> getRestaurant(String restaurantName) {
		// Implmementation in here
		Optional<Restaurant> opt = restaurantRepo.getRestaurant(restaurantName);

		Restaurant r = opt.get();

		// System.out.println(r.getRestaurantId());
        // System.out.println(r.getName());
        // System.out.println(r.getCuisine());
        // System.out.println(r.getAddress());
        // System.out.println(r.getCoordinates().getLongitude());
        // System.out.println(r.getCoordinates().getLatitude());
		// System.out.println(r.getMapURL());

		return Optional.of(r);
	}

	// // TODO Task 5
	// // Use this method to insert a comment into the restaurant database
	// // DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// public void addComment(Comment comment) {
	// 	// Implmementation in here
		
	// }
	//
	// You may add other methods to this class
}
