package com.example.testweather1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import Util.Utils;
import java.sql.Date;
import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {
    private TextView cityName;
    private TextView temp;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;
    private ImageView iconView;

    public static Weather weather = new Weather();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = findViewById(R.id.cityText);
        temp = findViewById(R.id.tempText);
        description = findViewById(R.id.cloudText);
        humidity = findViewById(R.id.humidText);
        pressure = findViewById(R.id.pressureText);
        wind = findViewById(R.id.windText);
        sunrise = findViewById(R.id.riseText);
        sunset = findViewById(R.id.setText);
        updated = findViewById(R.id.updateText);
        iconView = findViewById(R.id.thumbnailIcon);
        CityPreference cityPreference = new CityPreference(MainActivity.this);
        renderWeatherData(cityPreference.getCity());
    }

    public void renderWeatherData(String city) {

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "828ad524bf8f8cb64b58c45eb56c420b"});
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            iconView.setImageBitmap(bitmap);
            iconView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        private Bitmap downloadImage(String code) {

            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();

            //froming a HttoGet request
            final HttpGet getRequest = new HttpGet(Utils.ICON_URL + code + ".png");

            try {
                HttpResponse response = client.execute(getRequest);

                //check 200 OK for success
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode + statusCode +
                            "while retrieving bitmap from" + Utils.ICON_URL + code + ".png");
                    return null;
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        // getting contents from the stream
                        inputStream = entity.getContent();

                        //decoding stream data back into image Bitmap that android understands
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.w("ImageDownloader", "Something went wrong while "
                        + "while retrieving bitmap from " + Utils.ICON_URL + code + ".png");
            }
            return null;
        }

    }


    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... strings) {
            String data = ((new WeatherHttpClient()).getWeatherData(strings[0]));
            try {
                weather = JSONWeatherParser.getWeather(data);
                //Retrive the icon
                weather.iconData = weather.currentCondition.getIcon();
                // ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
                // weather.iconData = ((new WeatherHttpClient())getImage(weather.currentCondition.getIcon()));

                //Log.v("Data: ",weather.place.getCity());
                // Log.v("Data: ", weather.currentCondition.getDescription());
                Log.v("ICON DATA VALUE IS: ", String.valueOf(weather.currentCondition.getIcon()));

                //we call our ImageDownload task after the weather.iconData is set!
                new DownloadImageAsyncTask().execute(weather.iconData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());
            temp.setText("" + tempFormat + "°C");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "hPa");
            wind.setText("Wind: " + weather.wind.getSpeed() + "mps");

            DateFormat df = DateFormat.getDateTimeInstance();
            String sunriseData = df.format(new Date((long) weather.place.getSunrise()));
            String sunsetDate = df.format(new Date((long) weather.place.getSunset()));
            String updateD = df.format(new Date(weather.place.getLastupdate()));

            // sunrise.setText("Sunrise : " + sunriseData);
           sunrise.setText(String.format("Sunrise : %s ", unixTimeStampToDateTime(weather.place.getSunrise())));

            // sunset.setText("Sunset: " + sunsetDate);
            sunset.setText(String.format("Sunset: %s " , unixTimeStampToDateTime(weather.place.getSunset())));

            // updated.setText(String.format("Last Updated: %s", getDateNow()));
            updated.setText(String.format("Last Updated: %s ", getDateNow()));


            description.setText("Condition: " + weather.currentCondition.getCondition()
                    + "(" + weather.currentCondition.getDescription() + ")");

        }
    }
    public static String getDateNow() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = (Date) new java.util.Date();
        return dateFormat.format(date);
    }

    public static String unixTimeStampToDateTime(double unixTimeStamp){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = (Date) new java.util.Date();
        date.setTime((long)unixTimeStamp*1000);
        return dateFormat.format(date);
    }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.changeCityItem){
                showInputDialog();
            }
            return super.onOptionsItemSelected(item);
        }

        private void showInputDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Change City");

            final EditText cityInput = new EditText(MainActivity.this);
            cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
            cityInput.setHint("Shiraz,IR");
            builder.setView(cityInput);
            builder.setPositiveButton("Sumit", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CityPreference cityPreference = new CityPreference(MainActivity.this);
                    cityPreference.setCity(cityInput.getText().toString());

                    String newCity = cityPreference.getCity();

                    // new CityPreference(MainActivity.this).setCity(cityInput.getText().toString());

                    //re_render everything again
                    renderWeatherData(newCity);

                }
            });
            builder.show();
        }
    }

