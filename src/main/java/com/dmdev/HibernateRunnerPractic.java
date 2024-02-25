package com.dmdev;

import com.dmdev.converter.BirthdayConverter;
import com.dmdev.entity.Birthday;
import com.dmdev.entity.PersonalInfo;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.util.HibenateUtil;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;

@Slf4j // сгенерирует константу private static final Logger log = LoggerFactory.getLogger(HibernateRunnerPractic.class);
public class HibernateRunnerPractic {
//    private static final Logger log = LoggerFactory.getLogger(HibernateRunnerPractic.class);

    public static void main(String[] args) throws SQLException {
        BlockingQueue<Connection> pool = null; // пул соединений
//        Connection connection = pool.take();
        /** SessionFactory - вместо пула */
//        Connection connection = DriverManager
//                .getConnection("db.url", "db.username", "db.password");
        /** Session - вместо Connection */


        User user = User.builder()
                .username("ivan@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Petr")
                        .lastname("Petrov")
                        .birthDate(new Birthday(LocalDate.of(2000,1,2)))
                        .build())
                .build();
        log.info("User entity is in transient state, object: {} ", user);

        try (SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) /** обертка вокруг нашего Connection*/
            {

                 Transaction transaction = session1.beginTransaction();
                 log.trace("Transaction is created, {}", transaction);

                 session1.saveOrUpdate(user);
                 log.trace("User is in persistent state: {}, session {}", user, session1);

                 session1.getTransaction().commit();
            }
        log.warn("User is in detached state: {}, session is closed {}", user, session1);
//        try (SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory();
//              /** обертка вокруг нашего Connection*/
//
//        {
//            session2.beginTransaction();
////            session2.delete(user);
//            user.setFirstname("Sveta");
//
//              session2.merge(user); // поля в сущности главнее чем в бд, он идет в бд, создает сущность на основании данных и устанавливает в нового юзера данные из старого. которого мы передали и получаем нового юзера
////            session2.refresh(user); // берет изменения из бд, т.е меняет сущность
//
//            session2.getTransaction().commit();
//
//        }

            try (Session session = sessionFactory.openSession()) {

                PersonalInfo key =  PersonalInfo.builder()
                        .firstname("Petr")
                        .lastname("Petrov")
                        .birthDate(new Birthday(LocalDate.of(2000,1,2)))
                        .build();

                User user2 = session.get(User.class, key);
                System.out.println();
            }

    }catch (Exception e) {
            log.error("Exception occurred", e);
            throw e;
        }
}
}
