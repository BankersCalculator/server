package com.myZipPlan.server.community.repository;

import com.myZipPlan.server.community.domain.Post;
import com.myZipPlan.server.community.domain.PostLike;
import com.myZipPlan.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
    void deleteByPostAndUser(Post post, User user);
}
