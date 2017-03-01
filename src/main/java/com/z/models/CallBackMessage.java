/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.models;

/**
 *
 * @author MarianoLopez
 */
//clase holder para devolver mensajes en la api rest
public class CallBackMessage {
    public String message;
    public Object data;

    public CallBackMessage(){}
    public CallBackMessage(String message, Object data) {
        this.message = message;
        this.data = data;
    }
    
    
}
