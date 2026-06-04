import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class BusIntegrationTest {

    @TempDir
    Path tempDir;

    private Bus sampleBus() {
        return new Bus("12345678", 45, 80.0, "Diesel");
    }

    @Test
    @DisplayName("valid bus should be saved and loaded back from file")
    void saveAndReloadBus() {
        Path file = tempDir.resolve("buses.txt");
        BusRepository repo = new BusRepository(file.toString());

        repo.add(sampleBus());

        BusRepository reload = new BusRepository(file.toString());
        Bus bus = reload.retrieve("12345678");

        assertNotNull(bus);
        assertEquals(45, bus.getCapacity());
    }

    @Test
    @DisplayName("invalid bus should not be saved")
    void invalidBusShouldFail() {
        Path file = tempDir.resolve("buses.txt");
        BusRepository repo = new BusRepository(file.toString());

        
        assertThrows(IllegalArgumentException.class, () ->
        repo.add(new Bus("12AB5678", 45, 80.0, "Diesel"))
    );
        assertEquals(0, repo.count());
    }

    @Test
    @DisplayName("update should be saved in file")
    void updateIsPersisted() {
        Path file = tempDir.resolve("buses.txt");
        BusRepository repo = new BusRepository(file.toString());

        repo.add(sampleBus());

        Bus updated = new Bus("12345678", 40, 70.0, "Diesel");
        assertTrue(repo.update("12345678",updated));

        BusRepository reload = new BusRepository(file.toString());
        assertEquals(40, reload.retrieve("12345678").getCapacity());
    }

    @Test
    @DisplayName("count should stay correct after reload")
    void countPersists() {
        Path file = tempDir.resolve("buses.txt");
        BusRepository repo = new BusRepository(file.toString());

        repo.add(sampleBus());
        repo.add(new Bus("87654321", 60, 100.0, "Electricity"));

        assertEquals(2, repo.count());

        BusRepository reload = new BusRepository(file.toString());
        assertEquals(2, reload.count());
    }
}