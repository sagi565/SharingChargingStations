package com.example.sharingchargingstations.Model;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Rental {
    private User holderUser;
    private User renterUser;
    private Date startDate;
    private Date endDate;

    private double price;
    private RentalStatus status; //panding, inRent, done, canceled;

    public Rental(User holderUser, User renterUser, Date startDate, Date endDate) {
        this.holderUser = holderUser;
        this.renterUser = renterUser;
        this.startDate = startDate;
        this.endDate = endDate;
        long secs = (this.endDate.getTime() - this.startDate.getTime()) / 1000;
        double hours = secs / 3600;
        price = holderUser.getMyChargingStation().getPricePerHour() * hours;
        status = RentalStatus.panding;
    }
    public RentalStatus getStatus() {
        return status;
    }
    public double getPrice() {
        return price;
    }
    public void setStatus(RentalStatus status) {
        this.status = status;
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

    public String getTime(){
        Calendar calendarStart = GregorianCalendar.getInstance();
        calendarStart.setTime(startDate);
        double startHour = calendarStart.get(Calendar.HOUR_OF_DAY);

        Calendar calendarEnd = GregorianCalendar.getInstance();
        calendarEnd.setTime(endDate);
        double endHour = calendarEnd.get(Calendar.HOUR_OF_DAY);

        return (String)((int)startHour + ":" + (int)((int)startHour - startHour) + "0" +  " - " + (int)endHour + ":" + (int)((int)endHour - endHour) + "0");
    }


}
