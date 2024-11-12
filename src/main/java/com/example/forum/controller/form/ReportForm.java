package com.example.forum.controller.form;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReportForm {

    private int id;

    private String content;

    private Date createdDate;

    private Date updatedDate;
}

