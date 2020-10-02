package ru.chupaYchups.database.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="address")
public class Address {

    public Address() {
    }

    public Address(User user, String street) {
        this.user = user;
        this.street = street;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name="user_id")
    @OneToOne
    private User user;

    @Column(name = "street", nullable = false)
    private String street;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id.equals(address.id) &&
                user.equals(address.user) &&
                street.equals(address.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, street);
    }
}
