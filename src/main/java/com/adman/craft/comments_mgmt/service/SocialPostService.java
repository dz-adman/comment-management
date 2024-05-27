package com.adman.craft.comments_mgmt.service;

import com.adman.craft.comments_mgmt.action.LikeDislikeOperation;
import com.adman.craft.comments_mgmt.data.entity.SocialPost;
import com.adman.craft.comments_mgmt.representation.SocialPostTO;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public interface SocialPostService {

    @NonNull
    SocialPost createPost(@NonNull final String content);

    @NonNull
    SocialPost addOrUpdatePost(@NonNull final SocialPostTO post);

    @NonNull
    SocialPost getPost(@NonNull final Long postId);

    void deletePost(@NonNull final Long postId);

    void likePost(@NonNull final Long postId, @Nonnull final LikeDislikeOperation operation);

    void dislikePost(@NonNull final Long postId, @Nonnull final LikeDislikeOperation operation);
}