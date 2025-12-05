package com.starbucks.controller;

import com.starbucks.dto.SignUpRequest;
import com.starbucks.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignUpRequest request,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.signUpRequest",
                    "비밀번호가 일치하지 않습니다.");
            return "signup";
        }

        if (!request.getAgreeTerms()) {
            bindingResult.rejectValue("agreeTerms", "error.signUpRequest",
                    "이용약관에 동의해주세요.");
            return "signup";
        }

        try {
            userService.registerUser(request);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            bindingResult.reject("error.signUpRequest", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/coffee")
    public String coffee() {
        return "coffee";
    }
}