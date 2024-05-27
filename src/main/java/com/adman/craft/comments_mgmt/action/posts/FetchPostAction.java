package com.adman.craft.comments_mgmt.action.posts;

import com.adman.craft.comments_mgmt.action.BaseAction;
import com.adman.craft.comments_mgmt.data.entity.SocialPost;
import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.InternalRuntimeException;
import com.adman.craft.comments_mgmt.representation.SocialPostTO;
import com.adman.craft.comments_mgmt.service.SocialPostService;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
public class FetchPostAction extends BaseAction<SocialPostTO> implements PostsAction {

    private final Long postId;
    private final SocialPostService socialPostService;

    @Override
    public SocialPostTO perform() {
        this.validate();
        SocialPost post = socialPostService.getPost(postId);
        return new SocialPostTO(post.getId(), post.getContent(), post.getPostedBy(),
                post.getRootCommentId(), post.getMetaData());
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
