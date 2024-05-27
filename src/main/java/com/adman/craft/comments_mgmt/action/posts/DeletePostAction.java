package com.adman.craft.comments_mgmt.action.posts;

import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.action.BaseAction;
import com.adman.craft.comments_mgmt.errorhandling.exception.InternalRuntimeException;
import com.adman.craft.comments_mgmt.service.SocialPostService;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
public class DeletePostAction extends BaseAction<String> implements PostsAction {

    private final Long postId;
    private final SocialPostService socialPostService;

    @Override
    public String perform() {
        this.validate();
        socialPostService.deletePost(postId);
        return null;
    }

    @Override
    public void validate() {
        List<String> errorMessages = new ArrayList<>();
        if (Objects.isNull(postId)) {
            errorMessages.add("'postId' can't be null");
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
