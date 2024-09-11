package org.backend.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "conference")
public class Conference implements Serializable {

    @Id
    private Long id;

    @JsonProperty("conference_name")
    private String conferenceName;

    @JsonProperty("cfp_url")
    private String cfpUrl;

    @JsonProperty("conference_dates")
    private String conferenceDates;

    @JsonProperty("conference_location")
    private String conferenceLocation;

    @JsonProperty("is_cfp")
    private String isCfp;

    @JsonProperty("is_conference")
    private String isConference;

    @JsonProperty("submission_deadline")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private Date submissionDeadline;


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsCfp() {
        return isCfp;
    }

    public void setIsCfp(String isCfp) {
        this.isCfp = isCfp;
    }

    public String getIsConference() {
        return isConference;
    }

    public void setIsConference(String isConference) {
        this.isConference = isConference;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public Date getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }

    public String getCfpUrl() {
        return cfpUrl;
    }

    public void setCfpUrl(String cfpUrl) {
        this.cfpUrl = cfpUrl;
    }

    public String getConferenceLocation() {
        return conferenceLocation;
    }

    public void setConferenceLocation(String conferenceLocation) {
        this.conferenceLocation = conferenceLocation;
    }

    public String getConferenceDates() {
        return conferenceDates;
    }

    public void setConferenceDates(String conferenceDates) {
        this.conferenceDates = conferenceDates;
    }
}
