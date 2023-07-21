package com.lscavalcante.blog.service;

import com.lscavalcante.blog.configuration.exception.NotFoundException;
import com.lscavalcante.blog.configuration.exception.RecordException;
import com.lscavalcante.blog.configuration.exception.ResetPasswordTokenInvalidException;
import com.lscavalcante.blog.configuration.exception.UnMappedException;
import com.lscavalcante.blog.configuration.security.JwtService;
import com.lscavalcante.blog.dto.auth.*;
import com.lscavalcante.blog.dto.token.ResponseToken;
import com.lscavalcante.blog.dto.user.ResponseDetailUser;
import com.lscavalcante.blog.model.user.Role;
import com.lscavalcante.blog.model.user.User;
import com.lscavalcante.blog.repository.RoleRepository;
import com.lscavalcante.blog.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class UserService {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;

    final ModelMapperService modelMapperService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtService jwtService, ModelMapperService modelMapperService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.modelMapperService = modelMapperService;
        this.jwtService = jwtService;
    }

    public ResponseToken login(RequestLogin request) {
        try {
            var username = request.getEmail();
            var password = request.getPassword();

            User user = userRepository.findByEmail(username).orElseThrow(() -> new RecordException("user not found"));

            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));

            return jwtService.createJWT(user);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public ResponseCreateAccount createAccount(RequestCreateAccount dto) {
        try {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new NotFoundException("User already exists");
            }

            Role role = roleRepository.findByName("ADMIN").orElseThrow(() -> new NotFoundException("Role ADMIN not found is not possible create the user contact the support"));

            User user = new User();
            user.setEmail(dto.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
            user.setRoles(List.of(role));
            userRepository.save(user);

            return new ResponseCreateAccount("Account created with success for user " + user.getEmail());
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }

    }

    public ResponseResetPassword resetPassword(RequestResetPassword request) {
        try {
            var email = request.getEmail();
            var resetToken = request.getResetToken();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            if (verifyResetToken(user, resetToken)) {
                user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
                user.setResetToken(null);
                user.setResetTokenExpiry(null);
                userRepository.save(user);
                return new ResponseResetPassword("Password updated with success");
            }
            throw new ResetPasswordTokenInvalidException("The reset code is invalid try again with another code");

        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public ResponseForgetPassword forgetPassword(RequestForgetPassword request) {
        try {
            var email = request.getEmail();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            generateResetToken(user);

            return new ResponseForgetPassword("If the user exists we will send the code for reset the password for e-mail " + user.getEmail());
        } catch (NotFoundException ex) {
            return new ResponseForgetPassword("If the user exists we will send the code for reset the password for e-mail");
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public Page<ResponseDetailUser> list(Pageable pageable) {
        return userRepository.findAll(pageable).map((e) -> modelMapperService.map(e, ResponseDetailUser.class));
    }

    private void generateResetToken(User user) {
        Random random = new Random();
        int randomNumber = random.nextInt(999999 - 100000 + 1) + 100000;

        String resetToken = Integer.toString(randomNumber);
        LocalDateTime resetTokenExpiry = LocalDateTime.now().plusMinutes(5);

        user.setResetToken(resetToken);
        user.setResetTokenExpiry(resetTokenExpiry);

        System.out.println(user.getResetToken());
        System.out.println(user.getResetTokenExpiry());

        userRepository.save(user);
        // send the number for e-mail (por e-mail, por exemplo)
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.execute(() -> {
            // method or logic to send the email with thread
            // sendResetTokenByEmail(user)
        });

        // executor.shutdown();  // Encerrar o executor quando não for mais necessário
    }

    private boolean verifyResetToken(User user, String token) {
        if (user.getResetToken() == null || user.getResetTokenExpiry() == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(user.getResetTokenExpiry())) {
            return false;
        }

        return user.getResetToken().equals(token);
    }

}
