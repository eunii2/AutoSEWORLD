package org.backend;

import org.backend.Service.ConferenceDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class BackEndApplication implements CommandLineRunner {

    @Autowired
    private ConferenceDataLoader conferenceDataLoader;

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // JSON 파일의 절대 경로를 입력
            String jsonFilePath = "/Users/eunii/Documents/CODE/AutoSEWORLD/dataset.json";  // Mac/Linux 경로 예시

            // 데이터 로드 및 저장
            conferenceDataLoader.loadConferencesFromJson(jsonFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
