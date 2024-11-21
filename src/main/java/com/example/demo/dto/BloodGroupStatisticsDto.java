package com.example.demo.dto;

import java.math.BigInteger;

public class BloodGroupStatisticsDto {
    private BigInteger bloodGroupA;
    private BigInteger bloodGroupB;
    private BigInteger bloodGroupAB;
    private BigInteger bloodGroupO;

    public BloodGroupStatisticsDto(BigInteger bloodGroupA, BigInteger bloodGroupB, BigInteger bloodGroupAB, BigInteger bloodGroupO) {
        this.bloodGroupA = bloodGroupA;
        this.bloodGroupB = bloodGroupB;
        this.bloodGroupAB = bloodGroupAB;
        this.bloodGroupO = bloodGroupO;
    }

    // Getters and Setters
    public BigInteger getBloodGroupA() {
        return bloodGroupA;
    }

    public void setBloodGroupA(BigInteger bloodGroupA) {
        this.bloodGroupA = bloodGroupA;
    }

    public BigInteger getBloodGroupB() {
        return bloodGroupB;
    }

    public void setBloodGroupB(BigInteger bloodGroupB) {
        this.bloodGroupB = bloodGroupB;
    }

    public BigInteger getBloodGroupAB() {
        return bloodGroupAB;
    }

    public void setBloodGroupAB(BigInteger bloodGroupAB) {
        this.bloodGroupAB = bloodGroupAB;
    }

    public BigInteger getBloodGroupO() {
        return bloodGroupO;
    }

    public void setBloodGroupO(BigInteger bloodGroupO) {
        this.bloodGroupO = bloodGroupO;
    }
}
