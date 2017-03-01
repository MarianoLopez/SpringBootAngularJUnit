/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Mariano
 */
@Entity
@Table(name = "profile")//tabla en db
/*
    Desde netbeans la clase se puede generar automaticamente desde db
    New File -> Create Hibernate mapping POJO from db -> seleccionar tablas
*/
public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id//pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)//identity
    @Basic(optional = false)//obligatorio 
    @Column(name = "id")//columna en db
    private Long id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "type",unique = true)
    private String type;
    
    @JsonIgnore//Al parsear a json ignorar el atributo userList para evitar recursión
    /*Releción de muchos a muchos con User, las operaciones en Profile seran PERSISTENTES y en User se hará MERGE (incluye delete)*/
    @ManyToMany(mappedBy = "profileList",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> userList;
    //
    public Profile() {
    }

    public Profile(Long id) {
        this.id = id;
    }

    public Profile(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profile)) {
            return false;
        }
        Profile other = (Profile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.z.models.Profile[ id=" + id + " ]";
    }
    
}
