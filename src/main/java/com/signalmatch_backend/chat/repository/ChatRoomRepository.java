package com.signalmatch_backend.chat.repository;


import com.signalmatch_backend.chat.domain.ChatRoom;
import com.signalmatch_backend.user.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 스타트업–투자자 조합으로 채팅방 찾기
    Optional<ChatRoom> findByStartupIdAndInvestorId(Long startupId, Long investorId);

    List<ChatRoom> findAllByUser(Long userId, String role);


    List<ChatRoom> findAllByStartupId(Long startupId);

    List<ChatRoom> findAllByInvestorId(Long investorId);
}
