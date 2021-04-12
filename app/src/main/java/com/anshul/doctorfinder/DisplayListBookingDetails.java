package com.anshul.doctorfinder;


public class DisplayListBookingDetails {


    private  String DoctorName;
    private  String DoctorSpecification;
    private  String BookingDate;
    private  String BookingTime;


    public DisplayListBookingDetails( String DoctorName, String DoctorSpecification, String BookingDate, String BookingTime){
        this.DoctorName=DoctorName;
        this.DoctorSpecification=DoctorSpecification;
        this.BookingDate=BookingDate;
        this.BookingTime=BookingTime;

    }




    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getDoctorSpecification() {
        return DoctorSpecification;
    }

    public void setDoctorSpecification(String doctorSpecification) {
        DoctorSpecification = doctorSpecification;
    }
    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingdate) {
        BookingDate = bookingdate;
    }
    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String bookingtime) {
        BookingTime = bookingtime;
    }


}
