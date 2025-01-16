package com.eparkingsolution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eparkingsolution.model.ParkingSpace;
import com.eparkingsolution.model.Transaction;
import com.eparkingsolution.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//    @Query("SELECT t FROM Transaction t WHERE t.parkingSpace.id_ps = :id_ps AND t.startDate = :startDate AND t.startTime = :startTime AND t.endDate = :endDate AND t.endTime = :endTime")
//    List<Transaction> findByParkingSpaceIdAndStartDateAndStartTimeAndEndDateAndEndTime(@Param("id_ps") long id_ps, @Param("startDate") LocalDate startDate, @Param("startTime") LocalTime startTime, @Param("endDate") LocalDate endDate, @Param("endTime") LocalTime endTime);

    List<Transaction> findByParkingSpaceAndStartDateAndStartTimeAndEndDateAndEndTime(ParkingSpace parkingSpace,
                                                                                     LocalDate startDate,
                                                                                     LocalTime startTime,
                                                                                     LocalDate endDate,
                                                                                     LocalTime endTime);


    List<Transaction> findByParkingSpace(ParkingSpace parkingSpace);


    List<Transaction> findByUser(User user);

    Optional<Transaction> findAllById(long id);

    List<Transaction> findByParkingSpaceCarParkUser(User user);

    @Query("SELECT t.licensePlate, COUNT(t.id) FROM Transaction t WHERE t.parkingSpace.carPark.user.id = :userId GROUP BY t.licensePlate ORDER BY COUNT(t.id) DESC")
    List<Object[]> getMostBookedLicensePlate(@Param("userId") Integer userId);
}
