package com.jonah.vttp5_ssf_project.Models;

import java.util.List;

public class Place {
    private String id;
    private List<String> types;
    private String address;
    private double rating;
    private String googleMapsUrl;
    private String priceLevel;
    private String displayName;
    private double lowerPriceRange;
    private double upperPriceRange;
    //Note that some entries lack fields like price range;


    
    public Place() {
    }



    public Place(String id, List<String> types, String address, double rating, String googleMapsUrl, String priceLevel,
            String displayName, double lowerPriceRange, double upperPriceRange) {
        this.id = id;
        this.types = types;
        this.address = address;
        this.rating = rating;
        this.googleMapsUrl = googleMapsUrl;
        this.priceLevel = priceLevel;
        this.displayName = displayName;
        this.lowerPriceRange = lowerPriceRange;
        this.upperPriceRange = upperPriceRange;
    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public List<String> getTypes() {
        return types;
    }
    public void setTypes(List<String> types) {
        this.types = types;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }
    public void setGoogleMapsUrl(String googleMapsUrl) {
        this.googleMapsUrl = googleMapsUrl;
    }
    public String getPriceLevel() {
        return priceLevel;
    }
    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public double getLowerPriceRange() {
        return lowerPriceRange;
    }
    public void setLowerPriceRange(double lowerPriceRange) {
        this.lowerPriceRange = lowerPriceRange;
    }
    public double getUpperPriceRange() {
        return upperPriceRange;
    }
    public void setUpperPriceRange(double upperPriceRange) {
        this.upperPriceRange = upperPriceRange;
    }


    



    @Override
    public String toString() {
        return "Place [id=" + id + ", types=" + types + ", address=" + address + ", rating=" + rating
                + ", googleMapsUrl=" + googleMapsUrl + ", priceLevel=" + priceLevel + ", displayName=" + displayName
                + ", lowerPriceRange=" + lowerPriceRange + ", upperPriceRange=" + upperPriceRange + "]";
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((types == null) ? 0 : types.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        long temp;
        temp = Double.doubleToLongBits(rating);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((googleMapsUrl == null) ? 0 : googleMapsUrl.hashCode());
        result = prime * result + ((priceLevel == null) ? 0 : priceLevel.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        temp = Double.doubleToLongBits(lowerPriceRange);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upperPriceRange);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        Place other = (Place) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (types == null) {
            if (other.types != null)
                return false;
        } else if (!types.equals(other.types))
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (Double.doubleToLongBits(rating) != Double.doubleToLongBits(other.rating))
            return false;
        if (googleMapsUrl == null) {
            if (other.googleMapsUrl != null)
                return false;
        } else if (!googleMapsUrl.equals(other.googleMapsUrl))
            return false;
        if (priceLevel == null) {
            if (other.priceLevel != null)
                return false;
        } else if (!priceLevel.equals(other.priceLevel))
            return false;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
        if (Double.doubleToLongBits(lowerPriceRange) != Double.doubleToLongBits(other.lowerPriceRange))
            return false;
        if (Double.doubleToLongBits(upperPriceRange) != Double.doubleToLongBits(other.upperPriceRange))
            return false;
        return true;
    }


    

    
    
}
