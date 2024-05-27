package com.adman.craft.comments_mgmt.action.factory;

import com.adman.craft.comments_mgmt.action.posts.FetchPostAction;
import com.adman.craft.comments_mgmt.action.posts.LikePostAction;
import com.adman.craft.comments_mgmt.action.LikeDislikeOperation;
import com.adman.craft.comments_mgmt.action.posts.AddPostAction;
import com.adman.craft.comments_mgmt.action.posts.DeletePostAction;
import com.adman.craft.comments_mgmt.action.posts.DislikePostAction;
import com.adman.craft.comments_mgmt.action.posts.PatchSocialPostAction;
import com.adman.craft.comments_mgmt.representation.SocialPostTO;
import com.adman.craft.comments_mgmt.service.SocialPostService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsActionFactory {

    private final SocialPostService socialPostService;

    public AddPostAction addPostAction(@NonNull SocialPostTO post) {
        return AddPostAction.builder()
                .post(post)
                .socialPostService(socialPostService)
                .build();
    }

    public DeletePostAction deletePostAction(@NonNull Long postId) {
        return DeletePostAction.builder()
                .postId(postId)
                .socialPostService(socialPostService)
                .build();
    }

    public FetchPostAction fetchPostAction(@NonNull Long postId) {
        return FetchPostAction.builder()
                .postId(postId)
                .socialPostService(socialPostService)
                .build();
    }

    public PatchSocialPostAction patchSocialPostAction(@NonNull SocialPostTO post) {
        return PatchSocialPostAction.builder()
                .post(post)
                .socialPostService(socialPostService)
                .build();
    }

    public LikePostAction likePostAction(@NonNull Long postId, LikeDislikeOperation operation) {
        return LikePostAction.builder()
                .postId(postId)
                .operation(operation)
                .socialPostService(socialPostService)
                .build();
    }

    public DislikePostAction dislikePostAction(@NonNull Long postId, LikeDislikeOperation operation) {
        return DislikePostAction.builder()
                .postId(postId)
                .operation(operation)
                .socialPostService(socialPostService)
                .build();
    }

}
