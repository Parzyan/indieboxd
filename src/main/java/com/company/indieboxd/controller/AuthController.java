package com.company.indieboxd.controller;

import com.company.indieboxd.model.User;
import com.company.indieboxd.service.SessionService;
import com.company.indieboxd.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final SessionService sessionService;

    public AuthController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            Model model) {

        try {
            userService.registerUser(username, password, email);
            return "redirect:/auth/login?signupSuccess";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String signupSuccess,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (signupSuccess != null) {
            model.addAttribute("success", "Registration successful! Please login");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        User user = userService.login(username, password);
        if (user != null) {
            sessionService.login(user);
            if(user.isAdmin()) return "redirect:/admin/dashboard";
            else return "redirect:/profile";
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        sessionService.logout();
        return "redirect:/";
    }
}
