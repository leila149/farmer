package com.janet.farmersclub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.janet.farmersclub.MyFarms.getName;

/**
 * Created by anableila on 7/10/18.
 */

public class ViewFarmsActivity extends AppCompatActivity{
    String farmID;
    TextView farmname;
    TextView farmdesc;
    TextView location;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.janet.farmersclub.R.layout.activity_farm_layout);
        Toolbar toolbar = (Toolbar) findViewById(com.janet.farmersclub.R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        farmID = intent.getStringExtra("farmID");

        farmname = (TextView) findViewById(R.id.farmname);
        farmdesc = (TextView) findViewById(R.id.farmdesc);
        location = (TextView) findViewById(R.id.location);


        sharedPreferences = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);

        getName();

        Button farm  = (Button) findViewById(com.janet.farmersclub.R.id.addToFarmButton);
       farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFarm();
            }
        });
    }

    public void getName() {
        String URL = "http://farmersclub.000webhostapp.com/item.php?apicall=get";
        String TAG = "Item";

        StringRequest all = new StringRequest(Request.Method.POST, URL, new Response
                .Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Message", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("error") == false) {
                        JSONArray jsonArray = jsonObject.getJSONArray("item");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String id = jsonObject1.optString("farmID");
                        String name = jsonObject1.optString("name");
                        String description = jsonObject1.optString("desc");

                        farmname.setText(name);
                        farmdesc.setText(description);
                        location.setText((CharSequence) location);
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject
                                .optString("message"), Toast
                                .LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error" + error.getMessage());
                volleyClearCache();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("farmID", farmID);
                return params;
            }
        };

        Singleton.getInstance(getApplicationContext()).addToRequestQueue(all, TAG);
    }

    public void addToFav(View view){
        String URL = "http://farmersclub.000webhostapp.com/item.php?apicall=fav";
        String TAG = "Favourites";

        StringRequest all = new StringRequest(Request.Method.POST, URL, new Response
                .Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Message", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("error") == false) {
                        Toast.makeText(getApplicationContext(), jsonObject.optString("message"),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.optString("message"),
                                Toast.LENGTH_LONG)
                                .show();
                        volleyClearCache();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error" + error.getMessage());
                volleyClearCache();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("farmID", farmID);
                return params;
            }
        };

        Singleton.getInstance(getApplicationContext()).addToRequestQueue(all, TAG);
    }

    public void addToFarm(){


        String URL = "http://farmersclub.000webhostapp.com/order.php?apicall=addCart";

        StringRequest addCart = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.optBoolean("error")== false){
                        progressDialog.hide();
                        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(home);
                    } else{
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), jsonObject.optString("message"),
                                Toast.LENGTH_SHORT).show();
                        volleyClearCache();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error" +error.getMessage());
                volleyClearCache();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("farmID", farmID);
                return params;
            }
        };

        Singleton.getInstance(getApplicationContext()).addToRequestQueue(addCart, "ADDTOCART");
    }

    public void volleyInvalidateCache(String url) {
        Singleton.getInstance(getApplicationContext()).getRequestQueue().getCache()
                .invalidate
                        (url, true);
    }

    public void volleyDeleteCache(String url) {
        Singleton.getInstance(getApplicationContext()).getRequestQueue().getCache()
                .remove(url);
    }

    public void volleyClearCache() {
        Singleton.getInstance(getApplicationContext()).getRequestQueue().getCache()
                .clear();
    }
}

