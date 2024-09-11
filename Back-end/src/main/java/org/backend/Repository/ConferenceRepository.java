package org.backend.Repository;

import org.backend.Entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    @Query("SELECT c FROM Conference c WHERE c.submissionDeadline IS NOT NULL ORDER BY c.submissionDeadline ASC")
    List<Conference> findConferencesBySubmissionDeadline();
}
