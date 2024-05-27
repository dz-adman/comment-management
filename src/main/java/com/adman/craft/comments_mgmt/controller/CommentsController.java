package com.adman.craft.comments_mgmt.controller;

import com.adman.craft.comments_mgmt.action.LikeDislikeOperation;
import com.adman.craft.comments_mgmt.action.factory.CommentsActionFactory;
import com.adman.craft.comments_mgmt.representation.CommentTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Comments", description = "Posts Management related operations (APIs)")
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsActionFactory commentsActionFactory;

    @Operation(
            summary = "Get Comment by ID",
            description = "This API retrieves a specific comment by its ID.",
            tags = "Comments"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CommentTO> getComment(@PathVariable("id") Long commentId) {
        return ResponseEntity.ok(commentsActionFactory
                .fetchCommentAction(commentId)
                .perform());
    }

    @Operation(
            summary = "Create Comment",
            description = "This API creates a new comment.",
            tags = "Comments"
    )
    @PostMapping
    public ResponseEntity<CommentTO> createComment(@RequestBody CommentTO comment) {
        return new ResponseEntity<>(commentsActionFactory
                .addCommentAction(comment)
                .perform(), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Comment",
            description = "This API updates an existing comment.",
            tags = "Comments"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<CommentTO> updateComment(@PathVariable("id") Long commentId, @RequestBody CommentTO comment) {
        CommentTO mergedCommentObj = new CommentTO(commentId, comment.text(), comment.parentId());
        return ResponseEntity.ok(commentsActionFactory
                .patchCommentAction(mergedCommentObj)
                .perform());
    }

    @Operation(
            summary = "Delete Comment",
            description = "This API deletes a comment by its ID.",
            tags = "Comments"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long commentId) {
        commentsActionFactory
                .deleteCommentAction(commentId)
                .perform();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Like Comment",
            description = "This API allows users to like a comment.",
            tags = "Comments"
    )
    @PatchMapping("/{id}/like")
    public ResponseEntity<Void> likeComment(@PathVariable("id") Long commentId) {
        commentsActionFactory
                .likeCommentAction(commentId, LikeDislikeOperation.ADD)
                .perform();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Remove Like from Comment",
            description = "This API allows users to remove their like from a comment.",
            tags = "Comments"
    )
    @PatchMapping("/{id}/like/remove")
    public ResponseEntity<Void> removeLikeComment(@PathVariable("id") Long commentId) {
        commentsActionFactory
                .likeCommentAction(commentId, LikeDislikeOperation.REMOVE)
                .perform();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Dislike Comment",
            description = "This API allows users to dislike a comment.",
            tags = "Comments"
    )
    @PatchMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikeComment(@PathVariable("id") Long commentId) {
        commentsActionFactory
                .dislikeCommentAction(commentId, LikeDislikeOperation.ADD)
                .perform();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Remove Dislike from Comment",
            description = "This API allows users to remove their dislike from a comment.",
            tags = "Comments"
    )
    @PatchMapping("/{id}/dislike/remove")
    public ResponseEntity<Void> removeDislikeComment(@PathVariable("id") Long commentId) {
        commentsActionFactory
                .dislikeCommentAction(commentId, LikeDislikeOperation.REMOVE)
                .perform();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get Replies for Comment",
            description = "This API retrieves replies for a specific comment with pagination.",
            tags = "Comments"
    )
    @GetMapping("/{id}/replies/{pageNum}")
    public ResponseEntity<List<CommentTO>> getRepliesForComment(@PathVariable("id") Long parentCommentId,
                                                                @PathVariable("pageNum") int pageNum) {
        return ResponseEntity.ok(commentsActionFactory
                .fetchRepliesAction(parentCommentId, pageNum)
                .perform());
    }

    @Operation(
            summary = "Get Comments By Post",
            description = "This API retrieves all comments associated with a specific post.",
            tags = "Comments"
    )
    @GetMapping("/post/{id}")
    public ResponseEntity<List<CommentTO>> getCommentsByPost(@PathVariable("id") Long postId) {
        return ResponseEntity.ok(commentsActionFactory
                .fetchCommentsByPost(postId)
                .perform());
    }

}
