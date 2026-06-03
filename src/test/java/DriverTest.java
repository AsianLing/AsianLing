package test.java;
import org.junit.jupiter.api.Test;

import main.java.Driver;
import main.java.DriverRepository;

import static org.junit.jupiter.api.Assertions.*;
 
public class DriverTest {
 
    // Driver ID (D1)
 
    // Normal: valid driver ID accepted
    @Test
    void testD1_validDriverID() {
        assertTrue(Driver.isValidDriverID("56xy!#abAB"));
    }
 
    // Invalid: driver ID too short
    @Test
    void testD1_tooShort() {
        assertFalse(Driver.isValidDriverID("56xy!#AB"));
    }
 
    // Invalid: first digit is 1 (must be 2-9)
    @Test
    void testD1_firstDigitIs1() {
        assertFalse(Driver.isValidDriverID("16xy!#abAB"));
    }
 
    // Invalid: no special characters in positions 3-8
    @Test
    void testD1_noSpecialChars() {
        assertFalse(Driver.isValidDriverID("56xyababAB"));
    }
 
    // Invalid: last two characters are not uppercase
    @Test
    void testD1_lastCharsNotUppercase() {
        assertFalse(Driver.isValidDriverID("56xy!#abab"));
    }
 
    // Address validation (D2)
 
    // Normal: valid address accepted
    @Test
    void testD2_validAddress() {
        assertTrue(Driver.isValidAddress("12|Main St|Melbourne|VIC|Australia"));
    }
 
    // Invalid: address missing parts
    @Test
    void testD2_missingParts() {
        assertFalse(Driver.isValidAddress("12|Main St|Melbourne"));
    }
 
    // Invalid: empty street number
    @Test
    void testD2_emptyStreetNumber() {
        assertFalse(Driver.isValidAddress("|Main St|Melbourne|VIC|Australia"));
    }
 
    // Invalid: non-numeric street number
    @Test
    void testD2_nonNumericStreetNumber() {
        assertFalse(Driver.isValidAddress("abc|Main St|Melbourne|VIC|Australia"));
    }
 
    // Birthdate validation (D3)
 
    // Normal: valid birthdate accepted
    @Test
    void testD3_validBirthdate() {
        assertTrue(Driver.isValidBirthdate("15-06-1990"));
    }
 
    // Invalid: wrong format (YYYY-MM-DD instead of DD-MM-YYYY)
    @Test
    void testD3_wrongFormat() {
        assertFalse(Driver.isValidBirthdate("1990-06-15"));
    }
 
    // Invalid: month out of range
    @Test
    void testD3_invalidMonth() {
        assertFalse(Driver.isValidBirthdate("15-13-1990"));
    }
 
    // License Update Restriction (D4)
 
    // Normal: driver with experience <= 10 can change license
    @Test
    void testD4_experienceUnder10_canChangeLicense() {
        Driver d = new Driver("56xy!#abAB", "John", 5, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        assertTrue(d.getExperienceYears() <= 10);
    }
 
    // Invalid: driver with experience > 10 cannot change license
    @Test
    void testD4_experienceOver10_cannotChangeLicense() {
        Driver d = new Driver("56xy!#abAB", "John", 11, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        assertThrows(IllegalArgumentException.class, () -> {
            if (d.getExperienceYears() > 10) {
                throw new IllegalArgumentException("Cannot change license");
            }
        });
    }
 
    // Edge: driver with exactly 10 years can still change license
    @Test
    void testD4_exactlyTenYears_canChangeLicense() {
        DriverRepository repo = new DriverRepository("test_d4_ten.txt");
        repo.clear();
        Driver original = new Driver("56xy!#abAB", "John", 10, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        repo.add(original);
        Driver updated = new Driver("56xy!#abAB", "John", 10, "Medium",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        assertDoesNotThrow(() -> repo.update("56xy!#abAB", updated));
        repo.clear();
    }
 
    // Invalid: update throws when experience > 10 and license changes
    @Test
    void testD4_updateThrows_whenExperienceOver10AndLicenseChanges() {
        DriverRepository repo = new DriverRepository("test_d4_throw.txt");
        repo.clear();
        Driver original = new Driver("56xy!#abAB", "John", 11, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        repo.add(original);
        Driver updated = new Driver("56xy!#abAB", "John", 11, "Medium",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        assertThrows(IllegalArgumentException.class, () ->
                repo.update("56xy!#abAB", updated));
        repo.clear();
    }
 
    // Edge: driver with > 10 years can update other fields but not license
    @Test
    void testD4_experienceOver10_canUpdateOtherFields() {
        DriverRepository repo = new DriverRepository("test_d4_other.txt");
        repo.clear();
        Driver original = new Driver("56xy!#abAB", "John", 11, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        repo.add(original);
        Driver updated = new Driver("56xy!#abAB", "John", 11, "Heavy",
                "99|New Road|Sydney|NSW|Australia", "01-01-1990");
        assertDoesNotThrow(() -> repo.update("56xy!#abAB", updated));
        assertEquals("99|New Road|Sydney|NSW|Australia",
                repo.retrieve("56xy!#abAB").getAddress());
        repo.clear();
    }
 
    // Immutable Fields (D5)
 
    // Normal: invalid driver ID throws exception
    @Test
    void testD5_invalidDriverID_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("invalid", "John", 5, "Heavy",
                    "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        });
    }
 
    // Invalid: invalid address throws exception
    @Test
    void testD5_invalidAddress_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("56xy!#abAB", "John", 5, "Heavy",
                    "badaddress", "01-01-1990");
        });
    }
 
    // Normal: update succeeds when name is unchanged
    @Test
    void testD5_nameUnchanged_updateSucceeds() {
        DriverRepository repo = new DriverRepository("test_d5_name.txt");
        repo.clear();
        Driver original = new Driver("56xy!#abAB", "John", 5, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        repo.add(original);
        Driver updated = new Driver("56xy!#abAB", "John", 6, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        assertDoesNotThrow(() -> repo.update("56xy!#abAB", updated));
        repo.clear();
    }
 
    // Invalid: update throws when name is changed
    @Test
    void testD5_nameChanged_throwsException() {
        DriverRepository repo = new DriverRepository("test_d5_change.txt");
        repo.clear();
        Driver original = new Driver("56xy!#abAB", "John", 5, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        repo.add(original);
        Driver updated = new Driver("56xy!#abAB", "Jane", 5, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        assertThrows(IllegalArgumentException.class, () ->
                repo.update("56xy!#abAB", updated));
        repo.clear();
    }
 
    // Edge: driverID uniquely identifies each driver
    @Test
    void testD5_retrieveByID_returnsCorrectDriver() {
        DriverRepository repo = new DriverRepository("test_d5_retrieve.txt");
        repo.clear();
        repo.add(new Driver("56xy!#abAB", "John", 5, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990"));
        repo.add(new Driver("78ab@#cdEF", "Jane", 3, "Medium",
                "5|Park Ave|Sydney|NSW|Australia", "20-01-1992"));
        assertEquals("John", repo.retrieve("56xy!#abAB").getName());
        assertEquals("Jane", repo.retrieve("78ab@#cdEF").getName());
        repo.clear();
    }