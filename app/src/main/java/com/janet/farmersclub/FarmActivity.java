package com.janet.farmersclub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FarmActivity extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    String user;
    ProgressDialog progressDialog;
    EditText eFarmName;
    EditText eFarmDesc;
    EditText eLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        user = sharedPreferences.getString("user", "DEFAULT");

        progressDialog = new ProgressDialog(this);
        eFarmName = (EditText) findViewById(R.id.farmname);
        eFarmDesc = (EditText) findViewById(R.id.farmdesc);
        eLocation = (EditText) findViewById(R.id.location);

        Button savefarm = (Button) findViewById(R.id.savefarm_button);
        savefarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                farm();
            }
        });
    }

    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.tomato:
                if (checked)
                    list.add("tomato");
                break;

            case R.id.maize:
                if (checked)
                    list.add("maize");
                break;

            case R.id.potato:
                if (checked)
                    list.add("potato");
                break;

            case R.id.carrot:
                if (checked)
                    list.add("carrot");
                break;

            case R.id.cabbage:
                if (checked)
                    list.add("cabbage");
                break;

            default:
                CheckBox t = (CheckBox) findViewById(R.id.tomato);
                t.setError("Select one crop");
        }
    }

    private void farm(){
        String URL = "http://farmersclub.000webhostapp.com/farm.php?apicall=create";

        final String farmname = eFarmName.getText().toString().trim();
        final String farmdesc = eFarmDesc.getText().toString().trim();
        final String location = eLocation.getText().toString().trim();
        String crop = "";

        for (String s : list)
        {
            crop += s + ",";
        }

        final String crops = crop;

        if(TextUtils.isEmpty(farmname)){
            eFarmName.setError("Required!!");
        }
        else if(TextUtils.isEmpty(farmdesc)){
            eFarmDesc.setError("Required!!");
        }
        else if(TextUtils.isEmpty(location)){
            eLocation.setError("Required!!");
        }
        else {
            progressDialog.setMessage("Saving...");
            progressDialog.show();

            StringRequest registerUser = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optBoolean("error") == false) {
                            progressDialog.hide();
                            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(login);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + jsonObject.optString
                                    ("message"), Toast.LENGTH_LONG).show();
                            progressDialog.hide();
                            volleyClearCache();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error: " + error.getMessage());
                    volleyClearCache();
                    progressDialog.hide();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("farmname", farmname);
                    params.put("farmdesc", farmdesc);
                    params.put("crops", crops);
                    params.put("location", location);
                    params.put("userID", user);
                    return params;
                }
            };

            registerUser.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy
                    .DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(registerUser,
                    "UserRegistration");
        }
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
