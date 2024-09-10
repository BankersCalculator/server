package com.myZipPlan.server.community.repository;

import com.myZipPlan.server.community.domain.Comment;
import com.myZipPlan.server.community.domain.CommentLike;
import com.myZipPlan.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
    void deleteByCommentAndUser(Comment comment, User user);
}
