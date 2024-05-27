package com.adman.craft.comments_mgmt.action.posts;

import com.adman.craft.comments_mgmt.action.BaseAction;
import com.adman.craft.comments_mgmt.action.LikeDislikeOperation;
import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.InternalRuntimeException;
import com.adman.craft.comments_mgmt.service.SocialPostService;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
public class LikePostAction extends BaseAction<Void> implements PostsAction {

    private final Long postId;
    private LikeDislikeOperation operation;
    private final SocialPostService socialPostService;

    @Override
    public Void perform() {
        this.validate();
        socialPostService.likePost(postId, operation);
        return null;
    }

    @Override
    public void validate() {
        List<String> errorMessages = new ArrayList<>();
        if (Objects.isNull(postId)) {
            errorMessages.add("'postId' can't be null");
        }
        if (Objects.isNull(operation)) {
            errorMessages.add("'operation' can't be null");
        }
        if (Objects.isNull(socialPostService)) {
            errorMessages.add("'socialPostService' can't be null");
        }
        if (!errorMessages.isEmpty()) {
            throw InternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(errorMessages)
                    .build();
        }
    }
}
