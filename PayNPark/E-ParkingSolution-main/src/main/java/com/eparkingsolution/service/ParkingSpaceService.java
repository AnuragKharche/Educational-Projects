package com.eparkingsolution.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eparkingsolution.model.ParkingSpace;
import com.eparkingsolution.repository.ParkingSpaceRepository;

import java.util.List;

@Service
public class ParkingSpaceService {

    @Autowired
    private ParkingSpaceRepository repoPS;

    public List<ParkingSpace> listAll() {
        return repoPS.findAll();
    }

    public void save(ParkingSpace std) {
        repoPS.save(std);
    }

    public  ParkingSpace get(long id_ps) {
        return repoPS.findById(id_ps).get();
    }

    public void delete(long id_ps) {
        repoPS.deleteById(id_ps);
    }



}
