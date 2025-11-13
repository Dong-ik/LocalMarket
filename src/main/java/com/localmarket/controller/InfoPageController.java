package com.localmarket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class InfoPageController {

    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("pageTitle", "자주 묻는 질문");
        return "info/faq";
    }

    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("pageTitle", "고객 지원");
        return "info/support";
    }

    @GetMapping("/delivery")
    public String delivery(Model model) {
        model.addAttribute("pageTitle", "배송 안내");
        return "info/delivery";
    }

    @GetMapping("/returns")
    public String returns(Model model) {
        model.addAttribute("pageTitle", "교환/환불");
        return "info/returns";
    }

    @GetMapping("/seller/guide")
    public String sellerGuide(Model model) {
        model.addAttribute("pageTitle", "판매자 가이드");
        return "info/seller-guide";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "회사 소개");
        return "info/about";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("pageTitle", "개인정보처리방침");
        return "info/privacy";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("pageTitle", "이용약관");
        return "info/terms";
    }
}
