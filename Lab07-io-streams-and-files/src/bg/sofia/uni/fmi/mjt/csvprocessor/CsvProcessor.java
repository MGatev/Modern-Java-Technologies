package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.TablePrinter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CsvProcessor implements CsvProcessorAPI {

    Table table;

    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    private void checkIfSameColumnNames(String[] rowValues) throws CsvDataNotCorrectException {
        Set<String> temp = new HashSet<>();
        for (String value : rowValues) {
            if (!temp.add(value)) {
                throw new CsvDataNotCorrectException();
            }
        }
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        try (var input = new BufferedReader(reader)) {
            String line;
            int rowNumber = 0;
            int columnsAmount = 0;
            while ((line = input.readLine()) != null) {
                if (rowNumber++ == 0) {
                    String[] rowValues = line.split("\\Q" + delimiter + "\\E");
                    checkIfSameColumnNames(rowValues);

                    table.addData(rowValues);
                    columnsAmount = rowValues.length;
                    continue;
                }

                String[] rowValues = line.split("\\Q" + delimiter + "\\E");
                if (columnsAmount != rowValues.length) {
                    throw new CsvDataNotCorrectException();
                }

                table.addData(rowValues);
            }
        } catch (IOException e) {
            System.out.println("?");
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        TablePrinter printer = new MarkdownTablePrinter();
        Collection<String> rows = printer.printTable(table, alignments);

        try (var output = new BufferedWriter(writer)) {
            int rowNumber = 0;
            for (String row : rows) {
                if (rowNumber == table.getRowsCount()) {
                    row = row.replace("\n", "").replace("\r", "");
                    output.write(row);
                    output.flush();
                } else {
                    output.write(row);
                    output.write(System.lineSeparator());
                    output.flush();
                }

                rowNumber++;
            }
        } catch (IOException e) {
            System.out.println("abc");
        }
    }
}
