package com.eparkingsolution.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.CarParkRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarParkServiceImpl implements CarParkService {

    @Autowired
    private CarParkRepository carParkRepository;

    @Autowired
    private CarParkRepository repoCP;




    public List<CarPark> listAll(){
        return repoCP.findAll();
    }

    public void save(CarPark st) {repoCP.save(st);}

    public CarPark get(int id_cp){ return repoCP.findById(id_cp).get();}

    public void delete(int id_cp) { repoCP.deleteById(id_cp);}

    public List<CarPark> findByUser(User user) {
        return carParkRepository.findByUser(user);
    }

    public List<CarPark> findByUserId(Integer userId) {
        return carParkRepository.findByUserId(userId);
    }


    public List<CarPark> findAllCarParks() {
        return carParkRepository.findAll();
    }


    public List<CarPark> findAll() {
        return carParkRepository.findAll();
    }

    @Override
    public List<CarPark> getAllCarParks() {
        return carParkRepository.findAll();
    }

    @Override
    public CarPark getCarParkById(int id) {
        Optional<CarPark> optional = carParkRepository.findById(id);
        CarPark carPark = null;
        if (optional.isPresent()) {
            carPark = optional.get();
        } else {
            throw new RuntimeException(" Employee not found for id :: " + id);
        }
        return carPark;
    }


    @Override
    public Page<CarPark> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.carParkRepository.findAll(pageable);
    }

    public Page<CarPark> findPaginatedByUser(int pageNo, int pageSize, String sortField, String sortDirection, Integer userId) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return carParkRepository.findByUserId(userId, pageable);
    }

}
