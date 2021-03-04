package com.rudiwijaya.crud.demo.config.app;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.rudiwijaya.crud.demo.dao.PersonDao;
import com.rudiwijaya.crud.demo.service.PersonService;
import com.rudiwijaya.crud.demo.service.impl.PersonServiceImpl;

/**
 * @author Rudi_W144
 *
 */
@Configuration
public class ServiceConfig {
	
	@Inject
    private PlatformTransactionManager ptMgr;
	@Inject
	private PersonDao personDao;
	
	@Bean
	public PersonService personService() {
		return new PersonServiceImpl(ptMgr, personDao);
	}
	
}
