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
                    "http://api.openweathermap.org/data/2.5/weather?units=imperial&zip=61820,us&appid="
                            + "c4da8f31b1143ede3161289cb94e759d",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            TextView textView = findViewById(R.id.textView);
                            TextView textView1 = findViewById(R.id.textView2);
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            JsonParser jsonParser = new JsonParser();
                            JsonElement jsonElement = jsonParser.parse(response.toString());
                            String prettyJsonString = gson.toJson(jsonElement);
                            double temp11 = getTemp(prettyJsonString);
                            String temp12 = String.valueOf(temp11);
                            String temp21 = getWeather(prettyJsonString);
                            textView.setText("Temperature: " + temp12 + "F");
                            textView.setVisibility(View.VISIBLE);
                            textView1.setText("Weather: " + temp21);
                            textView1.setVisibility(View.VISIBLE);
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
    void toWear() {
        // if rain then give stuff to take etc.
    }
    void toDo() {

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

}
