package com.rudiwijaya.crud.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rudiwijaya.crud.demo.model.rs.PersonPlug;
import com.rudiwijaya.crud.demo.model.rs.UpsertPerson;


/**
 * @author rudiwijaya
 *
 */
public interface PersonService {
	
	PersonPlug findOne(long id);

	Page<PersonPlug> findAll(Pageable pageable);
	
	PersonPlug add(final UpsertPerson upPerson);

	PersonPlug modify(long id, UpsertPerson upPerson);
	
	void remove(long id);

}
