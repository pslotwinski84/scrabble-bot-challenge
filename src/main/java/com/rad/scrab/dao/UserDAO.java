package com.rad.scrab.dao;

import java.util.List;

import com.rad.scrab.model.User;

public interface UserDAO {
	public void addUser(User u);

	public void updateUser(User c);

	public List<User> listUser();

	public User getUserByName(String name);

	public void removeUser(String name);

	public List<User> getRandom();
}
