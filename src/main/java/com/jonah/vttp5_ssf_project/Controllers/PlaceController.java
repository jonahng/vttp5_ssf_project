package com.jonah.vttp5_ssf_project.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jonah.vttp5_ssf_project.Models.Place;
import com.jonah.vttp5_ssf_project.Services.PlaceService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class PlaceController {
    @Autowired
    PlaceService placeService;


    @GetMapping("")
    public String homePage(Model model){

        return "index";
    }


    @GetMapping("/apikey")
    public String printapikey(HttpSession httpSesssion, Model model){
        placeService.printApiKey();

        if(httpSesssion.getAttribute("session") ==null){
            System.out.println("user is not logged in yet!");
            return "redirect:/sessions";
        }
        String sessionName = httpSesssion.getAttribute("fullName").toString();
        System.out.println("fullName is : " + sessionName);
        System.out.println("HTTP SESSION Session" + httpSesssion.getAttribute("session"));

        String googleReply = placeService.tryPlaceApi(sessionName);

        model.addAttribute("sessionName", sessionName);
        model.addAttribute("googleReply", googleReply);
        return "embedmap";
    }


    @GetMapping("/apikey/redis")
    public String readFromRedis(HttpSession httpSession, Model model){
        String sessionName = httpSession.getAttribute("fullName").toString();
        model.addAttribute("sessionName", sessionName);

        String redisData = placeService.readFromRedis(sessionName, sessionName);
        List<Place> allPlaces = placeService.parsePlaceObjects(redisData);
        model.addAttribute("allPlaces", allPlaces);

        return "redisData";
    }
    
}
