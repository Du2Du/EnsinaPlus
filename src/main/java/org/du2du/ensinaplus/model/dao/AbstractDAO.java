package org.du2du.ensinaplus.model.dao;

import org.du2du.ensinaplus.model.entity.AbstractEntity;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.Dependent;

@Dependent
public class AbstractDAO<E extends AbstractEntity> implements PanacheRepositoryBase<E, UUID> {
}
