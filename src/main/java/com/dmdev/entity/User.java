package com.dmdev.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@TypeDef(name = "dmdev", typeClass = JsonBinaryType.class) // теперь можно в типе над полем ставить наше сокращение
@Data //генерирует геттеры, сеттеры, иквалс, хешкод, ту стринг
@NoArgsConstructor // конструктор без параметров
@AllArgsConstructor // со всеми параметрами
@ToString (exclude = {"company", "profile", "chats"}) // переопределяем ToString и исключаем company
@Builder //для создания и иниализациии наших сущностей
@Entity // JPA аннотация, говорит что это наша сущность
@Table(name = "users", schema = "public") // аннотация, чтобы хибер правильно вставлял название таблицы в запросе
/** Наш POJO класс - класс с приватными полями, геттерами и сеттерами*/
// Лучше не использовать @Access(AccessType.FIELD) // говорит что хибер будет использовать рефлекшн апиай для получения доступа к нашим полям сущности
// AccessType.PROPERTY - говорит, что нужно теперь прописывать геттеры и сеттеры над полями и аннотации ставить тоже над ними
public class User {

    @Id // первичный ключ (Каждая сущность должна иметь первичный ключ,
//    // а идентификатор поля должен наследовать Serializable)
    @GeneratedValue(generator = "user_gen", strategy =GenerationType.IDENTITY) // не будет вставляться id в бд, бд сама отвечает за ИД. IDENTITY - лучшая по производительности и быстроте стратегия
//    // strategy =GenerationType.SEQUENCE - обычный счетчик, не во всех бд есть
//    // strategy =GenerationType.TABLE - использ только если бд не поддерживает автогенерируемых ИД.
////    @SequenceGenerator(name = "user_gen", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;
//    @Transient // если мы не хотим чтобы это поле добавлялось в бд. Не стоит использовать
    @Column(unique = true)
    private String username;
//    @Embedded // встроенная сущность
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date")) // сопоставляет имя поля с именем в бд
//    @EmbeddedId // Id для составного ключа встраевомой сущности
    private PersonalInfo personalInfo;
//    private String firstname;
//    private String lastname;
//    @Column(name = "birth_date") // второй вариант сопоставления полей сущности относительно таблицы
////    private LocalDate birthDate;
////    @Convert(converter = BirthdayConverter.class)
//    private Birthday birthDate;
////    private Integer age;

    @Type(type = "dmdev") // вместо полного пути com.vladmihalcea.hibernate.type.json.JsonBinaryType указываем jsonb из метода getName класса JsonBinaryType
    private String info;
    @Enumerated(EnumType.STRING) // для использования нашего поля в Енаме в виде типа строка, по умолчанию ordinal - цифры
    private Role role;

    @ManyToOne(/*optional = false*/ fetch = FetchType.LAZY) // optional = false - будет использ inner join, но тогда поля company_id должны быть not null в нашей сущности
    // в данном случае можно не использ, т.к берется наша сущность с маленькой буквы company и с _ ставится поле id company_id
    @JoinColumn(name = "company_id") // указываем колонку в нашей таблице User
    private Company company;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "users_chat", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private Set<Chat> chats = new HashSet<>();

    public void addChat(Chat chat) {
        chats.add(chat);
        chat.getUsers().add(this);

    }

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
