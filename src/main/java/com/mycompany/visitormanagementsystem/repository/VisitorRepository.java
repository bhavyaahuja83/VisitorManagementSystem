package com.mycompany.visitormanagementsystem.repository;

import com.mycompany.visitormanagementsystem.config.HibernateUtil;
import com.mycompany.visitormanagementsystem.model.Visitor;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class VisitorRepository {
    public void save(Visitor visitor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(visitor);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<Visitor> findAllNewestFirst() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from Visitor visitor order by visitor.checkInAt desc, visitor.id desc",
                    Visitor.class
            ).list();
        }
    }
}
