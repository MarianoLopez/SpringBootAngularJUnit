/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.Services;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

/**
 *
 * @author MarianoLopez
 */
public interface ServiceInterface<T> {
    public List<T> getAll();
    public ResponseEntity insert(T t,BindingResult result);
    public ResponseEntity delete(T t);
    public ResponseEntity update(T t,BindingResult result);
}
