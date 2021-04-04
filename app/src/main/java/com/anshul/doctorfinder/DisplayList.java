package com.anshul.doctorfinder;

import android.graphics.Bitmap;

public class DisplayList {

    private  Bitmap DoctorImage;
    private  String DoctorName;
    private  String DoctorSpecification;
    private  String DoctorDescription;
    private  String DoctorAddress;

    public Bitmap getDoctorImage() {
        return DoctorImage;
    }

    public void setDoctorImage(Bitmap doctorImage) {
        DoctorImage = doctorImage;
    }

    public DisplayList(Bitmap DoctorImage, String DoctorName, String DoctorSpecification, String DoctorDescription, String DoctorAddress){
        this.DoctorImage=DoctorImage;
        this.DoctorName=DoctorName;
        this.DoctorSpecification=DoctorSpecification;
        this.DoctorDescription=DoctorDescription;
        this.DoctorAddress=DoctorAddress;

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
    public String getDoctorDescription() {
        return DoctorDescription;
    }

    public void setDoctorDescription(String doctorDescription) {
        DoctorDescription = DoctorDescription;
    }
    public String getDoctorAddress() {
        return DoctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        DoctorAddress = doctorAddress;
    }


}
