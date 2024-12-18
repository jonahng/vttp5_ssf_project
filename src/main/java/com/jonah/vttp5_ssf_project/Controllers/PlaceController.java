package com.jonah.vttp5_ssf_project.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jonah.vttp5_ssf_project.Services.PlaceService;

@Controller
@RequestMapping("")
public class PlaceController {
    @Autowired
    PlaceService placeService;


    @GetMapping("")
    public String homePage(Model Model){

        return "index";
    }


    @GetMapping("/apikey")
    public String printapikey(Model Model){
        placeService.printApiKey();

        placeService.tryPlaceApi();
        return "index";
    }
    
}
