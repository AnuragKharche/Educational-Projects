package com.eparkingsolution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String cons(){

        return "indexLandingPage";
    }

    @RequestMapping("indexDriver")
    public String indexDriver(){
        return "indexDriver";
    }

    @RequestMapping("indexParkingOwner")
    public String indexParkingOwner(){
        return "indexParkingOwner";
    }

    @RequestMapping("indexSiteAdministrator")
    public String indexSiteAdministrator(){
        return "indexSiteAdministrator";
    }





}
