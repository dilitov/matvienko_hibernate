package com.dmdev;

import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;

public class HibernateRunner {
    public static void main(String[] args) throws SQLException {
        BlockingQueue<Connection> pool = null; // пул соединений
//        Connection connection = pool.take();
        /** SessionFactory - вместо пула */
//        Connection connection = DriverManager
//                .getConnection("db.url", "db.username", "db.password");
        /** Session - вместо Connection */

        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy()); // меняем стратегию наименования полей в сущности относительно таблице
        configuration.addAnnotatedClass(User.class); // Добавляем сущность в SessionFactory, чтобы она отслеживала ее
        configuration.configure(); // подгружаем наш hibernate.xml для создания SessionFactory

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) /** обертка вокруг нашего Connection*/
             {
                 session.beginTransaction(); /**Открываем транзакцию, в хибере нет автокоммит мода */

             User user = User.builder()
                     .username("ivan@gmail.com")
                     .firstname("Ivan")
                     .lastname("Ivanov")
                     .birthDate(LocalDate.of(2000, 1, 19))
                     .age(20)
                     .role(Role.ADMIN)
                     .build();
             session.save(user); /** Сохраняем сущность в базу */

             session.getTransaction().commit(); /** Закрываем транзакцию если все хорошо, .rollback() если плохо */

        }

    }
}
