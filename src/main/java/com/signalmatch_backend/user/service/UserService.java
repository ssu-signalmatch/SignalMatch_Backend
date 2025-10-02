package com.signalmatch_backend.user.service;

import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.dto.SignupRequest;
import com.signalmatch_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest signupRequest) {
        if(userRepository.existsByLoginId(signupRequest.loginId())){
            throw new CustomException(ErrorCode.LOGINID_ALREADY_USED);
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.password());

        User newUser = User.builder()
                .loginId(signupRequest.loginId())
                .password(encodedPassword)
                .name(signupRequest.name()).build();
        userRepository.save(newUser);
    }

}
