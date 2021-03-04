package com.rudiwijaya.crud.demo.config.rs;

import org.springframework.hateoas.EntityModel;

import com.rudiwijaya.crud.demo.model.rs.PersonPlug;

/**
 * @author Rudi_W144
 *
 */
public class PersonModel extends EntityModel<PersonPlug> {

	public PersonModel(final PersonPlug entity) {
		super(entity);
	}

}
