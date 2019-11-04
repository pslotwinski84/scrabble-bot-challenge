package com.rad.scrab.dao;

import com.rad.scrab.model.User;

public interface UserDetailsDao {
	User findUserByUsername(String username);
}
