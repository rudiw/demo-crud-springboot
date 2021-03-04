package com.rudiwijaya.crud.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableList;
import com.rudiwijaya.crud.demo.dao.PersonDao;
import com.rudiwijaya.crud.demo.model.jpa.Person;
import com.rudiwijaya.crud.demo.model.rs.PersonPlug;
import com.rudiwijaya.crud.demo.model.rs.UpsertPerson;
import com.rudiwijaya.crud.demo.service.PersonService;

/**
 * @author Rudi_W144
 *
 */
public class PersonServiceImpl implements PersonService {
	
	private final TransactionTemplate txTemplate;
	private final PersonDao personDao;
	
	public PersonServiceImpl(final PlatformTransactionManager ptMgr, final PersonDao personDao) {
		super();
		this.txTemplate = new TransactionTemplate(ptMgr);
        txTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        this.personDao = personDao;
	}

	@Override
	public PersonPlug findOne(final long upId) {
		final Optional<Person> optPerson = personDao.findById(upId);
		if (optPerson.isPresent()) {
			return new PersonPlug().copy(optPerson.get());
		}
		return null;
	}
	
    @Override
	public Page<PersonPlug> findAll(final Pageable pageable) {
        final Page<PersonPlug> execute = txTemplate.execute(new TransactionCallback<Page<PersonPlug>>() {
            @Override
            public Page<PersonPlug> doInTransaction(TransactionStatus status) {
                final Page<Person> page = personDao.findAll(pageable);
                final List<PersonPlug> people = page.getContent().stream().map(new Function<Person, PersonPlug>() {
                    @Override
                    public PersonPlug apply(Person jpaPerson) {
                        return new PersonPlug().copy(jpaPerson);
                    }
                }).collect(Collectors.toList());

                return new PageImpl<>(people, pageable, page.getTotalElements());
            }
        });

        return execute;
    }

	@Override
	public PersonPlug add(UpsertPerson upPerson) {
		final Person addedPerson = txTemplate.execute(
				new TransactionCallback<Person>() {
            @Override
            public Person doInTransaction(TransactionStatus status) {
            	final Person newPerson = new Person().copy(upPerson);
            	final Person addedPerson = personDao.add(newPerson);
                return addedPerson;
            }
        });
        return new PersonPlug().copy(addedPerson);
	}

	@Override
	public PersonPlug modify(long id,
			UpsertPerson upPerson) {
        final Person modifiedPerson = txTemplate.execute(
        		new TransactionCallback<Person>() {
            @Override
            public Person doInTransaction(TransactionStatus status) {
            	final Person modifyPerson = personDao.findById(id).get().copy(upPerson);
                return personDao.modify(id, modifyPerson);
            }
        });
        return new PersonPlug().copy(modifiedPerson);
	}

	@Override
	public void remove(long id) {
		txTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				personDao.delete(ImmutableList.of(id));
				return null;
			}
		});
	}

}
