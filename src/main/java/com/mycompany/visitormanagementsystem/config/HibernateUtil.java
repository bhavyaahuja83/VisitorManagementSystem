package com.mycompany.visitormanagementsystem.config;

import com.mycompany.visitormanagementsystem.model.Visitor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public final class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySetting("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .applySetting("hibernate.connection.url", AppConfig.databaseUrl())
                    .applySetting("hibernate.connection.username", AppConfig.databaseUsername())
                    .applySetting("hibernate.connection.password", AppConfig.databasePassword())
                    .applySetting("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                    .applySetting("hibernate.hbm2ddl.auto", "validate")
                    .applySetting("hibernate.show_sql", "false")
                    .build();
            try {
                sessionFactory = new MetadataSources(registry)
                        .addAnnotatedClass(Visitor.class)
                        .buildMetadata()
                        .buildSessionFactory();
            } catch (RuntimeException e) {
                StandardServiceRegistryBuilder.destroy(registry);
                throw e;
            }
        }
        return sessionFactory;
    }

    public static synchronized void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
