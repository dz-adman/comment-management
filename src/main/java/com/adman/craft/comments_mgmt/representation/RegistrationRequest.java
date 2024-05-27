package com.adman.craft.comments_mgmt.representation;

import com.adman.craft.comments_mgmt.data.Role;
import lombok.Builder;

@Builder
public record RegistrationRequest(String loginId, String firstName, String lastName, String email, String password, Role role) {
}
