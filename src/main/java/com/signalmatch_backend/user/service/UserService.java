package com.signalmatch_backend.user.service;

import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.investor.InvestorFinder;
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.dto.InvestorProfileInfo;
import com.signalmatch_backend.investor.service.InvestorService;
import com.signalmatch_backend.match.domain.enums.MatchStatus;
import com.signalmatch_backend.match.dto.MatchResponse;
import com.signalmatch_backend.match.repository.MatchRepository;
import com.signalmatch_backend.startup.StartupFinder;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.startup.dto.StartupProfileInfo;
import com.signalmatch_backend.startup.service.StartupService;
import com.signalmatch_backend.user.UserFinder;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.domain.enums.UserRole;
import com.signalmatch_backend.user.dto.*;
import com.signalmatch_backend.user.jwt.JwtUtil;
import com.signalmatch_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFinder userFinder;
    private final InvestorFinder investorFinder;
    private final StartupFinder startupFinder;
    private final InvestorService investorService;
    private final StartupService startupService;
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

    public MyPageResponse getMyPage(Long userId) {
        User user = userFinder.findByUserId(userId);

        if(user.getUserRole().equals(UserRole.INVESTOR)){
            Investor investor = investorFinder.findByOwner(user);
            long matchedStartupCount = matchRepository.countByInvestorIdAndStatus(investor.getInvestorId(), MatchStatus.ACCEPTED);
            InvestorProfileInfo profile = investorService.findInvestorProfile(userId);
            return InvestorMyPageResponse.of(profile, matchedStartupCount);
        }else{
            Startup startup = startupFinder.findByOwner(user);
            long matchedInvestorCount = matchRepository.countByStartupIdAndStatus(startup.getStartupId(), MatchStatus.ACCEPTED);
            StartupProfileInfo profile = startupService.findStartupProfile(userId);
            return StartupMyPageResponse.of(profile, matchedInvestorCount);
        }
    }
}
