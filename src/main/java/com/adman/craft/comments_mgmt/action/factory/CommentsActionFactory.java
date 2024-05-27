package com.adman.craft.comments_mgmt.action.factory;

import com.adman.craft.comments_mgmt.action.LikeDislikeOperation;
import com.adman.craft.comments_mgmt.action.comments.AddCommentAction;
import com.adman.craft.comments_mgmt.action.comments.DeleteCommentAction;
import com.adman.craft.comments_mgmt.action.comments.FetchCommentAction;
import com.adman.craft.comments_mgmt.action.comments.FetchCommentsByPost;
import com.adman.craft.comments_mgmt.action.comments.FetchRepliesAction;
import com.adman.craft.comments_mgmt.action.comments.PatchCommentAction;
import com.adman.craft.comments_mgmt.representation.CommentTO;
import com.adman.craft.comments_mgmt.service.CommentService;
import com.adman.craft.comments_mgmt.service.SocialPostService;
import com.adman.craft.comments_mgmt.action.comments.DislikeCommentAction;
import com.adman.craft.comments_mgmt.action.comments.LikeCommentAction;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentsActionFactory {

    private final CommentService commentService;
    private final SocialPostService socialPostService;

    public AddCommentAction addCommentAction(@Nonnull CommentTO comment) {
        return AddCommentAction.builder()
                .comment(comment)
                .commentService(commentService)
                .build();
    }

    public DeleteCommentAction deleteCommentAction(@Nonnull Long commentId) {
        return DeleteCommentAction.builder()
                .commentId(commentId)
                .commentService(commentService)
                .build();
    }

    public LikeCommentAction likeCommentAction(@Nonnull Long commentId,
                                               @Nonnull LikeDislikeOperation operation) {
        return LikeCommentAction.builder()
                .commentId(commentId)
                .operation(operation)
                .commentService(commentService)
                .build();
    }

    public DislikeCommentAction dislikeCommentAction(@Nonnull Long commentId,
                                                     @Nonnull LikeDislikeOperation operation) {
        return DislikeCommentAction.builder()
                .commentId(commentId)
                .operation(operation)
                .commentService(commentService)
                .build();
    }

    public PatchCommentAction patchCommentAction(@Nonnull CommentTO comment) {
        return PatchCommentAction.builder()
                .comment(comment)
                .commentService(commentService)
                .build();
    }

    public FetchCommentAction fetchCommentAction(@Nonnull Long commentId) {
        return FetchCommentAction.builder()
                .commentId(commentId)
                .commentService(commentService)
                .build();
    }

    public FetchCommentsByPost fetchCommentsByPost(@Nonnull Long postId) {
        return FetchCommentsByPost.builder()
                .postId(postId)
                .commentService(commentService)
                .socialPostService(socialPostService)
                .build();
    }

    public FetchRepliesAction fetchRepliesAction(@Nonnull Long parentCommentId, int pageNum) {
        return FetchRepliesAction.builder()
                .parentCommentId(parentCommentId)
                .pageNum(pageNum)
                .commentService(commentService)
                .build();
    }

}
