package com.rudiwijaya.crud.demo.dao;

import com.rudiwijaya.crud.demo.data.MyPagingAndSortingRepository;
import com.rudiwijaya.crud.demo.model.jpa.Person;

/**
 * @author rudiwijaya
 *
 */
public interface PersonDao extends MyPagingAndSortingRepository<Person, Long> {

}
