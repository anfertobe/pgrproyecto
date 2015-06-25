package com.example.andres.gcmandro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;


public class registro extends ActionBarActivity {

    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";

    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;

    String SENDER_ID = "662224336608";

    static final String TAG = "GCMDemo";

    private String regid;
    private GoogleCloudMessaging gcm;

    private Context context;

    private Spinner carreras;
    private EditText carne;
    private EditText nombre;
    private EditText email;
    private EditText password;
    private Button registro;
    private ProgressDialog progreso;
    private HashMap <String, Integer> dictianaryCarreras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        carne = (EditText) findViewById(R.id.carne);
        nombre = (EditText) findViewById(R.id.nombre);
        email = (EditText) findViewById(R.id.email);
        password =(EditText) findViewById(R.id.contraseña);
        registro = (Button) findViewById(R.id.registrar);
        carreras = (Spinner) findViewById(R.id.carreras);
        dictianaryCarreras = new HashMap<String, Integer>();

        Bundle bundle = this.getIntent().getExtras();

        String carrera = bundle.getString("CARRERAS");

        System.out.println("Llgo: " + carrera);

        JSONArray array = new JSONArray();

        try {
            array = new JSONArray(carrera);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        List<String> lista = new LinkedList<String>();

        for(int i = 0; i<array.length(); i++)
            try {

                JSONObject carrerajson = array.getJSONObject(i);
                dictianaryCarreras.put(carrerajson.getString("nombre"), carrerajson.getInt("id"));
                lista.add(carrerajson.getString("nombre"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        System.out.println(lista.size());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(registro.this,  android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carreras.setAdapter(adapter);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progreso = new ProgressDialog(registro.this);
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Un momento por favor...");
                progreso.setCancelable(true);
                progreso.setMax(100);


                context = getApplicationContext();

                gcm = GoogleCloudMessaging.getInstance(registro.this);

                //Obtenemos el Registration ID guardado
                regid = getRegistrationId(context);

                //si no se obtiene nada se realiza el registro
                if (regid.equals("")) {

                    String carreraSeleccionada = carreras.getSelectedItem().toString();

                    System.out.println(carreraSeleccionada);

                    TareaRegistroGCM tarea = new TareaRegistroGCM(registro.this);
                    tarea.execute(carne.getText().toString(), nombre.getText().toString(), email.getText().toString(), password.getText().toString(), carreraSeleccionada);
                }
            }
        });

    }





    public void RealizarRegistro(){

    }

    private String getRegistrationId(Context context) {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.length() == 0) {
            Log.d(TAG, "Registro GCM no encontrado.");
            return "";
        }

        String registeredUser =
                prefs.getString(PROPERTY_USER, "user");

        int registeredVersion =
                prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        long expirationTime =
                prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String expirationDate = sdf.format(new Date(expirationTime));

        Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser +
                ", version=" + registeredVersion +
                ", expira=" + expirationDate + ")");

        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion) {
            Log.d(TAG, "Nueva versiÃ³n de la aplicaciÃ³n.");
            return "";
        } else if (System.currentTimeMillis() > expirationTime) {
            Log.d(TAG, "Registro GCM expirado.");
            return "";
        }

        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Error al obtener versiÃ³n: " + e);
        }
    }

    private class TareaRegistroGCM extends AsyncTask<String, Integer, Boolean> {

        private Context context;

        public TareaRegistroGCM(Context context){
            this.context = context;
        }


        @Override
        protected Boolean doInBackground(String... params) {
            String msg = "";

            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(SENDER_ID);

                Log.d(TAG, "Registrado en GCM: registration_id=" + regid);

                //Nos registramos en nuestro servidor
                boolean registrado = registroServidor(params[0], params[1], params[2], params[3], params[4], regid);

                //Guardamos los datos del registro
                if (registrado) {
                    setRegistrationId(context, params[0], regid);
                }
            } catch (IOException ex) {
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return true;
        }

        @Override
        protected void onPreExecute() {
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    TareaRegistroGCM.this.cancel(true);
                }
            });
            progreso.setProgress(0);
            progreso.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                progreso.dismiss();
                Toast.makeText(registro.this, "Tarea Finalizada", Toast.LENGTH_SHORT).show();


                Intent i=new Intent(context,Login.class);

                startActivity(i);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progreso.setProgress(1);
        }

    }

    private boolean registroServidor(String carne, String nombre, String email, String password, String carrera, String regId) {

        boolean reg = false;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://proyectopgr.herokuapp.com/rest/servergcm/registro/" + dictianaryCarreras.get(carrera));
        post.setHeader("content-type", "application/json");
        try {
            System.out.println("conexion");



            JSONObject json = new JSONObject();

            json.put("carne", carne);
            json.put("nombre", nombre);
            json.put("identificaciongoogle", regId);
            json.put("email", email);
            json.put("contraseña", password);

            StringEntity entity = new StringEntity(json.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);

            System.out.println(resp);
            System.out.println("execute");
            String respStr = EntityUtils.toString(resp.getEntity());
            System.out.println(respStr);

            if(respStr.equals("true")){
                Log.d(TAG, "Registrado en mi servidor.");
                reg = true;
                System.out.println("RegistroSatisfactorio");
            }
        } catch (Exception e) {
            Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage() + "||" + e.getStackTrace());
        }

        return reg;
    }

    private void setRegistrationId(Context context, String user, String regId) {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USER, user);
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.putLong(PROPERTY_EXPIRATION_TIME,
                System.currentTimeMillis() + EXPIRATION_TIME_MS);

        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro, menu);
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
