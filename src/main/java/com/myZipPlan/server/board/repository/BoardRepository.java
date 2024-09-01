package com.myZipPlan.server.board.repository;

import com.myZipPlan.server.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}