package com.example.andres.chatandroid;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class login extends Activity {

    private Button Registrar;
    private Button login;
    private EditText username;
    private EditText password;

    private String regid;
    private GoogleCloudMessaging gcm;

    private Context context;

    private SolicitudesHTTP solicitud;
    private RegistroDispositivoEnGcm registrogcm;
    private SharedPreferences preferences;

    private ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Registrar = (Button) findViewById(R.id.registro);

        login = (Button) findViewById(R.id.login);

        username = (EditText)findViewById(R.id.username);
        password =  (EditText)findViewById(R.id.pass);

        context = getApplicationContext();

        gcm = GoogleCloudMessaging.getInstance(login.this);

        preferences = getSharedPreferences(Constantes.sharePreference, context.MODE_PRIVATE);

        registrogcm = new RegistroDispositivoEnGcm(context);

        solicitud = new SolicitudesHTTP();

        Registrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progreso = new ProgressDialog(login.this);
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Un momento por favor...");
                progreso.setCancelable(true);
                progreso.setMax(100);

                traerCarrera carrera = new traerCarrera(login.this);
                carrera.execute();
            }
        });



        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progreso = new ProgressDialog(login.this);
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Un momento por favor...");
                progreso.setCancelable(true);
                progreso.setMax(100);

                context = getApplicationContext();

                //Obtenemos el Registration ID guardado

                regid = registrogcm.getRegistrationId(preferences);

                inicioSesion inicio = new inicioSesion(login.this);
                inicio.execute(username.getText().toString(), password.getText().toString());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private class traerCarrera extends AsyncTask<Void, Integer, Boolean> {
        private Context context;
        private JSONArray jsonArray;

        public traerCarrera(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            boolean respuesta = false;

            try {
                SolicitudesHTTP solicitud = new SolicitudesHTTP();
                jsonArray = solicitud.Get();

                Log.i("JsonArray", jsonArray.toString());

                if(!jsonArray.toString().equals(null) && jsonArray.length()>0){
                    Log.i("JsonArray",jsonArray.toString());
                    respuesta = true;
                }


            publishProgress(1);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(MainActivity.class.toString(),
                        "GET request failed" + e.getMessage());
            }

            return true;
        }


        @Override
        protected void onPreExecute() {
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    traerCarrera.this.cancel(true);
                }
            });
            progreso.setProgress(0);
            progreso.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){

                Toast.makeText(login.this, "Tarea Finalizada", Toast.LENGTH_SHORT).show();

                Intent i=new Intent(context,registro.class);

                //Creamos la informaci�n a pasar entre actividades
                Bundle b = new Bundle();
                Log.i("JsonArray", jsonArray.toString());
                b.putString("CARRERAS", jsonArray.toString());

                //A�adimos la informaci�n al intent
                i.putExtras(b);

                startActivity(i);
            }else{
                Toast.makeText(login.this, "Problemas de conexion con el servidor intentelo mas tarde", Toast.LENGTH_SHORT).show();
            }
            progreso.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progreso.setProgress(1);
        }
    }

    private class inicioSesion extends AsyncTask<String, Integer, Boolean> {
        private Context context;

        public inicioSesion(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params){
            boolean respuesta = false;

            try {
                if (gcm == null)
                    gcm = GoogleCloudMessaging.getInstance(context);

                //Nos registramos en los servidores de GCM
                if(regid.equals("")){
                    regid = gcm.register(Constantes.SENDER_ID);
                    Log.i("regid",regid);
                }


                Log.d(Constantes.TAG, "Registrado en GCM: registration_id=" + regid);

                //Guardamos los datos del registro
                registrogcm.setRegistrationId(preferences, params[0], regid);

                SolicitudesHTTP solicitud = new SolicitudesHTTP();
                respuesta = solicitud.inicioSesion(params[0], params[1], regid);

                publishProgress(1);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(MainActivity.class.toString(),
                        "GET request failed" + e.getMessage());
            }

            return respuesta;
        }


        @Override
        protected void onPreExecute() {
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    inicioSesion.this.cancel(true);
                }
            });
            progreso.setProgress(0);
            progreso.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                registrogcm.broadcastStatus(true);
                Toast.makeText(login.this, "Inicio de sesion completo", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(context,MainActivity.class);
                startActivity(i);

            }else{
                Toast.makeText(login.this, "Problemas de conexion con el servidor intentelo mas tarde", Toast.LENGTH_SHORT).show();
            }
            progreso.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progreso.setProgress(1);
        }
    }
}
