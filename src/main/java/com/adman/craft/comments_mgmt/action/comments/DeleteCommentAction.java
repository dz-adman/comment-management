package com.adman.craft.comments_mgmt.action.comments;

import com.adman.craft.comments_mgmt.action.BaseAction;
import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.InternalRuntimeException;
import com.adman.craft.comments_mgmt.service.CommentService;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
public class DeleteCommentAction extends BaseAction<Void> implements CommentsAction {

    private final Long commentId;
    private final CommentService commentService;

    @Override
    public Void perform() {
        this.validate();
        commentService.deleteComment(commentId);
        return null;
    }

    @Override
    public void validate() {
        List<String> errorMessages = new ArrayList<>();
        if (Objects.isNull(commentService)) {
            errorMessages.add("'commentService' can't be null");
        }
        if (Objects.isNull(commentId)) {
            errorMessages.add("'commentId' can't be null");
        }
        if (!errorMessages.isEmpty()) {
            throw InternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(errorMessages)
                    .build();
        }
    }
}
