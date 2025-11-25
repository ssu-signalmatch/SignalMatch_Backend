package com.signalmatch_backend.startup.repository;

import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.startup.domain.StartupBusinessArea;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
public class StartupSpecification {
    public static Specification<Startup> search(String keyword, List<String> areas) {
        return (root, query, cb) -> {
            query.distinct(true);
            var predicate = cb.conjunction();

            if (keyword != null && !keyword.isEmpty()) {
                String like = "%" + keyword + "%";
                predicate = cb.and(predicate, cb.or(
                    cb.like(root.get("startupName"), like),
                    cb.like(root.get("owner").get("name"), like),
                    cb.like(root.get("startupProfile").get("intro"), like)
                ));
            }

            if (areas != null && !areas.isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<StartupBusinessArea> subRoot = subquery.from(StartupBusinessArea.class);

                subquery.select(subRoot.get("startup").get("startupId"))
                    .where(subRoot.get("businessArea").get("name").in(areas));
                predicate = cb.and(predicate, root.get("startupId").in(subquery));
            }

            return predicate;
        };
    }
}