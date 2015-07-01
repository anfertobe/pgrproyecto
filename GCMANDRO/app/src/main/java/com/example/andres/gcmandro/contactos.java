package com.example.andres.gcmandro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.transform.Result;


public class contactos extends ActionBarActivity {

    private ProgressDialog progreso;

    private Button agregar;
    private ListView amigos;
    private EditText idamigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        agregar = (Button) findViewById(R.id.adicionar);
        amigos =(ListView) findViewById(R.id.amigos);
        idamigo = (EditText) findViewById(R.id.idamigo);

        amigos.setEnabled(false);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progreso = new ProgressDialog(contactos.this);
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Un momento por favor...");
                progreso.setCancelable(true);
                progreso.setMax(100);

                confirmarAmigo confirmar = new confirmarAmigo(amigos);
                confirmar.execute(idamigo.getText().toString());
            }
        });

        amigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Seleccion = parent.getItemAtPosition(position).toString();

                Intent i = new Intent(contactos.this, mensajes.class);
                Bundle bundle = new Bundle();
                bundle.putString("amigo",Seleccion);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }


    private class confirmarAmigo extends AsyncTask<String, Integer, Boolean> {
        private String idamigo;
        private ListView ListAmigos;
        private Boolean result;
        private String mensaje;
        public confirmarAmigo(ListView lista){
            ListAmigos = lista;
            result = false;
        }

        @Override
        protected Boolean doInBackground(String... params){

            idamigo = params[0];
            publishProgress(1);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost get = new HttpPost("http://192.168.0.31:8080/ServicioGcm/rest/servergcm/adicion/" + params[0].trim());
            try {
                HttpResponse response = httpClient.execute(get);
                String respStr = EntityUtils.toString(response.getEntity());
                publishProgress(1);
                if(respStr.equals("true")){
                    Log.d("gcm:","Registrado en mi servidor.");
                    System.out.println("RegistroSatisfactorio");
                    result = true;
                }else{
                    mensaje = respStr;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(MainActivity.class.toString(),
                        "GET request failed" + e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    confirmarAmigo.this.cancel(true);
                }
            });
            progreso.setProgress(0);
            progreso.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                progreso.dismiss();
                Toast.makeText(contactos.this, "Amigo adicionado", Toast.LENGTH_SHORT).show();

                ArrayList<String> myStringArray1 = new ArrayList<String>();
                myStringArray1.add(idamigo);
                ArrayAdapter adapter = new ArrayAdapter<String>(contactos.this, android.R.layout.simple_list_item_1, myStringArray1);
                ListAmigos.setAdapter(adapter);
                ListAmigos.setEnabled(true);



            }else{
                progreso.dismiss();
                Toast.makeText(contactos.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progreso.setProgress(1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contactos, menu);
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
