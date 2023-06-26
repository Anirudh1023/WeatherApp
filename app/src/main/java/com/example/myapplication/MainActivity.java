package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText editTextCity;
    private Button buttonFetchWeather;
    private TextView textViewWeatherData;
    private ImageView imageViewWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextCity = findViewById(R.id.editTextCity);
        buttonFetchWeather = findViewById(R.id.buttonFetchWeather);
        textViewWeatherData = findViewById(R.id.textViewWeatherData);
        imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon);

        // Set button click listener
        buttonFetchWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                fetchWeatherData(city);
            }
        });
    }

    private void fetchWeatherData(String city) {
        String apiKey = "62f28f76bbdb4d9a8db70323232606";
        String url = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + city;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the weather data from the response
                            JSONObject currentWeather = response.getJSONObject("current");
                            String temperature = currentWeather.getString("temp_c");
                            String condition = currentWeather.getJSONObject("condition").getString("text");
                            String iconUrl = currentWeather.getJSONObject("condition").getString("icon");
                            loadWeatherIcon("https:" + iconUrl);

                            // Display the weather data
                            textViewWeatherData.setText("Temperature: " + temperature + "°C\nCondition: " + condition);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showAlertDialog("Error parsing weather data");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors and show on-screen popups accordingly
                        String errorMessage;
                        if (error.networkResponse != null) {
                            errorMessage = "Wrong City Name";
                        } else {
                            errorMessage = "No Internet Connection";
                        }
                        showAlertDialog(errorMessage);
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }

    private void loadWeatherIcon(String iconUrl) {
        // You can use a library like Picasso or Glide to load the image from the URL
        // For simplicity, let's assume you're using Picasso

        Picasso.get().load(iconUrl).into(imageViewWeatherIcon);
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}