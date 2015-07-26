package com.tservice.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Hello world!
 *
 */
@Service
public class App 
{
    
    String apiKey = "AIzaSyBIhljIN82q0ZJDKNi9pgu3sMRMLKtTzXk";
    
    public void main(ArrayList<String> RegId, String remitente, String contenidoMensaje, String claseMensaje, String idGrupo)
    {
        System.out.println( "Sending POST to GCM" );
        
        
        Content content = createContent(RegId, remitente, contenidoMensaje, claseMensaje, idGrupo);
        
        POST2GCM.post(apiKey, content);
    }
    
    public static Content createContent(ArrayList<String> RegId, String remitente, String contenidoMensaje, String claseMensaje, String idGrupo){
		
        Content c = new Content();

        for(String id : RegId)
            c.addRegId(id);

        c.createData(remitente, contenidoMensaje, claseMensaje, idGrupo);

        return c;
    }
    
    public void mainCrearGrupo(String idGrupo, String nombre, String claseMensaje, ArrayList<String> RegIds){
        System.out.println( "Sending POST to GCM" );
        
        
        
        Content c = new Content();
        
             for(String id : RegIds)
                c.addRegId(id);
                               
        c.createDataCrearGrupo( idGrupo, nombre, claseMensaje);
        
        POST2GCM.post(apiKey, c);
    }
    
}
