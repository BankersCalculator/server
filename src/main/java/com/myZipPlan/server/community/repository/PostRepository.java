package com.myZipPlan.server.community.repository;

import com.myZipPlan.server.community.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/*
인터페이스 Repository는 **데이터 액세스 계층(DAL)**에서 중요한 역할을 하며,
 주로 데이터베이스와 상호작용하는 책임을 집니다. 주로 **JPA(Java Persistence API)**와 함께 사용되는 Repository
 인터페이스는 Spring Data JPA에서 제공하는 기능을 상속받아, 별도의 SQL 쿼리를 작성하지 않고도 데이터베이스에 접근할
 수 있게 해줍니다.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

}
