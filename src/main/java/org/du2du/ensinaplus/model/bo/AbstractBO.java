package org.du2du.ensinaplus.model.bo;

import java.util.Set;
import java.util.stream.Collectors;

import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.AbstractDAO;

import org.du2du.ensinaplus.model.dto.base.ValidateDTO;
import org.du2du.ensinaplus.model.entity.AbstractEntity;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public class AbstractBO<E extends AbstractEntity, D extends AbstractDAO<E>> {

  @Inject
  protected D dao;

  @Inject
  protected Validator validator;

  @Inject
  protected SessionBO sessionBO;

  public <T> ValidateDTO validate(T object) {
    Set<ConstraintViolation<T>> violations = validator.validate(object);
    if (!violations.isEmpty()) 
      return ValidateDTO.error("Dados inválidos!", this.joinViolations(violations));
    return ValidateDTO.ok();
  }

  private <T> String joinViolations(Set<ConstraintViolation<T>> violations) {
    return violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
  }
}
