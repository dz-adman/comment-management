package com.adman.craft.comments_mgmt.controller;

import com.adman.craft.comments_mgmt.action.factory.PostsActionFactory;
import com.adman.craft.comments_mgmt.action.LikeDislikeOperation;
import com.adman.craft.comments_mgmt.representation.SocialPostTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Posts", description = "Posts Management related operations (APIs)")
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final PostsActionFactory postsActionFactory;

    @Operation(
            summary = "Get Post by ID",
            description = "This API retrieves a specific post by its ID.",
            tags = "Posts"
    )
    @GetMapping("/{id}")
    public ResponseEntity<SocialPostTO> getPost(@PathVariable("id") Long postId) {
        return ResponseEntity.ok(postsActionFactory
                .fetchPostAction(postId)
                .perform());
    }

    @Operation(
            summary = "Create Post",
            description = "This API creates a new post.",
            tags = "Posts"
    )
    @PostMapping
    @Transactional
    public ResponseEntity<SocialPostTO> createPost(@RequestBody SocialPostTO post) {
        try {
            return new ResponseEntity<>(postsActionFactory
                    .addPostAction(post)
                    .perform(), HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("POSTS-CONTROLLER EX", ex);
            ex.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }

    @Operation(
            summary = "Update Post",
            description = "This API updates an existing post.",
            tags = "Posts"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<SocialPostTO> updatePost(@PathVariable("id") Long postId, @RequestBody SocialPostTO post) {
        return ResponseEntity.ok(postsActionFactory
                .patchSocialPostAction(post)
                .perform());
    }

    @Operation(
            summary = "Delete Post",
            description = "This API deletes a post by its ID.",
            tags = "Posts"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long postId) {
        postsActionFactory
                .deletePostAction(postId)
                .perform();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Like Post",
            description = "This API allows users to like a post.",
            tags = "Posts"
    )
    @PatchMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable("id") Long postId) {
        postsActionFactory
                .likePostAction(postId, LikeDislikeOperation.ADD)
                .perform();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Remove Like from Post",
            description = "This API allows users to remove their like from a post.",
            tags = "Posts"
    )
    @PatchMapping("/{id}/like/remove")
    public ResponseEntity<Void> removeLikePost(@PathVariable("id") Long postId) {
        postsActionFactory
                .likePostAction(postId, LikeDislikeOperation.REMOVE)
                .perform();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Dislike Post",
            description = "This API allows users to dislike a post.",
            tags = "Posts"
    )
    @PatchMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikePost(@PathVariable("id") Long postId) {
        postsActionFactory
                .dislikePostAction(postId, LikeDislikeOperation.ADD)
                .perform();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Remove Dislike from Post",
            description = "This API allows users to remove their dislike from a post.",
            tags = "Posts"
    )
    @PatchMapping("/{id}/dislike/remove")
    public ResponseEntity<Void> removeDislikePost(@PathVariable("id") Long postId) {
        postsActionFactory
                .dislikePostAction(postId, LikeDislikeOperation.REMOVE)
                .perform();
        return ResponseEntity.ok().build();
    }
}

