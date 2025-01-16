package com.eparkingsolution.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eparkingsolution.model.ParkingSpace;
import com.eparkingsolution.model.Transaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ParkingSpaceRepository extends JpaRepository <ParkingSpace, Long> {


    @Query("SELECT obj FROM ParkingSpace obj JOIN FETCH obj.carPark WHERE obj.carPark.id_cp = :id")
    List<ParkingSpace> findByForeignKeyId(@Param("id") int id);

    @Query("SELECT ps FROM ParkingSpace ps WHERE ps.carPark.id_cp = :carParkId")
    Page<ParkingSpace> findByCarParkId(@Param("carParkId") Integer carParkId, Pageable pageable);
}



