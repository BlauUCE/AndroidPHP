package com.bluesoft.cht.manejoapiback;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluesoft.cht.R;

import java.util.ArrayList;

public class ActivityChat extends AppCompatActivity {

    private TextView txtChat;
    private TextView txtSend;
    private TextView txtCouples;
    private ManejoAPIChat manejoAPIChat;
    private int user_idLoged;
    private int user_id2 = 0;
    private String user_logged_name="";

    private LinearLayout lytChat;
    private LinearLayout lytCouples;

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
        txtSend = (TextView)findViewById(R.id.txtSend);
        txtChat = (TextView)findViewById(R.id.txtChat);
        txtCouples = (TextView)findViewById(R.id.txtCouples);
        txtChat.setMovementMethod(new ScrollingMovementMethod());

        lytChat = (LinearLayout)findViewById(R.id.lytChatPrincipal);
        lytCouples = (LinearLayout)findViewById(R.id.lytCouples);

        //ocultar chat mostrar couples
        lytChat.setVisibility(View.GONE);
        lytCouples.setVisibility(View.VISIBLE);

        user_idLoged = getIntent().getExtras().getInt("user_id1"); //id del usuario logeado
        user_logged_name = getIntent().getExtras().getString("user_logged_name"); //nombre del usuario logeado

        manejoAPIChat = new ManejoAPIChat(this, user_idLoged, user_id2, lytChat, lytCouples, txtChat, user_logged_name, this);

        final ArrayList<String> names = new ArrayList();

        ListView listview;
        listview = (ListView) findViewById(R.id.lstCouples);

        manejoAPIChat.getCouple(this, listview, names);

        //miChat();
    }

    public void mostrarUsuarios(View view) {
        lytChat.setVisibility(View.GONE);
        lytCouples.setVisibility(View.VISIBLE);
        setTitle("Contactos");
    }


    public void enviarChat(View view) {
        String chat = txtSend.getText().toString();
        if(!chat.isEmpty()) {
            manejoAPIChat.enviarTxt(view, txtSend.getText().toString());
            txtSend.setText("");
            miChat();
            //txtChat.setScrollY(txtChat.getText().length());
        }
    }

    public void cerrar(View view) {
        lytChat.setVisibility(View.VISIBLE);
        lytCouples.setVisibility(View.GONE);
    }

    public void miChat() {
        manejoAPIChat.getMyChat(txtChat, this);
    }


}
