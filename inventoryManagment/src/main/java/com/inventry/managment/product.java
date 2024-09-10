package com.inventry.managment;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//import javax.persistence.*;

@Entity
@Table(name = "product")
public class product {
    
    @Id
    private int pid;
    
    private String pname;
    private double price;
    private int pquantity;

    public product() {}

    public product(String pname, double price, int pquantity) {
        this.pname = pname;
        this.price = price;
        this.pquantity = pquantity;
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPquantity() {
        return pquantity;
    }

    public void setPquantity(int pquantity) {
        this.pquantity = pquantity;
    }
}
