package com.java.doc.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static final Logger logger = Logger.getLogger(HibernateUtil.class);
	
	 @SuppressWarnings("deprecation")
	private static SessionFactory configureSessionFactory()
	            throws HibernateException {
	        Configuration configuration = new Configuration();
	        configuration.configure("hibernate.cfg.xml");
	        sessionFactory = configuration.buildSessionFactory();
	        return sessionFactory;
	    }

	    public static SessionFactory buildIfNeeded()
	            throws HibernateException {
	        if (sessionFactory != null) {
	            return sessionFactory;
	        }
	        try {
	            return configureSessionFactory();
	        } catch (HibernateException e) {
	            throw new HibernateException(e);
	        }
	    }

	    public static SessionFactory buildSessionFactory()
	            throws HibernateException {
	        if (sessionFactory != null) {
	            closeFactory();
	        }
	        return configureSessionFactory();
	    }

	    public static SessionFactory getSessionFactory() {
	        return sessionFactory;
	    }

	    public static Session openSession() throws HibernateException {
	        buildIfNeeded();
	        return sessionFactory.openSession();
	    }

	    public static void closeFactory() {
	        if (sessionFactory != null) {
	            try {
	                sessionFactory.close();
	            } catch (HibernateException ignored) {
	                logger.error("Couldn't close SessionFactory", ignored);
	            }
	        }
	    }

	    public static void close(Session session) {
	        if (session != null) {
	            try {
	                session.close();
	            } catch (HibernateException ignored) {
	                logger.error("Couldn't close Session", ignored);
	            }
	        }
	    }

	    public static void rollback(Transaction tx) {
	        try {
	            if (tx != null) {
	                tx.rollback();
	            }
	        } catch (HibernateException ignored) {
	            logger.error("Couldn't rollback Transaction", ignored);
	        }
	    }
}
