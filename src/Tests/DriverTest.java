
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class DriverTest {
    
// TESTING DRIVER ID RULES

@Test
void validDriverID() {
    assertTrue(Driver.isValidDriverID("78@#xxxxYZ"));
}
    
}
