package com.signalmatch_backend.user.service;

import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.user.UserFinder;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.dto.LoginRequest;
import com.signalmatch_backend.user.dto.LoginResponse;
import com.signalmatch_backend.user.dto.SignupRequest;
import com.signalmatch_backend.user.jwt.JwtUtil;
import com.signalmatch_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFinder userFinder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequest signupRequest) {
        if(userRepository.existsByLoginId(signupRequest.loginId())){
            throw new CustomException(ErrorCode.LOGINID_ALREADY_USED);
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.password());

        User newUser = User.builder()
                .loginId(signupRequest.loginId())
                .password(encodedPassword)
                .name(signupRequest.name())
                .userRole(signupRequest.userRole())
                .build();
        userRepository.save(newUser);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userFinder.findByLoginId(loginRequest.loginId());
        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtUtil.createAccessToken(loginRequest.loginId());

        return new LoginResponse(accessToken);
    }

    public void delete(Long userId) {
        User user = userFinder.findByUserId(userId);
        userRepository.delete(user);
    }
}
