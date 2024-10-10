package org.backend.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.Entity.Conference;
import org.backend.Repository.ConferenceRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ConferenceDataLoader {

    private final ConferenceRepository conferenceRepository;
    private final ObjectMapper objectMapper;

    public ConferenceDataLoader(ConferenceRepository conferenceRepository, ObjectMapper objectMapper) {
        this.conferenceRepository = conferenceRepository;
        this.objectMapper = objectMapper;
    }

    public void loadConferencesFromJson(String filePath) throws IOException {
        // JSON 파일을 읽기 위해 파일 경로를 지정
        File jsonFile = new File(filePath);

        // JSON 데이터를 Conference 리스트로 변환
        List<Conference> conferences = objectMapper.readValue(jsonFile, new TypeReference<List<Conference>>() {});

        // 데이터베이스에 저장 (조건: is_cfp와 is_conference가 모두 "Y"인 경우만 저장)
        for (Conference conference : conferences) {
            // is_cfp와 is_conference가 둘 다 "Y"인 경우에만 저장
            if ("Y".equals(conference.getIsCfp()) && "Y".equals(conference.getIsConference())) {
                conferenceRepository.save(conference);
            }
        }

        System.out.println("DB에 조건을 만족하는 데이터만 저장 완료");
    }
    // GPT 응답 데이터를 DB에 저장하는 메서드
    public void saveGptResponseToDb(String gptJsonResponse) throws IOException {
        Conference conference = objectMapper.readValue(gptJsonResponse, Conference.class);

        if ("Y".equals(conference.getIsCfp()) && "Y".equals(conference.getIsConference())) {
            conferenceRepository.save(conference);
            System.out.println("GPT 데이터 DB 저장 완료: " + conference.getConferenceName());
        } else {
            System.out.println("GPT 데이터 저장 조건을 만족하지 않음");
        }
    }
}
