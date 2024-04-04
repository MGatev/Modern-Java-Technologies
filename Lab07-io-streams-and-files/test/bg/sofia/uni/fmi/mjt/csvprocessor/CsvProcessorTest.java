package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.TablePrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvProcessorTest {
    @Mock
    private Table table;

    @InjectMocks
    CsvProcessorAPI processor = new CsvProcessor();

    @Test
    void testReadCsvThrowsCsvDataNotCorrectWhenDuplicates() {
        Reader reader = new StringReader("abv,abc,tr,abv");
        assertThrows(CsvDataNotCorrectException.class, () -> processor.readCsv(reader, ","));
    }

    @Test
    void testReadCsvThrowsCsvDataNotCorrectWhenDiffSizes() {
        Reader reader = new StringReader("ime,familiq,godini,grad\nMarti,Gatev" +
            ",Gabrovo\nVeni,Andreeva,21,Vratsa");
        assertThrows(CsvDataNotCorrectException.class, () -> processor.readCsv(reader, ","));
    }

    @Test
    void testReadCsvWithValidFile() {
        Reader reader = new StringReader("ime,familiq,godini,grad\nMarti,Gatev,20" +
            ",Gabrovo\nVeni,Andreeva,21,Vratsa");
        String[] data = {"Marti", "Veni"};
        when(table.getColumnData("ime")).thenReturn(new ArrayList<>(List.of(data)));
        assertDoesNotThrow(() -> processor.readCsv(reader, ","));
        assertIterableEquals(table.getColumnData("ime"), List.of(data));
    }

    @Test
    void testWriteTableWithValidTable() {
        TablePrinter printer = new MarkdownTablePrinter();
        when(table.getColumnNames()).thenReturn(List.of("ime", "familiq", "godini"));
        when(table.getColumnData("ime")).thenReturn(List.of("Marti", "Kiro"));
        when(table.getColumnData("familiq")).thenReturn(List.of("Gatev", "Georgiev"));
        when(table.getColumnData("godini")).thenReturn(List.of("20", "21"));
        when(table.getRowsCount()).thenReturn(3);

        ColumnAlignment[] alignments = {ColumnAlignment.LEFT, ColumnAlignment.RIGHT};

        Writer writer = new StringWriter();
        assertDoesNotThrow(() -> processor.writeTable(writer, alignments));

        String expected = "| ime   | familiq  | godini |\r\n| :---- | -------: | ------ |\r\n| Marti | Gatev    | 20     |\r\n| Kiro  | Georgiev | 21     |";
        assertEquals(expected ,writer.toString());



    }
}
