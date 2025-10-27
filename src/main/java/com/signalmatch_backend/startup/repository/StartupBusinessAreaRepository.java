package com.signalmatch_backend.startup.repository;

import com.signalmatch_backend.startup.domain.StartupBusinessArea;
import com.signalmatch_backend.startup.domain.key.StartupBusinessAreaKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StartupBusinessAreaRepository extends JpaRepository<StartupBusinessArea, StartupBusinessAreaKey> {

}
