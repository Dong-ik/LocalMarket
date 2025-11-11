package com.localmarket.controller;

import com.localmarket.domain.Favorite;
import com.localmarket.domain.Member;
import com.localmarket.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteViewController {

    private final FavoriteService favoriteService;

    /**
     * 관심 목록 메인 페이지 (리다이렉트)
     * GET /favorites
     */
    @GetMapping("")
    public String favoriteRedirect() {
        return "redirect:/favorites/list";
    }

    /**
     * 관심 목록 페이지
     * GET /favorites/list
     */
    @GetMapping("/list")
    public String viewFavorites(Model model, HttpSession session) {
        try {
            // 로그인 체크
            Member member = (Member) session.getAttribute("member");
            if (member == null) {
                return "redirect:/members/login";
            }

            // 시장 관심 목록 조회
            List<Favorite> marketFavorites = favoriteService.getMarketFavorites(member.getMemberNum());

            // 가게 관심 목록 조회
            List<Favorite> storeFavorites = favoriteService.getStoreFavorites(member.getMemberNum());

            log.info("=== 관심 목록 페이지 ===");
            log.info("회원번호: {}", member.getMemberNum());
            log.info("시장 관심 수: {}", marketFavorites != null ? marketFavorites.size() : 0);
            log.info("가게 관심 수: {}", storeFavorites != null ? storeFavorites.size() : 0);

            model.addAttribute("marketFavorites", marketFavorites);
            model.addAttribute("storeFavorites", storeFavorites);
            model.addAttribute("member", member);

            return "favorite/list";
        } catch (Exception e) {
            log.error("관심 목록 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
}
