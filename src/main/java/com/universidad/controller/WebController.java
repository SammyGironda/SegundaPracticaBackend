package com.universidad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Bienvenido al Sistema Universitario");
        return "index";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping("/materias")
    public String materias() {
        return "materias/list";
    }

    @GetMapping("/docentes")
    public String docentes() {
        return "docentes/list";
    }

    @GetMapping("/inscripciones")
    public String inscripciones() {
        return "inscripciones/list";
    }
}