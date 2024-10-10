package org.backend.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {
    @Value("${kakao.api-key")
    private String kakaoApiKey;

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        return "main";
    }
}
