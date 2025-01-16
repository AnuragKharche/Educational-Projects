package com.eparkingsolution.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "car_park")
public class CarPark {

    @Id
    @Column(name = "car_park_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_cp;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "disabled_space")
    private String disabledSpaces;

    @Column(name = "distance_in_miles")
    private double distanceInMiles;


    @OneToMany(mappedBy = "carPark", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ParkingSpace> parkingSpace;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;


    public CarPark() {
        super();
    }


    public CarPark(int id_cp, String name, String address, String disabledSpaces, double distanceInMiles, List<ParkingSpace> parkingSpace, User user) {
        this.id_cp = id_cp;
        this.name = name;
        this.address = address;
        this.disabledSpaces = disabledSpaces;
        this.distanceInMiles = distanceInMiles;
        this.parkingSpace = parkingSpace;
        this.user = user;
    }

    public int getId_cp() {
        return id_cp;
    }

    public void setId_cp(int id_cp) {
        this.id_cp = id_cp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisabledSpaces() {
        return disabledSpaces;
    }

    public void setDisabledSpaces(String disabledSpaces) {
        this.disabledSpaces = disabledSpaces;
    }

    public double getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setDistanceInMiles(double distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }

    public List<ParkingSpace> getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(List<ParkingSpace> parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
