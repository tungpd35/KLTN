package com.example.logistic.security;

import com.example.logistic.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request ->
                        request.requestMatchers("api/v1/user/auth/login",
                                                "api/v1/user/getUser",
                                                "api/v1/category/getAll",
                                                "api/setting/listAll",
                                                "api/currency/listAll",
                                                "api/v1/warehouse/list",
                                                "api/v1/user/auth/staff/sign-up","/app/chat",
                                                "/data","/notify/**","/topic/staff/notify","/create-order",
                                                "api/v1/user/forget-password","/resetPassword/**","/css/**","/js/**",
                                                "/register-done/**",
                                                "api/v1/payment/get-url-payment",
                                                "api/v1/user/auth/customer/sign-up").permitAll().
                                requestMatchers("api/v1/product/create",
                                                "api/v1/product/detail",
                                                "api/v1/product/update/{id}",
                                                "api/v1/category/create",
                                                "api/v1/category/list",
                                                "api/v1/order/create",
                                                "api/v1/receiver/create",
                                                "api/v1/receiver/list",
                                                "api/v1/order/payment/success",
                                                "api/v1/customer/getAvatar",
                                                "api/v1/product/delete",
                                                "api/v1/user/update-info",
                                                "api/v1/product/list").hasRole("CUSTOMER").
                                requestMatchers("api/v1/order/list",
                                                "api/v1/order/countStatus",
                                                "api/v1/order/getOrder",
                                                "api/v1/user/change-password","api/v1/notify/list","api/v1/notify/read").hasAnyRole("CUSTOMER","STAFF").
                                requestMatchers("api/v1/order/editStatus",
                                                "api/v1/order/preparing",
                                                "api/v1/order/deny",
                                                "api/v1/order/picking").hasRole("STAFF")
                ).addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).build();
    }
}
