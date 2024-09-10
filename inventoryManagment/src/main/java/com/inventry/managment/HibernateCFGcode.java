package com.inventry.managment;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateCFGcode {
	  public static SessionFactory sessionfactory;
	    
	    //static class where configuration and mapping code is wrritten.
		static {
			Configuration cfg = new Configuration();
			cfg.configure("Hibernate.cfg.xml");
	     	sessionfactory = cfg.buildSessionFactory();
		}
		
		//passing sessionfactory object
		public static SessionFactory sessionfactory() {
			return sessionfactory;
		}
		//passing session object
		public static Session getSession() {
			//return sessionfactory().openSession();
			return sessionfactory.openSession();
		}
		//passing currentSession object.
		public static Session currentSession() {
			return sessionfactory().getCurrentSession();
			//return sessionfactory.getCurrentSession();
		}
		public static void shutdown() {
			sessionfactory.close();
		}
}

