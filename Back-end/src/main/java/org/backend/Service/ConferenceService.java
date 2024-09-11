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
        return conferenceRepository.findAllOrderedByDeadline();
    }

    // 모든 컨퍼런스 데이터를 조회하는 메서드
    public List<Conference> findAllConferences() {
        return conferenceRepository.findAll();
    }

    // 날짜 형식 변환
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd");

    // 데이터 저장하는 로직
    public Conference saveConference(Conference conference) throws ParseException {
        if ("Y".equals(conference.getIsCfp()) && "Y".equals(conference.getIsConference())) {
            return conferenceRepository.save(conference);
        }
        return null;
    }

    // 10분마다 데이터를 정렬하여 캐싱 또는 업데이트
    @Scheduled(fixedRate = 600000)  // 10분 간격으로 실행
    public void updateSortedConferences() {
        List<Conference> sortedConferences = conferenceRepository.findAllOrderedByDeadline();
    }
}
