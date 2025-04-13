package com.company.indieboxd.controller;

import com.company.indieboxd.model.User;
import com.company.indieboxd.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final SessionService sessionService;

    public ProfileController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public String profile(Model model) {
        User user = sessionService.getCurrentUser();
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }
}
