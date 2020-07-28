package com.bluesoft.cht;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bluesoft.cht.manejoapiback.ActivityChat;
import com.bluesoft.cht.manejoapiback.ManejoAPIUsers;
import com.bluesoft.cht.manejoapiback.ManejoURL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ManejoURL manejoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manejoURL = new ManejoURL((Activity)this);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "En construcción...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void clickGo(View view) {
        ManejoAPIUsers manejoAPIUsers = new ManejoAPIUsers(this, (Activity) this);
        //manejoAPIUsers.verificarUsuario("admin", "admin");
        TextView txtUser = (TextView)findViewById(R.id.txtUser);
        TextView txtPass = (TextView)findViewById(R.id.txtPass);
        manejoAPIUsers.login(txtUser.getText().toString(), txtPass.getText().toString(), this);
        //Intent intent = new Intent(this, ActivityChat.class);
        //startActivityForResult(intent, 0);
    }

    public void setEndPoint(final View view) {
        final Activity activity = (Activity)this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Servidor");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //m_Text = input.getText().toString();
                manejoURL.setBase(input.getText().toString(), activity);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
