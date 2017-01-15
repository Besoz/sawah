package com.accorpa.sawah;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Bassem on 15/01/17.
 */

public class User {

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Password")
    private String password;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("Sex")
    private String sex;

    @JsonProperty("DOB")
    private String birthDate;

    @JsonProperty("Mobile")
    private String mobileNumber;

    @JsonProperty("UserID")
    private String UserID;


    public User(String email, String password, String birthDate, String fullName, String sex, String mobileNumber, String userID) {
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.fullName = fullName;
        this.sex = sex;
        this.mobileNumber = mobileNumber;
        UserID = userID;
    }
}
