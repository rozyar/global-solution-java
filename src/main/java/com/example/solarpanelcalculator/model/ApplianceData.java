package com.example.solarpanelcalculator.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class ApplianceData {

    private String applianceName;
    private double powerConsumption;

    public ApplianceData() {}

    public ApplianceData(String applianceName, double powerConsumption) {
        this.applianceName = applianceName;
        this.powerConsumption = powerConsumption;
    }

    public String getApplianceName() {
        return applianceName;
    }

    public void setApplianceName(String applianceName) {
        this.applianceName = applianceName;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }
}
