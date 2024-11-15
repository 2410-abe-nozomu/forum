package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.mapper.ReportMapper;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    private ReportMapper reportMapper;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport(String startDate, String endDate) {
        //絞り込み日時の入力有無のチェック
        //入力なしの場合はデフォルト値をセット
        if (!StringUtils.isBlank(startDate)) {
            startDate = startDate + " 00:00:00";
        } else {
            startDate = "2024-11-01 00:00:00";
        }
        if (!StringUtils.isBlank(endDate)) {
            endDate = endDate + " 23:59:59";
        } else {
            //現在日時の取得→endDateに現在日時をセット
            Date date = new Date();
            endDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            endDate = endDate + " 23:59:59";
        }
        //startDateとendDateをDate型に変換
        Date StartDate = null;
        Date EndDate = null;
        try {
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StartDate = sdFormat.parse(startDate);
            EndDate = sdFormat.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*//repositoryを呼び出して、戻り値をEntityにつめて、Formに移す
        List<Report> results = reportRepository.findByCreatedDateBetweenOrderByUpdatedDateDesc(StartDate, EndDate);
        List<ReportForm> reports = setReportForm(results);
        return reports;*/
        List<Report> results = reportMapper.getReport(StartDate, EndDate);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }
    /*
     * IDを指定してレコードを取得する処理
     */
    public ReportForm findByIdReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add((Report) reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }
    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }
    /*
     * レコード追加or更新（新規投稿・投稿編集）
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }
    /*
     * 返信を投稿・編集時の返信先投稿の更新日時の更新
     */
    public void updateReport(Integer id) {
        //現在日時を取得
        Date nowDate = new Date();
        //現在日時とIDを引数にupdatedメソッドを呼び出す
        reportRepository.updateReportDate(nowDate, id);
    }
    /*
     * レコード追加（投稿削除）
     */
    public void deleteReport(Integer id) {
        //Entityは使わず直接IDのみをreportRepositoryに渡す
        reportRepository.deleteById(id);
    }
    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        return report;
    }

}

