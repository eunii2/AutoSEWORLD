package org.backend.Service;

import org.backend.Entity.Conference;
import org.backend.Repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;

    public ConferenceService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    public void saveConferences(List<Conference> conferences) {
        conferenceRepository.saveAll(conferences);
    }

    public List<Conference> getSortedConferences() {
        return conferenceRepository.findConferencesBySubmissionDeadline();
    }

    // 모든 컨퍼런스 데이터를 조회하는 메서드
    public List<Conference> findAllConferences() {
        return conferenceRepository.findAll();
    }

    // 날짜 형식 변환기
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd");

    // 데이터 저장하는 로직
    public Conference saveConference(Conference conference) throws ParseException {
        if ("Y".equals(conference.getIsCfp()) && "Y".equals(conference.getIsConference())) {
            // 이미 submissionDeadline이 Date 타입이므로 추가적인 변환 불필요
            return conferenceRepository.save(conference);
        }
        return null; // 조건에 맞지 않으면 저장하지 않음
    }

    // 10분마다 데이터를 정렬하여 캐싱 또는 업데이트
    @Scheduled(fixedRate = 600000)  // 10분 간격으로 실행
    public void updateSortedConferences() {
        List<Conference> sortedConferences = conferenceRepository.findConferencesBySubmissionDeadline();
        // 캐시나 메모리에 저장 등 필요한 작업 수행 가능
    }
}
