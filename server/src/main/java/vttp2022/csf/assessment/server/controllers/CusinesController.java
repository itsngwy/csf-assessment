package vttp2022.csf.assessment.server.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.services.RestaurantService;

@Controller
@RequestMapping(path="/api")
public class CusinesController {

    @Autowired
    private RestaurantService resSvc;
    
    @GetMapping(path="/cuisines")
    @ResponseBody
    public ResponseEntity<String> getCuisines() {

        System.out.println("In Controller");

        List<Restaurant> lsRes = resSvc.getCuisines();

        System.out.println(lsRes.get(0).getRestaurantId());
        System.out.println(lsRes.get(0).getName());
        System.out.println(lsRes.get(0).getCuisine());
        System.out.println(lsRes.get(0).getAddress());
        System.out.println(lsRes.get(0).getCoordinates().getLongitude());
        System.out.println(lsRes.get(0).getCoordinates().getLatitude());

        JsonArrayBuilder jab = Json.createArrayBuilder();

        lsRes.stream()
            .forEach(r -> {
                jab.add(Json.createObjectBuilder()
                        .add("restaurantId", r.getRestaurantId())
                        .add("name", r.getName())
                        .add("cuisine", r.getCuisine())
                        .add("address", r.getAddress())
                        .add("coordinates", Json.createArrayBuilder().add(r.getCoordinates().getLongitude()).add(r.getCoordinates().getLatitude()))
                        // .add("mapUrl", r.getMapURL())
                        .build());
            });

        //System.out.println(jab.build().toString());
        
        return ResponseEntity.ok(jab.build().toString());
    }

    @GetMapping(path="/{cuisine}/restaurants")
    @ResponseBody
    public ResponseEntity<String> getRestaurantsByCuisine(@PathVariable String cuisine) {
        System.out.println(cuisine);

        List<Restaurant> lsRes = resSvc.getRestaurantsByCuisine(cuisine);

        System.out.println();
        System.out.println(lsRes.get(0).getRestaurantId());
        System.out.println(lsRes.get(0).getName());
        System.out.println(lsRes.get(0).getCuisine());
        System.out.println(lsRes.get(0).getAddress());
        System.out.println(lsRes.get(0).getCoordinates().getLongitude());
        System.out.println(lsRes.get(0).getCoordinates().getLatitude());
        System.out.println();

        JsonArrayBuilder jab = Json.createArrayBuilder();
        
        lsRes.stream()
            .forEach(r -> {
                jab.add(Json.createObjectBuilder()
                        .add("restaurantId", r.getRestaurantId())
                        .add("name", r.getName())
                        .add("cuisine", r.getCuisine())
                        .add("address", r.getAddress())
                        .add("coordinates", Json.createArrayBuilder().add(r.getCoordinates().getLongitude()).add(r.getCoordinates().getLatitude()))
                        // .add("mapUrl", r.getMapURL())
                        .build());
            });
        
        // System.out.println(jab.build().toString());

        return ResponseEntity.ok(jab.build().toString());
    }

    @GetMapping(path="/{restaurantName}/restDetail")
    @ResponseBody
    public ResponseEntity<String> getRestaurantDetails(@PathVariable String restaurantName) {

        System.out.println(restaurantName);

        Optional<Restaurant> opt = resSvc.getRestaurant(restaurantName);

        Restaurant r = opt.get();

        JsonObject jo = Json.createObjectBuilder()
                .add("restaurantId", r.getRestaurantId())
                .add("name", r.getName())
                .add("cuisine", r.getCuisine())
                .add("address", r.getAddress())
                .add("coordinates", Json.createArrayBuilder().add(r.getCoordinates().getLongitude()).add(r.getCoordinates().getLatitude()))
                .add("mapUrl", r.getMapURL())
                .build();

        return ResponseEntity.ok(jo.toString());
    }
}
