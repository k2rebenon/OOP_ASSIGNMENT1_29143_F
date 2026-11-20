package com.auca.classroom;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

public class Classroom implements Identifiable, Registrable {
    private String classroomId;
    private String buildingName;
    private String roomNumber;
    private int capacity;
    private int floorNumber;
    private boolean available;

    public Classroom(String classroomId, String buildingName, String roomNumber,
                     int capacity, int floorNumber, boolean available) throws ValidationException {
        this.classroomId = Registrable.requireText(classroomId, "Classroom ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        this.buildingName = Registrable.requireText(buildingName, "Building name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        this.roomNumber = Registrable.requireText(roomNumber, "Room number", 1, 10);
        this.capacity = validateCapacity(capacity);
        this.floorNumber = validateFloor(floorNumber);
        this.available = available;
    }

    private static int validateCapacity(int capacity) throws ValidationException {
        if (capacity <= 0) {
            throw new ValidationException("Classroom capacity must be greater than zero.");
        }
        return capacity;
    }

    private static int validateFloor(int floorNumber) throws ValidationException {
        if (floorNumber < 0) {
            throw new ValidationException("Floor number must be zero or greater.");
        }
        return floorNumber;
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(classroomId, "Classroom ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireText(buildingName, "Building name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        Registrable.requireText(roomNumber, "Room number", 1, 10);
        validateCapacity(capacity);
        validateFloor(floorNumber);
    }

    @Override
    public String getId() {
        return classroomId;
    }

    @Override
    public String getDisplayName() {
        return buildingName + " / Room " + roomNumber;
    }

    @Override
    public boolean isActive() {
        return available;
    }

    @Override
    public String getRoleDescription() {
        return "Classroom";
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + classroomId + "] " + getDisplayName() + " | capacity " + capacity
                + " | floor " + floorNumber + " | " + (available ? "AVAILABLE" : "UNAVAILABLE");
    }

    @Override
    public void printDetails() {
        System.out.println("  Classroom ID       : " + classroomId);
        System.out.println("  Building name      : " + buildingName);
        System.out.println("  Room number        : " + roomNumber);
        System.out.println("  Capacity           : " + capacity);
        System.out.println("  Floor number       : " + floorNumber);
        System.out.println("  Available          : " + (available ? "YES" : "NO"));
    }

    @Override
    public String toString() {
        return "Classroom{classroomId='" + classroomId + "', buildingName='" + buildingName
                + "', roomNumber='" + roomNumber + "', capacity=" + capacity
                + ", floorNumber=" + floorNumber + ", available=" + available + "}";
    }

    public String getClassroomId() {
        return classroomId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) throws ValidationException {
        this.buildingName = Registrable.requireText(buildingName, "Building name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) throws ValidationException {
        this.roomNumber = Registrable.requireText(roomNumber, "Room number", 1, 10);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) throws ValidationException {
        this.capacity = validateCapacity(capacity);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) throws ValidationException {
        this.floorNumber = validateFloor(floorNumber);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
