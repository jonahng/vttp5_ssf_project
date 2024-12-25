package com.jonah.vttp5_ssf_project.Models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SessionData {
    

    //VALIDATION
    @NotEmpty(message="Please input a name! Even a fake one is fine :(")
    @Size(min = 3, max = 50, message = "Name can be 3 to 50 characters")
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
