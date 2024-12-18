package com.jonah.vttp5_ssf_project.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    @Value("${apikey}")
    private String apikey;


    public void printApiKey(){
        System.out.println("the api key is currently: " + apikey);
    }


    
}
