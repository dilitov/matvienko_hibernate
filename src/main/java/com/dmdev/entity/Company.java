package com.dmdev.entity;

import lombok.*;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users") // исключаем пользователей, чтобы не было зацикливания
@EqualsAndHashCode(of = "name") // включаем name, т.к это уникальное поле для Company
@Builder
@Entity
// @Table не нужна, т.к название сущности совпадает с полем в таблице
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
    @Builder.Default // чтобы при создании company через билдер установились дефолтные значения, что и в полях
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true) // CascadeType.ALL удаляет-добавляет в обоих таблицах
   // orphanRemoval = true - означает что мы будем делать с коллекцией юзеров, т.е действия только над юзерами
    // @JoinColumn(name = "company_id") не нужно указывать т.к в User стоит ManyToOne
//    @org.hibernate.annotations.OrderBy(clause = "username DESK, lastname ASK")
//    @OrderBy("username DESC, personalInfo.lastname asc ") // лучше ордер бай из джава персистенц, использ hql и поля сущности а не таблицы
    @MapKey(name = "username")
    @SortNatural
    private Map<String, User> users = new TreeMap<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        user.setCompany(this); // нужно всегда подставлять обе связи, т.к у обоих сущностей есть ссылки друг на друга
    }
}
