package com.jonah.vttp5_ssf_project.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.jonah.vttp5_ssf_project.Models.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/sessions")
public class SessionController {

    
    @GetMapping("/list")
    public String showSessions(HttpSession httpSession, Model model) {

        List<SessionData> sessions = null;
        if (httpSession.getAttribute("session") == null) {
            sessions = new ArrayList<>();
        } else {
            sessions = (List<SessionData>) httpSession.getAttribute("session");
        }

        model.addAttribute("sessions", sessions);
        return "sessionlist";
    }

    @GetMapping("")
    public String sessionCreate(Model model) {
        SessionData sessionData = new SessionData();
        model.addAttribute("session", sessionData);
        return "sessioncreate";
    }

    @PostMapping("")
    public String postSessionCreate(@Valid @ModelAttribute("session") SessionData entity, BindingResult bindingResult, HttpSession httpSession, Model model) {
        //check the order, bindingresult may or may not be before httpsession

        if(bindingResult.hasErrors()){
            System.out.println("binding result has errors" + bindingResult.getErrorCount() +bindingResult.toString());
            return "sessioncreate";
        }


        List<SessionData> sessions = null;
        if (httpSession.getAttribute("session") == null) {
            sessions = new ArrayList<>();
        } else {
            sessions = (List<SessionData>) httpSession.getAttribute("session");
        }
        sessions.add(entity);
        
        httpSession.setAttribute("session", sessions);
        httpSession.setAttribute("fullName", entity.getFullName());

        model.addAttribute("sessions", sessions);
        return "redirect:/apikey";
    }
    
    @GetMapping("/clear")
    public String clearSession(HttpSession httpSession) {
        httpSession.removeAttribute("session");
        httpSession.invalidate();
        System.out.println("Session has been cleared");

        return "redirect:/alltodo";
    }
    
    
    
}
