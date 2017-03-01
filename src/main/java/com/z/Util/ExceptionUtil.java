/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.Util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 *
 * @author MarianoLopez
 */
public class ExceptionUtil {
    
    //obtener la excepción original del problema
    public static Throwable getCause(Throwable e) {
        Throwable cause = null;
        Throwable result = e;
        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
    
    public static String getErrores(BindingResult result){
        String errores="";
        for (FieldError fieldError : result.getFieldErrors()) {
            errores+=(fieldError.getField()+": "+fieldError.getDefaultMessage()+". ");
        }
        return errores;
    }
}
