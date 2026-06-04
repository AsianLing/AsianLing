import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    // Valid dummy values used across tests
    private static final String VALID_ID        = "12345678";
    private static final String VALID_DRIVER_ID = "56xy!#abAB";
    private static final String VALID_ADDRESS   = "12|Main St|Melbourne|VIC|Australia";
    private static final String VALID_DOB       = "01-01-1990";

    // Helper to make a valid driver quickly
    private Driver makeDriver(int experience, String license) {
        return new Driver(VALID_DRIVER_ID, "John Smith",
                experience, license, VALID_ADDRESS, VALID_DOB);
    }

    // Bus ID Rules

    // Normal: valid 8-digit busID accepted
    @Test
    void testB1_validBusID() {
        assertTrue(Bus.isValidBusID("12345678"));
    }

    // Invalid: busID too short
    @Test
    void testB1_tooShort() {
        assertFalse(Bus.isValidBusID("1234567"));
    }

    // Invalid: busID contains letters
    @Test
    void testB1_containsLetters() {
        assertFalse(Bus.isValidBusID("1234ABCD"));
    }

    // Edge: busID too long (9 digits)
    @Test
    void testB1_tooLong() {
        assertFalse(Bus.isValidBusID("123456789"));
    }

    // Invalid: duplicate busID rejected by repository
    @Test
    void testB1_duplicateBusID() {
        BusRepository repo = new BusRepository("test_b1_dup.txt");
        repo.clear();
        repo.add(new Bus(VALID_ID, 40, 80.0, "Diesel"));
        assertThrows(IllegalArgumentException.class, () ->
                repo.add(new Bus(VALID_ID, 50, 60.0, "Hybrid")));
        repo.clear();
    }

    // Capacity Update Restriction

    // Normal: capacity can decrease
    @Test
    void testB2_capacityCanDecrease() {
        BusRepository repo = new BusRepository("test_b2_dec.txt");
        repo.clear();
        repo.add(new Bus(VALID_ID, 50, 80.0, "Diesel"));
        repo.update(VALID_ID, new Bus(VALID_ID, 40, 80.0, "Diesel"));
        assertEquals(40, repo.retrieve(VALID_ID).getCapacity());
        repo.clear();
    }

    // Invalid: capacity cannot increase
    @Test
    void testB2_capacityCannotIncrease() {
        BusRepository repo = new BusRepository("test_b2_inc.txt");
        repo.clear();
        repo.add(new Bus(VALID_ID, 40, 80.0, "Diesel"));
        assertThrows(IllegalArgumentException.class, () ->
                repo.update(VALID_ID, new Bus(VALID_ID, 60, 80.0, "Diesel")));
        repo.clear();
    }

    // Edge: same capacity during update is allowed
    @Test
    void testB2_sameCapacityAllowed() {
        BusRepository repo = new BusRepository("test_b2_same.txt");
        repo.clear();
        repo.add(new Bus(VALID_ID, 40, 80.0, "Diesel"));
        assertDoesNotThrow(() ->
                repo.update(VALID_ID, new Bus(VALID_ID, 40, 60.0, "Diesel")));
        repo.clear();
    }

    // B3: Driver Age Restriction

    // Normal: driver aged 45 can drive bus with capacity 50
    @Test
    void testB3_youngDriverAllowed() {
        Bus bus = new Bus(VALID_ID, 50, 80.0, "Diesel");
        assertTrue(BusRepository.isDriverAllowedByAge(45, bus));
    }

    // Invalid: driver older than 50 cannot drive bus capacity >= 50
    @Test
    void testB3_oldDriverNotAllowed() {
        Bus bus = new Bus(VALID_ID, 50, 80.0, "Diesel");
        assertFalse(BusRepository.isDriverAllowedByAge(51, bus));
    }

    // Edge: driver aged exactly 50 is still allowed
    @Test
    void testB3_exactlyAge50Allowed() {
        Bus bus = new Bus(VALID_ID, 50, 80.0, "Diesel");
        assertTrue(BusRepository.isDriverAllowedByAge(50, bus));
    }

    // Electric Bus Restriction

    // Normal: driver with 5+ years can drive electric
    @Test
    void testB4_experiencedDriverAllowed() {
        Bus bus = new Bus(VALID_ID, 40, 80.0, "Electricity");
        assertTrue(BusRepository.isDriverAllowedByExperience(makeDriver(5, "Heavy"), bus));
    }

    // Invalid: driver with less than 5 years cannot drive electric
    @Test
    void testB4_inexperiencedDriverNotAllowed() {
        Bus bus = new Bus(VALID_ID, 40, 80.0, "Electricity");
        assertFalse(BusRepository.isDriverAllowedByExperience(makeDriver(3, "Heavy"), bus));
    }

    // Edge: driver with exactly 4 years cannot drive electric
    @Test
    void testB4_fourYearsNotAllowed() {
        Bus bus = new Bus(VALID_ID, 40, 80.0, "Electricity");
        assertFalse(BusRepository.isDriverAllowedByExperience(makeDriver(4, "Heavy"), bus));
    }

    // Normal: experience restriction does not apply to Diesel
    @Test
    void testB4_dieselNoRestriction() {
        Bus bus = new Bus(VALID_ID, 40, 80.0, "Diesel");
        assertTrue(BusRepository.isDriverAllowedByExperience(makeDriver(1, "Heavy"), bus));
    }

    // Driver Licence Restriction

    // Normal: Heavy licence can drive electric
    @Test
    void testB5_heavyLicenceCanDriveElectric() {
        Bus bus = new Bus(VALID_ID, 40, 80.0, "Electricity");
        assertTrue(BusRepository.isDriverAllowedByLicence(makeDriver(5, "Heavy"), bus));
    }

    // Normal: PublicTransport licence can drive hybrid
    @Test
    void testB5_publicTransportCanDriveHybrid() {
        Bus bus = new Bus(VALID_ID, 40, 80.0, "Hybrid");
        assertTrue(BusRepository.isDriverAllowedByLicence(makeDriver(5, "PublicTransport"), bus));
    }

    // Invalid: Light licence cannot drive electric
    @Test
    void testB5_lightLicenceCannotDriveElectric() {
        Bus bus = new Bus(VALID_ID, 40, 80.0, "Electricity");
        assertFalse(BusRepository.isDriverAllowedByLicence(makeDriver(5, "Light"), bus));
    }

    // Invalid: Medium licence cannot drive hybrid
    @Test
    void testB5_mediumLicenceCannotDriveHybrid() {
        Bus bus = new Bus(VALID_ID, 40, 80.0, "Hybrid");
        assertFalse(BusRepository.isDriverAllowedByLicence(makeDriver(5, "Medium"), bus));
    }

    // Edge: Light licence can drive Diesel
    @Test
    void testB5_lightLicenceCanDriveDiesel() {
        Bus bus = new Bus(VALID_ID, 40, 80.0, "Diesel");
        assertTrue(BusRepository.isDriverAllowedByLicence(makeDriver(5, "Light"), bus));
    }
}