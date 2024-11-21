package com.example.demo.dto;

import java.math.BigInteger;
import java.util.List;

public class MonthlyDonationStatisticsDto {
    private List<BigInteger> monthlyDonations;

    public MonthlyDonationStatisticsDto(List<BigInteger> monthlyDonations) {
        this.monthlyDonations = monthlyDonations;
    }

    public List<BigInteger> getMonthlyDonations() {
        return monthlyDonations;
    }

    public void setMonthlyDonations(List<BigInteger> monthlyDonations) {
        this.monthlyDonations = monthlyDonations;
    }
}
