package com.mycompany.visitormanagementsystem.repository;

import com.mycompany.visitormanagementsystem.config.HibernateUtil;
import com.mycompany.visitormanagementsystem.model.Visitor;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class VisitorRepository {
    public Long save(Visitor visitor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(visitor);
            transaction.commit();
            return visitor.getId();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<Visitor> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String condition = switch (status) {
                case "active" -> "where visitor.checkOutAt is null ";
                case "past" -> "where visitor.checkOutAt is not null ";
                default -> "";
            };
            return session.createQuery("from Visitor visitor " + condition
                    + "order by visitor.checkInAt desc, visitor.id desc", Visitor.class).list();
        }
    }

    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String condition = switch (status) {
                case "active" -> "where visitor.checkOutAt is null ";
                case "past" -> "where visitor.checkOutAt is not null ";
                default -> "";
            };
            return session.createQuery("select count(visitor.id) from Visitor visitor " + condition,
                    Long.class).getSingleResult();
        }
    }

    public Optional<Visitor> findById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(Visitor.class, id));
        }
    }

    public boolean checkOut(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int updated = session.createMutationQuery(
                            "update Visitor visitor set visitor.checkOutAt = :checkedOutAt "
                                    + "where visitor.id = :id and visitor.checkOutAt is null")
                    .setParameter("checkedOutAt", LocalDateTime.now())
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
            return updated == 1;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
