package com.example.solarpanelcalculator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "analyses")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private double totalConsumption;

    @Min(value = 0, message = "O número de horas deve ser no mínimo 0")
    @Max(value = 24, message = "O número de horas não pode exceder 24")
    @Column(nullable = false)
    private int sunlightHours;

    @Column(nullable = false)
    private String result;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection
    @CollectionTable(name = "analysis_appliances_data", joinColumns = @JoinColumn(name = "analysis_id"))
    private List<ApplianceData> appliancesData;

    public Analysis() {}

    public Analysis(double totalConsumption, int sunlightHours, String result, User user, List<ApplianceData> appliancesData) {
        this.totalConsumption = totalConsumption;
        this.sunlightHours = sunlightHours;
        this.result = result;
        this.user = user;
        this.appliancesData = appliancesData;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(double totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public int getSunlightHours() {
        return sunlightHours;
    }

    public void setSunlightHours(int sunlightHours) {
        this.sunlightHours = sunlightHours;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ApplianceData> getAppliancesData() {
        return appliancesData;
    }

    public void setAppliancesData(List<ApplianceData> appliancesData) {
        this.appliancesData = appliancesData;
    }
}
