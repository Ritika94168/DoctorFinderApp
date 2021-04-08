package com.anshul.doctorfinder;

import android.graphics.Bitmap;

public class DisplayListAllPatientDetails {


    private  String PatientName;
    private  String PatientMobileNumber;
    private  String BookingDate;
    private  String BookingTime;



    public DisplayListAllPatientDetails( String PatientName, String PatientMobileNumber, String BookingDate, String BookingTime){
        this.PatientName=PatientName;
        this.PatientMobileNumber=PatientMobileNumber;
        this.BookingDate=BookingDate;
        this.BookingTime=BookingTime;

    }




    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getPatientMobileNumber() {
        return PatientMobileNumber;
    }

    public void setPatientMobileNumber(String patientNumber) {
        PatientMobileNumber = patientNumber;
    }
    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }
    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String bookingTime) {
        BookingTime = bookingTime;
    }

}
