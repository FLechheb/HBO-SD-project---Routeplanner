package com.school.project1.backend.model.needs;


public enum NeedType {

    DEAF ("Deaf"),
    BLIND("Blind"),
    WHEELCHAIR_BOUND ("Rolstoel"),
    ELDERLY ("Bejaard"),
    PHYSICAL_IMPAIRED ("Slecht ter been");

    private final String description;
    NeedType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
