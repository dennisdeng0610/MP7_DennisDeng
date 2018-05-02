package edu.illinois.cs.cs125.cs125_mp7_final;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import com.android.volley.Request;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static RequestQueue requestQueue;
    private static final String TAG = "MP7:Main";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         *  set up sss.
         */
        requestQueue = Volley.newRequestQueue(this);

        final ImageButton refresh = findViewById(R.id.Refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CSFinal:Main", "Refreshed");
                startAPICall();
            }
        });

    }
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://api.openweathermap.org/data/2.5/forecast?units=imperial&zip=61820,us&appid="
                            + "c4da8f31b1143ede3161289cb94e759d",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            EditText date = findViewById(R.id.editText2);
                            EditText time = findViewById(R.id.editText);
                            String dateAsString = date.getText().toString().trim();
                            String timeAsString = time.getText().toString().trim();
                            String dateAndTime = dateAsString + " " + timeAsString;
                            System.out.println(dateAndTime);
                            TextView textView = findViewById(R.id.textView);
                            TextView textView1 = findViewById(R.id.textView2);
                            TextView textView3 = findViewById(R.id.textView3);
                            TextView textView4 = findViewById(R.id.textView4);
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            JsonParser jsonParser = new JsonParser();
                            JsonElement jsonElement = jsonParser.parse(response.toString());
                            String prettyJsonString = gson.toJson(jsonElement);
                            JsonObject specificWeather = getSpecificWeather(prettyJsonString, dateAndTime);
                            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
                            JsonParser jsonParser1 = new JsonParser();
                            JsonElement jsonElement1 = jsonParser1.parse(specificWeather.toString());
                            String prettyJsonString1 = gson1.toJson(jsonElement1);
                            double temp11 = getTemp(prettyJsonString1);
                            String temp12 = String.valueOf(temp11);
                            String temp21 = getWeather(prettyJsonString1);
                            textView.setText("Temperature: " + temp12 + "F");
                            textView.setVisibility(View.VISIBLE);
                            textView1.setText("Weather: " + temp21);
                            textView1.setVisibility(View.VISIBLE);
                            textView3.setText("What to wear: " + toWear(temp11));
                            textView3.setVisibility(View.VISIBLE);
                            textView4.setText("What to do: " + toDo(temp11));
                            textView4.setVisibility(View.VISIBLE);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String getWeather(final String json) {
        JsonParser parser = new JsonParser();
        JsonObject result0 = parser.parse(json).getAsJsonObject();
        JsonArray result1 = result0.get("weather").getAsJsonArray();
        JsonObject result2 = result1.get(0).getAsJsonObject();
        return result2.get("main").getAsString();
    }
    double getTemp(final String json) {
        JsonParser parser = new JsonParser();
        JsonObject result0 = parser.parse(json).getAsJsonObject();
        JsonObject result1 = result0.get("main").getAsJsonObject();
        return result1.get("temp").getAsDouble();
    }
    JsonObject getSpecificWeather(final String json, final String Timing) {
        JsonParser parser = new JsonParser();
        JsonObject result0 = parser.parse(json).getAsJsonObject();
        JsonArray result1 = result0.get("list").getAsJsonArray();
        for (int i = 0; i < result1.size(); i++) {
            JsonObject result2 = result1.get(i).getAsJsonObject();
            String result3 = result2.get("dt_txt").getAsString().trim();
            if (result3.equalsIgnoreCase(Timing)) {
                return result2;
            }
        }
        return null;
    }
    String toWear(final double temp) {
        int tempAsInt = (int) temp;
        if (tempAsInt < 30) {
            return "Down Jacket/Overcoat, Sweater/Hoodie, Boots, Thick Socks, Scarf, Gloves & Hat";
        } else if (tempAsInt <= 50) {
            return "Jacket/Wind Coat, Hoodie/Sweatshirt, Sneakers/Boots, Jeans/Pants";
        } else if (tempAsInt <= 75) {
            return "T-Shirt/Jacket, Dress, Cap, Sneakers/Leather Shoes, Jeans/Pants";
        }
        return "T-Shirt/Tank Top, Shorts/Skirts, Sun Glasses, Sandals, Caps, Active-Wear/Swim-Wear";
    }
    String toDo(final double temp) {
        int tempAsInt = (int) temp;
        if (tempAsInt < 30) {
            return "Stay at home and do your homework.";
        } else if (tempAsInt <= 50) {
            return "Go to classes.";
        } else if (tempAsInt <= 75) {
            return "Study CS 125.";
        }
        return "Do sports outside.";
    }

}
