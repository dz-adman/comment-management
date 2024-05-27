package com.adman.craft.comments_mgmt.data.repository;

import com.adman.craft.comments_mgmt.data.entity.RefreshToken;
import com.adman.craft.comments_mgmt.data.entity.SocialUser;
import com.adman.craft.comments_mgmt.data.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByUser(SocialUser user);
    List<Token> findAllByUserAndRefreshToken(SocialUser user, RefreshToken refreshToken);
    Optional<Token> findByToken(String token);
}
