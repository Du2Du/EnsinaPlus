package org.du2du.ensinaplus.model.bo.impl;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.dao.impl.UserDAO;
import org.du2du.ensinaplus.model.entity.impl.User;

import jakarta.enterprise.context.Dependent;

@Dependent
public class UserBO extends AbstractBO<User, UserDAO> {
}
