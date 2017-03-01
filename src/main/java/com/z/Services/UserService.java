/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.Services;

import com.z.DAO.UserDAO;
import com.z.Util.ExceptionUtil;
import static com.z.Util.ExceptionUtil.*;
import com.z.models.CallBackMessage;
import com.z.models.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 *
 * @author MarianoLopez
 */
@Service
public class UserService implements ServiceInterface<User> {

    @Autowired
    private UserDAO userDAO;//Inyección(Autowired) de UserDAO(Data Access Object)
    @Autowired
    private PasswordEncoder passwordEncoder;//encriptador

    @Override
    public List<User> getAll() {
        return userDAO.getAll();
    }
    public User getByUserName(String userName){
        return userDAO.getByUserName(userName);
    }
    
    @Override
    public ResponseEntity insert(User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CallBackMessage("Error en insert: "+getErrores(result), result.getFieldErrors()));
        } else {
            try {
                userPasswordEncryp(user);
                userDAO.save(user);//insert
                return ResponseEntity.ok(new CallBackMessage("Usuario añadido", user));
            } catch (Exception e) {
                //ante cualquier excepción se retorna HTTP status 400 + el mensaje del error
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CallBackMessage("Error en insert: " +getCause(e).getMessage(), e));
            }
        }
    }

    @Override
    public ResponseEntity delete(User user) {
        try {
            userDAO.delete(user);//delete
            return ResponseEntity.ok(new CallBackMessage("Usuario eliminado", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CallBackMessage("Error en delete: " + getCause(e).getMessage(), e));
        }
    }

    @Override
    public ResponseEntity update(User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CallBackMessage("Error en actualizar: "+getErrores(result), result.getFieldErrors()));
        } else {
            try {
                userPasswordEncryp(user);
                userDAO.update(user);//update
                return ResponseEntity.ok(new CallBackMessage("Usuario modificado", user));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CallBackMessage("Error en update: " + getCause(e).getMessage(), e));
            }
        }
    }

    private void userPasswordEncryp(User user) {//encriptar solo si no es null o vacía "evitar nullpointer"
        if (user != null && user.getPassword() != null && !user.getPassword().equals("")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println(user.getUsername() + " - Pass Encryp");
        }
    }
}
