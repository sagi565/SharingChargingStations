package com.example.sharingchargingstations.Model;

import java.util.Date;
import java.util.Objects;
// ToDo: Add Status

public class Rental {
    private User holderUser;
    private User renterUser;
    private Date startDate;
    private Date endDate;
    private String status; //panding, inRent, done, canceled;

    public Rental(User holderUser, User renterUser, Date startDate, Date endDate) {
        this.holderUser = holderUser;
        this.renterUser = renterUser;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public User getHolderUser() {
        return holderUser;
    }

    public void setHolderUser(User holderUser) {
        this.holderUser = holderUser;
    }

    public User getRenterUser() {
        return renterUser;
    }

    public void setRenterUser(User renterUser) {
        this.renterUser = renterUser;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
