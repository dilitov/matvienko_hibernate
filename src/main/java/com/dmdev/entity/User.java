package com.dmdev.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@TypeDef(name = "dmdev", typeClass = JsonBinaryType.class) // теперь можно в типе над полем ставить наше сокращение
@Data //генерирует геттеры, сеттеры, иквалс, хешкод, ту стринг
@NoArgsConstructor // конструктор без параметров
@AllArgsConstructor // со всеми параметрами
@Builder //для создания и иниализациии наших сущностей
@Entity // JPA аннотация, говорит что это наша сущность
@Table(name = "users", schema = "public") // аннотация, чтобы хибер правильно вставлял название таблицы в запросе
/** Наш POJO класс - класс с приватными полями, геттерами и сеттерами*/
public class User {

    @Id // первичный ключ (Каждая сущность должна иметь первичный ключ,
    // а идентификатор поля должен наследовать Serializable)
    private String username;
    private String firstname;
    private String lastname;
    @Column(name = "birth_date") // второй вариант сопоставления полей сущности относительно таблицы
//    private LocalDate birthDate;
//    @Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;
//    private Integer age;

    @Type(type = "dmdev") // вместо полного пути com.vladmihalcea.hibernate.type.json.JsonBinaryType указываем jsonb из метода getName класса JsonBinaryType
    private String info;
    @Enumerated(EnumType.STRING) // для использования нашего поля в Енаме в виде типа строка, по умолчанию ordinal - цифры
    private Role role;

    /** Это все ниже заменяет Lombok*/

//    public User() {
//    }
//
//    public User(String username, String firstname, String lastname, LocalDate birthDate, Integer age) {
//        this.username = username;
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.birthDate = birthDate;
//        this.age = age;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getFirstname() {
//        return firstname;
//    }
//
//    public void setFirstname(String firstname) {
//        this.firstname = firstname;
//    }
//
//    public String getLastname() {
//        return lastname;
//    }
//
//    public void setLastname(String lastname) {
//        this.lastname = lastname;
//    }
//
//    public LocalDate getBirthDate() {
//        return birthDate;
//    }
//
//    public void setBirthDate(LocalDate birthDate) {
//        this.birthDate = birthDate;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        User user = (User) o;
//        return Objects.equals(username, user.username) && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname) && Objects.equals(birthDate, user.birthDate) && Objects.equals(age, user.age);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(username, firstname, lastname, birthDate, age);
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "username='" + username + '\'' +
//                ", firstname='" + firstname + '\'' +
//                ", lastname='" + lastname + '\'' +
//                ", birthDate=" + birthDate +
//                ", age=" + age +
//                '}';
//    }

}
