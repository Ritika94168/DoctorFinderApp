package com.anshul.doctorfinder;

public class DisplayList {

    private  String DoctorImage;
    private  String DoctorName;
    private  String DoctorSpecification;
    private  String DoctorDescription;
    private  String DoctorAddress;

    public DisplayList(String DoctorImage,String DoctorName,String DoctorSpecification,String DoctorDescription,String DoctorAddress){
        this.DoctorImage=DoctorImage;
        this.DoctorName=DoctorName;
        this.DoctorSpecification=DoctorSpecification;
        this.DoctorDescription=DoctorDescription;
        this.DoctorAddress=DoctorAddress;

    }

    public String getDoctorImage() {
        return DoctorImage;
    }

    public void setDoctorImage(String doctorImage) {
        DoctorImage = doctorImage;
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
