package com.example.andres.chatandroid;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;


public class chackListGrupos extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private ActionBar actionBar;
    private ContactCursorAdapter1 ContactCursorAdapter;
    public static PhotoCache photoCache;
    private ListView selectContact;
    private ArrayList<String> usuariosSelecionados;
    private Button crear;
    private EditText nombreGrupo;
    private ProgressDialog progreso;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chack_list_grupos);

        //Generate list View from ArrayList
        selectContact = (ListView)findViewById(R.id.listView1);
        selectContact.setOnItemClickListener(this);
        ContactCursorAdapter = new ContactCursorAdapter1(this, null);
        selectContact.setAdapter(ContactCursorAdapter);
        photoCache = new PhotoCache(this);
        usuariosSelecionados = new ArrayList<String>();
        getSupportLoaderManager().initLoader(0, null, this);

        nombreGrupo = (EditText)findViewById(R.id.nombreGrupo);
        crear = (Button) findViewById(R.id.crearGrupo);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usuariosSelecionados.size() > 1){

                    progreso = new ProgressDialog(chackListGrupos.this);
                    progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progreso.setMessage("Un momento por favor...");
                    progreso.setCancelable(true);
                    progreso.setMax(100);

                    RegistroGrupo registro = new RegistroGrupo(chackListGrupos.this);
                    registro.execute();
                }else{
                    Toast.makeText(chackListGrupos.this,"Debe escoger al menos dos usuarios", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                DataProvider.CONTENT_URI_PROFILE,
                new String[]{DataProvider.COL_IDENTIFICACION, DataProvider.COL_CARNE, DataProvider.COL_NAME, DataProvider.COL_COUNT},
                null,
                null,
                DataProvider.COL_IDENTIFICACION + " DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor arg1) {
        ContactCursorAdapter.swapCursor(arg1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ContactCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewHolder holder = (ViewHolder)view.getTag();
        boolean result = false;

        if(holder.checkBox.isChecked()){
            holder.checkBox.setChecked(false);
            result = usuariosSelecionados.remove(holder.textEmail.getText().toString());
            Log.i("Remove", Boolean.toString(result));
        }else{
            holder.checkBox.setChecked(true);
            result = usuariosSelecionados.add(holder.textEmail.getText().toString());
            Log.i("insert", Boolean.toString(result));
        }


    }

    /*****************************************************************/


    private class ContactCursorAdapter1 extends CursorAdapter {

        private LayoutInflater mInflater;
        private View itemLayout;

        public ContactCursorAdapter1(Context context, Cursor c) {
            super(context, c, 0);
            this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return getCursor() == null ? 0 : super.getCount();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            itemLayout = mInflater.inflate(R.layout.check_list_info, parent, false);
            ViewHolder holder = new ViewHolder();
            itemLayout.setTag(holder);
            holder.checkBox = (CheckBox) itemLayout.findViewById(R.id.checkBox1);
            holder.textEmail = (TextView) itemLayout.findViewById(R.id.textEmail);
            holder.avatar = (ImageView) itemLayout.findViewById(R.id.avatar);
            return itemLayout;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.checkBox.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME)));
            holder.textEmail.setText(cursor.getString(cursor.getColumnIndex(DataProvider.COL_CARNE)));
            int count = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_COUNT));
            Log.i("Cant mensajes", Integer.toString(count));

            photoCache.DisplayBitmap(requestPhoto(cursor.getString(cursor.getColumnIndex(DataProvider.COL_IDENTIFICACION))), holder.avatar);

        }
    }

    private static class ViewHolder {
        TextView textEmail;
        ImageView avatar;
        CheckBox checkBox;
    }

    @SuppressLint("InlinedApi")
    private Uri requestPhoto(String email) {
        Cursor emailCur = null;
        Uri uri = null;
        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT >= 11) {
                String[] projection = {ContactsContract.CommonDataKinds.Email.PHOTO_URI};
                ContentResolver cr = getContentResolver();
                emailCur = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection,
                        ContactsContract.CommonDataKinds.Email.ADDRESS + " = ?",
                        new String[]{email}, null);
                if (emailCur != null && emailCur.getCount() > 0) {
                    if (emailCur.moveToNext()) {
                        String photoUri = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.PHOTO_URI));
                        if (photoUri != null)
                            uri = Uri.parse(photoUri);
                    }
                }
            } else if (SDK_INT < 11) {
                String[] projection = {ContactsContract.CommonDataKinds.Photo.CONTACT_ID};
                ContentResolver cr = getContentResolver();
                emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        projection,
                        ContactsContract.CommonDataKinds.Email.ADDRESS + " = ?",
                        new String[]{email}, null);
                if (emailCur.moveToNext()) {
                    int columnIndex = emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.CONTACT_ID);
                    long contactId = emailCur.getLong(columnIndex);
                    uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
                    uri = Uri.withAppendedPath(uri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (emailCur != null)
                    emailCur.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return uri;
    }


    /*************************************************************************/

    private class RegistroGrupo extends AsyncTask<String, Integer, Boolean> {
        private Context context;

        public RegistroGrupo(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params){
            boolean respuesta = false;
            publishProgress(1);

            SolicitudesHTTP solicitud = new SolicitudesHTTP();


            return respuesta;
        }


        @Override
        protected void onPreExecute() {
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    RegistroGrupo.this.cancel(true);
                }
            });
            progreso.setProgress(0);
            progreso.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){

                Toast.makeText(chackListGrupos.this, "Inicio de sesion completo", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(context,MainActivity.class);
                startActivity(i);

            }else{
                Toast.makeText(chackListGrupos.this, "Problemas de conexion con el servidor intentelo mas tarde", Toast.LENGTH_SHORT).show();
            }
            progreso.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progreso.setProgress(1);
        }
    }
}
