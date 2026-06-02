import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Driver {
    
    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType;
    private String address;
    private String birthdate;

    // Constructor for driver
    public Driver(String driverID, String name, int experienceYears, String licenseType, String address, String birthdate) {

        if(!isValidDriverID(driverID)) {
            throw new IllegalArgumentException("Invalid driver Id: " + driverID);
        }
        if (!isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address: " + address);
        }
        if (!isValidBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid birthdate: " + birthdate);
        }
        
        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    //Validates driverID:
    public static boolean isValidDriverID(String id) {
        if (id == null || id.length() != 10) {
            return false;
        }
 
        // First 2 chars must be digits 2-9
        char c0 = id.charAt(0);
        char c1 = id.charAt(1);
        if (!Character.isDigit(c0) || c0 < '2' || c0 > '9') return false;
        if (!Character.isDigit(c1) || c1 < '2' || c1 > '9') return false;
 
        // Characters 3-8 (index 2-7) must contain at least 2 special characters
        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char ch = id.charAt(i);
            if (!Character.isLetterOrDigit(ch)) {
                specialCount++;
            }
        }
        if (specialCount < 2) return false;
 
        // Last 2 chars must be uppercase A-Z
        char c8 = id.charAt(8);
        char c9 = id.charAt(9);
        if (!Character.isUpperCase(c8) || !Character.isUpperCase(c9)) return false;
 
        return true;
    }
 

    //Validates address format:
    public static boolean isValidAddress(String address) {
        if (address == null) return false;
 
        String[] parts = address.split("\\|");
        if (parts.length != 5) return false;
 
        String streetNum  = parts[0];
        String streetName = parts[1];
        String city       = parts[2];
        String state      = parts[3];
        String country    = parts[4];
 
        // Street number must be non-empty and numeric
        if (streetNum.isEmpty()) return false;
        for (int i = 0; i < streetNum.length(); i++) {
            if (!Character.isDigit(streetNum.charAt(i))) return false;
        }
 
        // All other parts must be non-empty
        if (streetName.isEmpty() || city.isEmpty()
                || state.isEmpty() || country.isEmpty()) {
            return false;
        }
 
        return true;
    }
 
    //Validates birthdate format: DD-MM-YYYY
    // Day 1-31, Month 1-12
    public static boolean isValidBirthdate(String birthdate) {
        if (birthdate == null || birthdate.length() != 10) return false;
 
        if (birthdate.charAt(2) != '-' || birthdate.charAt(5) != '-') return false;
 
        // All non-dash positions must be digits
        for (int i = 0; i < birthdate.length(); i++) {
            if (i == 2 || i == 5) continue;
            if (!Character.isDigit(birthdate.charAt(i))) return false;
        }
 
        int day   = Integer.parseInt(birthdate.substring(0, 2));
        int month = Integer.parseInt(birthdate.substring(3, 5));
        int year  = Integer.parseInt(birthdate.substring(6, 10));
 
        if (day < 1 || day > 31)     return false;
        if (month < 1 || month > 12) return false;
        if (year < 1900 || year > 2025) return false;
 
        return true;
    }
 
    //Getters
 
    public String getDriverID()       { return driverID; }
    public String getName()           { return name; }
    public int getExperienceYears()   { return experienceYears; }
    public String getLicenseType()    { return licenseType; }
    public String getAddress()        { return address; }
    public String getBirthdate()      { return birthdate; }
 
    //Setters (used by DriverRepository for updates)
 
    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
 
    //Convert driver to a single line for TXT file storage
    public String toFileString() {
        return driverID + "," + name + "," + experienceYears + ","
                + licenseType + "," + address + "," + birthdate;
    }
 
    //Create a driver from a TXT file line
    public static Driver fromFileString(String line) {
        String[] parts = line.split(",", 6);
        return new Driver(parts[0], parts[1],
                Integer.parseInt(parts[2]), parts[3], parts[4], parts[5]);
    }
 
    @Override
    public String toString() {
        return "Driver{driverID='" + driverID + "', name='" + name
                + "', experience=" + experienceYears
                + ", license='" + licenseType + "'}";
    }
}