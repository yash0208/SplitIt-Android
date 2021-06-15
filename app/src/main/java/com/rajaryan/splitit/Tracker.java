package com.rajaryan.splitit;

public class Tracker {
    String Partner,Uid,Description,Amount,Choice;
    public Tracker(){

    }
    public Tracker(String partner, String uid, String description, String amount, String choice) {
        Partner = partner;
        Uid = uid;
        Description = description;
        Amount = amount;
        Choice = choice;
    }

    public String getPartner() {
        return Partner;
    }

    public void setPartner(String partner) {
        Partner = partner;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getChoice() {
        return Choice;
    }

    public void setChoice(String choice) {
        Choice = choice;
    }
}

