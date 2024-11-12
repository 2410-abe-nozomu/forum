package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    public List<Report> findByCreatedDateBetweenOrderByUpdatedDateDesc(Date startDate, Date endDate);

    @Modifying
    @Transactional
    @Query("UPDATE Report r SET r.updatedDate = :updatedDate WHERE r.id = :id")
    public void updateReportDate(@Param("updatedDate") Date startDate, @Param("id") Integer id);
}