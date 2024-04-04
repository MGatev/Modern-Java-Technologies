package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class MarkdownTablePrinterTest {
    Table table = new BaseTable();
    TablePrinter printer = new MarkdownTablePrinter();

    @Test
    void testPrintTableWithActualTable() {
        String[] names = {"ime", "familiq", "godini", "grad"};
        String[] validData = {"Marti", "Gatev","20", "Gabrovo"};
        String[] validData1 = {"Veni", "Gateva","21", "Vratsa"};
        String[] validData2 = {"Kiro", "Georgiev", "18", "Blagoevgrad"};

        ColumnAlignment[] alignments = {ColumnAlignment.LEFT, ColumnAlignment.RIGHT,
                                        ColumnAlignment.CENTER};

        assertDoesNotThrow(() -> table.addData(names));
        assertDoesNotThrow(() -> table.addData(validData));
        assertDoesNotThrow(() -> table.addData(validData1));
        assertDoesNotThrow(() -> table.addData(validData2));

        Collection<String> exprectedValues = new ArrayList<>();
        exprectedValues.add("| ime   | familiq  | godini | grad        |");
        exprectedValues.add("| :---- | -------: | :----: | ----------- |");
        exprectedValues.add("| Marti | Gatev    | 20     | Gabrovo     |");
        exprectedValues.add("| Veni  | Gateva   | 21     | Vratsa      |");
        exprectedValues.add("| Kiro  | Georgiev | 18     | Blagoevgrad |");

        assertIterableEquals(exprectedValues, printer.printTable(table, alignments));
    }

}
