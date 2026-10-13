package com.school.project1.backend.model.facilities;

public abstract class Facility { //TODO: op zelfde manier als facility opbouwen (niet elke lift is bv hetzelfde (wel/geen stoel in de lift bv))
    boolean isLevelFloored = true;
    boolean hasSeating = false;
    boolean usableWithoutVision = true;
    boolean usableWithoutHearing = true;
    boolean isWheelchairAccessible = false;
    boolean hasGuidingLine = false;

    public boolean hasGuidingLine() {
        return hasGuidingLine;
    }

    public boolean isLevelFloored() {
        return isLevelFloored;
    }
    public boolean hasSeating() {
        return hasSeating;
    }

    public boolean isUsableWithoutVision() {
        return usableWithoutVision;
    }

    public boolean isUsableWithoutHearing() {
        return usableWithoutHearing;
    }

    public boolean isWheelchairAccessible(){
        return isWheelchairAccessible;
    }
}
