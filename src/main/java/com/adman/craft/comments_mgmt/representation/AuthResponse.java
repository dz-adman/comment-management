package com.adman.craft.comments_mgmt.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.adman.craft.comments_mgmt.data.Permission;
import com.adman.craft.comments_mgmt.data.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record AuthResponse(
        long userId, boolean isAuthenticated, Role role, Set<Permission> permissions,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken
) {
}
