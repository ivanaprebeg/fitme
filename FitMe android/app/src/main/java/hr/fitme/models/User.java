package hr.fitme.models;


import java.util.Date;
import java.util.Calendar;

/**
 * Created by ivanc on 21/05/2017.
 */

public class User {

    private int id;
    private String username;
    private String email;
    private String password;
    private float height;
    private float desiredWeight;
    private int desiredKcal;
    private Date dateOfBirth;
    private boolean gender;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getDesiredWeight() {
        return desiredWeight;
    }

    public void setDesiredWeight(float desiredWeight) {
        this.desiredWeight = desiredWeight;
    }

    public int getDesiredKcal() {
        return desiredKcal;
    }

    public void setDesiredKcal(int desiredKcal) {
        this.desiredKcal = desiredKcal;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int calculateBMR(float desiredWeight) {
        //gender==true for men, false for women
        if (gender==true)
            return (int)(66.47+ (13.75* desiredWeight) + (5.0*height) - (6.75*calculateAge()));
        else
            return (int)(66.50 + (9.56* desiredWeight) + (1.84*height) - (4.67*calculateAge()));
    }

    public int calculateAge() {
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        long timeBetween = now.getTime() - dateOfBirth.getTime();
        double yearsBetween = timeBetween / 3.156e+10;
        return (int) Math.floor(yearsBetween);
    }

    public double calculateStep() {
        if (gender==true)
            return height*0.00415;
        return height*0.00413;
    }

    public int calculateCalories(float distance) {
        return (int)(0.41602 * desiredWeight * distance/1000);
    }

}
