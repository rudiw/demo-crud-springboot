package com.rudiwijaya.crud.demo.controller;

import javax.inject.Inject;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rudiwijaya.crud.demo.config.rs.PersonAssembler;
import com.rudiwijaya.crud.demo.config.rs.PersonModel;
import com.rudiwijaya.crud.demo.model.rs.UpsertPerson;
import com.rudiwijaya.crud.demo.model.rs.PersonPlug;
import com.rudiwijaya.crud.demo.service.PersonService;

/**
 * @author rudiwijaya
 *
 */
@RestController("person")
@RequestMapping("person")
public class PersonController {
	
	@Inject
	private PersonService personService;
	@Autowired
	private PersonAssembler personAssembler;
	@Inject
	private PagedResourcesAssembler<PersonPlug> personPagedResourcesAssembler; 
	
	@RequestMapping(value = "/add", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<PersonPlug>> add(@RequestBody final UpsertPerson upPerson) {
		final PersonPlug addedPerson = personService.add(upPerson);
		final EntityModel<PersonPlug> model = personAssembler.toModel(addedPerson);
		return new ResponseEntity<>(model, HttpStatus.CREATED);
	}
	

	@RequestMapping(value = "/{id}", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<PersonPlug>> findOne(
			@PathVariable(value = "id", required = true) final long upId) {
		final PersonPlug person = personService.findOne(upId);

		if (person == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		final EntityModel<PersonPlug> model = personAssembler.toModel(person);
		return new ResponseEntity<>(model, HttpStatus.FOUND);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedModel<PersonModel>> findAll(@PageableDefault(size = 10) final Pageable pageable) {
		final Page<PersonPlug> page = personService.findAll(pageable);

		final PagedModel<PersonModel> resources = personPagedResourcesAssembler.toModel(page, personAssembler);
		return new ResponseEntity<PagedModel<PersonModel>>(resources, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/modify/{id}", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel<PersonPlug>> modify(
			@PathVariable(value = "id", required = true) final long upId,
			@RequestBody final UpsertPerson upPerson) {
		final PersonPlug addedPerson = personService.modify(upId, upPerson);
		final EntityModel<PersonPlug> model = personAssembler.toModel(addedPerson);
		return new ResponseEntity<>(model, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> remove(
			@PathVariable(value = "id", required = true) final long upId) {
		personService.remove(upId);
		return new ResponseEntity<String>("REMOVED", HttpStatus.OK);
	}

}
