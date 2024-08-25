package com.bankersCalculator.server.board.repository;

import com.bankersCalculator.server.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}