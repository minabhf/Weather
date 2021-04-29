package data;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {

    SharedPreferences prefs;
    public CityPreference (Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    //If user has not chosen a city , return default

    public String getCity (){
        return prefs.getString("city","Shiraz,IR");
    }

    public void setCity (String city) {
        prefs.edit().putString("city", city).commit();
    }
}
