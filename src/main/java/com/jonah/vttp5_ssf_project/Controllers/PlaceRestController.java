package com.jonah.vttp5_ssf_project.Controllers;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonah.vttp5_ssf_project.Models.Place;
import com.jonah.vttp5_ssf_project.Services.PlaceService;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
@RequestMapping(path = "/api/location", produces = "application/json")
public class PlaceRestController {
    @Autowired
    PlaceService placeService;


    //THIS API ALLOWS PEOPLE TO GET A LIST OF RESTAURANTS AROUND A LOCATION OF THEIR CHOICE, DEFINED WITH LONGITUDE AND LATITUDE IN THE URL
    @PostMapping("/{userName}/{lat}/{long}")
    public ResponseEntity<JsonArray> postListOfPlacesUsingLongAndLat(@PathVariable Map<String, String> pathMap){
        String longitude = pathMap.get("long");
        String latitude = pathMap.get("lat");
        String userName = pathMap.get("userName");
        String googleApiResponse = placeService.tryPlaceApi(userName, Double.parseDouble(longitude) , Double.parseDouble(latitude));

        JsonReader jsonReader = Json.createReader(new StringReader(googleApiResponse));
        JsonObject entireJsonObject = jsonReader.readObject();
        JsonArray placesArray = entireJsonObject.getJsonArray("places");
        return ResponseEntity.ok().body(placesArray);
        
    }


    @GetMapping("/{userName}/{lat}/{long}")
    public ResponseEntity<JsonArray> getListOfPlacesUsingLongAndLat(@PathVariable Map<String, String> pathMap){
        String longitude = pathMap.get("long");
        String latitude = pathMap.get("lat");
        String userName = pathMap.get("userName");
        String googleApiResponse = placeService.tryPlaceApi(userName, Double.parseDouble(longitude) , Double.parseDouble(latitude));

        JsonReader jsonReader = Json.createReader(new StringReader(googleApiResponse));
        JsonObject entireJsonObject = jsonReader.readObject();
        JsonArray placesArray = entireJsonObject.getJsonArray("places");
        return ResponseEntity.ok().body(placesArray);
        
    }




    //THIS API ALLOWS PEOPLE TO GET THE HIGHEST RATED PLACE NEAR THE LOCATION GIVEN IN LONGITUDE AND LATITUDE
    @PostMapping("/{userName}/{lat}/{long}/topsuggestion")
    public ResponseEntity<Place> getTopSuggestionUsingLongAndLat(@PathVariable Map<String, String> pathMap){
        String longitude = pathMap.get("long");
        String latitude = pathMap.get("lat");
        String userName = pathMap.get("userName");
        String googleApiResponse = placeService.tryPlaceApi(userName, Double.parseDouble(longitude) , Double.parseDouble(latitude));

        List<Place> allPlaces = placeService.parsePlaceObjects(googleApiResponse);
        //model.addAttribute("allPlaces", allPlaces);
        Place highestRatedPlace = placeService.highestRatedPlace(allPlaces);

        return ResponseEntity.ok().body(highestRatedPlace);
    }
}
