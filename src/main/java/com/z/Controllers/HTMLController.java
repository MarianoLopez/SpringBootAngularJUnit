/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Mariano
 */

@Controller//indica a spring que la clase es un controlador y debe administrar los request
public class HTMLController {
   
    @RequestMapping("/")//url para el request, GET method by default
    public String index(){
        return "Users";//nombre de la vista sin la extensión
    }
    
    
}
