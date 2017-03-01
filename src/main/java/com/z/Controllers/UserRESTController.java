/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.Controllers;

import com.z.Services.UserService;
import com.z.models.User;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mariano
 */
@RestController//retorna JSON
@RequestMapping("/users")//url padre
public class UserRESTController{
@Autowired
private UserService userService;//capa de servicios de user
  
  @RequestMapping("/")
  public List<User> getAll(){return userService.getAll();}//select *
  
  @RequestMapping("/{username}")
  public User get(@PathVariable(value="username") String username){return userService.getByUserName(username);}//select where
  
  @Secured({"ROLE_ADMIN"})//se necesita el rol admin para acceder al método
  @RequestMapping(value= "/",method = RequestMethod.POST)
  public ResponseEntity insert(@RequestBody @Valid User user,//en el cuerpo de la petición se recibe un JSON que se parsea a User automaticamente 
          BindingResult result){//Result es el resultado de la validación "@Valid" de user
      System.out.println("User request insert: "+user.toString());
      return userService.insert(user,result);
  }
  
  @Secured({"ROLE_ADMIN"})
  @RequestMapping(value= "/",method = RequestMethod.PUT)
  public ResponseEntity update(@RequestBody @Valid User user,BindingResult result){
      System.out.println("User request update: " + user.toString());
      return userService.update(user, result);
  }
  
  @Secured({"ROLE_ADMIN"})
  @RequestMapping(value= "/",method = RequestMethod.DELETE)
  public ResponseEntity delete(@RequestBody User user){
      System.out.println("User request delete: " + user.toString());
      return userService.delete(user);
  }
  
}
