package com.company.indieboxd.controller;

import com.company.indieboxd.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final SessionService sessionService;

    public AdminController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        if (!sessionService.isAdmin()) {
            return "redirect:/auth/login";
        }
        return "admin/dashboard";
    }
}
