package com.adman.craft.comments_mgmt.data.repository;

import com.adman.craft.comments_mgmt.data.entity.LoginDetails;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long> {
    Optional<LoginDetails> findByLoginId(@Nonnull String loginId);
    boolean existsByLoginId(@Nonnull String loginId);
}
