package com.rudiwijaya.crud.demo.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author rudiwijaya
 * @param <T>
 * @param <ID>
 */
public abstract class PagingAndSortingRepositoryImpl<T, ID> implements MyPagingAndSortingRepository<T, ID> {

    @Override @Transactional(readOnly = true)
    public abstract Iterable<T> findAll(Sort sort);

    @Override @Transactional(readOnly = true)
    public abstract Page<T> findAll(Pageable pageable);

    @Override @Transactional(readOnly = false)
    public <S extends T> S save(S s) {
        return this.saveAll(ImmutableList.of(s)).iterator().next();
    }

    @Override @Transactional(readOnly = false)
    public <S extends T> Iterable<S> saveAll(Iterable<S> iterable){
        throw new UnsupportedOperationException(String.format("Please use #signUp or #modify methods!!!"));
    };

    @Override @Transactional(readOnly = true)
    public abstract Optional<T> findById(ID id);

    @Override @Transactional(readOnly = true)
    public abstract boolean existsById(ID id);

    @Override @Transactional(readOnly = true)
    public abstract Iterable<T> findAll();

    @Override @Transactional(readOnly = true)
    public abstract Iterable<T> findAllById(Iterable<ID> iterable);

    @Override @Transactional(readOnly = true)
    public abstract long count();

    @Override @Transactional(readOnly = false)
    public abstract void deleteById(ID id);

    @Override @Transactional(readOnly = false)
    public void delete(T t) {
        this.deleteAll(ImmutableList.of(t));
    }

    @Override @Transactional(readOnly = false)
    public abstract void deleteAll(Iterable<? extends T> iterable);

    @Override @Transactional(readOnly = false)
    public abstract void deleteAll();

    @Override @Transactional(readOnly = false)
    public T add(T entity) {
        return this.add(ImmutableList.of(entity)).get(0);
    }

    @Override @Transactional(readOnly = false)
    public abstract List<T> add(Collection<T> entities);

    @Override @Transactional(readOnly = false)
    public T modify(ID id, T entity) {
        return this.modify(ImmutableMap.of(id, entity)).get(0);
    }

    @Override @Transactional(readOnly = false)
    public abstract List<T> modify(Map<ID, T> entities);

    @Override @Transactional(readOnly = false)
    public abstract long delete(Collection<ID> ids);
}
