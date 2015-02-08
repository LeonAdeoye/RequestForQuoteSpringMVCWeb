package com.leon.rfq.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontControllerImpl
{
    @RequestMapping("/Version")
    public String helloWorld(Model model)
    {
        model.addAttribute("version", "Version 1.0.0");
        return "Version 1.0.0";
    }
}
