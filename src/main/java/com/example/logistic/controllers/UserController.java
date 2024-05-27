package com.example.logistic.controllers;

import com.example.logistic.dto.ChangePasswordRequest;
import com.example.logistic.dto.UpdateInfoDto;
import com.example.logistic.dto.UserCreateDto;
import com.example.logistic.dto.UserDto;
import com.example.logistic.entities.*;
import com.example.logistic.payload.ChangePassword;
import com.example.logistic.payload.ErrorResponse;
import com.example.logistic.payload.Response;
import com.example.logistic.repositories.ConfirmTokenRepository;
import com.example.logistic.repositories.PasswordTokenRepository;
import com.example.logistic.repositories.UserRepository;
import com.example.logistic.security.JwtTokenProvider;
import com.example.logistic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfirmTokenRepository confirmTokenRepository;
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword())
        );
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Steecurity Conxt
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = jwtTokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(new Response(200,true,jwt));
    }
    @PostMapping("/auth/customer/sign-up")
    public ResponseEntity<?> register(@RequestBody UserCreateDto userDto) {
        if(userRepository.findUserByEmail(userDto.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(null,400,"Địa chỉ email đã tồn tại"));
        } else {
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(userDto.getFullName(),userDto.getEmail(), userDto.getPassword(),token,userDto.getPhone());
            confirmTokenRepository.save(confirmationToken);
            String resetLink = "http://localhost:8080/register-done/" + token;
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("phamductung69@gmail.com");
            msg.setTo(userDto.getEmail());
            msg.setSubject("Hoàn tất đăng ký");
            msg.setText("Nhấn vào liên kết dưới đây để hoàn tất đăng ký.\n\n" + resetLink + "\n\nNếu đây không phải yêu cầu của bạn, vui lòng bỏ qua email này.");
            CompletableFuture.runAsync(() -> {
                javaMailSender.send(msg);
            });
            return ResponseEntity.ok().body(new Response(200,true,userDto));
        }
    }
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        UserDto userDto = new UserDto(user.getRole() == Role.STAFF ? user.getStaff().getFullName() : user.getCustomer().getFullName()
                ,user.getEmail(),null,user.getRole(), user.getAvatar(), user.getRole() == Role.STAFF ? user.getStaff().getPhoneNumber() : user.getCustomer().getPhoneNumber());
        return ResponseEntity.ok().body(new Response(200,true,userDto));
    }
    @PostMapping("/auth/staff/sign-up")
    public ResponseEntity<?> registerStaff(@RequestBody UserCreateDto userDto) {
        return userService.createUserRoleStaff(userDto);
    }
    @PutMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordRequest request) {
        User user = userService.getUserFromToken(token);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(),request.getPassword())
        );
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new ChangePassword(true,null,"Đổi mật khẩu thành công"));
    }
    @PostMapping("/forget-password")
    public @ResponseBody ResponseEntity<?> resetPassword(@RequestParam String email) throws Exception {
        System.out.println(email);
        if(userRepository.findUserByEmail(email) == null) {
            return ResponseEntity.badRequest().body(new Response(404,false,"Email không tồn tại"));
        } else {
            User user = userRepository.findUserByEmail(email);
            String token = UUID.randomUUID().toString();
            if(user.getPasswordResetToken() == null) {
                PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
                passwordTokenRepository.save(passwordResetToken);
                user.setPasswordResetToken(passwordResetToken);
                userRepository.save(user);
            } else {
                user.getPasswordResetToken().setToken(token);
                userRepository.save(user);
            }
            String resetLink = "http://localhost:8080/resetPassword/" + token;
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("phamductung69@gmail.com");
            msg.setTo(email);
            msg.setSubject("Đặt lại mật khẩu");
            msg.setText("Bạn nhận được e-mail này vì bạn hoặc ai đó đã yêu cầu đặt lại mật khẩu cho tài khoản của bạn.\n\n" +
                    "Nhấp vào liên kết bên dưới để đặt lại mật khẩu của bạn.\n" + resetLink + "\n\nNếu đây không phải yêu cầu của bạn, vui lòng bỏ qua email này.");
            javaMailSender.send(msg);
            return ResponseEntity.ok().body(new Response(200,true,"Kiểm tra email của "));
        }
    }
    @PostMapping("/update-info")
    public ResponseEntity<?> updateInfo(@RequestHeader("Authorization") String token, @RequestBody UpdateInfoDto userInfo) {
        User user = userService.getUserFromToken(token);
        if (userInfo.getName() != null && !userInfo.getName().isEmpty()) user.getCustomer().setFullName(userInfo.getName());
        if (userInfo.getPhone() != null && !userInfo.getPhone().isEmpty()) user.getCustomer().setPhoneNumber(userInfo.getPhone());
        if (userInfo.getAvatar_url() != null && !userInfo.getAvatar_url().isEmpty()) user.setAvatar(userInfo.getAvatar_url());
        userRepository.save(user);
        return ResponseEntity.ok(new Response(200,true,userInfo));
    }
}
