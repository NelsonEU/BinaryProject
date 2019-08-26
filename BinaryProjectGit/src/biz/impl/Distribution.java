package biz.impl;

import biz.dto.IDistributionDto;

public class Distribution implements IDistributionDto {
    private String id;
    private String distribution;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDistribution() {
        return distribution;
    }

    @Override
    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    @Override
    public String toString() {
        return "Distribution{" +
                "id=" + id +
                ", distribution=" + distribution +
                '}';
    }
}
