package org.du2du.ensinaplus.model.bo;

import org.du2du.ensinaplus.model.dao.AbstractDAO;
import org.du2du.ensinaplus.model.entity.AbstractEntity;

import jakarta.inject.Inject;

public class AbstractBO<E extends AbstractEntity, D extends AbstractDAO<E>> {

  @Inject
  protected D dao;
  
}
