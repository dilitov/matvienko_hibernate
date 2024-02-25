package com.dmdev.util;

import com.dmdev.converter.BirthdayConverter;
import com.dmdev.entity.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibenateUtil {

    public static SessionFactory buildSessioneFactory() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy()); // меняем стратегию наименования полей в сущности относительно таблице
        configuration.addAnnotatedClass(User.class); // Добавляем сущность в SessionFactory, чтобы она отслеживала ее
        configuration.addAttributeConverter(new BirthdayConverter(),true); // можно еще в самом конвертере прописать в аннотации autoapply = true
        configuration.registerTypeOverride(new JsonBinaryType()); // подключаем наш новый тип
        configuration.configure(); // подгружаем наш hibernate.xml для создания SessionFactory

        return configuration.buildSessionFactory();
    }
}
