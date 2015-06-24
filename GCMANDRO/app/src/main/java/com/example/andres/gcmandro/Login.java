package com.example.andres.gcmandro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class Login extends Activity {

    private Button Registrar;
    private Button login;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Registrar = (Button) findViewById(R.id.registro);

        login = (Button) findViewById(R.id.login);

        username = (EditText)findViewById(R.id.username);
        password =  (EditText)findViewById(R.id.pass);

        Registrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),registro.class);
                startActivity(i);
            }
        });



        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(IniciarSesion(username.getText().toString(), password.getText().toString())){
                    Intent i=new Intent(v.getContext(),MenuPrincipal.class);
                    startActivity(i);
                }else{

                }


            }
        });
    }


    private boolean IniciarSesion(String usuario, String password){

        boolean respuesta = false;

        HttpClient httpClient = new DefaultHttpClient();

        HttpPost get =
                new HttpPost("http://pgrproyecto.herokuapp.com/rest/servergcm/login/usuario/password");

        get.setHeader("content-type", "application/json");

        JSONObject dato = new JSONObject();
        try {
            dato.put("usuario", usuario);
            dato.put("password", password);

            StringEntity entity = null;

            entity = new StringEntity(dato.toString());

            get.setEntity(entity);

            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());

            if(respStr.equals("true"))
                respuesta = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respuesta;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_principal, menu);
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
