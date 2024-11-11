package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.entity.Report;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        CommentForm form = new CommentForm();
        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport();
        //コメントを全件取得
        List<CommentForm> commentData = commentService.findAllComment();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿・返信データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("comments", commentData);
        //バインドを返信のFormに設定
        mav.addObject("formModel", form);
        return mav;
    }
    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }
    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") ReportForm reportForm){
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 削除対象の投稿データを引数にサービスを呼び出す
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
    /*
     * 投稿編集画面表示
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        //htmlから受け取ったIDをサービスに渡して投稿を取得
        ReportForm report = reportService.findByIdReport(id);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        // 遷移先に渡すデータを設定
        mav.addObject("formModel", report);
        return mav;
    }
    /*
     * 投稿編集処理
     */
    @PutMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id, @ModelAttribute("formModel") ReportForm report){
        //Modelから受け取ったIDをreportFormに格納
        report.setId(id);
        // IDと編集した投稿が格納されたreportFormをサービスへ渡してDBに登録
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
    /*
     * 返信投稿処理
     */
    @PostMapping("/comment/{id}")
    public ModelAndView addContent(@PathVariable Integer id, @ModelAttribute("formModel") CommentForm commentForm){
        //取得した返信対象のIDをセット
        commentForm.setContent_id(id);
        // 投稿をテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
    /*
     * 返信編集画面表示
     */
    @GetMapping("/commentEdit/{id}")
    public ModelAndView editComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        //htmlから受け取ったIDをサービスに渡して投稿を取得
        CommentForm comment = commentService.findByIdReport(id);
        // 画面遷移先を指定
        mav.setViewName("/commentEdit");
        // 遷移先に渡すデータを設定
        mav.addObject("formModel", comment);
        return mav;
    }
    /*
     * 投稿編集処理
     */
    @PutMapping("/commentEdit/{id}")
    public ModelAndView editComment(@PathVariable Integer id, @ModelAttribute("formModel") CommentForm comment){
        //Viewから受け取ったIDをcommentFormに格納
        comment.setId(id);
        // IDと編集した投稿が格納されたreportFormをサービスへ渡してDBに登録
        commentService.saveComment(comment);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}