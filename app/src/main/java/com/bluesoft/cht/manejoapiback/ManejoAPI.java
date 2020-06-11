package com.bluesoft.cht.manejoapiback;

import android.content.Context;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ManejoAPI {

    private Context context;
    private TextView textView;
    private RequestQueue queue;

    public ManejoAPI(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
    }

    public TextView getTextView() {
        return textView;
    }
    public void setTextView(TextView textView) {
        this.textView = textView;
    }


    //get all

    //get 1 by id

    public String getAll() {
        String url = "https://lucho27.000webhostapp.com/data/index.php";
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

    public void enviar(String txt) {
        String url = "https://lucho27.000webhostapp.com/data/index.php?id=4";
        JSONObject object = new JSONObject();
        try {
            try {
                //input your API parameters
                object.put("title", "modificaaaaaar");
                object.put("status", "1");
                object.put("content", txt);
                object.put("user_id", 99);
                object.put("put", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Enter the correct url for your api service site
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            textView.setText(textView.getText()+" "+ response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textView.setText(textView.getText()+" "+"Error getting response "+error);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("Response", response);
                        textView.setText(textView.getText()+response.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Jolly");
                params.put("domain", "http://freakyjolly.com");

                return params;
            }

        };

        queue.add(putRequest);*/

        /*try {
            try{
            this.queue = Volley.newRequestQueue(this.context);
            } catch (Exception e) {
                System.out.println("error enviar " +e);
            }
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("title", "1-1");
            jsonBody.put("status", "2");
            jsonBody.put("content", txt);
            jsonBody.put("user_id", 99);
            final String requestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("VOLLEY", response);
                        textView.setText(textView.getText()+response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("VOLLEY", error.toString());
                        textView.setText(textView.getText()+error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    public void enviarPut(int id, String txt) {
        String url = "https://lucho27.000webhostapp.com/data/index.php?id="+String.valueOf(id);
        JSONObject object = new JSONObject();
        try {
            try {
                //input your API parameters
                object.put("title", "modificaaaaaar");
                object.put("status", "1");
                object.put("content", txt);
                object.put("user_id", 99);
                object.put("put", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Enter the correct url for your api service site
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            textView.setText(textView.getText()+" "+ response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textView.setText(textView.getText()+" "+"Error getting response "+error);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("Response", response);
                        textView.setText(textView.getText()+response.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Jolly");
                params.put("domain", "http://freakyjolly.com");

                return params;
            }

        };

        queue.add(putRequest);*/

        /*try {
            try{
            this.queue = Volley.newRequestQueue(this.context);
            } catch (Exception e) {
                System.out.println("error enviar " +e);
            }
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("title", "1-1");
            jsonBody.put("status", "2");
            jsonBody.put("content", txt);
            jsonBody.put("user_id", 99);
            final String requestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("VOLLEY", response);
                        textView.setText(textView.getText()+response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("VOLLEY", error.toString());
                        textView.setText(textView.getText()+error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    public String getMyBase(int id) {
        String url = "https://lucho27.000webhostapp.com/data/index.php?id=5";
        this.queue = Volley.newRequestQueue(this.context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText("My base: " + response);
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

}
