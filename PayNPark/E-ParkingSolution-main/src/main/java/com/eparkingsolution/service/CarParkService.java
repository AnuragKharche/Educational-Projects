package com.eparkingsolution.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.CarParkRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public interface CarParkService {

//    @Autowired
//    private CarParkRepository repoCP;
//
//    @Autowired
//    private CarParkRepository carParkRepository;

//    public List<CarPark> listAll(){
//        return repoCP.findAll();
//    }
//
//    public void save(CarPark st) {repoCP.save(st);}
//
//    public CarPark get(int id_cp){ return repoCP.findById(id_cp).get();}
//
//    public void delete(int id_cp) { repoCP.deleteById(id_cp);}
//
//    public List<CarPark> findByUser(User user) {
//        return carParkRepository.findByUser(user);
//    }
//
//    public List<CarPark> findByUserId(Integer userId) {
//        return carParkRepository.findByUserId(userId);
//    }
//
//    public List<CarPark> findAllCarParks() {
//        return carParkRepository.findAll();
//    }
//
//
//    public List<CarPark> findAll() {
//        return carParkRepository.findAll();
//    }

    List<CarPark> getAllCarParks();


    CarPark getCarParkById(int id);

    Page<CarPark> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);





}

