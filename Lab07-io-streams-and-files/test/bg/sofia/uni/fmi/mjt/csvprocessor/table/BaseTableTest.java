package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseTableTest {
    Table table = new BaseTable();

    @Test
    void testAddDataThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> table.addData(null));
    }

    @Test
    void testAddDataThrowsCsvDataNotCorrect() {
        String[] data = {"ime", "familiq", "godini"};
        String[] invalidData = {"Marti", "Veni"};
        assertDoesNotThrow(() -> table.addData(data));
        assertThrows(CsvDataNotCorrectException.class, () -> table.addData(invalidData));
    }

    @Test
    void testAddDataSuccessAdd() {
        String[] data = {"ime", "familiq", "godini"};
        String[] validData = {"Marti", "Gatev","20"};
        assertDoesNotThrow(() -> table.addData(data));
        assertDoesNotThrow(() -> table.addData(validData));
    }

    @Test
    void testGetColumnNamesSuccess() {
        String[] names = {"ime", "familiq", "godini"};
        String[] validData = {"Marti", "Gatev","20"};
        assertDoesNotThrow(() -> table.addData(names));
        assertDoesNotThrow(() -> table.addData(validData));
        Collection<String> expectedNames = new LinkedHashSet<>(List.of(names));
        assertIterableEquals(expectedNames, table.getColumnNames());
    }

    @Test
    void testGetColumnDataThrowsIllegalArgumentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> table.getColumnData(null));
    }

    @Test
    void testGetColumnDataThrowsIllegalArgumentWithInvalidName() throws CsvDataNotCorrectException {
        String[] names = {"ime", "familiq", "godini"};
        table.addData(names);
        assertThrows(IllegalArgumentException.class, () -> table.getColumnData("aaa"));
    }

    @Test
    void testGetColumnDataSuccess() {
        String[] names = {"ime", "familiq", "godini"};
        String[] validData = {"Marti", "Gatev","20"};
        String[] validData1 = {"Marti", "Gatev2","20"};
        String[] values = {"Gatev", "Gatev2"};
        assertDoesNotThrow(() -> table.addData(names));
        assertDoesNotThrow(() -> table.addData(validData));
        assertDoesNotThrow(() -> table.addData(validData1));
        Collection<String> expectedValues = new LinkedHashSet<>(List.of(values));
        assertIterableEquals(expectedValues, table.getColumnData("familiq"));
    }

    @Test
    void testGetRowsCountSuccess() {
        assertDoesNotThrow(() -> table.getRowsCount());
    }
}
