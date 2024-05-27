package com.adman.craft.comments_mgmt.representation;

import java.time.LocalDateTime;
import java.util.List;

public record CommentMetaDataTO(int likes, int dislikes,
                                List<UserTO> likedBy, List<UserTO> dislikedBy,
                                LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
}
