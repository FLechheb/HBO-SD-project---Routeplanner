package com.school.project1.backend.model.needs;


public enum NeedType {

    AUDITORY_INFORMATION_REQUIRED("Hoorbare informatie nodig"),
    ELEVATOR_REQUIRED("Lift nodig"),
    EXTRA_TRANSFER_TIME("Extra overstaptijd"),
    GUIDING_LINE_REQUIRED("Geleidelijn nodig"),
    LEVEL_ACCESS_REQUIRED("Trapvrije toegang nodig"),
    SEATING_REQUIRED("Zitplaats nodig"),
    VISUAL_INFORMATION_REQUIRED("Visuele informatie nodig"),
    WHEELCHAIR_ACCESSIBLE("Rolstoeltoegankelijk");

    private final String description;


    NeedType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}