package com.jonah.vttp5_ssf_project.Models;

public class ShortListPlace {
    private String placeName;
    private String googleMapsUrl;

    

    public ShortListPlace() {
    }

    
    public ShortListPlace(String placeName, String googleMapsUrl) {
        this.placeName = placeName;
        this.googleMapsUrl = googleMapsUrl;
    }


    public String getPlaceName() {
        return placeName;
    }
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }
    public void setGoogleMapsUrl(String googleMapsUrl) {
        this.googleMapsUrl = googleMapsUrl;
    }

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((placeName == null) ? 0 : placeName.hashCode());
        result = prime * result + ((googleMapsUrl == null) ? 0 : googleMapsUrl.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShortListPlace other = (ShortListPlace) obj;
        if (placeName == null) {
            if (other.placeName != null)
                return false;
        } else if (!placeName.equals(other.placeName))
            return false;
        if (googleMapsUrl == null) {
            if (other.googleMapsUrl != null)
                return false;
        } else if (!googleMapsUrl.equals(other.googleMapsUrl))
            return false;
        return true;


    }

    



    
}
