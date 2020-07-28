package com.bluesoft.cht.manejoapiback;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bluesoft.cht.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityChat extends AppCompatActivity {

    private TextView txtChat;
    private TextView txtSend;
    private TextView txtCouples;
    private ManejoAPIChat manejoAPIChat;
    private int user_idLoged;
    private int user_id2 = 0;
    private String user_logged_name = "";

    private ManejoAPILoc manejoAPILoc;

    private LinearLayout lytChat;
    private LinearLayout lytCouples;

    private boolean enviadoLoc = false;

    private long tiempoInicio = System.currentTimeMillis();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarUsuarios(null);
            }
        });

        setTitle("Contactos");
        txtSend = (TextView) findViewById(R.id.txtSend);
        txtChat = (TextView) findViewById(R.id.txtChat);
        txtCouples = (TextView) findViewById(R.id.txtCouples);
        txtChat.setMovementMethod(new ScrollingMovementMethod());

        lytChat = (LinearLayout) findViewById(R.id.lytChatPrincipal);
        lytCouples = (LinearLayout) findViewById(R.id.lytCouples);

        //ocultar chat mostrar couples
        lytChat.setVisibility(View.GONE);
        lytCouples.setVisibility(View.VISIBLE);

        user_idLoged = getIntent().getExtras().getInt("user_id1"); //id del usuario logeado
        user_logged_name = getIntent().getExtras().getString("user_logged_name"); //nombre del usuario logeado

        manejoAPIChat = new ManejoAPIChat(this, user_idLoged, user_id2, lytChat, lytCouples, txtChat, user_logged_name, this);

        manejoAPILoc =  new ManejoAPILoc(this, (Activity) this);

        final ArrayList<String> names = new ArrayList();

        ListView listview;
        listview = (ListView) findViewById(R.id.lstCouples);

        manejoAPIChat.getCouple(this, listview, names);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void mostrarUsuarios(View view) {
        lytChat.setVisibility(View.GONE);
        lytCouples.setVisibility(View.VISIBLE);
        setTitle("Contactos");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtSend.getWindowToken(), 0);

        /*final ManejoEncriptacion  manejoEncriptacion = new ManejoEncriptacion();

        String originalString = "narkol";
        String encryptedString = manejoEncriptacion.encriptar(originalString);
        String decryptedString = manejoEncriptacion.desencriptar(encryptedString);
        System.out.println("????????????????????????????????????????????????????????????????");
        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);

        originalString = "hola mensaje";
        encryptedString = manejoEncriptacion.encriptar(originalString);
        decryptedString = manejoEncriptacion.desencriptar(encryptedString);
        System.out.println("????????????????????????????????????????????????????????????????");
        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString); */
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enviarChat(View view) {
        String chat = txtSend.getText().toString();
        if(!chat.isEmpty()) {
            manejoAPIChat.enviarTxt(view, chat); //esta funcion encripta y manda
            txtSend.setText("");
            //pausa para permirtir que se grabe el mensaje enviado
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            miChat();
            //txtChat.setScrollY(txtChat.getText().length());
        }
    }

    public void cerrar(View view) {
        lytChat.setVisibility(View.VISIBLE);
        lytCouples.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtSend.getWindowToken(), 0);
    }

    public void miChat() {
        manejoAPIChat.getMyChat(txtChat, this);
    }


    /////////////////////////////////////////////////////////////////////////////////////777
    // GPS
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(ActivityChat.this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        //latitud.setText("Localización agregada");
        //direccion.setText("");
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    //direccion.setText(DirCalle.getAddressLine(0));
                    //System.out.println("SIRECCION CALLE");
                    //System.out.println(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Localizacion implements LocationListener {
        ActivityChat mainActivity;

        public ActivityChat getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(ActivityChat mainActivity) {
            this.mainActivity = mainActivity;
        }

        // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        // debido a la deteccion de un cambio de ubicacion
        @Override
        public void onLocationChanged(Location loc) {

            //cada 7 minutos enviar lat, long  420000
            if( System.currentTimeMillis() - tiempoInicio >= 420000 ) {
                //loc.getLatitude();
                //loc.getLongitude();
                //String sLatitud = String.valueOf(loc.getLatitude());
                //String sLongitud = String.valueOf(loc.getLongitude());
                manejoAPILoc.enviarUbicacion(user_idLoged, loc.getLatitude(), loc.getLongitude());
                tiempoInicio = System.currentTimeMillis();
            }


            if(enviadoLoc) {
                return; //enviar la ubicacion una sola vez
            } else {
                manejoAPILoc.enviarUbicacion(user_idLoged, loc.getLatitude(), loc.getLongitude());
                enviadoLoc = true;
            }


        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //latitud.setText("GPS Desactivado");

        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //latitud.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    //calcaulat cada 5 minutos
                    /*if( System.currentTimeMillis() - tiempoInicio >= 3000 ) {
                        Log.d("debug", "LocationProvider.AVAILABLE Enviar lat-long ------------------------"+extras);
                        tiempoInicio = System.currentTimeMillis();
                    }*/

                    //Log.d("debug", "LocationProvider.AVAILABLE");

                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}
