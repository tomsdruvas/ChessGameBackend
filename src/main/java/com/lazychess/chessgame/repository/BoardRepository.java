package com.lazychess.chessgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lazychess.chessgame.chessgame.Board;

public interface BoardRepository extends JpaRepository<Board, String> {
}
