package com.signalmatch_backend.startup.repository;

import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.startup.domain.StartupBusinessArea;
import com.signalmatch_backend.startup.domain.key.StartupBusinessAreaKey;
import java.util.List;
import org.hibernate.sql.ast.tree.expression.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StartupBusinessAreaRepository extends JpaRepository<StartupBusinessArea, StartupBusinessAreaKey> {
    void deleteAllByStartup_StartupId(Long startupId);

    List<StartupBusinessArea> findByStartup(Startup startup);
}
