
package com.tservice.restcontrollersserver;

import com.tservice.Model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping("/test")
public class TestRestController {
    
//    @RequestMapping(value="/echo/",method = RequestMethod.PUT)        
//    public ResponseEntity<?> consultaX(@RequestBody Postulante input) { 
//     
//        return new ResponseEntity<>("REST API working. Echo:"+input.getIdentificacion(),HttpStatus.ACCEPTED);
//    }
}
