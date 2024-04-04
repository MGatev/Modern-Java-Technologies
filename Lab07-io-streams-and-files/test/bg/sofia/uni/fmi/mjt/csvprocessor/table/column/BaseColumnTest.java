package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseColumnTest {
    Column column = new BaseColumn();
    @Test
    void testAddDataThrowIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> column.addData(null));
    }

    @Test
    void testAddDataSuccessAdd() {
        assertDoesNotThrow(() -> column.addData("abv"));
    }

    @Test
    void testGetDataSuccessGet() {
        column.addData("abv");
        assertDoesNotThrow(() -> column.getData());
    }
}
