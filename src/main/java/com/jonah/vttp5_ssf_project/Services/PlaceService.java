package com.jonah.vttp5_ssf_project.Services;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jonah.vttp5_ssf_project.Constants.Constants;
import com.jonah.vttp5_ssf_project.Models.Place;
import com.jonah.vttp5_ssf_project.Repos.ListRepo;
import com.jonah.vttp5_ssf_project.Repos.MapRepo;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class PlaceService {
    @Autowired
    MapRepo maprepo;

    @Autowired
    ListRepo listRepo;

    @Value("${apikey}")
    private String apikey;

    RestTemplate restTemplate = new RestTemplate();


    public void printApiKey(){
        System.out.println("the api key is currently: " + apikey);
    }

    public String getApiKey(){
        return apikey;
    }


    public String tryPlaceApi(String fullName){
        String url = "https://places.googleapis.com/v1/places:searchNearby";
        String requestBody = "{\n" + //
                        "  \"includedTypes\": [\n" + //
                        "    \"restaurant\"\n" + //
                        "  ],\n" + //
                        "  \"maxResultCount\": 20,\n" + //
                        "  \"locationRestriction\": {\n" + //
                        "    \"circle\": {\n" + //
                        "      \"center\": {\n" + //
                        "        \"latitude\": 1.38705,\n" + //
                        "        \"longitude\": 103.87023\n" + //
                        "      },\n" + //
                        "      \"radius\": 1500\n" + //
                        "    }\n" + //
                        "  }\n" + //
                        "}";



                        String requestBodyDistanceRanked = "{\n" + //
                        "  \"includedTypes\": [\n" + //
                        "    \"restaurant\"\n" + //
                        "  ],\n" + //
                        "  \"maxResultCount\": 30,\n" + //
                        "  \"locationRestriction\": {\n" + //
                        "    \"circle\": {\n" + //
                        "      \"center\": {\n" + //
                        "        \"latitude\": 1.38705,\n" + //
                        "        \"longitude\": 103.87023\n" + //
                        "      },\n" + //
                        "      \"radius\": 1500\n" + //
                        "    }\n" + //
                        "  },\n" + //
                        "  \"rankPreference\": \"DISTANCE\"\n" + //
                        "}";



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", apikey); //REMOVE THE API KEY
        headers.set("X-Goog-FieldMask",
         "places.id,places.displayName,places.formattedAddress,places.rating,places.types,places.googleMapsUri,places.priceLevel,places.primaryType,places.editorialSummary,places.priceRange");



        //CHANGE REQUEST HERE
        JsonReader jsonReader = Json.createReader(new StringReader(requestBody)); //The requestbody affects the results

        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("THE JSON OBJECT TO POST IS: " + jsonObject);
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> replyRawData = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        System.out.println("Trying to post to google and response:" + replyRawData.getBody());
        String replyBody = replyRawData.getBody();


        //change the constants.ObjectKey to http id, expire after 1 hour?
        addToRedis(fullName,fullName, replyBody);
        maprepo.oneHourExpire(fullName);

        return replyBody;
        

    }

    public void addToRedis(String redisKey, String hashKey, String hashValue){
        maprepo.create(redisKey, hashKey, hashValue);
        System.out.println("added to redis, redisKey, hashkey:" + redisKey +  hashKey);
    }



    public String readFromRedis(String redisKey, String hashKey){
        String redisData = maprepo.get(redisKey, hashKey).toString();
        return redisData;
    }


    public List<Place> parsePlaceObjects(String redisData){
        List<Place> allPlaces = new ArrayList<>();
        JsonReader jsonReader = Json.createReader(new StringReader(redisData));
        JsonObject entireJsonObject = jsonReader.readObject();
        JsonArray placesArray = entireJsonObject.getJsonArray("places");

        for(Integer i = 0; i < placesArray.size(); i++){
            JsonObject placeObject = placesArray.getJsonObject(i);
            //System.out.println("Reading Json place object from redisData string:" + placeObject.toString() + "\n");
            Place place = new Place();
            place.setId(placeObject.getString("id"));
            try {
                place.setAddress(placeObject.getString("formattedAddress", "Data Not Available"));
                
                place.setGoogleMapsUrl(placeObject.getString("googleMapsUri", "Data Not Available"));
                place.setPriceLevel(placeObject.getString("priceLevel", "Data Not Available"));
                place.setDisplayName(placeObject.getJsonObject("displayName").getString("text","Data Not Available"));

                try {
                    place.setRating(Double.parseDouble(placeObject.get("rating").toString()));

                } catch (Exception e) {
                    // TODO: handle exception
                    place.setRating(-1);
                }
                
                try {
                    place.setLowerPriceRange(Integer.parseInt(placeObject.getJsonObject("priceRange").getJsonObject("startPrice").getString("units", "1")));
                    place.setUpperPriceRange(Integer.parseInt(placeObject.getJsonObject("priceRange").getJsonObject("endPrice").getString("units", "1")));
                } catch (Exception e) {
                    // TODO: handle exception
                    place.setLowerPriceRange(-1);
                    place.setUpperPriceRange(-1);
                }
                

            } catch (Exception e) {
                System.out.println("problem getting certain fields from json object     " + e);
                continue;
                // TODO: handle exception
            }
            allPlaces.add(place);
            //System.out.println("the place object generated from reading redis:" + place.toString() + "\n");
        }
        //System.out.println("all places objects read from redis:" + allPlaces);
        return allPlaces;
    }


    public Place highestRatedPlace(List<Place> listOfPlaces){
        Place highestRatedPlace = new Place();

        for(Place p : listOfPlaces){
            if(p.getRating() > highestRatedPlace.getRating()){
                highestRatedPlace = p;
            }
        }
        return highestRatedPlace;
    }


    public void suggestDifferentPlace(String fullName, Place checkedPlace){
        Place suggestedPlace = new Place();
        
    }


    public List<Place> removePlaceFromPlaceList(Place place, List<Place> listOfPlaces){
        int indexToRemove = -1;
        for(Place p: listOfPlaces){
            if(place.getId().equals(p.getId()) ){
                indexToRemove = listOfPlaces.indexOf(p);
                System.out.println("the place removed is from the place list is" + indexToRemove);
            }
        }
        if(indexToRemove > -1){
            listOfPlaces.remove(indexToRemove);
        }
    
        return listOfPlaces;

    }   



    public List<Place> removePlacesFromPlaceList(List<Place> listOfPlaces, List<String> idsToRemove){
        List<Integer> indexesToRemove = new ArrayList<>();
        for(Place p: listOfPlaces){
            for(String removeId: idsToRemove){
                if(removeId.equals(p.getId())){
                    indexesToRemove.add(listOfPlaces.indexOf(p));
                }
            }
        }
        Comparator<Integer> comparator = Collections.reverseOrder();
        Collections.sort(indexesToRemove, comparator);

        System.out.println("the indexes to remove from the display list are: " + indexesToRemove);
        for(Integer indexToRemove: indexesToRemove){
            int i = indexToRemove;
            listOfPlaces.remove(i);
        }
        return listOfPlaces;

    }




    public void addIdToIgnoreList(String httpSessionName, String placeId){
        String listObjectKey = httpSessionName + "indexList";
        String placeToAddwithComma = placeId + ",";

        if(!maprepo.keyExists(httpSessionName, listObjectKey)){
            maprepo.create(httpSessionName, listObjectKey, placeToAddwithComma);
        }else{
            String existingString = getStringMapRepoForIds(httpSessionName);
            String updatedString = existingString + placeToAddwithComma;
            maprepo.delete(httpSessionName, listObjectKey);
            maprepo.create(httpSessionName, listObjectKey, updatedString);
        }
        maprepo.oneHourExpire(httpSessionName);

    }

    public List<String> getListOfIdToIgnore(String httpSessionName){
        String listObjectKey = httpSessionName + "indexList";
        String oldList = maprepo.get(httpSessionName, listObjectKey).toString();
        String[] indexStringarray = oldList.split(",");
        List<String> listOfPlaceId = new ArrayList<>();
        for(String id : indexStringarray){
            listOfPlaceId.add(id);
        }

        System.out.println("in getlistofidtoignore, the list of place id is:" + listOfPlaceId);
        return listOfPlaceId;
        

    }

    public String getStringMapRepoForIds(String httpSessionName){
        String listObjectKey = httpSessionName + "indexList";
        String oldIdList = maprepo.get(httpSessionName, listObjectKey).toString();
        return oldIdList;
    }


}
