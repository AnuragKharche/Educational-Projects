package com.eparkingsolution.model;


import javax.persistence.*;
import java.util.List;



@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "role")
    private String role = "Driver";

    @Column(name = "enabled")
    private boolean enabled = true;


    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CarPark> carParks;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions;


    public User() {
    }

    public User(Integer id, String username, String password, String address, String role, boolean enabled, List<CarPark> carParks, List<Transaction> transactions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.address = address;
        this.role = role;
        this.enabled = enabled;
        this.carParks = carParks;
        this.transactions = transactions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<CarPark> getCarParks() {
        return carParks;
    }

    public void setCarParks(List<CarPark> carParks) {
        this.carParks = carParks;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
