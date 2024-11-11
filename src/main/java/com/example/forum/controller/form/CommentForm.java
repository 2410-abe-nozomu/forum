package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {

    private int id; //返信のID
    private String content; //返信のtext
    private int content_id; //返信対象のID
}

