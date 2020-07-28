package com.bluesoft.cht.manejoapiback;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManejoAPILoc {

    private Context context;
    private RequestQueue queue;

    ManejoURL manejoURL;

    public ManejoAPILoc(Context context, Activity activity) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        manejoURL = new ManejoURL(activity);
    }

    //get all
    public String getAllLocsOfUser(int idUser, final TextView textView) {
        String url = manejoURL.url_getAllLocsOfUser()+String.valueOf(idUser);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("VolleyError! "+error);
            }
        });
        queue.add(stringRequest);
        return stringRequest.toString();
    }

    //enviar ubicacion
    public void enviarUbicacion(int user_id, double lat, double lon) {
        String url = manejoURL.url_sendubicacion();
        JSONObject object = new JSONObject();
        //JSONObject jsonArray = new JSONArray();
        try {
            try {
                object.put("user_id", user_id);
                object.put("lat", lat);
                object.put("lon", lon);
                //jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //System.out.println("  enviarUbicacion ***************  "+ object);

            // Enter the correct url for your api service site
            JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //System.out.println("enviarUbicacion "+ response.toString());
                            //System.out.println(" onResponse enviarUbicacion "+ response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //System.out.println("Error getting response enviarUbicacion "+error);
                }
            });
            queue.add(jsonArrayRequest);
        } catch (Exception e) {
            //System.out.println("Error getting response enviarUbicacion "+e);
            e.printStackTrace();
        }
    }
}
