package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Data //генерирует геттеры, сеттеры, иквалс, хешкод, ту стринг
@NoArgsConstructor // конструктор без параметров
@AllArgsConstructor // со всеми параметрами
@Builder //для создания и иниализациии наших сущностей
@Embeddable // встраиваемый компонент
public class PersonalInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String firstname;
    private String lastname;
    @Column(name = "birth_date") // второй вариант сопоставления полей сущности относительно таблицы

    private Birthday birthDate;
}
