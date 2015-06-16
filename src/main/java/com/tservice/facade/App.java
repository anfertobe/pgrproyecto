package com.tservice.facade;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * Hello world!
 *
 */
@Service
public class App 
{
    public void main(String RegId)
    {
        System.out.println( "Sending POST to GCM" );
        
        String apiKey = "AIzaSyDDO1Rb4lyjbUp5HjeYJ-LMCrm-RiiAYZU";
        Content content = createContent(RegId);
        
        POST2GCM.post(apiKey, content);
    }
    
    public static Content createContent(String RegId){
		
		Content c = new Content();
		
		c.addRegId(RegId);
		c.createData("Mensaje Prueba", "Este mensaje es para revisar el funcionamiento de la aplicacion");
		
		return c;
    }
}
