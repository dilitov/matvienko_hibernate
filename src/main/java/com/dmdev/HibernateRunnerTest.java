package com.dmdev;

import com.dmdev.entity.Chat;
import com.dmdev.entity.Company;
import com.dmdev.entity.Profile;
import com.dmdev.entity.User;
import com.dmdev.util.HibenateUtil;
import lombok.Cleanup;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


class HibernateRunnerTest {

    @Test
    void checkOrderBy () {
        try (SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 1);
            company.getUsers().forEach((k,v) ->System.out.println(v));


            session.getTransaction().commit();
        }

    }

    @Test
    void checkManyToMany () {
        try (SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 5L);
            user.getChats().clear();

//            Chat chat = Chat.builder()
//                    .name("dmdev")
//                    .build();
//
//
//            user.addChat(chat);
//            session.save(chat);
            session.getTransaction().commit();
        }


    }

    @Test
    void checkOneToOne () {
        try (SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            User user = session.get(User.class, 5L);
//            System.out.println();

            User user = User.builder()
                    .username("test4@gmail.com")
                    .build();
            Profile profile = Profile.builder()
                    .language("ru")
                    .street("Kolasa 18")
                    .build();
            profile.setUser(user);
            session.save(user);



            session.getTransaction().commit();
        }

    }

    @Test
    void checkOrhanRemoval () {
        try (SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

           Company company = session.getReference(Company.class, 1); // getReference получаем proxy
//            company.getUsers().removeIf(user -> user.getId().equals(7L));


            session.getTransaction().commit();
        }


    }

    @Test
    void checkLazyInitialisation () {
        Company company = null;
        try (SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

             company = session.get(Company.class, 1);


            session.getTransaction().commit();
        }
//        List<User> users = company.getUsers();
//        System.out.println(users.size());
    }
    @Test
    void deleteCompany() {
        @Cleanup SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory(); // @Cleanup вместо try с ресурсами
        @Cleanup Session session = sessionFactory.openSession(); // @Cleanup использ где нет autoClosable и закрывет тот метод, который укажем
        session.beginTransaction();

        Company company = session.get(Company.class, 6);
        session.delete(company);

        session.getTransaction().commit();
    }


    @Test
    void addUserToNewCompany() {
        @Cleanup SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory(); // @Cleanup вместо try с ресурсами
        @Cleanup Session session = sessionFactory.openSession(); // @Cleanup использ где нет autoClosable и закрывет тот метод, который укажем
        session.beginTransaction();

        Company company = Company.builder()
                .name("Facebook")
                .build();

        User user = User.builder()
                .username("sveta@gmail.com")
                .build();
        company.addUser(user);
        session.save(company);

        session.getTransaction().commit();
    }

    @Test
    void oneToMany () {
       @Cleanup SessionFactory sessionFactory = HibenateUtil.buildSessioneFactory(); // @Cleanup вместо try с ресурсами
       @Cleanup Session session = sessionFactory.openSession(); // @Cleanup использ где нет autoClosable и закрывет тот метод, который укажем
        session.beginTransaction();

        Company company = session.get(Company.class, 1);
        Hibernate.initialize(company.getUsers());
        System.out.println(company.getUsers());

        session.getTransaction().commit();
    }


    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
//                .username("ivan@gmail.com")
//                .firstname("Ivan")
//                .lastname("Ivanov")
//                .birthDate(LocalDate.of(2000, 1, 19))
//                .age(20)
                .build();

        String sql = """
                INSERT 
                INTO 
                %s
                (%s)
                values
                (%s)
                ;
                """;

        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(Collectors.joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));
        System.out.println(sql.formatted(tableName, columnNames, columnValues));


        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            preparedStatement.setObject(1, declaredField.get(user));

        }
    }

}