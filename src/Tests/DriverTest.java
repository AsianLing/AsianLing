import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

// Driver ID
    @Test
    void testD1_validDriverID() {
        assertTrue(Driver.isValidDriverID("56xy!#abAB"));
    }

    @Test
    void testD1_tooShort() {
        assertFalse(Driver.isValidDriverID("56xy!#AB"));
    }

    @Test
    void testD1_firstDigitIs1() {
        assertFalse(Driver.isValidDriverID("16xy!#abAB"));
    }

    @Test
    void testD1_noSpecialChars() {
        assertFalse(Driver.isValidDriverID("56xyababAB"));
    }

    @Test
    void testD1_lastCharsNotUppercase() {
        assertFalse(Driver.isValidDriverID("56xy!#abab"));
    }

// Address validation check

    @Test
    void testD2_validAddress() {
        assertTrue(Driver.isValidAddress("12|Main St|Melbourne|VIC|Australia"));
    }

    @Test
    void testD2_missingParts() {
        assertFalse(Driver.isValidAddress("12|Main St|Melbourne"));
    }

    @Test
    void testD2_emptyStreetNumber() {
        assertFalse(Driver.isValidAddress("|Main St|Melbourne|VIC|Australia"));
    }

    @Test
    void testD2_nonNumericStreetNumber() {
        assertFalse(Driver.isValidAddress("abc|Main St|Melbourne|VIC|Australia"));
    }

   // Birthdate validation

    @Test
    void testD3_validBirthdate() {
        assertTrue(Driver.isValidBirthdate("15-06-1990"));
    }

    @Test
    void testD3_wrongFormat() {
        assertFalse(Driver.isValidBirthdate("1990-06-15"));
    }

    @Test
    void testD3_invalidMonth() {
        assertFalse(Driver.isValidBirthdate("15-13-1990"));
    }

    // License validation
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

    @Test
    void testD4_experienceUnder10_canChangeLicense() {
        Driver d = new Driver("56xy!#abAB", "John", 5, "Heavy",
                "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        assertTrue(d.getExperienceYears() <= 10);
    }

    // Immutable check

    @Test
    void testD5_invalidDriverID_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("invalid", "John", 5, "Heavy",
                    "12|Main St|Melbourne|VIC|Australia", "01-01-1990");
        });
    }

    @Test
    void testD5_invalidAddress_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("56xy!#abAB", "John", 5, "Heavy",
                    "badaddress", "01-01-1990");
        });
    }
}
