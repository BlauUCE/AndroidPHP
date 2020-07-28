package com.bluesoft.cht.manejoapiback;


import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//URL ENDPOINT
public class ManejoURL {

    private String url_base = "https://chato12.000webhostapp.com/cht/";

    public ManejoURL(Activity activity) {
        //verificar si existe archivo de configuracion del servidor
        try
        {
            BufferedReader fin = new BufferedReader(new InputStreamReader(activity.openFileInput("inicio.txt")));
            String texto = fin.readLine();
            //System.out.println("ARCHIVO ____________________ "+texto);
            url_base = texto;
            fin.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error al leer fichero desde memoria interna");
            url_base = "https://chato12.000webhostapp.com/cht/";
            try {
                OutputStreamWriter archivo = new OutputStreamWriter(activity.openFileOutput("inicio.txt", Activity.MODE_PRIVATE));
                archivo.write(url_base);
                archivo.flush();
                archivo.close();
            } catch (IOException e) {
                System.out.println("error archivo "+e);
                url_base = "https://chato12.000webhostapp.com/cht/";
            }
        }
    }

    public void setBase(String base, Activity activity) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(activity.openFileOutput("inicio.txt", Activity.MODE_PRIVATE));
            archivo.write(base);
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
            System.out.println("error archivo "+e);
            url_base = "https://chato12.000webhostapp.com/cht/";
        }
    }

    //getAllLocsOfUser(int idUser, final TextView textView)
    public String url_getAllLocsOfUser() { return url_base+"loc.php?id="; }

    //enviarTxt(View view, String chat)
    public String url_enviarTxt() { return url_base+"chat.php"; }

    //getMyChat(final TextView textView, final Activity activity)
    public String url_getMyChat() { return url_base+"chat.php?id=1"; }

    //getCouple(final Context ctx, final ListView listview, final ArrayList<String> txtCouples)
    public String url_getCouple() { return url_base+"couple.php?user_id1="; }

    //crearCouple(int user1, int user2, String username)
    public String url_crearCouple() { return url_base+"couple.php"; }

    //getAll() user
    public String url_getAll() { return url_base+"user.php"; }

    //login(final String user, final String pass, final Context context)
    public String url_login() { return url_base+"user.php?id=1"; }

    //crear usuario
    public String url_crearusr() { return url_base+"user.php"; }

    //enviar ubicacion
    public String url_sendubicacion() { return url_base+"loc.php?id=1"; }

}
