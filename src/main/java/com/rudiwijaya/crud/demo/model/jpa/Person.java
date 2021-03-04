package com.rudiwijaya.crud.demo.model.jpa;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rudiwijaya.crud.demo.model.rs.UpsertPerson;

/**
 * @author Rudi_W144
 */
@Entity(name = "Person")
@Table(name = "person")
public class Person implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;
    
    @Basic(optional = true)
    @Column(nullable = true)
    private String phone;


    public Person() {
        super();
    }


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", phone=" + phone + "]";
	}
	
	public Person copy(final UpsertPerson up) {
		this.name = up.getName();
		this.phone = up.getPhone();
		return this;
	}

}
