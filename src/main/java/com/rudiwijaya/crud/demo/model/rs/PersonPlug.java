package com.rudiwijaya.crud.demo.model.rs;

import java.io.Serializable;

import com.rudiwijaya.crud.demo.model.jpa.Person;

/**
 * @author rudiwijaya
 *
 */
public class PersonPlug implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long id;
	
	private String name;
	
	private String phone;

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
		return "PersonPlug [id=" + id + ", name=" + name + ", phone=" + phone + "]";
	}
	
	public PersonPlug copy(final Person up) {
		this.id = up.getId();
		this.name = up.getName();
		this.phone = up.getPhone();
		return this;
	}
	

}
