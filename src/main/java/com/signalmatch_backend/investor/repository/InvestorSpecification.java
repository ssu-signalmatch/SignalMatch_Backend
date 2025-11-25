package com.signalmatch_backend.investor.repository;

import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.domain.InvestorPreferredArea;
import com.signalmatch_backend.search.dto.InvestorSearch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
public class InvestorSpecification {

    public static Specification<Investor> search(String keyword, List<String> businessAreaNames) {
        return (root, query, cb) -> {

            query.distinct(true);
            var predicate = cb.conjunction();

            if (keyword != null && !keyword.isEmpty()) {
                String like = "%" + keyword + "%";
                predicate = cb.and(
                    predicate,
                    cb.or(
                        cb.like(root.get("investorName"), like),
                        cb.like(root.get("intro"), like),
                        cb.like(root.get("organizationName"), like)
                    )
                );
            }

            if (businessAreaNames != null && !businessAreaNames.isEmpty()) {

                Subquery<Long> subquery = query.subquery(Long.class);

                Root<InvestorPreferredArea> subRoot = subquery.from(InvestorPreferredArea.class);

                subquery.select(subRoot.get("investor").get("investorId"))
                    .where(
                        subRoot.get("businessArea").get("name").in(businessAreaNames)
                    );

                predicate = cb.and(
                    predicate,
                    root.get("investorId").in(subquery)
                );
            }

            return predicate;
        };
    }
}