package com.rudiwijaya.crud.demo.config.rs;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.rudiwijaya.crud.demo.controller.PersonController;
import com.rudiwijaya.crud.demo.model.rs.PersonPlug;

/**
 * @author Rudi_W144
 */
@Component
public class PersonAssembler extends RepresentationModelAssemblerSupport<PersonPlug, PersonModel> {
	
    public PersonAssembler() {
    	super(PersonController.class, PersonModel.class);
	}
    
    @Override
    public PersonModel toModel(PersonPlug entity) {
    	final PersonModel model = instantiateModel(entity);
    	
    	final Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class)
    			.findOne(entity.getId())).withSelfRel();

        model.add(link);
        return model;
    }
    
    @Override
	protected PersonModel instantiateModel(PersonPlug entity) {
		return new PersonModel(entity);
	}
    
}
