/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.Services;

import com.z.DAO.UserDAO;
import com.z.models.Profile;
import com.z.models.User;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mariano
 */
/*
   specialized version of @Component to inform the spring component-scanning mechanism to 
   load the service classes 

   UserDetailsService interface is used in order to lookup the username, password 
   and GrantedAuthorities for any given user. This interface provide only one method 
   which implementing class need to implement.
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername("+username+")");
        User u = userDAO.getByUserName(username);//select user where name = name
        if(u==null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("Username not found");
        }else{
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            //recuperar roles del usuario
            for (Profile profile : u.getProfileList()){
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+profile.getType()));
            }
            System.out.println("authorities :"+grantedAuthorities);
            //return Usuario Spring (username,pass,estado(bool),accountnonexpired,credentialsnonexpired,accountnonlocked,roles)
            return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), u.getState(),true, true, true, grantedAuthorities);
        }
    }
    
 
}
