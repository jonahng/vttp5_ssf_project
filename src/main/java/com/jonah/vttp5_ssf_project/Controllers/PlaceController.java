package com.jonah.vttp5_ssf_project.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jonah.vttp5_ssf_project.Models.Location;
import com.jonah.vttp5_ssf_project.Models.Place;
import com.jonah.vttp5_ssf_project.Models.ShortListPlace;
import com.jonah.vttp5_ssf_project.Services.PlaceService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class PlaceController {
    @Autowired
    PlaceService placeService;


    @GetMapping("")
    public String homePage(Model model){

        return "redirect:/sessions";
    }




    @GetMapping("/location")
    public String getLocationSelection(HttpSession httpSession, Model model){
        model.addAttribute("sessionName", httpSession.getAttribute("fullName"));
        model.addAttribute("apikey", placeService.getApiKey()); //REWRITE CODE SO API KEY DOES NOT GO TO HTML!
        model.addAttribute("lat", 0);
        model.addAttribute("lon", 0);
        Location location = new Location();
        model.addAttribute("location", location);
        return "locationselection";
    }

    @PostMapping("/location")
    public String postLocationSelection(@RequestBody MultiValueMap<String, String> formEntity, HttpSession httpSession, Model model){
        try {
            if(formEntity.getFirst("placeLong")==""){
                return "redirect:/location";
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        System.out.println("received post from location form:" + formEntity);

        httpSession.setAttribute("longitude", Double.parseDouble(formEntity.getFirst("placeLong")));
        httpSession.setAttribute("latitude", Double.parseDouble(formEntity.getFirst("placeLat")));
        //placeService.tryPlaceApi(httpSession.getAttribute("fullName").toString(), Double.parseDouble(formEntity.getFirst("cityLng")), Double.parseDouble(formEntity.getFirst("cityLat")));

        return "redirect:/apikey"; //CHANGE THIS TO REDIRECT TO REDIS PAGE?
    }






    @GetMapping("/apikey")
    public String printapikey(HttpSession httpSession, Model model){
        placeService.printApiKey();

        if(httpSession.getAttribute("session") ==null){
            System.out.println("user is not logged in yet!");
            return "redirect:/sessions";
        }
        String sessionName = httpSession.getAttribute("fullName").toString();
        System.out.println("fullName is : " + sessionName);
        System.out.println("HTTP SESSION Session" + httpSession.getAttribute("session"));

        String googleReply = placeService.tryPlaceApi(sessionName, Double.parseDouble(httpSession.getAttribute("longitude").toString()), Double.parseDouble(httpSession.getAttribute("latitude").toString()));

        model.addAttribute("sessionName", sessionName);
        model.addAttribute("googleReply", googleReply);
        return "redirect:/apikey/redis";
    }

    @GetMapping("/apikey/fordemo")
    public String demoprintapikey(HttpSession httpSession, Model model){
        placeService.printApiKey();

        if(httpSession.getAttribute("session") ==null){
            System.out.println("user is not logged in yet!");
            return "redirect:/sessions";
        }
        String sessionName = httpSession.getAttribute("fullName").toString();
        System.out.println("fullName is : " + sessionName);
        System.out.println("HTTP SESSION Session" + httpSession.getAttribute("session"));

        String googleReply = placeService.tryPlaceApi(sessionName, Double.parseDouble(httpSession.getAttribute("longitude").toString()), Double.parseDouble(httpSession.getAttribute("latitude").toString()));

        model.addAttribute("sessionName", sessionName);
        model.addAttribute("googleReply", googleReply);
        return "embedmap";
    }


    @GetMapping("/apikey/redis")
    public String readFromRedis(HttpSession httpSession, Model model){

        if(httpSession.getAttribute("session") ==null){
            System.out.println("user is not logged in yet!");
            return "redirect:/sessions";
        }

        String sessionName = httpSession.getAttribute("fullName").toString();
        model.addAttribute("sessionName", sessionName);

        String redisData = placeService.readFromRedis(sessionName, sessionName);
        List<Place> allPlaces = placeService.parsePlaceObjects(redisData);
        model.addAttribute("allPlaces", allPlaces);
        System.out.println("\n Highest Rated Place is:" + placeService.highestRatedPlace(allPlaces));

        return "redirect:/suggestion";
    }


    @GetMapping("/apikey/redis/fordemo")
    public String demoreadFromRedis(HttpSession httpSession, Model model){

        if(httpSession.getAttribute("session") ==null){
            System.out.println("user is not logged in yet!");
            return "redirect:/sessions";
        }

        String sessionName = httpSession.getAttribute("fullName").toString();
        model.addAttribute("sessionName", sessionName);

        String redisData = placeService.readFromRedis(sessionName, sessionName);
        List<Place> allPlaces = placeService.parsePlaceObjects(redisData);
        model.addAttribute("allPlaces", allPlaces);
        System.out.println("\n Highest Rated Place is:" + placeService.highestRatedPlace(allPlaces));

        return "redisData";
    }


    //THIS PATHVARIABLE ALLOWS ADMIN TO READ THE REDIS DATABASE FOR ANY GIVEN USER SESSION
    @GetMapping("/apikey/redis/{sessionName}")
    public String readFromRedisSpecificUser(@PathVariable Map<String, String> pathMap, Model model){

        String sessionName = pathMap.get("sessionName");
        model.addAttribute("sessionName", sessionName);
        String redisData = placeService.readFromRedis(sessionName, sessionName);
        List<Place> allPlaces = placeService.parsePlaceObjects(redisData);
        model.addAttribute("allPlaces", allPlaces);
        System.out.println("\n Highest Rated Place is:" + placeService.highestRatedPlace(allPlaces));

        return "redisData";
    }



    @GetMapping("/suggestion")
    public String suggestPlace(HttpSession httpSession, Model model){

        if(httpSession.getAttribute("session") ==null){
            System.out.println("user is not logged in yet!");
            return "redirect:/sessions";
        }

        String sessionName = httpSession.getAttribute("fullName").toString();
        model.addAttribute("sessionName", sessionName);

        String redisData = placeService.readFromRedis(sessionName, sessionName);
        List<Place> allPlaces = placeService.parsePlaceObjects(redisData);
        //model.addAttribute("allPlaces", allPlaces);

        Place highestRatedPlace = placeService.highestRatedPlace(allPlaces);
        System.out.println("\n Highest Rated Place is:" + highestRatedPlace);
        model.addAttribute("place", highestRatedPlace);


        return "suggestionpage";

    }



    @PostMapping("/suggestion")
    public String postSuggestion(@ModelAttribute("place") Place place, HttpSession httpSession, Model model){

        if(httpSession.getAttribute("session") ==null){
            System.out.println("user is not logged in yet!");
            return "redirect:/sessions";
        }
        String sessionName = httpSession.getAttribute("fullName").toString();
        model.addAttribute("sessionName", sessionName);
        System.out.println("the current http session is:" + sessionName);
        

        System.out.println("post place received: " + place);


        String redisData = placeService.readFromRedis(sessionName, sessionName);
        List<Place> allPlaces = placeService.parsePlaceObjects(redisData);


        placeService.addIdToIgnoreList(sessionName, place.getId()); 
        List<Place> allPlacesMinusAllChecked = placeService.removePlacesFromPlaceList(allPlaces, placeService.getListOfIdToIgnore(sessionName));


        //model.addAttribute("allPlaces", allPlaces);
        /* List<Place> allPlacesMinusPrevious = placeService.removePlaceFromPlaceListUpdateIndexRepo(place, allPlaces,sessionName);
        List<Integer> listOfPlacesToRemove = placeService.getIntegerListMapRepoForIndexes(sessionName);
        System.out.println("index of the places to remove is:" + listOfPlacesToRemove);
        List<Place> allPlacesMinusAllChecked = allPlaces;
        for(int i : listOfPlacesToRemove){
            allPlaces.remove(i);
        } */


        Place highestRatedPlace = placeService.highestRatedPlace(allPlacesMinusAllChecked);//CHANGE THIS TO ALLPLACESMINUSALLCHECKED

        //get service to find new place,
        //add new place to model
        //return the suggestionpage with the new place in the mode, not redirect!
        model.addAttribute("place", highestRatedPlace);


        //for shortlist:
        String oldShortList = httpSession.getAttribute("shortList").toString();
        List<String> oldShortListArray = new ArrayList<String>(Arrays.asList(oldShortList.split(",")));
        model.addAttribute("shortList", oldShortListArray);
        String oldShortListOfNames = httpSession.getAttribute("shortListPlaceNames").toString();
        List<String> oldShortListOfNamesArray = new ArrayList<String>(Arrays.asList(oldShortListOfNames.split(",")));

        List<ShortListPlace> shortListedPlacesObjects = new ArrayList<>();
        for(String gmapsURL : oldShortListArray){
            ShortListPlace shortListPlace = new ShortListPlace(oldShortListOfNamesArray.get(oldShortListArray.indexOf(gmapsURL)), gmapsURL);
            shortListedPlacesObjects.add(shortListPlace);
        }
        model.addAttribute("shortListObjects", shortListedPlacesObjects);
        return "suggestionpage";
    }



    @PostMapping("/suggestion/shortlist")
    public String postShortlist(@ModelAttribute("place") Place place, HttpSession httpSession, Model model){
        String existingShortList = httpSession.getAttribute("shortList").toString();
        String updatedShortList = existingShortList +  place.getGoogleMapsUrl().toString() + ",";

        httpSession.setAttribute("shortList", updatedShortList);


        String existingShortListNames = httpSession.getAttribute("shortListPlaceNames").toString();
        String updatedShortListNames = existingShortListNames + place.getDisplayName().toString() + ",";

        httpSession.setAttribute("shortListPlaceNames", updatedShortListNames);
        //get the short list, which should have been initialised as empty list of place
        //add new place to list of places
        //set shortlist to updated list
        
        System.out.println("the updated shortlist is:" + updatedShortList);
        if(httpSession.getAttribute("session") ==null){
            System.out.println("user is not logged in yet!");
            return "redirect:/sessions";
        }
        String sessionName = httpSession.getAttribute("fullName").toString();
        model.addAttribute("sessionName", sessionName);
        System.out.println("the current http session is:" + sessionName);
        

        System.out.println("post place received: " + place);


        String redisData = placeService.readFromRedis(sessionName, sessionName);
        List<Place> allPlaces = placeService.parsePlaceObjects(redisData);


        placeService.addIdToIgnoreList(sessionName, place.getId()); 
        List<Place> allPlacesMinusAllChecked = placeService.removePlacesFromPlaceList(allPlaces, placeService.getListOfIdToIgnore(sessionName));


        //model.addAttribute("allPlaces", allPlaces);
        /* List<Place> allPlacesMinusPrevious = placeService.removePlaceFromPlaceListUpdateIndexRepo(place, allPlaces,sessionName);
        List<Integer> listOfPlacesToRemove = placeService.getIntegerListMapRepoForIndexes(sessionName);
        System.out.println("index of the places to remove is:" + listOfPlacesToRemove);
        List<Place> allPlacesMinusAllChecked = allPlaces;
        for(int i : listOfPlacesToRemove){
            allPlaces.remove(i);
        } */


        Place highestRatedPlace = placeService.highestRatedPlace(allPlacesMinusAllChecked);//CHANGE THIS TO ALLPLACESMINUSALLCHECKED

        //get service to find new place,
        //add new place to model
        //return the suggestionpage with the new place in the mode, not redirect!
        model.addAttribute("place", highestRatedPlace);


        //for shortlist:
        String oldShortList = httpSession.getAttribute("shortList").toString();
        List<String> oldShortListArray = new ArrayList<String>(Arrays.asList(oldShortList.split(",")));
        model.addAttribute("shortList", oldShortListArray);
        

        String oldShortListOfNames = httpSession.getAttribute("shortListPlaceNames").toString();
        List<String> oldShortListOfNamesArray = new ArrayList<String>(Arrays.asList(oldShortListOfNames.split(",")));

        List<ShortListPlace> shortListedPlacesObjects = new ArrayList<>();
        for(String gmapsURL : oldShortListArray){
            ShortListPlace shortListPlace = new ShortListPlace(oldShortListOfNamesArray.get(oldShortListArray.indexOf(gmapsURL)), gmapsURL);
            shortListedPlacesObjects.add(shortListPlace);
        }
        model.addAttribute("shortListObjects", shortListedPlacesObjects);


        return "suggestionpage";
    }


    @GetMapping("/context")
    public String contextPicture(){

        return "context";
    }

}
