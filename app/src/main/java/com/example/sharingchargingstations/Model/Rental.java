package com.example.sharingchargingstations.Model;

import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Rental {
    private String documentId;

    private User holderUser;
    private User renterUser;
    private Date startDate;
    private Date endDate;
    private ChargingStation chargingStation;
    private RentalStatus status; // panding, inRent, done, canceled;
    public Rental(){}
    public Rental(ChargingStation chargingStation, User holderUser, User renterUser, Date startDate, Date endDate) {
        this.holderUser = holderUser;
        this.renterUser = renterUser;
        this.startDate = startDate;
        this.endDate = endDate;
        this.chargingStation = chargingStation;
        status = RentalStatus.panding;
    }
    public RentalStatus getStatus() {
        return status;
    }
    @Exclude
    public double getPrice() {
        return chargingStation.getPricePerHour();
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
    @Exclude
    public String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(startDate);
    }
    @Exclude
    public long getDateInLong(){
        return startDate.getTime();
    }
    @Exclude
    public String getTime(){
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startDate);
        double startHour = calendarStart.get(Calendar.HOUR_OF_DAY);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endDate);
        double endHour = calendarEnd.get(Calendar.HOUR_OF_DAY);

        return (String)((int)startHour + ":" + (int)((int)startHour - startHour) + "0" +  " - " + (int)endHour + ":" + (int)((int)endHour - endHour) + "0");
    }

    public ChargingStation getChargingStation() {
        return chargingStation;
    }

    public void setChargingStation(ChargingStation chargingStation) {
        this.chargingStation = chargingStation;
    }

    @Exclude
    public String getDocumentId() {return documentId;}

    public void setDocumentId(String documentId) {this.documentId = documentId;}



}
