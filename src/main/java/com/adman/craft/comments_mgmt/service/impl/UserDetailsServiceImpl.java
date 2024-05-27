package com.adman.craft.comments_mgmt.service.impl;

import com.adman.craft.comments_mgmt.data.repository.LoginDetailsRepository;
import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.InvalidCredentialException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoginDetailsRepository loginDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginDetailsRepository.findByLoginId(username)
                .orElseThrow(() -> new InvalidCredentialException(ErrorCode.INVALID_CREDENTIALS, "User not found"));
    }
}
