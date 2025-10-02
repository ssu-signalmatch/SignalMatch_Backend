package com.signalmatch_backend.user.controller;

import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.user.dto.LoginRequest;
import com.signalmatch_backend.user.dto.LoginResponse;
import com.signalmatch_backend.user.dto.SignupRequest;
import com.signalmatch_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "User", description = "로그인 관련 API입니다.")
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/signup")
    @Operation(summary = "회원 가입", description = "아이디, 비밀번호로 회원가입하는 API입니다.")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @PostMapping("/auth/login")
    @Operation(summary = "로그인", description = "아이디, 비밀번호를 받아 jwt 토큰을 발급하는 API입니다.")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest){
        LoginResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.",response));
    }
}
