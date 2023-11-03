package com.lazychess.chessgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lazychess.chessgame.repository.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, String> {
}
