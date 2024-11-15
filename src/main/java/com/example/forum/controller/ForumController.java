package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.entity.Report;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;
    @Autowired
    HttpSession session;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(name = "startDate",  required = false)String startDate, @RequestParam(name = "endDate", required = false)String endDate) {
        ModelAndView mav = new ModelAndView();
        CommentForm form = new CommentForm();
        // 投稿を全件取得(絞り込み日時情報を引数とする)
        List<ReportForm> contentData = reportService.findAllReport(startDate, endDate);
        //コメントを全件取得
        List<CommentForm> commentData = commentService.findAllComment();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿・返信データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("comments", commentData);
        //バインドを返信のFormに設定
        mav.addObject("formModel", form);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);

        // セッションからエラーメッセージを取得
        String contentError = (String) session.getAttribute("content");
        //エラーメッセージの有無を確認
        if (contentError != null) {
            //セッションからIDを取得
            Integer reportId = (Integer)session.getAttribute("id");
            //エラーメッセージとIDをそれぞれViewに渡す
            mav.addObject("contentError", contentError);
            mav.addObject("reportId", reportId);
            //セッションからエラーメッセージとIDを削除
            session.removeAttribute("content");
            session.removeAttribute("id");
        }

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
    public ModelAndView addContent(@ModelAttribute("formModel") @Validated ReportForm reportForm, BindingResult result){
        //投稿の入力がブランクでないことをチェック
        if(result.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("/new");
            return modelAndView;
        }
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
    public ModelAndView editContent(@PathVariable Integer id, @ModelAttribute("formModel") @Validated ReportForm report, BindingResult result){
        //コメントの入力チェック
        if(result.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("/edit");
            return modelAndView;
        }
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
    public ModelAndView addContent(@PathVariable Integer id, @ModelAttribute("formModel") @Validated CommentForm commentForm, BindingResult result){
        //返信の入力チェック
        if(result.hasErrors()) {
            session.setAttribute("content", "コメントを入力してください");
            session.setAttribute("id", id);
            return new ModelAndView("redirect:/");
        }
        //取得した返信対象のIDをセット
        commentForm.setContent_id(id);
        // 投稿をテーブルに格納
        commentService.saveComment(commentForm);
        //返信先の投稿の更新日時を更新する
        //IDを引数にReportServiceのupdateReportを呼び出す
        reportService.updateReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
    /*
     * 返信削除処理
     */
    @DeleteMapping("/commentDelete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        // 削除対象の投稿データを引数にサービスを呼び出す
        commentService.deleteComment(id);
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
     * 返信編集処理
     */
    @PutMapping("/commentEdit/{id}")
    public ModelAndView editComment(@PathVariable Integer id, @ModelAttribute("formModel")@Validated CommentForm comment, BindingResult result){
        //投稿の入力がブランクでないことをチェック
        if(result.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("/commentEdit");
            return modelAndView;
        }
        //Viewから受け取ったIDをcommentFormに格納
        comment.setId(id);
        // IDと編集した投稿が格納されたreportFormをサービスへ渡してDBに登録
        commentService.saveComment(comment);
        //返信先の投稿の更新日時を更新する
        //IDを引数にReportServiceのupdateReportを呼び出す
        Integer ID = comment.getContent_id();
        reportService.updateReport(ID);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}