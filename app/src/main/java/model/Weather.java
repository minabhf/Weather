package model;

public class Weather {
    public Place place;
    public String iconData;
    public CurrentCondition currentCondition =new CurrentCondition();
    public Temperature temperature=new Temperature();
    public Wind wind=new Wind();
    public Snow snow=new Snow();
    public Clouds clouds=new Clouds();

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getIconData() {
        return iconData;
    }

    public void setIconData(String iconData) {
        this.iconData = iconData;
    }

    public CurrentCondition getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(CurrentCondition currentCondition) {
        this.currentCondition = currentCondition;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }
}
