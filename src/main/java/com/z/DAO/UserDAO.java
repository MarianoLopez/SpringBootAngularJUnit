/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.DAO;

import com.z.models.User;
import java.util.List;


import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mariano
 */

/*
Los Objetos de Acceso a Datos son un Patrón de los subordinados de Diseño Core J2EE y 
considerados una buena práctica. La ventaja de usar objetos de acceso a datos es que cualquier 
objeto de negocio (aquel que contiene detalles específicos de operación o aplicación) no requiere 
conocimiento directo del destino final de la información que manipula.

@Repository is specialized implementation of @Component for indicating the Data Access Objects (DAO).
Advantage of using the @Repository annotation, importing the DAOs into the DI container and also this 
annotation makes the unchecked exceptions (thrown from DAO methods) eligible for translation into 
Spring DataAccessException.

By using @Transactional, many important aspects such as transaction propagation are handled 
automatically.
*/
@Repository
@Transactional(readOnly = true) //readOnly = true para todos los métodos sin @Transactional
public class UserDAO{
  @Autowired
  private SessionFactory sessionFactory;//hibernate
  
  @Transactional(readOnly = false)  
  public void save(User user){this.sessionFactory.getCurrentSession().persist(user);}  
  
  @Transactional(readOnly = false)
  public void update(User user){
      if(user.getPassword()==null||user.getPassword().equals("")){
          //recuperar anterior contraseña
          Query query = this.sessionFactory.getCurrentSession().createQuery("select password from User WHERE username=:username");
          query.setParameter("username", user.getUsername());
          String old_password = query.uniqueResult().toString();
          user.setPassword(old_password);//mantener contraseña vieja
      }
      this.sessionFactory.getCurrentSession().update(user);
  }  
  
  @Transactional(readOnly = false)
  public void delete(User user) {this.sessionFactory.getCurrentSession().delete(user);}
  
  public List<User> getAll() {return this.sessionFactory.getCurrentSession().createQuery("from User").list();}
  
  public User getByUserName(String username) {
    Query q = this.sessionFactory.getCurrentSession().createQuery("from User WHERE username=:username");
    q.setParameter("username", username);
    return (User) q.uniqueResult();
  }
}