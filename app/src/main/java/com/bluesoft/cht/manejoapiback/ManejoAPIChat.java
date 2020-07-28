package com.bluesoft.cht.manejoapiback;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluesoft.cht.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ManejoAPIChat {

    private Context context;
    private RequestQueue queue;
    private int user_idLoged;
    private int user_id2;
    private TextView txtChat;
    private String user1Name;
    private String user2Name;
    private String user_logged_name="";
    private LinearLayout lytChat;
    private LinearLayout lytCouples;
    private Activity activity;
    private String prefijo = "-";

    ManejoEncriptacion  manejoEncriptacion;

    ManejoURL manejoURL;

    public ManejoAPIChat(Context context, int user_id, int user2, LinearLayout lytChat, LinearLayout lytCouples, TextView txtChat, String user_logged_name, Activity activity) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        user_idLoged = user_id;
        user_id2 = user2;
        this.lytChat = lytChat;
        this.lytCouples = lytCouples;
        this.txtChat = txtChat;
        this.user_logged_name = user_logged_name;
        this.activity = activity;
        Random r = new Random();
        int tope = r.nextInt(18)+1;
        for(int i=0; i<tope; i++) {
            prefijo += "-";
        }
        prefijo += ">";
        manejoEncriptacion = new ManejoEncriptacion();
        manejoURL = new ManejoURL(activity);
    }

    public String getUser1Name() {
        return user1Name;
    }

    public void setUser1Name(String user1Name) {
        this.user1Name = user1Name;
    }

    public String getUser2Name() {
        return user2Name;
    }

    public void setUser2Name(String user2Name) {
        this.user2Name = user2Name;
    }

    public void setUser_id2(int user_id2) {
        this.user_id2 = user_id2;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enviarTxt(View view, String chat) {
        String url = manejoURL.url_enviarTxt();
        JSONObject object = new JSONObject();
        JSONArray jsonArray =  new JSONArray();
        String chatCrip = manejoEncriptacion.encriptar(prefijo+chat);
        //System.out.println("chat cripto = "+chatCrip);
        try {
            try {
                object.put("user_id1", user_idLoged);
                object.put("user_id2", user_id2);
                object.put("content", chatCrip);
                object.put("type", 1);
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Enter the correct url for your api service site
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                     new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //System.out.println("enviarTxt "+ response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //System.out.println(("Error getting response "+error));
                }
            });
            queue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMyChat(final TextView textView, final Activity activity) {
        String url = manejoURL.url_getMyChat();
        JSONObject object = new JSONObject();
        JSONArray jsonArray =  new JSONArray();
        try {
            try {
                object.put("user_id1", user_idLoged);
                object.put("user_id2", user_id2);
                object.put("ch", 1);
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println("getMyChat()------------------EJECUTANDO "+ new Date());
            // Enter the correct url for your api service site
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                    new Response.Listener<JSONArray>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(JSONArray response) {
                            //String total =  user1Name+" - "+user2Name + "\n";
                            String total =  "";
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jresponse = null;
                                String texto = "";
                                String decryptedString="";
                                try {
                                    jresponse = response.getJSONObject(i);
                                    //texto = jresponse.getString("content");
                                    texto += jresponse.getString("content");
                                    decryptedString = manejoEncriptacion.desencriptar(texto);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //System.out.println("-----------------------------------------------------");
                                //System.out.println(texto);
                                //System.out.println(decryptedString);
                                total += decryptedString + "\n";
                                //total += texto + "\n";
                            }
                            textView.setText(total);
                            if(activity!=null && lytChat.getVisibility()==View.VISIBLE) {
                                if(user_logged_name.equals(user2Name))
                                    activity.setTitle("Chat con " + user1Name);
                                else
                                    activity.setTitle("Chat con " + user2Name);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //textView.setText("Error getting response "+error);
                            Toast.makeText(context, "Error getting response "+error, Toast.LENGTH_LONG).show();
                        }
                });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    public void getCouple(final Context ctx, final ListView listview, final ArrayList<String> txtCouples){
        String url = manejoURL.url_getCouple()+String.valueOf(user_idLoged);
        final ArrayList<String> listaNombre1 = new ArrayList();
        final ArrayList<String> listaNombre2 = new ArrayList();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray =  new JSONArray(response);
                        } catch (JSONException e) {
                            //System.out.println("error jsonarray");
                            e.printStackTrace();
                        }
                        int le = jsonArray.length();
                        //txtCouples.add(response);
                        JSONObject jsonObject = new JSONObject();
                        for (int i = 0; i < le; i++) {
                            jsonObject = new JSONObject();
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                txtCouples.add("Charla\n"+jsonObject.getString("name1") + " - " + jsonObject.getString("name2"));
                                listaNombre1.add(jsonObject.getString("name1"));
                                listaNombre2.add(jsonObject.getString("name2"));
                                //System.out.println(txtCouples);
                            } catch (JSONException e) {
                                //System.out.println("error jsonarray.length");
                                e.printStackTrace();
                            }
                        }

                        //System.out.println(response);
                        MyAdapter myAdapter = new MyAdapter(ctx, R.layout.fila, txtCouples);
                        listview.setAdapter(myAdapter);
                        final JSONArray finalJsonArray = jsonArray;
                        //ELECCION DE LA LISTA DE CHARLAS
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                try {
                                    Toast.makeText(ctx, txtCouples.get(position), Toast.LENGTH_LONG).show();
                                    user_id2 = finalJsonArray.getJSONObject(position).getInt("user_id2");

                                    if(user_id2 == user_idLoged) {
                                        user_id2 = finalJsonArray.getJSONObject(position).getInt("user_id1");
                                    }

                                    //System.out.println(finalJsonArray);
                                    //System.out.println("user_id1 = "+user_idLoged);
                                    //System.out.println("user_id2 = "+user_id2);

                                    ejecutar();  //temporizador cada x segundos de chequeo de nuevos mensajes


                                    user1Name = listaNombre1.get(position);
                                    user2Name = listaNombre2.get(position);

                                    if(user_logged_name.equals(user2Name)) {
                                        String tmp = user1Name;
                                        user1Name = user2Name;
                                        user2Name = tmp;
                                    }

                                    //System.out.println("user1: "+user1Name+" user2: "+user2Name);

                                    //System.out.println("primera llamada getMyChat");
                                    getMyChat(txtChat, activity);

                                    //hago visible la sala de chat, quito vista de couples
                                    lytChat.setVisibility(View.VISIBLE);
                                    lytCouples.setVisibility(View.GONE);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtCouples.add("VolleyError! "+error);
                //System.out.println(error);
            }
        });
        queue.add(stringRequest);
    }

    //TEMPORIZADOR--REFRESCO CHAT--------------------------------------------------------------
    public void hilo() {
        try {
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ejecutar() {
        time time = new time();
        time.execute();
    }

    public class time extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i=1; i<=3; i++) {
                hilo();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ejecutar();
            //Toast.makeText(context, "cada x segundos", Toast.LENGTH_SHORT).show();
            getMyChat(txtChat, activity);
        }
    }
}
