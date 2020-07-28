package com.bluesoft.cht.manejoapiback;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManejoAPICouple {

    private RequestQueue queue;
    private Context context;

    ManejoURL manejoURL;

    public ManejoAPICouple(Context context, Activity activity) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        manejoURL = new ManejoURL(activity);
    }

    public void crearCouple(int user1, String username1, int user2) {
        String url = manejoURL.url_crearCouple();
        JSONObject object = new JSONObject();
        try {
            try {
                object.put("user_id1", user1);
                object.put("name1", username1);
                object.put("user_id2", user2);
                object.put("name2", "admin");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Enter the correct url for your api service site
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //System.out.println("---------------------------crear couple "+ response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(("Error getting response couple "+error));
                }
            });
            queue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
