package test.java;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import main.java.Driver;
import main.java.DriverRepository;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DriverIntegrationTest {

    @TempDir
    Path tempDir;

    private Driver validDriver() {
        return new Driver(
                "56xy!#abAB",
                "Alice",
                4,
                "Medium",
                "12|Main Street|Melbourne|VIC|Australia",
                "15-06-1990"
        );
    }

    @Test
    @DisplayName("TC-DI-01: valid driver stored and reloaded from file")
    void tcDI01_validDriverStoredCorrectly() {

        Path file = tempDir.resolve("drivers.txt");

        DriverRepository repo = new DriverRepository(file.toString());

        assertTrue(repo.add(validDriver()));

        // Reload repository from same file
        DriverRepository reloaded =
                new DriverRepository(file.toString());

        Driver fromFile = reloaded.retrieve("56xy!#abAB");

        assertNotNull(fromFile);
        assertEquals("Alice", fromFile.getName());
        assertEquals(1, reloaded.count());
    }

    @Test
    @DisplayName("TC-DI-02: invalid driver rejected")
    void tcDI02_invalidDriverRejected() {

        Path file = tempDir.resolve("drivers.txt");

        DriverRepository repo = new DriverRepository(file.toString());

        assertThrows(IllegalArgumentException.class, () -> {
            new Driver(
                    "12345", // invalid ID
                    "Bad",
                    4,
                    "Medium",
                    "12|Main Street|Melbourne|VIC|Australia",
                    "15-06-1990"
            );
        });

        assertEquals(0, repo.count());
    }

    @Test
    @DisplayName("TC-DI-03: update persisted to file")
    void tcDI03_updatePersisted() {

        Path file = tempDir.resolve("drivers.txt");

        DriverRepository repo = new DriverRepository(file.toString());

        repo.add(validDriver());

        Driver updated = new Driver(
                "56xy!#abAB",
                "Alice", // name unchanged (required by D5)
                4,
                "Medium",
                "99|New Road|Geelong|VIC|Australia",
                "15-06-1990"
        );

        assertTrue(repo.update("56xy!#abAB", updated));

        DriverRepository reloaded =
                new DriverRepository(file.toString());

        Driver driver =
                reloaded.retrieve("56xy!#abAB");

        assertNotNull(driver);
        assertTrue(driver.getAddress().contains("Geelong"));
    }

    @Test
    @DisplayName("TC-DI-04: count updated and persisted")
    void tcDI04_countUpdatedCorrectly() {

        Path file = tempDir.resolve("drivers.txt");

        DriverRepository repo = new DriverRepository(file.toString());

        repo.add(validDriver());

        repo.add(new Driver(
                "78ab@#cdEF",
                "Bob",
                5,
                "Heavy",
                "5|Park Avenue|Sydney|NSW|Australia",
                "20-01-1992"
        ));

        assertEquals(2, repo.count());

        DriverRepository reloaded =
                new DriverRepository(file.toString());

        assertEquals(2, reloaded.count());
    }
}