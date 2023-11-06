package com.example.citizenshipassessment.model;

import java.util.Date;

public class CitizenshipData {
    private String firstName;
    private String middleName;
    private String lastName;
    private String nationality;
    private Date dob;
    private String gender;
    private String fatherFirstName;
    private String fatherMiddleName;
    private String motherFirstName;
    private String motherMiddleName;
    private String motherLastName;
    private String fatherLastName;
    private String citizenshipNumber;
    private String issuingCountry;

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public java.sql.Date getDob() {
        return (java.sql.Date) dob;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherFirstName() {
        return fatherFirstName;
    }

    public String getFatherMiddleName() {
        return fatherMiddleName;
    }

    public String getMotherFirstName() {
        return motherFirstName;
    }

    public String getMotherMiddleName() {
        return motherMiddleName;
    }

    public String getMotherLastName() {
        return motherLastName;
    }

    public String getFatherLastName() {
        return fatherLastName;
    }

    public String getCitizenshipNumber() {
        return citizenshipNumber;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFatherFirstName(String fatherFirstName) {
        this.fatherFirstName = fatherFirstName;
    }

    public void setFatherMiddleName(String fatherMiddleName) {
        this.fatherMiddleName = fatherMiddleName;
    }

    public void setMotherFirstName(String motherFirstName) {
        this.motherFirstName = motherFirstName;
    }

    public void setMotherMiddleName(String motherMiddleName) {
        this.motherMiddleName = motherMiddleName;
    }

    public void setMotherLastName(String motherLastName) {
        this.motherLastName = motherLastName;
    }

    public void setFatherLastName(String fatherLastName) {
        this.fatherLastName = fatherLastName;
    }

    public void setCitizenshipNumber(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }
}


