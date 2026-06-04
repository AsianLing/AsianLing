package main.java;

public class Bus {

    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType;

    // Constructor, validates busID on creation
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {

        //Validate busID format
        if (!isValidBusID(busID)) {
            throw new IllegalArgumentException("Invalid busId: " + busID);
        }
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    //Validates busID:
    public static boolean isValidBusID(String busID) {
        if (busID == null || busID.length() != 8) return false;
 
        for (int i = 0; i < busID.length(); i++) {
            if (!Character.isDigit(busID.charAt(i))) return false;
        }
        return true;
    }
 
    //Getters
 
    public String getBusID()     { return busID; }
    public int getCapacity()     { return capacity; }
    public double getFuelLevel() { return fuelLevel; }
    public String getFuelType()  { return fuelType; }
 
    //Setters (used by BusRepository for updates)
 
    public void setCapacity(int capacity)     { this.capacity = capacity; }
    public void setFuelLevel(double fuelLevel){ this.fuelLevel = fuelLevel; }
    public void setFuelType(String fuelType)  { this.fuelType = fuelType; }
 
    //Convert bus to a single line for TXT file storage
    public String toFileString() {
        return busID + "," + capacity + "," + fuelLevel + "," + fuelType;
    }
 
    //Create a Bus from a TXT file line
    public static Bus fromFileString(String line) {
        String[] parts = line.split(",", 4);
        return new Bus(parts[0], Integer.parseInt(parts[1]),
                Double.parseDouble(parts[2]), parts[3]);
    }
 
    @Override
    public String toString() {
        return "Bus{busID='" + busID + "', capacity=" + capacity
                + ", fuelType='" + fuelType + "'}";
    }
}