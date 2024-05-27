package com.adman.craft.comments_mgmt.service.impl;

import com.adman.craft.comments_mgmt.action.LikeDislikeOperation;
import com.adman.craft.comments_mgmt.data.entity.MetaData;
import com.adman.craft.comments_mgmt.data.entity.SocialPost;
import com.adman.craft.comments_mgmt.data.repository.SocialPostRepository;
import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.ExternalRuntimeException;
import com.adman.craft.comments_mgmt.representation.SocialPostTO;
import com.adman.craft.comments_mgmt.service.AuthService;
import com.adman.craft.comments_mgmt.service.CommentService;
import com.adman.craft.comments_mgmt.service.MetaDataService;
import com.adman.craft.comments_mgmt.service.SocialPostService;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SocialPostServiceImpl implements SocialPostService {

    private final SocialPostRepository socialPostRepository;
    private final AuthService authService;
    private final MetaDataService metaDataService;
    private final CommentService commentService;

    public @NonNull SocialPost createPost(@NonNull final String content) {
        MetaData emptyMetaData = metaDataService.addEmptyMetaData();
        return socialPostRepository.save(SocialPost.builder()
                .content(content)
                .postedBy(authService.getCurrentUser())
                .metaData(emptyMetaData)
                // with root comment
                .rootCommentId(commentService.addRootComment(emptyMetaData).getId())
                .build()
        );
    }

    public @NonNull SocialPost addOrUpdatePost(@NonNull final SocialPostTO post) {
        SocialPost postToReturn;
        if (!Objects.isNull(post.id()) && socialPostRepository.existsById(post.id())) {
            SocialPost existingPost = socialPostRepository.findById(post.id()).get();
            // case : update
            existingPost.setContent(post.content());
            socialPostRepository.save(existingPost);
            postToReturn = existingPost;
        } else {
            // case : create
            postToReturn = createPost(post.content());
        }
        return postToReturn;
    }


    public @NonNull SocialPost getPost(@NonNull final Long postId) {
        Optional<SocialPost> socialPost = socialPostRepository.findById(postId);
        if (socialPost.isPresent()) {
            return socialPost.get();
        } else {
            throw ExternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(List.of("Invalid 'postId' ", "No post found for id " + postId))
                    .build();
        }
    }

    public void deletePost(@NonNull final Long postId) {
        // verify if record exists
        Optional<SocialPost> post = socialPostRepository.findById(postId);
        // and act accordingly
        // separate deletion of metadata not required as there is @OneToOne rel defined with CascadeType.ALL
        // metaDataService.deleteMetaData(p.getMetaData().getId());
        post.ifPresentOrElse(socialPostRepository::delete, () -> {
            throw ExternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(List.of("Invalid 'postId' ", "No post found for id " + postId))
                    .build();
        });
    }

    public void likePost(@NonNull final Long postId, @Nonnull final LikeDislikeOperation operation) {
        // verify if record exists
        Optional<SocialPost> post = socialPostRepository.findById(postId);
        // and act accordingly
        post.ifPresentOrElse(p -> {
            switch (operation) {
                case ADD -> p.getMetaData().addLike(authService.getCurrentUser());
                case REMOVE -> p.getMetaData().removeLike(authService.getCurrentUser());
            }
            metaDataService.saveOrUpdateMetaData(p.getMetaData());
        }, () -> {
            throw ExternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(List.of("Invalid 'postId' ", "No post found for id " + postId))
                    .build();
        });
    }

    public void dislikePost(@NonNull final Long postId, @Nonnull LikeDislikeOperation operation) {
        // verify if record exists
        Optional<SocialPost> post = socialPostRepository.findById(postId);
        // and act accordingly
        post.ifPresentOrElse(p -> {
            switch (operation) {
                case ADD -> p.getMetaData().addDislike(authService.getCurrentUser());
                case REMOVE -> p.getMetaData().removeDislike(authService.getCurrentUser());
            }
            metaDataService.saveOrUpdateMetaData(p.getMetaData());
        }, () -> {
            throw ExternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(List.of("Invalid 'postId' ", "No post found for id " + postId))
                    .build();
        });
    }

}
