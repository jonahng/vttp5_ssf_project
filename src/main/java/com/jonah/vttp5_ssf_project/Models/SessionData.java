package com.jonah.vttp5_ssf_project.Models;

import jakarta.validation.constraints.NotEmpty;

public class SessionData {
    

    //BASIC VALIDATION
    @NotEmpty(message="Please input a name! Even a fake one is fine :(")
    private String fullName;

    public SessionData() {
    }

    public SessionData(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }

    
    
}
