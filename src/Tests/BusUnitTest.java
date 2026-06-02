import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusUnitTest {

    private Bus createBus(String id, int capacity, String fuel) {
        return new Bus(id, capacity, 80.0, fuel);
    }

    private Driver createDriver(int exp, String licence) {
        return new Driver(
                "25!!aaaaBB",
                "Test Driver",
                exp,
                licence,
                "12|Main Street|Melbourne|VIC|Australia",
                "01-01-1990"
        );
    }

    // -------- B1 --------

    @Test
    void validBusId_shouldBeAccepted() {
        assertTrue(BusValidator.isValidBusID("12345678"));
    }

    @Test
    void busIdWithLetter_shouldFail() {
        assertFalse(BusValidator.isValidBusID("1234567A"));
    }

    @Test
    void busIdTooShort_shouldFail() {
        assertFalse(BusValidator.isValidBusID("1234567"));
    }

    // -------- B2 --------

    @Test
    void cannotIncreaseCapacity() {
        Bus oldBus = createBus("12345678", 50, "Diesel");
        Bus newBus = createBus("12345678", 60, "Diesel");

        assertFalse(BusValidator.isCapacityUpdateAllowed(oldBus, newBus));
    }

    @Test
    void canDecreaseCapacity() {
        Bus oldBus = createBus("12345678", 50, "Diesel");
        Bus newBus = createBus("12345678", 40, "Diesel");

        assertTrue(BusValidator.isCapacityUpdateAllowed(oldBus, newBus));
    }

    @Test
    void sameCapacity_isAllowed() {
        Bus oldBus = createBus("12345678", 50, "Diesel");
        Bus newBus = createBus("12345678", 50, "Diesel");

        assertTrue(BusValidator.isCapacityUpdateAllowed(oldBus, newBus));
    }

    // -------- B3 --------

    @Test
    void driverTooOld_cannotDriveLargeBus() {
        Driver driver = createDriver(10, "Heavy");
        Bus bus = createBus("12345678", 55, "Diesel");

        assertFalse(BusValidator.isAgeEligible(driver, bus));
    }

    @Test
    void youngDriver_canDriveLargeBus() {
        Driver driver = createDriver(10, "Heavy");
        Bus bus = createBus("12345678", 55, "Diesel");

        assertTrue(BusValidator.isAgeEligible(driver, bus));
    }

    @Test
    void oldDriver_canDriveSmallBus() {
        Driver driver = createDriver(10, "Heavy");
        Bus bus = createBus("12345678", 49, "Diesel");

        assertTrue(BusValidator.isAgeEligible(driver, bus));
    }

    // -------- B4 --------

    @Test
    void lowExperience_cannotDriveElectric() {
        Driver driver = createDriver(3, "Heavy");
        Bus bus = createBus("12345678", 30, "Electricity");

        assertFalse(BusValidator.isExperienceEligibleForElectric(driver, bus));
    }

    @Test
    void enoughExperience_canDriveElectric() {
        Driver driver = createDriver(8, "Heavy");
        Bus bus = createBus("12345678", 30, "Electricity");

        assertTrue(BusValidator.isExperienceEligibleForElectric(driver, bus));
    }

    @Test
    void exactlyFiveYears_isAllowedForElectric() {
        Driver driver = createDriver(5, "Heavy");
        Bus bus = createBus("12345678", 30, "Electricity");

        assertTrue(BusValidator.isExperienceEligibleForElectric(driver, bus));
    }

    // -------- B5 --------

    @Test
    void lightLicence_cannotDriveElectric() {
        Driver driver = createDriver(10, "Light");
        Bus bus = createBus("12345678", 30, "Electricity");

        assertFalse(BusValidator.isLicenseEligibleForFuelType(driver, bus));
    }

    @Test
    void heavyLicence_canDriveElectric() {
        Driver driver = createDriver(10, "Heavy");
        Bus bus = createBus("12345678", 30, "Electricity");

        assertTrue(BusValidator.isLicenseEligibleForFuelType(driver, bus));
    }

    @Test
    void publicTransport_canDriveHybrid() {
        Driver driver = createDriver(10, "PublicTransport");
        Bus bus = createBus("12345678", 30, "Hybrid");

        assertTrue(BusValidator.isLicenseEligibleForFuelType(driver, bus));
    }
}