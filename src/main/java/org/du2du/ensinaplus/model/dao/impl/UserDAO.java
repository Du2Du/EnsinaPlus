package org.du2du.ensinaplus.model.dao.impl;

import java.util.Map;

import org.du2du.ensinaplus.model.dao.AbstractDAO;
import org.du2du.ensinaplus.model.entity.impl.User;

import jakarta.enterprise.context.Dependent;

@Dependent
public class UserDAO extends AbstractDAO<User>{
  
  public User findByEmail(String email) {
    return find("email = :email and deleted is false", Map.of("email", email)).firstResult();
  }

  public User findByEmailAndPassword(String email, String password) {
    return find("email = :email and password = :password and deleted is false", Map.of("email", email, "password", password))
        .firstResult();
  }
}
