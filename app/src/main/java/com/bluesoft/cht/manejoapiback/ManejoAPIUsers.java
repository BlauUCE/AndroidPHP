package com.bluesoft.cht.manejoapiback;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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

public class ManejoAPIUsers {

    private Context context;
    private RequestQueue queue;

    public ManejoAPIUsers(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
    }

    //verifica si existen los datos del usuario ingresado
    /*public boolean verificarUsuario(final String user, final String pass) {
        String url = "https://lucho27.000webhostapp.com/chat/user.php";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonUser;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                if(user.equals(jo.getJSONObject("user")) && pass.equals(jo.getJSONObject("pass"))) {
                                    //usuario encontrado
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("VolleyError! "+error);
            }
        });
        queue.add(stringRequest);
        return true;
    }
*/


    //get all
    public String getAll() {
        String url = "https://lucho27.000webhostapp.com/chat/user.php";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //textView.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("VolleyError! "+error);
            }
        });
        queue.add(stringRequest);
        return stringRequest.toString();
    }

    //login
    public void login(final String user, final String pass, final Context context) {
        String url = "https://lucho27.000webhostapp.com/chat/user.php?id=1";
        JSONObject object = new JSONObject();
        try {
            try {
                object.put("user", user);
                object.put("pass", pass);
                object.put("log", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Enter the correct url for your api service site
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            int idUser = 0;
                            //lanzar ventana de chat
                            try {
                                //System.out.println(response.get("0"));
                                idUser = Integer.valueOf(response.get("0").toString()); //respuesta del login, id del usuario aceptado
                                if(idUser > 0) {
                                    //lanzar actividad  de chat
                                    Intent intent = new Intent(context, ActivityChat.class);
                                    intent.putExtra("user_id1", idUser);
                                    intent.putExtra("user_logged_name", user);
                                    context.startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(idUser < 0) {
                                //System.out.println("login data " + response);
                                Toast.makeText(context, "ACCESO NEGADO", Toast.LENGTH_LONG).show();

                                if(user.isEmpty() || pass.isEmpty()) {
                                    Toast.makeText(context, "Ingrese datos válidos!", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                //Yes button clicked
                                                String url = "https://lucho27.000webhostapp.com/chat/user.php";
                                                JSONObject object = new JSONObject();
                                                JSONArray jsonArray =  new JSONArray();
                                                try {
                                                    try {
                                                        object.put("name", user);
                                                        object.put("pass", pass);
                                                        object.put("type", 2);
                                                        jsonArray.put(object);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    // Enter the correct url for your api service site
                                                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                                                            new Response.Listener<JSONArray>() {
                                                                @Override
                                                                public void onResponse(JSONArray response) {
                                                                    System.out.println("crear user "+ response.toString());
                                                                }
                                                            }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            System.out.println(("Error getting response "+error));
                                                        }
                                                    });
                                                    queue.add(jsonArrayRequest);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Crear usuario?").setPositiveButton("Si", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //textView.setText(textView.getText()+" "+"Error getting response "+error);
                    System.out.println("login error onErrorResponse "+error);
                    Toast.makeText(context, "ACCESO NEGADO", Toast.LENGTH_LONG).show();
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

