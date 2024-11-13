package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentForm {

    private int id; //返信のID

    @NotBlank(message = "コメントを入力してください")
    private String content; //返信のtext

    private int content_id; //返信対象のID

    private Date createdDate; //返信の投稿日時

    private Date updatedDate; //返信の更新日時
}

