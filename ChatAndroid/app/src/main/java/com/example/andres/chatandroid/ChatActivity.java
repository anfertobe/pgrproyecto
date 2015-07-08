package com.example.andres.chatandroid;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by andres on 03/07/2015.
 */
public class ChatActivity extends ActionBarActivity implements MessagesFragment.OnFragmentInteractionListener,
        EditContactDialog.OnFragmentInteractionListener, View.OnClickListener {

    private EditText msgEdit;
    private Button sendBtn;
    private String profileId;
    private String profileName;
    private String profileCarne;
    private String user;
    private SolicitudesHTTP solicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        user = getSharedPreferences(Constantes.sharePreference, Context.MODE_PRIVATE).getString(Constantes.PROPERTY_USER, "user");
        Log.i("user", user);
        profileId = getIntent().getStringExtra(Common.PROFILE_ID);
        Log.i("id",profileId);
        msgEdit = (EditText) findViewById(R.id.msg_edit);
        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        solicitud = new SolicitudesHTTP();

        Cursor c = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), null, null, null, null);
        if (c.moveToFirst()) {
            profileName = c.getString(c.getColumnIndex(DataProvider.COL_NAME));
            profileCarne = c.getString(c.getColumnIndex(DataProvider.COL_CARNE));
            actionBar.setTitle(profileName);
        }
        actionBar.setSubtitle("connecting ...");

        registerReceiver(registrationStatusReceiver, new IntentFilter(Common.ACTION_REGISTER));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                EditContactDialog dialog = new EditContactDialog();
                Bundle args = new Bundle();
                args.putString(Common.PROFILE_ID, profileId);
                args.putString(DataProvider.COL_NAME, profileName);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "EditContactDialog");
                return true;

            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.send_btn:
                send(msgEdit.getText().toString());
                msgEdit.setText(null);
                break;
        }
    }

    @Override
    public void onEditContact(String name) {
        getSupportActionBar().setTitle(name);
    }

    @Override
    public String getProfileEmail() {
        return profileCarne;
    }

    private void send(final String txt) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                    solicitud.send(user, profileCarne, txt);
                    ContentValues values = new ContentValues(2);
                    values.put(DataProvider.COL_MESSAGE, txt);
                    values.put(DataProvider.COL_RECEIVER_EMAIL, profileCarne);
                    values.put(DataProvider.COL_SENDER_EMAIL,user);
                    getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onPause() {
        ContentValues values = new ContentValues(1);
        values.put(DataProvider.COL_COUNT, 0);
        getContentResolver().update(Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE, profileId), values, null, null);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(registrationStatusReceiver);
        //gcmUtil.cleanup();
        super.onDestroy();
    }

    private BroadcastReceiver registrationStatusReceiver = new  BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Common.ACTION_REGISTER.equals(intent.getAction())) {
                switch (intent.getIntExtra(Common.EXTRA_STATUS, 100)) {
                    case Common.STATUS_SUCCESS:
                        getSupportActionBar().setSubtitle("online");
                        sendBtn.setEnabled(true);
                        break;

                    case Common.STATUS_FAILED:
                        getSupportActionBar().setSubtitle("offline");
                        break;
                }
            }
        }
    };

}
