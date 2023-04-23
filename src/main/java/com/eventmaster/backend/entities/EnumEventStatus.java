package com.eventmaster.backend.entities;

public enum EnumEventStatus {
    INPREPARATION("inpreparation"), SCHEDULED("scheduled"), RUNNING("running"), ACCOMPLISHED("accomplished"), CANCELLED("cancelled");


    public final String status;

    EnumEventStatus(String status) {
        this.status = status;
    }

}
