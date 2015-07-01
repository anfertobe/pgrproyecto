package com.example.andres.gcmandro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Login extends Activity {

    private Button Registrar;
    private Button login;
    private EditText username;
    private EditText password;

    private ProgressDialog progreso;

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

                progreso = new ProgressDialog(Login.this);
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Un momento por favor...");
                progreso.setCancelable(true);
                progreso.setMax(100);


                traerCarrera carrera = new traerCarrera(Login.this);
                carrera.execute();
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
                new HttpPost("https://192.168.0.31:8080/ServicioGcm/rest/servergcm/login/usuario/password");

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

    private class traerCarrera extends AsyncTask<Void, Integer, Boolean> {
        private String array;
        private Context context;

        public traerCarrera(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            StringBuilder builder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get =
                    new HttpGet("http://192.168.0.31:8080/ServicioGcm/rest/servergcm/carreras");
            publishProgress(1);
            try {
                System.out.println("execute");
                HttpResponse response = httpClient.execute(get);
                System.out.println("execute");
                StatusLine statusLine = response.getStatusLine();
                System.out.println("execute");
                HttpEntity entity = response.getEntity();
                System.out.println("entity");
                InputStream content = entity.getContent();
                System.out.println("content");
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(content));
                System.out.println("reader");
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    System.out.println(line);
                }

                array = builder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(MainActivity.class.toString(),
                        "GET request failed" + e.getMessage());
            }

            return true;
        }
        public String getArray(){
            return array;
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
                progreso.dismiss();
                Toast.makeText(Login.this, "Tarea Finalizada", Toast.LENGTH_SHORT).show();

                System.out.println(getArray());

                Intent i=new Intent(context,registro.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("CARRERAS", getArray());

                //Añadimos la información al intent
                i.putExtras(b);

                startActivity(i);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progreso.setProgress(1);
        }
    }
}
