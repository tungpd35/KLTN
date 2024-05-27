package com.example.logistic.services;


import com.example.logistic.dto.UserCreateDto;
import com.example.logistic.entities.*;
import com.example.logistic.payload.ErrorResponse;
import com.example.logistic.payload.Response;
import com.example.logistic.repositories.CustomerRepository;
import com.example.logistic.repositories.UserRepository;
import com.example.logistic.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }
    public void createUserRoleCustomer(UserCreateDto userCreateDto) {
        if(userRepository.findUserByEmail(userCreateDto.getEmail()) != null) {
            ResponseEntity.badRequest().body(new ErrorResponse(null, 400, "Địa chỉ email đã tồn tại"));
            return;
        }
        Customer customer = new Customer();
        customer.setFullName(userCreateDto.getFullName());
        customer.setPhoneNumber(userCreateDto.getPhone());
        User user = new User(userCreateDto.getEmail(),passwordEncoder.encode(userCreateDto.getPassword()), Role.CUSTOMER,customer,"https://i.imgur.com/UPVy6Mw.png");
        if (customer.getUsers() == null) customer.setUsers(new HashSet<>());
        customer.getUsers().add(user);
        userRepository.save(user);
    }
    public ResponseEntity<?> createUserRoleStaff(UserCreateDto userCreateDto) {
        if(userRepository.findUserByEmail(userCreateDto.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(null,400,"Địa chỉ email đã tồn tại"));
        }
        Staff staff = new Staff();
        staff.setFullName(userCreateDto.getFullName());
        User user = new User(userCreateDto.getEmail(),passwordEncoder.encode(userCreateDto.getPassword()), Role.STAFF,staff,"https://i.imgur.com/UPVy6Mw.png");
        if (staff.getUsers() == null) staff.setUsers(new HashSet<>());
        staff.getUsers().add(user);
        userRepository.save(user);
        return ResponseEntity.ok().body(new Response(200,true,userCreateDto));
    }
    public User getUserFromToken(String token) {
        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        System.out.println(email);
        return userRepository.findUserByEmail(email);
    }

}
