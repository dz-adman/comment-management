package com.adman.craft.comments_mgmt.action.comments;

import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.InternalRuntimeException;
import com.adman.craft.comments_mgmt.action.BaseAction;
import com.adman.craft.comments_mgmt.data.entity.Comment;
import com.adman.craft.comments_mgmt.representation.CommentTO;
import com.adman.craft.comments_mgmt.service.CommentService;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
public class FetchRepliesAction extends BaseAction<List<CommentTO>> implements CommentsAction {

    private final Long parentCommentId;
    private final int pageNum;
    private final CommentService commentService;

    @Override
    public List<CommentTO> perform() {
        this.validate();
        List<Comment> repliesOnComment = commentService.getRepliesOnComment(parentCommentId, pageNum);
        return repliesOnComment.stream().map(reply ->
                new CommentTO(reply.getId(), reply.getText(), parentCommentId))
                .collect(Collectors.toList());
    }

    @Override
    public void validate() {
        List<String> errorMessages = new ArrayList<>();
        if (pageNum < 0) {
            errorMessages.add("'pageNum' can't be less than 0");
        }
        if (Objects.isNull(commentService)) {
            errorMessages.add("'commentService' can't be null");
        }
        if (Objects.isNull(parentCommentId)) {
            errorMessages.add("'parentCommentId' can't be null");
        }
        if (!errorMessages.isEmpty()) {
            throw InternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(errorMessages)
                    .build();
        }
    }
}
