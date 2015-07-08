package com.example.andres.chatandroid;

/**
 * Created by andres on 03/07/2015.
 */
public class Constantes {

    public static final String SERVER_URL = "http://192.168.0.31:8080/ServicioGcm/rest/servergcm";
    public static final String SENDER_ID = "662224336608";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    public static final String PROPERTY_USER = "user";
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    public static final String TAG = "GCMDemo";
    public static final String sharePreference = "preferenciasAndroid";
}
