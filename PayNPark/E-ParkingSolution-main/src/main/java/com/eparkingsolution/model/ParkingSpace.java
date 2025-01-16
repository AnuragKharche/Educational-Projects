package com.eparkingsolution.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "parking_space")
public class ParkingSpace {

    @Id
    @Column(name = "id_ps")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ps;

    @Column(name = "disabled_space")
    private String disabledSpace;

    @Column(name = "floor_level")
    private String floorLevel;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "price")
    private float price;

    @ManyToOne
    @JoinColumn(name = "id_cp", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CarPark carPark;

    @OneToMany(mappedBy = "parkingSpace", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transaction;

    public ParkingSpace(){}


    public ParkingSpace(Long id_ps, String disabledSpace, String floorLevel, boolean enabled, float price, CarPark carPark, List<Transaction> transaction) {
        this.id_ps = id_ps;
        this.disabledSpace = disabledSpace;
        this.floorLevel = floorLevel;
        this.enabled = enabled;
        this.price = price;
        this.carPark = carPark;
        this.transaction = transaction;
    }

    public Long getId_ps() {
        return id_ps;
    }

    public void setId_ps(Long id_ps) {
        this.id_ps = id_ps;
    }

    public String getDisabledSpace() {
        return disabledSpace;
    }

    public void setDisabledSpace(String disabledSpace) {
        this.disabledSpace = disabledSpace;
    }

    public String getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(String floorLevel) {
        this.floorLevel = floorLevel;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public CarPark getCarPark() {
        return carPark;
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }

    public List<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }
}
