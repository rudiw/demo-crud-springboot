package com.rudiwijaya.crud.demo.dao.impl;

import com.rudiwijaya.crud.demo.dao.PersonDao;
import com.rudiwijaya.crud.demo.data.jpa.JpaRepositoryBase;
import com.rudiwijaya.crud.demo.model.jpa.Person;

/**
 * @author rudiwijaya
 *
 */
public class PersonDaoImpl extends JpaRepositoryBase<Person, Long> implements PersonDao {

	public PersonDaoImpl() {
		super(Person.class);
	}

}
