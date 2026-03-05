package com.Vlad.RepairDesk.controller;

import com.Vlad.RepairDesk.model.User;
import com.Vlad.RepairDesk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/auth/register")
    public String register(@RequestParam String login,
                           @RequestParam String password,
                           RedirectAttributes redirectAttributes) {

        if (userRepository.findByLogin(login).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Логин уже занят");
            return "redirect:/login";
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Регистрация успешна, войдите");
        return "redirect:/login";
    }
}