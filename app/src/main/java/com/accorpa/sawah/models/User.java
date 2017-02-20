package com.accorpa.sawah.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orm.annotation.Ignore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bassem on 15/01/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public static final String MALE = "Male", FEMALE = "Female", DATE_FORMAT = "yyyy-MM-dd";

    @JsonProperty("Email")
    private String email;

    @JsonProperty("UserName")
    private String userName;

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
    private String userID;

    @JsonProperty("ImageLocation")
    private String imageLocation;

    @JsonProperty("ImageName")
    private String imageName;

    @Ignore
    private Date date;

    public String getUserName() {
        return userName;
    }

    public User(String email, String password, String birthDate, String fullName, String sex,
                String mobileNumber, String userID, String imageLocation, String userName) {
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.fullName = fullName;
        this.sex = sex;
        this.mobileNumber = mobileNumber;
        this.userID = userID;
        this.imageLocation = imageLocation;
        this.userName = userName;

    }

    public User(){
        date = new Date();
    }

    public String getUserID() {
        return this.userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDate(String birthDate) {

        this.birthDate = birthDate;

        if(!birthDate.equals("")){
            SimpleDateFormat parser=new SimpleDateFormat(DATE_FORMAT);
            try {
                date = parser.parse(birthDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public String getBirthDate(){
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getBirthDateObject() {
        return date;
    }
}
