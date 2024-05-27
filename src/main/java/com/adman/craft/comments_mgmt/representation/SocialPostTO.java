package com.adman.craft.comments_mgmt.representation;

import com.adman.craft.comments_mgmt.data.entity.MetaData;
import com.adman.craft.comments_mgmt.data.entity.SocialUser;

public record SocialPostTO(Long id, String content, SocialUser postedBy, Long rootCommentId, MetaData metaData) {
}
