package com.rad.scrab.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rad.scrab.model.User;

import com.rad.scrab.dao.UserDAO;

@Repository
public class UserDAOImpl implements UserDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public void addUser(User c) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(c);

	}

	public void updateUser(User c) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(c);

	}

	@SuppressWarnings("unchecked")

	public List<User> listUser() {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> personsList = session.createQuery("from User").list();
		for (User c : personsList) {

		}
		return personsList;
	}

	public User getUserByName(String name) {
		Session session = this.sessionFactory.getCurrentSession();
		User u = (User) session.get(User.class, new String(name));

		return u;
	}

	public void removeUser(String username) {
		Session session = this.sessionFactory.getCurrentSession();
		User u = (User) session.get(User.class, new String(username));
		if (null != u) {
			session.delete(u);
		}
	}

	@SuppressWarnings("unchecked")

	public List<User> getRandom() {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> u = session.createQuery("select u from User as u order by newid()").setMaxResults(3).list();
		return u;
	}
}
