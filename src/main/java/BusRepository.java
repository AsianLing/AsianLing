package main.java;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BusRepository {
 
    private List<Bus> buses = new ArrayList<>();
    private String filePath;
 
    //Constructor, loads existing buses from file on startup
    public BusRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile();
    }
 
    //Adds a new bus to the repository
    //rejects duplicate busIDs
    public boolean add(Bus bus) {
        //check for duplicate busID
        for (Bus b : buses) {
            if (b.getBusID().equals(bus.getBusID())) {
                throw new IllegalArgumentException(
                        "Duplicate busID: " + bus.getBusID());
            }
        }
        buses.add(bus);
        saveToFile();
        return true;
    }
 
    //Updates an existing bus's details.
    //capacity cannot increase during update
    public boolean update(String id, Bus updatedBus) {
        for (Bus b : buses) {
            if (b.getBusID().equals(id)) {
 
                //capacity cannot increase
                if (updatedBus.getCapacity() > b.getCapacity()) {
                    throw new IllegalArgumentException(
                            "Bus capacity cannot increase during update (B2)");
                }
 
                b.setCapacity(updatedBus.getCapacity());
                b.setFuelLevel(updatedBus.getFuelLevel());
                b.setFuelType(updatedBus.getFuelType());
 
                saveToFile();
                return true;
            }
        }
        return false; //bus not found
    }
 
    //Retrieves a bus by busID
    public Bus retrieve(String id) {
        for (Bus b : buses) {
            if (b.getBusID().equals(id)) {
                return b;
            }
        }
        return null;
    }
 
    //Returns the total number of buses stored
    public int count() {
        return buses.size();
    }
 
    //Checks if a driver is allowed to drive a given bus based on age
    public static boolean isDriverAllowedByAge(int driverAge, Bus bus) {
        if (driverAge > 50 && bus.getCapacity() >= 50) {
            return false;
        }
        return true;
    }
 
    //Checks if a driver has enough experience to drive an electric bus
    //Driver must have at least 5 years experience to drive electric buses
    public static boolean isDriverAllowedByExperience(Driver driver, Bus bus) {
        if (bus.getFuelType().equalsIgnoreCase("Electricity")
                && driver.getExperienceYears() < 5) {
            return false;
        }
        return true;
    }
 
    //Checks if a driver's licence allows them to drive the given bus
    public static boolean isDriverAllowedByLicence(Driver driver, Bus bus) {
        String fuelType   = bus.getFuelType();
        String licenseType = driver.getLicenseType();
 
        boolean isRestrictedBus = fuelType.equalsIgnoreCase("Electricity")
                || fuelType.equalsIgnoreCase("Hybrid");
 
        boolean hasValidLicence = licenseType.equalsIgnoreCase("Heavy")
                || licenseType.equalsIgnoreCase("PublicTransport");
 
        if (isRestrictedBus && !hasValidLicence) {
            return false;
        }
        return true;
    }
 
    //File persistence
 
    //Saves all buses to the TXT file
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Bus b : buses) {
                writer.write(b.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving buses to file: " + e.getMessage());
        }
    }
 
    //Loads buses from the TXT file on startup
    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;
 
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    buses.add(Bus.fromFileString(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading buses from file: " + e.getMessage());
        }
    }
 
    //Clear all buses
    public void clear() {
        buses.clear();
        saveToFile();
    }
}