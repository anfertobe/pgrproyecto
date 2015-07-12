package com.tservice.facade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Content implements Serializable {

	
	private List<String> registration_ids;
	private Map<String,String> data;
	
	
	public void addRegId(String regId){
		if(registration_ids == null)
			registration_ids = new LinkedList<String>();
		registration_ids.add(regId);
	}
	
	public void createData(String remitente, String message, String claseMensaje, String idGrupo){
		if(data == null)
			data = new HashMap<String,String>();
                
                    data.put("tipo", claseMensaje);
                    data.put("msg", message);
                
                if(claseMensaje.equals(ConstantesServerGcm.ClaseMensaje)){
                    data.put("remitente", remitente);
                }else if(claseMensaje.equals(ConstantesServerGcm.ClaseGrupo)){
                    data.put("remitente", idGrupo);
                    data.put("remitente2", remitente);
                }                
	}
        
        public void createDataCrearGrupo(String id, String nombre, String claseMensaje){
            
            if(data == null)
			data = new HashMap<String,String>();
            
            data.put("tipo", claseMensaje);
            data.put("id", id);
            data.put("nombre", nombre);
        }
	
	
	public List<String> getRegistration_ids() {
		return registration_ids;
	}

	public void setRegistration_ids(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
