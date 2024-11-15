package com.example.forum.mapper;

import com.example.forum.repository.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ReportMapper {
    List<Report> getReport(@Param("startDate") Date startDate, @Param("endDate")Date endDate);
}


