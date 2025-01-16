package com.eparkingsolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.ParkingSpace;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.CarParkRepository;
import com.eparkingsolution.repository.UserRepository;
import com.eparkingsolution.service.CarParkService;
import com.eparkingsolution.service.CarParkServiceImpl;
import com.eparkingsolution.service.ParkingSpaceService;
import com.eparkingsolution.service.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ManageCarParkController {

    @Autowired
    private CarParkServiceImpl carParkService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarParkRepository carParkRepository;


//    @GetMapping("/manageCarPark")
//    public String viewMyCarParks(Model model, Principal principal) {
//        User user = userRepository.findByUsername(principal.getName());
//        List<CarPark> listcarpark = carParkRepository.findByUserId(user.getId());
//        model.addAttribute("listcarpark", listcarpark);
//        return "manageCarPark";
//    }

    @GetMapping({"/manageCarPark", "/manageCarPark/page/{pageNo}"})
    public String viewMyCarParks(@PathVariable Optional<Integer> pageNo,
                                 @RequestParam(value = "sortField", defaultValue = "name") String sortField,
                                 @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                                 Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        int pageSize = 5;
        int currentPage = pageNo.orElse(1);

        Page<CarPark> page = carParkService.findPaginatedByUser(currentPage, pageSize, sortField, sortDir, user.getId());

        List<CarPark> listcarpark = page.getContent();
        model.addAttribute("listcarpark", listcarpark);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "manageCarPark";
    }




    @GetMapping("/createCarPark")
    public String createCarPark(Model model){
        model.addAttribute("carpark", new CarPark());

        return "createCarPark";
    }


    @RequestMapping(value = "/saveCP", method = RequestMethod.POST)
    public String saveCarPark(@ModelAttribute("carpark") CarPark st, Authentication authentication){

        User user = userRepository.findByUsername(authentication.getName());


        st.setUser(user);
        carParkService.save(st);
        return "redirect:/manageCarPark";
    }

    @RequestMapping("/edit/{id_cp}")
    public ModelAndView showEditCarParkPage(@PathVariable(name = "id_cp") int id_cp){
        ModelAndView mav1 = new ModelAndView("createCarPark");
        CarPark st = carParkService.get(id_cp);
        mav1.addObject("carpark", st);
        return mav1;
    }

    @RequestMapping("/delete/{id_cp}")
    public String deleteCarPark(@PathVariable(name = "id_cp") int id_cp) {
        carParkService.delete(id_cp);
        return "redirect:/manageCarPark";
    }

    //view
    @RequestMapping("/manageParkingSpace/{id_cp}")
    public ModelAndView manageParkingSpacePage(@PathVariable(name = "id_cp") int id_cp){
        ModelAndView mav2 = new ModelAndView("manageParkingSpace");
        CarPark st = carParkService.get(id_cp);
        mav2.addObject("parkingspace", st);
        return mav2;
    }


}
