package com.signalmatch_backend.bookmark.repository;

import com.signalmatch_backend.bookmark.domain.Bookmark;
import com.signalmatch_backend.bookmark.dto.StartupBookmarkInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    boolean existsByUserIdAndInvestorIdAndStartupId(Long userId, Long investorId, Long startupId);

    Optional<Bookmark> findByUserIdAndInvestorId(Long userId, Long investorId);

    Optional<Bookmark> findByUserIdAndStartupId(Long userId, Long startupId);

    List<Bookmark> findByUserId(Long userId);

    @Query("SELECT new com.signalmatch_backend.bookmark.dto.StartupBookmarkInfo(" +
        "s.startupId, s.startupName, sp.intro, COUNT(b)) " +
        "FROM Startup s " +
        "LEFT JOIN s.startupProfile sp " +
        "LEFT JOIN Bookmark b ON b.startupId = s.startupId " +
        "GROUP BY s.startupId, s.startupName, sp.intro " +
        "ORDER BY COUNT(b) DESC")
    List<StartupBookmarkInfo> findTop10ByBookmarkCount(Pageable pageable);

    long countByInvestorId(Long investorId);
    long countByStartupId(Long startupId);
}
