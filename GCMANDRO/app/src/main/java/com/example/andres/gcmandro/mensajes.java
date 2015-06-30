package com.example.andres.gcmandro;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.app.ActionBar.LayoutParams;
import android.os.*;
import android.view.*;
import android.widget.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.*;



public class mensajes extends ActionBarActivity {

    private ViewGroup layout;
    private ScrollView scrollView;
    private String mensaje;
    private Button envio;
    private EditText mensajeEnvio;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        layout = (ViewGroup) findViewById(R.id.content);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        Bundle bun = this.getIntent().getExtras();

        envio = (Button)findViewById(R.id.envio);
        mensajeEnvio = (EditText)findViewById(R.id.mensaje);

        if(bun.containsKey("msgEntrada")){
            mensaje = bun.getString("msgEntrada");
            Log.i("Mensaje recibido", mensaje);
            addLeft(mensaje);
        }


        envio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mensaje = mensajeEnvio.getText().toString();
                addRight(mensaje);

                Bundle bundle = getIntent().getExtras();

                enviarMensaje envio = new enviarMensaje();
                envio.execute(mensajeEnvio.getText().toString(), bundle.getString("amigo"));

            }
        });


    }

    private class enviarMensaje extends AsyncTask<String, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(String... params){
            StringBuilder builder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post =
                    new HttpPost("http://192.168.0.31:8080/ServicioGcm/rest/servergcm/mensaje");
            publishProgress(1);
            try {

                JSONObject mensaje = JsonMensaje(params[0],params[1]);

                StringEntity entity = new StringEntity(mensaje.toString());
                entity.setContentType("application/json;charset=UTF-8");
                entity.setContentEncoding(new
                        BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));

                post.setEntity(entity);

                HttpResponse response = httpClient.execute(post);
                System.out.println("execute");
                String reqResponse= EntityUtils.toString(response.getEntity());
                System.out.println("entity");
                System.out.println(reqResponse);
                if(reqResponse.equals("true"))
                    Toast.makeText(mensajes.this,"mensaje enviado", Toast.LENGTH_SHORT);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(MainActivity.class.toString(),
                        "GET request failed" + e.getMessage());
            }

            return true;
        }

    }

    public JSONObject JsonMensaje(String mensaje, String destino) throws JSONException {
        JSONObject Mensajes = new JSONObject();
        Mensajes.put("id","0");
        Mensajes.put("grupos",JSONObject.NULL);
        Mensajes.put("usuariosByUsuariodestino",JsonDestino(destino));
        Mensajes.put("usuariosByUsuariosorigen",JSONObject.NULL);
        Mensajes.put("fecha", JSONObject.NULL);
        Mensajes.put("visto",JSONObject.NULL);
        Mensajes.put("contenido",mensaje);

        return Mensajes;
    }

    public JSONObject JsonDestino(String destino) throws JSONException {
        JSONObject usuarioDestino = new JSONObject();
        usuarioDestino.put("carne",destino);
        usuarioDestino.put("nombre",JSONObject.NULL);
        usuarioDestino.put("identificaciongoogle",JSONObject.NULL);
        usuarioDestino.put("email",JSONObject.NULL);
        usuarioDestino.put("password",JSONObject.NULL);
        usuarioDestino.put("perfil",JSONObject.NULL);
        usuarioDestino.put("carrerases",JSONObject.NULL);
        usuarioDestino.put("gruposes_1",JSONObject.NULL);

        return usuarioDestino;

    }

    public JSONObject JsonOrigen(String origen) throws JSONException {
        JSONObject usuarioOrigen = new JSONObject();
        usuarioOrigen.put("carne",origen);
        usuarioOrigen.put("nombre",JSONObject.NULL);
        usuarioOrigen.put("identificaciongoogle",JSONObject.NULL);
        usuarioOrigen.put("email",JSONObject.NULL);
        usuarioOrigen.put("contraseña",JSONObject.NULL);
        usuarioOrigen.put("perfil",JSONObject.NULL);
        usuarioOrigen.put("carrerases",JSONObject.NULL);
        usuarioOrigen.put("gruposes_1",JSONObject.NULL);

        return usuarioOrigen;
    }
    public void addRight(String mensaje)
    {
        addChild(true, mensaje);
    }

    public void addLeft(String mensaje)
    {
        addChild(false, mensaje);
    }

    private void addChild(boolean right, String Mensaje)
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        int id = R.layout.layout_right;
        if (right)
        {
            id = R.layout.layout_left;
        }

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

        TextView textView = (TextView) relativeLayout.findViewById(R.id.textViewDate);
        Log.i("Mensaje recibido", Mensaje);
        textView.setText(String.valueOf(Mensaje));

        //layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (right)
        {
            params.gravity = Gravity.RIGHT;
        }
        params.topMargin = 15;
        relativeLayout.setPadding(5, 3, 5, 3);
        relativeLayout.setLayoutParams(params);
        ///////

        layout.addView(relativeLayout);

        //scroll to last element
        //http://stackoverflow.com/questions/6438061/can-i-scroll-a-scrollview-programmatically-in-android
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        ///////
    }
}
