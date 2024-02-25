package com.dmdev;

import com.dmdev.entity.Birthday;
import com.dmdev.entity.Company;
import com.dmdev.entity.PersonalInfo;
import com.dmdev.entity.User;
import com.dmdev.util.HibenateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;

@Slf4j // сгенерирует константу private static final Logger log = LoggerFactory.getLogger(HibernateRunnerPractic.class);
public class HibernateRunnerMappingEntity {
//    private static final Logger log = LoggerFactory.getLogger(HibernateRunnerPractic.class);

    public static void main(String[] args) throws SQLException {
        BlockingQueue<Connection> pool = null; // пул соединений
//        Connection connection = pool.take();
        /** SessionFactory - вместо пула */
//        Connection connection = DriverManager
//                .getConnection("db.url", "db.username", "db.password");
        /** Session - вместо Connection */

        Company company = Company.builder()
                .name("Amazone")
                .build();
        User user = User.builder()
                .username("sveta@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Sveta")
                        .lastname("Svetova")
                        .birthDate(new Birthday(LocalDate.of(2001,1,2)))
                        .build())
                .company(company)
                .build();

        try (SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) /** обертка вокруг нашего Connection*/
            {

                 Transaction transaction = session1.beginTransaction();

                User user1 = session1.get(User.class, 1L);
                Company company1 = user1.getCompany();
                System.out.println(company1.getName());
//                session1.save(company);
                session1.save(user);
                Object object = Hibernate.unproxy(company1); // вытаскивает proxy
                System.out.println(object);
                session1.getTransaction().commit();
            }





    }
}
}
