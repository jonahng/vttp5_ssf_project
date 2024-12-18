package com.jonah.vttp5_ssf_project.Services;

import java.io.InputStream;
import java.io.StringReader;

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

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class PlaceService {

    @Value("${apikey}")
    private String apikey;

    RestTemplate restTemplate = new RestTemplate();


    public void printApiKey(){
        System.out.println("the api key is currently: " + apikey);
    }


    public void tryPlaceApi(){
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
                        "      \"radius\": 2000\n" + //
                        "    }\n" + //
                        "  }\n" + //
                        "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", apikey); //REMOVE THE API KEY
        headers.set("X-Goog-FieldMask", "places.displayName");

        JsonReader jsonReader = Json.createReader(new StringReader(requestBody));
        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("THE JSON OBJECT TO POST IS: " + jsonObject);
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> replyRawData = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        System.out.println("Trying to post to google and response:" + replyRawData.getBody());
        

    }


    
}
