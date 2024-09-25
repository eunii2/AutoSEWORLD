package org.backend.Controller;

import org.backend.Entity.Conference;
import org.backend.Repository.ConferenceRepository;
import org.backend.Service.ConferenceDataLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
public class ConferenceController {

    @Autowired
    private ConferenceDataLoader conferenceDataLoader;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @GetMapping("/conference")
    public List<Conference> getAllConferences() {
        return conferenceRepository.findAllOrderedByDeadline();
    }

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
