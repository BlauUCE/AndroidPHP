package com.bluesoft.cht.manejoapiback;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

    ManejoURL manejoURL;
    Activity activi;

    public ManejoAPIUsers(Context context, Activity activ) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        manejoURL = new ManejoURL(activ);
        activi = activ;
    }

    //verifica si existen los datos del usuario ingresado
    /*public boolean verificarUsuario(final String user, final String pass) {
        String url = "https://chato12.000webhostapp.com/cht/user.php";
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
        String url = manejoURL.url_getAll();
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void login(final String user, final String pass, final Context context) {
        String url = manejoURL.url_login();
        JSONObject object = new JSONObject();
        final ManejoEncriptacion  manejoEncriptacion = new ManejoEncriptacion();
        String criptoPass="";
        criptoPass = manejoEncriptacion.encriptar(pass);
        try {
            try {
                object.put("user", user);
                object.put("pass", criptoPass); //clave encriptada
                object.put("log", 1);
            } catch (JSONException e) {
                //System.out.println("manejoapiusers--------------------------");
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
                                //System.out.println("manejoapiusers intent--------------------------");
                                e.printStackTrace();
                            }
                            if(idUser < 0) {
                                //System.out.println("login data " + response);
                                Toast.makeText(context, "ACCESO NEGADO", Toast.LENGTH_LONG).show();

                                if(user.isEmpty() || pass.isEmpty()) {
                                    Toast.makeText(context, "Ingrese datos válidos!", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                //DIALOGO DE CREAR USUARIO
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                //Yes button clicked<
                                                String url = manejoURL.url_crearusr();
                                                JSONObject object = new JSONObject();
                                                JSONArray jsonArray =  new JSONArray();
                                                String criptoPass = "";
                                                criptoPass = manejoEncriptacion.encriptar(pass);
                                                //System.out.println("CREAR USUARIO-------- "+pass+" cripto "+criptoPass);
                                                //criptoPass = pass;
                                                //criptoPass = criptoPass.toString();
                                                try {
                                                    try {
                                                        object.put("name", user);
                                                        object.put("pass", criptoPass);
                                                        object.put("type", 2);  //Tipo 2 usuario social
                                                        jsonArray.put(object);
                                                    } catch (JSONException e) {
                                                        System.out.println("error DIALOGO DE CREAR USUARIO--------------------------------");
                                                        e.printStackTrace();
                                                    }
                                                    // Enter the correct url for your api service site
                                                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                                                            new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    Toast.makeText(context, "OK Usuario creado!, ingrese...", Toast.LENGTH_LONG).show();
                                                                    //creo nuevo couple con admin
                                                                    ManejoAPICouple manejoAPICouple = new ManejoAPICouple(context, activi);
                                                                    JSONObject jsonRespuesta = null;
                                                                    try {
                                                                        jsonRespuesta = new JSONObject(response.toString());
                                                                    } catch (JSONException e) {
                                                                        //System.out.println("manejoapiusers- 174 -------------------------");
                                                                        e.printStackTrace();
                                                                    }
                                                                    try {
                                                                        //System.out.println(" id nuevo "+ jsonRespuesta.getInt("id"));
                                                                        manejoAPICouple.crearCouple(jsonRespuesta.getInt("id"), 1, user);
                                                                    } catch (JSONException e) {
                                                                        //System.out.println("manejoapiusers--- 181 -----------------------");
                                                                        e.printStackTrace();
                                                                    }
                                                                    //System.out.println("---------------------------crear user "+ response.toString());
                                                                }
                                                            }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {

                                                                    System.out.println(("LOGIN Error getting response --------------"+error));
                                                                }
                                                    });
                                                    queue.add(jsonArrayRequest);
                                                } catch (Exception e) {
                                                    //System.out.println("manejoapiusers--- 194 -----------------------");
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
                                builder.setMessage("Crear usuario con los datos ingresados?").setPositiveButton("Si", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //textView.setText(textView.getText()+" "+"Error getting response "+error);
                    //System.out.println("login error onErrorResponse "+error);
                    Toast.makeText(context, "ACCESO NEGADO", Toast.LENGTH_LONG).show();
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

