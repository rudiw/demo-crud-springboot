package com.rudiwijaya.crud.demo.data.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.rudiwijaya.crud.demo.data.PagingAndSortingRepositoryImpl;

/**
 * @author Rudi_W144
 * @param <T>
 * @param <ID>
 */
public abstract class JpaRepositoryBase<T, ID> extends PagingAndSortingRepositoryImpl<T, ID> implements MyJpaRepository {

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> entityClass;

    protected final Logger LOG;

    public JpaRepositoryBase(final Class<T> entityClass) {
        super();
        this.entityClass = entityClass;
        LOG = LoggerFactory.getLogger(JpaRepositoryBase.class.getName() + "/" + this.entityClass.getSimpleName());
    }

    @Override @Transactional(readOnly = true)
    public Iterable<T> findAll(Sort sort) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // SELECT * FROM entityClass WHERE ... ORDER BY ... LIMIT x, y
        // FROM
        final CriteriaQuery<T> cq = cb.createQuery(entityClass);
        final Root<T> root = cq.distinct(true).from(entityClass);
        cq.select(root);
        // WHERE
        // ORDER BY
        final List<javax.persistence.criteria.Order> jpaOrders = FluentIterable
                .from(com.google.common.base.Optional.<Iterable<Sort.Order>>fromNullable(sort).or(ImmutableList.<Sort.Order>of()))
                .transform(new Function<Sort.Order, Order>() {
                    @Override
                    @Nullable
                    public javax.persistence.criteria.Order apply(@Nullable Sort.Order input) {
                        return input.getDirection() == Sort.Direction.ASC ? cb.asc(root.get(input.getProperty())) : cb.desc(root.get(input.getProperty()));
                    }
                }).toList();
        cq.orderBy(jpaOrders);

        final TypedQuery<T> typedQuery = em.createQuery(cq);
        final List<T> pageContent = typedQuery.getResultList();

        LOG.debug("FindAll {} returned {} (sort by {})",
                entityClass.getSimpleName(), pageContent.size(), sort);
        return pageContent;
    }

    @Override @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // SELECT COUNT(*) FROM entityClass WHERE ...
        final CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        final Root<T> countRoot = countCq.distinct(true).from(entityClass);
        countCq.select(cb.countDistinct(countRoot));
        final long totalRowCount = em.createQuery(countCq).getSingleResult();


        // SELECT * FROM entityClass WHERE ... ORDER BY ... LIMIT x, y
        // FROM
        final CriteriaQuery<T> cq = cb.createQuery(entityClass);
        final Root<T> root = cq.distinct(true).from(entityClass);
        cq.select(root);
        // WHERE
        // ORDER BY
        final List<javax.persistence.criteria.Order> jpaOrders = FluentIterable
                .from(com.google.common.base.Optional.<Iterable<Sort.Order>>fromNullable(pageable.getSort()).or(ImmutableList.<Sort.Order>of()))
                .transform(new Function<Sort.Order, Order>() {
                    @Override
                    @Nullable
                    public javax.persistence.criteria.Order apply(@Nullable Sort.Order input) {
                        return input.getDirection() == Sort.Direction.ASC ? cb.asc(root.get(input.getProperty())) : cb.desc(root.get(input.getProperty()));
                    }
                }).toList();
        cq.orderBy(jpaOrders);

        final TypedQuery<T> typedQuery = em.createQuery(cq)
                .setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        final List<T> pageContent = typedQuery.getResultList();

        LOG.debug("FindAll {} returned {} of {} rows (paged by {})",
                entityClass.getSimpleName(), pageContent.size(), totalRowCount, pageable);
        return new PageImpl<>(pageContent, pageable, totalRowCount);
    }

    @Override @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<T> cq = cb.createQuery(entityClass);
        final Root<T> root = cq.distinct(true).from(entityClass);
        cq.select(root);
        cq.where(cb.equal(root.get(("id")), id));

        try {
            final T found = em.createQuery(cq).getSingleResult();
            LOG.debug("Found by id '{}'", id);
            return Optional.of( (T) found );
        } catch (NoResultException e) {
            LOG.debug("Not found by id '{}'", id);
            return Optional.empty();
        }
    }

    @Override @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        final Root<T> root = cq.distinct(true).from(entityClass);

        cq.select(cb.countDistinct(root));
        cq.where(cb.equal(root.get("id"), id));

        final Long singleResult = em.createQuery(cq).getSingleResult();
        return singleResult.longValue() > 0;
    }

    @Override @Transactional(readOnly = true)
    public Iterable<T> findAll() {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<T> cq = cb.createQuery(entityClass);
        final Root<T> root = cq.distinct(true).from(entityClass);
        cq.select(root);

        final List<T> result = em.createQuery(cq).getResultList();
        LOG.debug("Found all {} rows", result.size());
        return result;
    }

    @Override @Transactional(readOnly = true)
    public Iterable<T> findAllById(Iterable<ID> iterable) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<T> cq = cb.createQuery(entityClass);
        final Root<T> root = cq.distinct(true).from(entityClass);
        cq.select(root);
        final ImmutableList<ID> ids = ImmutableList.copyOf(iterable);
        cq.where(root.get("id").in(ids));

        final List<T> result = em.createQuery(cq).getResultList();
        LOG.debug("found {} rows by {} ids: {}", result.size(), ids.size(), ids);
        return result;
    }

    @Override @Transactional(readOnly = true)
    public long count() {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        final Root<T> countRoot = countCq.distinct(true).from(entityClass);
        countCq.select(cb.countDistinct(countRoot));
        final long totalRowCount = em.createQuery(countCq).getSingleResult();
        LOG.debug("count {} row(s)", totalRowCount);
        return totalRowCount;
    }

    @Override @Transactional(readOnly = false)
    public void deleteById(ID id) {
        delete(ImmutableList.of(id));
    }

    @Override @Transactional(readOnly = false)
    public void deleteAll(Iterable<? extends T> iterable) {
        for (final T t : iterable) {
            em.remove(t);
        }
    }

    @Override @Transactional(readOnly = false)
    public void deleteAll() {
        final Query q = em.createQuery("DELETE FROM " + entityClass + " e ");
        final int update = q.executeUpdate();
        LOG.info("Removed {} item(s)", update);
    }

    @Override @Transactional(readOnly = false)
    public List<T> add(Collection<T> entities) {
        LOG.debug("Adding {} {} entities", entities.size(), entityClass.getSimpleName());
        final List<T> savedItems = FluentIterable.from(entities).transform(new Function<T, T>() {
            @Override @Nullable
            public T apply(@Nullable T input) {
                em.persist(input);
                return input;
            }
        }).toList();
        LOG.debug("Added {} {} entities", entities.size(), entityClass.getSimpleName());
        return savedItems;
    }

    @Override @Transactional(readOnly = false)
    public List<T> modify(Map<ID, T> entities) {
        LOG.debug("Modifying {} {} entities", entities.size(), entityClass.getSimpleName());
        final List<T> mergedEntities = FluentIterable.from(entities.entrySet()).transform(new Function<Map.Entry<ID, T>, T>() {
            @Override
            public T apply(Map.Entry<ID, T> input) {
                final T mergedEntity = em.merge(input.getValue());
                return mergedEntity;
            };
        }).toList();
        LOG.debug("{} {} entities have been modified", mergedEntities.size(), entityClass.getSimpleName());
        return mergedEntities;
    }
    

    @Override @Transactional(readOnly = false)
    public long delete(Collection<ID> ids) {
        final Query q = em.createQuery("DELETE FROM " + entityClass.getName() + " e " +
                "WHERE e.id IN :upIds ");
        q.setParameter("upIds", ids);
        final int update = q.executeUpdate();
        return update;
    }
}
