package com.mycompany.visitormanagementsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "visitors")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visitor_name", nullable = false, length = 120)
    private String visitorName;

    @Column(name = "mobile_number", nullable = false, length = 25)
    private String mobileNumber;

    @Column(name = "person_to_visit", nullable = false, length = 120)
    private String personToVisit;

    @Column(name = "purpose", nullable = false, length = 500)
    private String purpose;

    @Lob
    @Column(name = "visitor_photo", columnDefinition = "MEDIUMBLOB")
    private byte[] visitorPhoto;

    @CreationTimestamp
    @Column(name = "check_in_at", nullable = false, updatable = false)
    private LocalDateTime checkInAt;

    @Column(name = "check_out_at")
    private LocalDateTime checkOutAt;

    public Long getId() {
        return id;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPersonToVisit() {
        return personToVisit;
    }

    public void setPersonToVisit(String personToVisit) {
        this.personToVisit = personToVisit;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public byte[] getVisitorPhoto() {
        return visitorPhoto;
    }

    public void setVisitorPhoto(byte[] visitorPhoto) {
        this.visitorPhoto = visitorPhoto;
    }

    public LocalDateTime getCheckInAt() {
        return checkInAt;
    }

    public LocalDateTime getCheckOutAt() {
        return checkOutAt;
    }

    public void setCheckOutAt(LocalDateTime checkOutAt) {
        this.checkOutAt = checkOutAt;
    }

    public String getCheckInAtDisplay() {
        return checkInAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getCheckOutAtDisplay() {
        return checkOutAt == null ? "" : checkOutAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
