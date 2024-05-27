package com.adman.craft.comments_mgmt.action.comments;

import com.adman.craft.comments_mgmt.action.BaseAction;
import com.adman.craft.comments_mgmt.data.entity.Comment;
import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.InternalRuntimeException;
import com.adman.craft.comments_mgmt.representation.CommentTO;
import com.adman.craft.comments_mgmt.service.CommentService;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
public class AddCommentAction extends BaseAction<CommentTO> implements CommentsAction {

    private final CommentTO comment;
    private final CommentService commentService;

    @Override
    public CommentTO perform() {
        this.validate();
        Comment c = commentService.addOrUpdateComment(comment);
        return new CommentTO(c.getId(), c.getText(), comment.parentId());
    }

    @Override
    public void validate() {
        List<String> errorMessages = new ArrayList<>();
        if (Objects.isNull(commentService)) {
            errorMessages.add("'commentService' can't be null");
        }
        if (Objects.isNull(comment)) {
            errorMessages.add("'comment' can't be null");
        }
        if (!errorMessages.isEmpty()) {
            throw InternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(errorMessages)
                    .build();
        }
    }
}
