package com.mycompany.visitormanagementsystem.repository;

import com.mycompany.visitormanagementsystem.config.HibernateUtil;
import com.mycompany.visitormanagementsystem.model.Visitor;
import java.time.LocalDate;
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

    public List<Visitor> findByFilters(String status, LocalDate from, LocalDate to, String search) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("from Visitor visitor where 1 = 1");
            if ("active".equals(status)) {
                hql.append(" and visitor.checkOutAt is null");
            } else if ("past".equals(status)) {
                hql.append(" and visitor.checkOutAt is not null");
            }
            if (from != null) {
                hql.append(" and visitor.checkInAt >= :from");
            }
            if (to != null) {
                hql.append(" and visitor.checkInAt < :toExclusive");
            }
            if (search != null && !search.isBlank()) {
                hql.append(" and (lower(visitor.visitorName) like :search")
                        .append(" or lower(visitor.mobileNumber) like :search")
                        .append(" or lower(visitor.personToVisit) like :search")
                        .append(" or lower(visitor.purpose) like :search)");
            }
            hql.append(" order by visitor.checkInAt desc, visitor.id desc");
            var query = session.createQuery(hql.toString(), Visitor.class);
            if (from != null) {
                query.setParameter("from", from.atStartOfDay());
            }
            if (to != null) {
                query.setParameter("toExclusive", to.plusDays(1).atStartOfDay());
            }
            if (search != null && !search.isBlank()) {
                query.setParameter("search", "%" + search.toLowerCase() + "%");
            }
            return query.list();
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

    public long countCheckedInOn(LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select count(visitor.id) from Visitor visitor "
                                    + "where visitor.checkInAt >= :start and visitor.checkInAt < :end",
                            Long.class)
                    .setParameter("start", date.atStartOfDay())
                    .setParameter("end", date.plusDays(1).atStartOfDay())
                    .getSingleResult();
        }
    }

    public long countInRange(LocalDate from, LocalDate to) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("select count(visitor.id) from Visitor visitor where 1 = 1");
            if (from != null) {
                hql.append(" and visitor.checkInAt >= :from");
            }
            if (to != null) {
                hql.append(" and visitor.checkInAt < :toExclusive");
            }
            var query = session.createQuery(hql.toString(), Long.class);
            if (from != null) {
                query.setParameter("from", from.atStartOfDay());
            }
            if (to != null) {
                query.setParameter("toExclusive", to.plusDays(1).atStartOfDay());
            }
            return query.getSingleResult();
        }
    }

    public List<Object[]> findDailyCounts(LocalDate from, LocalDate to, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder sql = new StringBuilder(
                    "select date(check_in_at), count(*) from visitors where 1 = 1");
            if (from != null) {
                sql.append(" and check_in_at >= :from");
            }
            if (to != null) {
                sql.append(" and check_in_at < :toExclusive");
            }
            sql.append(" group by date(check_in_at) order by date(check_in_at) desc limit :limit");
            var query = session.createNativeQuery(sql.toString(), Object[].class);
            if (from != null) {
                query.setParameter("from", from.atStartOfDay());
            }
            if (to != null) {
                query.setParameter("toExclusive", to.plusDays(1).atStartOfDay());
            }
            query.setParameter("limit", limit);
            return query.getResultList();
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
