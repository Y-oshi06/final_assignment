package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.EmployeeRepository;
import com.techacademy.repository.ReportRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 1件を検索
    public Report findByCode(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    //社員日報情報検索
    public List<Report> findByEmployee(Employee employee) {
        return reportRepository.findByEmployee(employee);
    }

    //日報登録
    @Transactional
    public ErrorKinds save(Report report) {

            report.setDeleteFlg(false);
            LocalDateTime now = LocalDateTime.now();
            report.setCreatedAt(now);
            report.setUpdatedAt(now);

            reportRepository.save(report);
            return ErrorKinds.SUCCESS;

    }

    //日報を書いた社員＋入力された日付の検索
    public List<Report> findByReportDateAndEmployee(LocalDate reportDate,Employee employee){
        return reportRepository.findByReportDateAndEmployee(reportDate,employee);
    }


    //日報削除
    @Transactional
    public ErrorKinds delete(Integer id) {
        Report report = findByCode(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

    //日報更新
    @Transactional
    public ErrorKinds update(Report report,UserDetail userDetail,Integer id) {

        List<Report> reportUser =  reportRepository.findByReportDateAndEmployee(report.getReportDate(),userDetail.getEmployee());

        //更新前のレポート
        Report reportBefore = reportRepository.findById(id).get();

        if(reportUser != null && id == report.getId() && reportBefore.getReportDate() == report.getReportDate()) {
            for(Report report1 :reportUser) {
                LocalDate reportDate = report1.getReportDate();
                if(reportDate.equals(report.getReportDate())) {
                    return ErrorKinds.DATECHECK_ERROR;
                }
            }
        }

        report.setDeleteFlg(false);
        report.setReportDate(report.getReportDate());

        Report beforeReport =reportRepository.findById(id).get();
        report.setCreatedAt(beforeReport.getCreatedAt());

        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }










}
