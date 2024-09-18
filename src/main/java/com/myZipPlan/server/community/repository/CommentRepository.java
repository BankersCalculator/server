package com.myZipPlan.server.community.repository;

import com.myZipPlan.server.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.id = :commentId")
    Optional<Comment> findByIdWithUser(@Param("commentId") Long commentId);
    List<Comment> findByPostId(Long postId);
}