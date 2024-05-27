package com.example.logistic.controllers;

import com.example.logistic.dto.UserCreateDto;
import com.example.logistic.dto.UserDto;
import com.example.logistic.entities.ConfirmationToken;
import com.example.logistic.entities.PasswordResetToken;
import com.example.logistic.entities.Role;
import com.example.logistic.entities.User;
import com.example.logistic.repositories.ConfirmTokenRepository;
import com.example.logistic.repositories.PasswordTokenRepository;
import com.example.logistic.repositories.UserRepository;
import com.example.logistic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@CrossOrigin
public class ResetPassword {
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfirmTokenRepository confirmTokenRepository;
    @GetMapping("/resetPassword/{token}")
    public String resetPage(@PathVariable String token) throws Exception {
        System.out.println(token);
        PasswordResetToken passwordResetToken = passwordTokenRepository.findPasswordResetTokenByToken(token);
        if(passwordResetToken == null) {
            throw new Exception();
        }
        return "resetpassword";
    }
    @PutMapping("/resetPassword/{token}/{password}")
    public @ResponseBody String resetPass(@PathVariable String token, @PathVariable String password) throws Exception {
        PasswordResetToken passwordResetToken = passwordTokenRepository.findPasswordResetTokenByToken(token);
        if(passwordResetToken == null) {
            throw new Exception();
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "ok";
    }
    @GetMapping("/resetPassword/done")
    public String resetDone() {return "resetdone";}
    @GetMapping("/register-done/{token}")
    public String registerDone(@PathVariable String token) throws Exception {
        try {
            ConfirmationToken confirmationToken = confirmTokenRepository.findConfirmationTokenByToken(token);
            userService.createUserRoleCustomer(new UserCreateDto(confirmationToken.getName(),confirmationToken.getEmail(),confirmationToken.getPassword(),confirmationToken.getPhone()));;
            confirmTokenRepository.delete(confirmationToken);
            return "done";
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
