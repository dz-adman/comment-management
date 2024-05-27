package com.adman.craft.comments_mgmt.data.repository;

import com.adman.craft.comments_mgmt.data.entity.SocialUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialUserRepository extends CrudRepository<SocialUser, Long> {
}
