package com.adman.craft.comments_mgmt.data.repository;

import com.adman.craft.comments_mgmt.data.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>, PagingAndSortingRepository<Comment, Long> {

    @Query("SELECT c FROM comment c WHERE c.parent.id = ?1 ORDER BY c.id ASC")
    List<Comment> findRepliesByParentId(Long parentId, Pageable pageable);
}

