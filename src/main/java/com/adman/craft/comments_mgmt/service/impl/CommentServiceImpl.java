package com.adman.craft.comments_mgmt.service.impl;

import com.adman.craft.comments_mgmt.action.LikeDislikeOperation;
import com.adman.craft.comments_mgmt.data.entity.Comment;
import com.adman.craft.comments_mgmt.data.entity.MetaData;
import com.adman.craft.comments_mgmt.data.repository.CommentRepository;
import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.ExternalRuntimeException;
import com.adman.craft.comments_mgmt.representation.CommentTO;
import com.adman.craft.comments_mgmt.service.AuthService;
import com.adman.craft.comments_mgmt.service.CommentService;
import com.adman.craft.comments_mgmt.service.MetaDataService;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    @Value("${comments.replies.depth.default:5}")
    private int defaultDepthForFetchReplies;

    private final CommentRepository commentRepository;
    private final MetaDataService metaDataService;
    private final AuthService authService;

    @Override
    public @NonNull Comment addRootComment(@NonNull final MetaData metaData) {
        return commentRepository.save(Comment.builder()
                .text("_NIL_")
                .metaDataId(metaData.getId())
                .replies(new ArrayList<>())
                .postedBy(authService.getCurrentUser().getId())
                .build());
    }

    @Override
    public @NonNull Comment addOrUpdateComment(@NonNull final CommentTO comment) {
        Comment commentToReturn;
        if (!Objects.isNull(comment.id()) && commentRepository.existsById(comment.id())) {
            Comment existingComment = commentRepository.findById(comment.id()).get();
            // case : update
            existingComment.setText(comment.text());
            commentRepository.save(existingComment);
            commentToReturn = existingComment;
        } else {
            // case : create
            Comment c = commentRepository.save(Comment.builder()
                    .text(comment.text())
                    // with empty metadata
                    .metaDataId(metaDataService.addMetaData().getId())
                    // no replies
                    .replies(new ArrayList<>())
                    .postedBy(authService.getCurrentUser().getId())
                    .build()
            );
            commentToReturn = c;
            // add it as a reply on parent
            Optional<Comment> parentComment = commentRepository.findById(comment.parentId());
            parentComment.ifPresentOrElse(parent -> {
                parent.getReplies().add(c);
                commentRepository.save(parent);
            }, () -> {
                throw ExternalRuntimeException.builder()
                        .errorCode(ErrorCode.BAD_REQUEST)
                        .messages(List.of("Invalid 'parentId' ", "No comment found for parent comment id " + comment.parentId()))
                        .build();
            });
        }
        return commentToReturn;
    }

    @Override
    public @NonNull Comment getComment(@NonNull final Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw ExternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(List.of("Invalid 'commentId' ", "No comment found for id " + commentId))
                    .build();
        }
    }

    @Override
    public void deleteComment(@Nonnull final Long commentId) {
        // verify if record exists
        Optional<Comment> comment = commentRepository.findById(commentId);
        // and act accordingly
        comment.ifPresentOrElse(c -> {
            metaDataService.deleteMetaData(c.getMetaDataId());
            commentRepository.delete(c);
        }, () -> {
            throw ExternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(List.of("Invalid 'commentId' ", "No comment found for id " + commentId))
                    .build();
        });
    }

    @Override
    public void likeComment(@NonNull final Long commentId, @Nonnull final LikeDislikeOperation operation) {
        // verify if record exists
        Optional<Comment> comment = commentRepository.findById(commentId);
        // and act accordingly
        comment.ifPresentOrElse(c -> {
            MetaData metaData = metaDataService.getMetaData(c.getMetaDataId())
                    .orElse(metaDataService.addMetaData());
            switch (operation) {
                case ADD -> metaData.addLike(authService.getCurrentUser());
                case REMOVE -> metaData.removeLike(authService.getCurrentUser());
            }
            metaDataService.saveOrUpdateMetaData(metaData);
        }, () -> {
            throw ExternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(List.of("Invalid 'commentId' ", "No comment found for id " + commentId))
                    .build();
        });
    }

    @Override
    public void dislikeComment(@NonNull final Long commentId, @Nonnull LikeDislikeOperation operation) {
        // verify if record exists
        Optional<Comment> comment = commentRepository.findById(commentId);
        // and act accordingly
        comment.ifPresentOrElse(c -> {
            MetaData metaData = metaDataService.getMetaData(c.getMetaDataId())
                    .orElse(metaDataService.addMetaData());
            switch (operation) {
                case ADD -> metaData.addDislike(authService.getCurrentUser());
                case REMOVE -> metaData.removeDislike(authService.getCurrentUser());
            }
            metaDataService.saveOrUpdateMetaData(metaData);
        }, () -> {
            throw ExternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(List.of("Invalid 'commentId' ", "No comment found for id " + commentId))
                    .build();
        });
    }

    @Override
    public @Nonnull List<Comment> getRepliesOnComment(@Nonnull Long parentCommentId, int pageNum) {
        List<Comment> replies = new ArrayList<>();
        if (commentRepository.existsById(parentCommentId)) {
            replies.addAll(commentRepository.findRepliesByParentId(parentCommentId,
                    PageRequest.of(pageNum, defaultDepthForFetchReplies)));
        } else {
            throw ExternalRuntimeException.builder()
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .messages(List.of("Invalid 'parentCommentId' ", "No comment found for parent comment id " + parentCommentId))
                    .build();
        }
        return replies;
    }


}
