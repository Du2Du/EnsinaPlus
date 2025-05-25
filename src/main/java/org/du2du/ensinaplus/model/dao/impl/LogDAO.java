package org.du2du.ensinaplus.model.dao.impl;

import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.Log;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.Dependent;

@Dependent
public class LogDAO implements PanacheRepositoryBase<Log, UUID>{
    
}
