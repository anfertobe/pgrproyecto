package com.example.andres.chatandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andres on 03/07/2015.
 */
public class AddContactDialog extends DialogFragment {
    private AlertDialog alertDialog;
    private EditText et;
    private ProgressDialog progreso;
    private Context context;

    public static AddContactDialog newInstance() {
        AddContactDialog fragment = new AddContactDialog();
        return fragment;
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        et = new EditText(getActivity());
        et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        et.setHint("#carne");
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Adiconar Amigo").setMessage("Adicionar Amigo")
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .setView(et).create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button okBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String carne = et.getText().toString();
                        try {
                            progreso = new ProgressDialog(getActivity());
                            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progreso.setMessage("Un momento por favor...");
                            progreso.setCancelable(true);
                            progreso.setMax(100);
                            AdicionAmigo adicion = new AdicionAmigo(context);
                            adicion.execute(carne);

                        } catch (SQLException sqle) {}
                        //alertDialog.dismiss();
                    }
                });
            }
        });
        return alertDialog;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class AdicionAmigo extends AsyncTask<String, Integer, Boolean> {

        Context context;
        JSONObject json;

        public AdicionAmigo(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean result = false;

            SolicitudesHTTP solicitudpost = new SolicitudesHTTP();

            json = solicitudpost.Post(params[0]);

            if(json.has("nombre"))
                result = true;

            return result;
        }

        @Override
        protected void onPreExecute() {
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AdicionAmigo.this.cancel(true);
                }
            });
            progreso.setProgress(0);
            progreso.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {



            if(result){

                Toast.makeText(context, "Amigo agregado", Toast.LENGTH_SHORT).show();
                ContentValues values = new ContentValues(2);
                try {
                    values.put(DataProvider.COL_NAME, json.getString("nombre"));
                    values.put(DataProvider.COL_CARNE, json.getString("carne"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().getContentResolver().insert(DataProvider.CONTENT_URI_PROFILE, values);
            }else{
                Toast.makeText(context, "Este contacto no se encuentra registrado", Toast.LENGTH_SHORT).show();
            }
            progreso.dismiss();
            alertDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progreso.setProgress(1);
        }
    }
}
