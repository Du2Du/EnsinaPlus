package org.du2du.ensinaplus.model.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.du2du.ensinaplus.model.dao.AbstractDAO;
import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.entity.impl.User;

import jakarta.enterprise.context.Dependent;

@Dependent
public class UserDAO extends AbstractDAO<User> {

  public User findByEmail(String email) {
    return find("email = :email and deleted is false", Map.of("email", email)).firstResult();
  }

  public User findByEmailAndPassword(String email, String password) {
    return find("email = :email and password = :password and deleted is false",
        Map.of("email", email, "password", password))
        .firstResult();
  }

  public List<UserDTO> listAll(Integer page, Integer limit, UUID userUUID) {
    return getEntityManager()
        .createQuery("select new org.du2du.ensinaplus.model.dto.UserDTO(u) from User u where deleted is false and u.uuid != :userUUID order by u.name asc",
            UserDTO.class).setParameter("userUUID", userUUID)
        .getResultList();
  }

  public Long countOfListAll() {
    return find("select count(u.uuid) from User u where deleted is false").count();
  }
}
