package main.java;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository {
 
    private List<Driver> drivers = new ArrayList<>();
    private String filePath;
 
    // Constructor,loads existing drivers from file on startup
    public DriverRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile();
    }


    //rejects duplicate driverIDs
    public boolean add(Driver driver) {
        //check for duplicate driverID
        for (Driver d : drivers) {
            if (d.getDriverID().equals(driver.getDriverID())) {
                throw new IllegalArgumentException(
                        "Duplicate driverID: " + driver.getDriverID());
            }
        }
        drivers.add(driver);
        saveToFile();
        return true;
    }
 
    //Updates an existing driver's details.
    //licenseType cannot be changed if experienceYears > 10
    //driverID and name cannot be modified
    public boolean update(String id, Driver updatedDriver) {
        for (Driver d : drivers) {
            if (d.getDriverID().equals(id)) {
 
                //name cannot be modified
                if (!updatedDriver.getName().equals(d.getName())) {
                    throw new IllegalArgumentException(
                            "Name cannot be modified (D5)");
                }
 
                //Update experience first
                d.setExperienceYears(updatedDriver.getExperienceYears());
 
                //if experience > 10, licenseType cannot change
                if (d.getExperienceYears() > 10) {
                    if (!updatedDriver.getLicenseType().equals(d.getLicenseType())) {
                        throw new IllegalArgumentException(
                                "LicenseType cannot be changed for drivers with more than 10 years experience (D4)");
                    }
                } else {
                    d.setLicenseType(updatedDriver.getLicenseType());
                }
 
                d.setAddress(updatedDriver.getAddress());
                d.setBirthdate(updatedDriver.getBirthdate());
 
                saveToFile();
                return true;
            }
        }
        return false; // driver not found
    }
 
    //Retrieves a driver by driverID
    public Driver retrieve(String id) {
        for (Driver d : drivers) {
            if (d.getDriverID().equals(id)) {
                return d;
            }
        }
        return null;
    }
 

    //Returns the total number of drivers stored.

    public int count() {
        return drivers.size();
    }
 
    //File persistence
 
    //saves all drivers to the TXT file
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Driver d : drivers) {
                writer.write(d.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving drivers to file: " + e.getMessage());
        }
    }
 
    //loads drivers from the TXT file on startup
    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return; //no file yet
 
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    drivers.add(Driver.fromFileString(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading drivers from file: " + e.getMessage());
        }
    }
 
    //clear all drivers, used for testing
    public void clear() {
        drivers.clear();
        saveToFile();
    }
}
