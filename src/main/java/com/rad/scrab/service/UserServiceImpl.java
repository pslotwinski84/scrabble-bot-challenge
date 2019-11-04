package com.rad.scrab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rad.scrab.dao.UserDAO;
import com.rad.scrab.model.User;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Transactional
	public void addUser(User c) {
		this.userDAO.addUser(c);
	}

	@Transactional
	public void updateUser(User c) {
		this.userDAO.updateUser(c);
	}

	@Transactional
	public List<User> listUser() {
		return this.userDAO.listUser();
	}

	@Transactional
	public User getUserByName(String name) {
		return this.userDAO.getUserByName(name);
	}

	@Transactional
	public void removeUser(String name) {
		this.userDAO.removeUser(name);
	}

	@Transactional
	public List<User> getRandom() {
		return this.userDAO.getRandom();
	}

}
