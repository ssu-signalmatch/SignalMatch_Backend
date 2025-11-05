package com.signalmatch_backend.bookmark.repository;

import com.signalmatch_backend.bookmark.domain.Bookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    boolean existsByUserIdAndInvestorIdAndStartupId(Long userId, Long investorId, Long startupId);

    Optional<Bookmark> findByUserIdAndInvestorId(Long userId, Long investorId);

    Optional<Bookmark> findByUserIdAndStartupId(Long userId, Long startupId);

    List<Bookmark> findByUserId(Long userId);
}
