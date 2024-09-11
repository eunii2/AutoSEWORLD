package org.backend.Controller;

import org.backend.Service.ConferenceDataLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@RestController
@RequestMapping("/api/conference")
public class ConferenceController {

    @Autowired
    private ConferenceDataLoader conferenceDataLoader;

    @PostMapping("/upload")
    public String uploadJsonData(@RequestParam("filePath") String filePath) {
        try {
            conferenceDataLoader.loadConferencesFromJson(filePath);
            return "데이터 업로드 성공";
        } catch (IOException e) {
            e.printStackTrace();
            return "데이터 업로드 실패: " + e.getMessage();
        }
    }
}
