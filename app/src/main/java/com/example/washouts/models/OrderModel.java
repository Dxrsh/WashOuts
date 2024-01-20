package com.example.washouts.models;

public class OrderModel {
    String userId,orderId,fullName,fullAddress,pickUpDate,pickUpTime,serviceType,noOfGarments,payment,modeOfPayment;


    public OrderModel() {
    }

    public OrderModel(String userId, String orderId, String fullName, String fullAddress, String pickUpDate,
                      String pickUpTime, String serviceType, String noOfGarments, String payment, String modeOfPayment) {
        this.userId = userId;
        this.orderId = orderId;
        this.fullName = fullName;
        this.fullAddress = fullAddress;
        this.pickUpDate = pickUpDate;
        this.pickUpTime = pickUpTime;
        this.serviceType = serviceType;
        this.noOfGarments = noOfGarments;
        this.payment = payment;
        this.modeOfPayment = modeOfPayment;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getNoOfGarments() {
        return noOfGarments;
    }

    public void setNoOfGarments(String noOfGarments) {
        this.noOfGarments = noOfGarments;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
