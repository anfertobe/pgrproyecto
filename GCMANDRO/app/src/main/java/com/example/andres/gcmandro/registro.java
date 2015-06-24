package com.example.andres.gcmandro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class registro extends ActionBarActivity {

    private Spinner carreras;
    private EditText carne;
    private EditText nombre;
    private EditText email;
    private EditText password;
    private Button registro;
    private ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        progreso = new ProgressDialog(registro.this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Un momento por favor...");
        progreso.setCancelable(true);
        progreso.setMax(100);

        traerCarrera carrera = new traerCarrera();
        carrera.execute();

        JSONArray array = null;
        try {
            array = new JSONArray(carrera.getArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<String> lista = new LinkedList<String>();

        for(int i = 0; i<array.length(); i++)
            try {
                lista.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        carreras = (Spinner) findViewById(R.id.carreras);
        carreras.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lista));

    }

    private class traerCarrera extends AsyncTask<Void, Integer, Boolean>

    {
        private String array;
        @Override
        protected Boolean doInBackground(Void... params){
            StringBuilder builder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get =
                new HttpGet("http://pgrproyecto.herokuapp.com/rest/servergcm/carreras");
                publishProgress(1);
            try {
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
                Toast.makeText(registro.this,"Tarea Finalizada",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progreso.setProgress(1);
        }
    }

    public void crearspinner(JSONArray array){



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
