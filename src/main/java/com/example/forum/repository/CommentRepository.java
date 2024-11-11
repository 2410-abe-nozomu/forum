package com.example.forum.repository;

import com.example.forum.repository.entity.Comment;
import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    public List<Comment> findAllByOrderByIdDesc();

}