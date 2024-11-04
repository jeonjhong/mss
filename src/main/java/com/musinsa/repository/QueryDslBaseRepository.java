package com.musinsa.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

@Repository
public abstract class QueryDslBaseRepository<E> {
    private final Class<E> domainClass;

    private Querydsl querydsl;
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    protected QueryDslBaseRepository(Class<E> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        this.domainClass = domainClass;
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(em, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }

    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    @Autowired
    public void setEntityManager(EntityManager em) {
        Assert.notNull(em, "EntityManager must not be null!");

        JpaEntityInformation<E, ?> entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
        SimpleEntityPathResolver resolver;
        resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<E> path = resolver.createPath(entityInformation.getJavaType());

        this.em = em;
        this.querydsl = new Querydsl(em, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 일반 JPQLQuery를 Page로 변환
     *
     * @param query    내부 JPQLQuery
     * @param pageable 페이징 인자
     * @param <T>      반환 타입
     * @return 페이징 쿼리 결과
     */
    protected final <T> Page<T> queryToPage(JPQLQuery<T> query, Pageable pageable) {
        long count = query.fetchCount();
        if (count == 0) {
            return Page.empty(pageable);
        }
        List<T> content = queryWithPage(query, pageable);
        return new PageImpl<>(content, pageable, count);
    }

    /**
     * 일반 JPQLQuery를 Pagination 내 List로 변환
     *
     * @param query    내부 JPQLQuery
     * @param pageable 페이징 인자
     * @param <T>      반환 타입
     * @return 페이징 쿼리 결과
     */
    private final <T> List<T> queryWithPage(JPQLQuery<T> query, Pageable pageable) {
        //noinspection ConstantConditions
        return querydsl.applyPagination(pageable, query).fetch();
    }

    /**
     * 일반 JPQLQuery를 정렬 적용
     *
     * @param query JPQLQuery
     * @param sort  정렬 인자
     * @param <T>   반환 타입
     * @return 정렬 쿼리 결과
     */
    protected final <T> List<T> queryWithSort(JPQLQuery<T> query, Sort sort) {
        //noinspection ConstantConditions
        return querydsl.applySorting(sort, query).fetch();
    }
}
