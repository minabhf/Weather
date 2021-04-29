package model;

public class Place {

    private float lon;
    private float lat;
    private float sunset;
    private float sunrise;
    private String country;
    private String city;
    private long lastupdate;

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getSunset() {
        return sunset;
    }

    public void setSunset(float sunset) {
        this.sunset = sunset;
    }

    public float getSunrise() {
        return sunrise;
    }

    public void setSunrise(float sunrise) {
        this.sunrise = sunrise;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
    }
}

