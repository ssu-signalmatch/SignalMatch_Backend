package com.signalmatch_backend.bookmark.repository;

import com.signalmatch_backend.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    boolean existsByUserIdAndInvestorIdAndStartupId(Long userId, Long investorId, Long startupId);

}
