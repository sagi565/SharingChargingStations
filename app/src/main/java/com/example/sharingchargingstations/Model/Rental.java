package com.example.sharingchargingstations.Model;

import java.util.Date;
import java.util.Objects;
// ToDo: Add Status

public class Rental {
    private User holderUser;
    private User renterUser;
    private Date startDate;
    private Date endDate;
    private RentalStatus status; //panding, inRent, done, canceled;

    private double price;

    public Rental(User holderUser, User renterUser, Date startDate, Date endDate) {
        this.holderUser = holderUser;
        this.renterUser = renterUser;
        this.startDate = startDate;
        this.endDate = endDate;
        status = RentalStatus.futureRent;
        price = renterUser.getMyChargingStation().getPricePerHour();  //todo: multiply by hours  https://www.javatpoint.com/how-to-calculate-date-difference-in-java
    }
    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
