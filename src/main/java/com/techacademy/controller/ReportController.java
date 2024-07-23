
package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;

import com.techacademy.entity.Employee;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;


    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(Model model) {

        model.addAttribute("listSize", reportService.findAll().size());
        model.addAttribute("employeeList", reportService.findAll());

        return "daily_report/daily.list";
    }

    // 日報詳細画面
    @GetMapping(value = "/{code}/")
    public String detail(@PathVariable String code, Model model) {

        model.addAttribute("employee", reportService.findByCode(code));
        return "daily_report/daily.detail";
    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Employee employee) {

        return "daily_report/daily.new";
    }

    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Employee employee, BindingResult res, Model model) {



        return "redirect:/daily_report";
    }

    // 日報削除処理
    @PostMapping(value = "/{code}/delete")
    public String delete(@PathVariable String code, @AuthenticationPrincipal UserDetail userDetail, Model model) {



        return "redirect:/daily_report";
    }
    //日報更新処理
    @GetMapping(value = "/{code}/update")
    public String getUser(@PathVariable String code, Employee employee, Model model) {

         return "daily_report/daily.update";
    }

    @PostMapping(value = "/{code}/update")
    public String postUser(@PathVariable String code,@Validated Employee employee,BindingResult res,Model model) {
        if(res.hasErrors()) {
            return getUser(null,employee,model);
        }

        reportService.update(code,employee);

        return "redirect:/reports";
    }

}
