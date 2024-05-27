package com.adman.craft.comments_mgmt.action.comments;

import com.adman.craft.comments_mgmt.action.BaseAction;
import com.adman.craft.comments_mgmt.data.entity.Comment;
import com.adman.craft.comments_mgmt.data.entity.SocialPost;
import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.InternalRuntimeException;
import com.adman.craft.comments_mgmt.representation.CommentTO;
import com.adman.craft.comments_mgmt.service.CommentService;
import com.adman.craft.comments_mgmt.service.SocialPostService;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
public class FetchCommentsByPost extends BaseAction<List<CommentTO>> implements CommentsAction {

    private final Long postId;
    private final SocialPostService socialPostService;
    private final CommentService commentService;

    @Override
    public List<CommentTO> perform() {
        this.validate();
        SocialPost post = socialPostService.getPost(postId);
        List<Comment> repliesOnComment = commentService.getRepliesOnComment(post.getRootCommentId(), 0);
        return repliesOnComment.stream().map(reply ->
                        new CommentTO(reply.getId(), reply.getText(), post.getRootCommentId()))
                .collect(Collectors.toList());
    }

    @Override
    public void validate() {
        List<String> errorMessages = new ArrayList<>();
        if (Objects.isNull(commentService)) {
            errorMessages.add("'commentService' can't be null");
        }
        if (Objects.isNull(socialPostService)) {
            errorMessages.add("'socialPostService' can't be null");
        }
        if (Objects.isNull(postId)) {
            errorMessages.add(("'postId' can't be null"));
        }
        if (!errorMessages.isEmpty()) {
            throw InternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(errorMessages)
                    .build();
        }
    }
}
