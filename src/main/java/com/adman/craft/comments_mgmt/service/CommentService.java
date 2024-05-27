package com.adman.craft.comments_mgmt.service;

import com.adman.craft.comments_mgmt.action.LikeDislikeOperation;
import com.adman.craft.comments_mgmt.data.entity.Comment;
import com.adman.craft.comments_mgmt.data.entity.MetaData;
import com.adman.craft.comments_mgmt.representation.CommentTO;
import jakarta.annotation.Nonnull;
import lombok.NonNull;

import java.util.List;

public interface CommentService {
    @NonNull
    Comment addRootComment(@NonNull final MetaData metaData);

    @NonNull
    Comment addOrUpdateComment(@NonNull final CommentTO comment);

    @NonNull
    Comment getComment(@NonNull final Long commentId);

    void deleteComment(@Nonnull final Long commentId);

    void likeComment(@NonNull final Long commentId, @Nonnull final LikeDislikeOperation operation);

    void dislikeComment(@NonNull final Long commentId, @Nonnull LikeDislikeOperation operation);

    @Nonnull
    List<Comment> getRepliesOnComment(@Nonnull Long parentCommentId, int pageNum);
}
