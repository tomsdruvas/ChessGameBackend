package com.lazychess.chessgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lazychess.chessgame.repository.entity.BoardDao;

public interface BoardRepository extends JpaRepository<BoardDao, String> {
}
