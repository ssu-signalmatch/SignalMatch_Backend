package com.signalmatch_backend.match.service;

import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.investor.InvestorFinder;
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.match.MatchFinder;
import com.signalmatch_backend.match.domain.Match;
import com.signalmatch_backend.match.domain.enums.MatchReasonCode;
import com.signalmatch_backend.match.domain.enums.MatchStatus;
import com.signalmatch_backend.match.dto.MatchAcceptedEvent;
import com.signalmatch_backend.match.dto.MatchCanceledEvent;
import com.signalmatch_backend.match.dto.MatchRejectedEvent;
import com.signalmatch_backend.match.dto.MatchRequestedEvent;
import com.signalmatch_backend.match.repository.MatchRepository;
import com.signalmatch_backend.startup.StartupFinder;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.user.UserFinder;
import com.signalmatch_backend.user.domain.User;
import com.signalmatch_backend.user.domain.enums.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchEventPublisher publisher;
    private final InvestorFinder investorFinder;
    private final StartupFinder startupFinder;
    private final UserFinder userFinder;
    private final MatchFinder matchFinder;

    //매칭 생성
    public Long createMatch(long userId, long investorId, long startupId){
        Startup startup = startupFinder.findByStartupId(startupId);
        Investor investor = investorFinder.findByInvestorId(investorId);

        User user = userFinder.findByUserId(userId);

        assertOwnership(user, startup, investor);

        assertNoDuplicateActiveMatch(startup.getStartupId(), investor.getInvestorId());

        Match match = Match.builder()
                .startupId(startup.getStartupId())
                .investorId(investor.getInvestorId())
                .status(MatchStatus.REQUESTED)
                .requestedBy(user.getUserRole())
                .build();
        match = matchRepository.save(match);

        // 이벤트 발행
        publisher.publishMatchRequested(new MatchRequestedEvent(
                match.getMatchId(),
                match.getStartupId(),
                match.getInvestorId(),
                match.getStatus().name(),
                LocalDateTime.now()
        ));
        return match.getMatchId();
    }


    //매칭 수락
    @Transactional
    public void acceptMatch(Long userId, Long matchId) {
        User user = userFinder.findByUserId(userId);
        Match match = matchFinder.findByMatchId(matchId);

        Startup startup = startupFinder.findByStartupId(match.getStartupId());
        Investor investor = investorFinder.findByInvestorId(match.getInvestorId());

        assertOwnership(user, startup, investor);

        assertTransition(match.getStatus(), MatchStatus.ACCEPTED);

        LocalDateTime now = LocalDateTime.now();
        match.setStatus(MatchStatus.ACCEPTED);
        match.setMatchedAt(now);

        match = matchRepository.save(match);

        publisher.publishMatchAccepted(new MatchAcceptedEvent(
                match.getMatchId(),
                match.getStartupId(),
                match.getInvestorId(),
                match.getStatus().name(),
                LocalDateTime.now()
        ));
    }

    //매칭 거부
    public void rejectMatch(Long userId, Long matchId,
                            MatchReasonCode code, String text) {
        User actor = userFinder.findByUserId(userId);
        Match match = matchFinder.findByMatchId(matchId);

        Startup startup = startupFinder.findByStartupId(match.getStartupId());
        Investor investor = investorFinder.findByInvestorId(match.getInvestorId());

        assertRejectAuthority(actor, startup, investor, match);

        assertTransition(match.getStatus(), MatchStatus.REJECTED);

        match.setEndedAt(LocalDateTime.now());
        match.setStatus(MatchStatus.REJECTED);
        match = matchRepository.save(match);

        publisher.publishMatchRejected(new MatchRejectedEvent(
                match.getMatchId(),
                match.getStartupId(),
                match.getInvestorId(),
                match.getStatus().name(),
                LocalDateTime.now(),
                code,
                text
        ));
    }

    //매칭 해제
    public void cancelMatch(Long userId, Long matchId,
                            MatchReasonCode code, String text) {
        User user = userFinder.findByUserId(userId);
        Match match = matchFinder.findByMatchId(matchId);

        Startup startup = startupFinder.findByStartupId(match.getStartupId());
        Investor investor = investorFinder.findByInvestorId(match.getInvestorId());

        assertOwnership(user, startup, investor);

        assertTransition(match.getStatus(), MatchStatus.CANCELLED);

        match.setStatus(MatchStatus.CANCELLED);
        match.setEndedAt(LocalDateTime.now());
        match = matchRepository.save(match);

        publisher.publishMatchCanceled(new MatchCanceledEvent(
                match.getMatchId(),
                match.getStartupId(),
                match.getInvestorId(),
                match.getStatus().name(),
                LocalDateTime.now(),
                code,
                text
        ));
    }

    //유저 검증
    private void assertOwnership(User user, Startup startup, Investor investor) {
        UserRole role = user.getUserRole();
        Long authUserId = user.getUserId();

        switch (role) {
            case STARTUP -> {
                if (startup == null || !startup.getOwner().getUserId().equals(authUserId)) {
                    throw new CustomException(ErrorCode.ACCESS_DENIED);
                }
            }
            case INVESTOR -> {
                if (investor == null || !investor.getOwner().getUserId().equals(authUserId)) {
                    throw new CustomException(ErrorCode.ACCESS_DENIED);
                }
            }
            default -> throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    //중복 방지
    private void assertNoDuplicateActiveMatch(Long startupId, Long investorId) {
        boolean exists = matchRepository.existsByStartupIdAndInvestorIdAndStatusIn(
                startupId, investorId, List.of(MatchStatus.REQUESTED, MatchStatus.PROPOSED)
        );
        if (exists) {
            throw new CustomException(ErrorCode.MATCH_ALREADY_USED);
        }
    }

    //허용 범위
    private void assertTransition(MatchStatus current, MatchStatus target) {
        if (current == target) return;
        boolean allowed = switch (current) {
            case REQUESTED, PROPOSED -> (target == MatchStatus.ACCEPTED
                    || target == MatchStatus.REJECTED
                    || target == MatchStatus.CANCELLED);
            case ACCEPTED -> (target == MatchStatus.CANCELLED);
            default -> false;
        };
        if (!allowed) throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }


    //매칭 거부 가능 여부
    private void assertRejectAuthority(User actor, Startup s, Investor i, Match m) {
        if (m.getRequestedBy() == UserRole.STARTUP) {
            // 스타트업이 요청 → 투자자 측만 거절 가능
            if (!i.getOwner().getUserId().equals(actor.getUserId())) {
                throw new CustomException(ErrorCode.ACCESS_DENIED);
            }
        } else if (m.getRequestedBy() == UserRole.INVESTOR) {
            // 투자자가 요청 → 스타트업 측만 거절 가능
            if (!s.getOwner().getUserId().equals(actor.getUserId())) {
                throw new CustomException(ErrorCode.ACCESS_DENIED);
            }
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }


}
