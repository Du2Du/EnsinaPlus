package org.du2du.ensinaplus.model.dao;

import java.util.Map;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.Dependent;

@Dependent
public class AbstractBaseDAO<E extends PanacheEntityBase> implements PanacheRepositoryBase<E, UUID> {

  public E findByTag(String tag) {
    return find("tag = :tag and deleted is false", Map.of("tag", tag)).firstResult();
  }

}
