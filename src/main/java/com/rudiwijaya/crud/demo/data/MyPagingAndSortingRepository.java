package com.rudiwijaya.crud.demo.data;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author rudiwijaya
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface MyPagingAndSortingRepository<T, ID> extends PagingAndSortingRepository<T, ID> {

    T add(T entity);

    List<T> add(Collection<T> entities);

    T modify(ID id, T entity);

    List<T> modify(Map<ID, T> entities);

    long delete(Collection<ID> ids);

}
