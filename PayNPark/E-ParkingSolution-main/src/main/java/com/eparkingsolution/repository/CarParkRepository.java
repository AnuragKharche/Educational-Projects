package com.eparkingsolution.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.User;

import java.util.List;

@Repository
public interface CarParkRepository extends JpaRepository<CarPark, Integer> {
    List<CarPark> findByUser(User user);
    List<CarPark> findByUserId(int userId);
//    List<CarPark> findByUserId(Integer userId);

    public Page<CarPark> findAll(Pageable pageable);

    Page<CarPark> findByUserId(Integer userId, Pageable pageable);




}

