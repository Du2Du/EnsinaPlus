package org.du2du.ensinaplus.model.dao.impl;

import java.util.UUID;

import org.du2du.ensinaplus.model.dao.AbstractBaseDAO;
import org.du2du.ensinaplus.model.entity.impl.UserResource;

import jakarta.enterprise.context.Dependent;

@Dependent
public class UserResourceDAO extends AbstractBaseDAO<UserResource> {

  public UserResource findByUserAndResource(UUID userUUID, UUID resourceUUID) {
    return find("user_uuid = :userUUID and resource_uuid = :resourceUUID", "userUUID", userUUID, "resourceUUID",
        resourceUUID).firstResult();
  }

  public long deleteUserResources(UUID userUUID, UUID courseUUID) {
    return delete("user.uuid = :userUUID and resource.module.course.uuid = :courseUUID", "userUUID", userUUID, "courseUUID", courseUUID);
  }
}
