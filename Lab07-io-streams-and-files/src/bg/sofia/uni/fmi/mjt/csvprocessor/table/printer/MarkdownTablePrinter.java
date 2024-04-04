package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class MarkdownTablePrinter implements TablePrinter {
    private static final int DEFAULT_MAX_SIZE = 3;
    private int getColumnMaxLength(Collection<String> colValues) {
        int maxLength = DEFAULT_MAX_SIZE;
        for (String s : colValues) {
            maxLength = Math.max(maxLength, s.length());
        }

        return maxLength;
    }

    private void printHeader(Table table, Collection<String> rows) {
        Collection<String> columnNames = table.getColumnNames();
        StringBuilder row = new StringBuilder("|");

        for (String colName : columnNames) {
            Collection<String> colWithName = new ArrayList<>(table.getColumnData(colName));
            colWithName.add(colName);

            row.append(" ");
            int maxColLength = getColumnMaxLength(colWithName);
            int nameLength = colName.length();
            row.append(colName);
            row.append(" ".repeat(Math.max(0, maxColLength - nameLength) + 1));
            row.append("|");
        }
        rows.add(row.toString());
    }

    private void printAllignments(Table table, Collection<String> rows, ColumnAlignment... alignments) {
        int alIndex = 0;
        Collection<String> columnNames = table.getColumnNames();
        StringBuilder row = new StringBuilder("|");

        for (String colName : columnNames) {
            Collection<String> colWithName = new ArrayList<>(table.getColumnData(colName));
            colWithName.add(colName);

            row.append(" ");
            int maxColLength = getColumnMaxLength(colWithName);

            ColumnAlignment currAlignment = ColumnAlignment.NOALIGNMENT;
            if (alIndex < alignments.length) {
                currAlignment = alignments[alIndex++];
            }
            switch(currAlignment) {
                case CENTER -> row.append(":").append("-".repeat(Math.max(0, maxColLength) - 2)).append(": ");
                case LEFT -> row.append(":").append("-".repeat(Math.max(0, maxColLength) - 1)).append(" ");
                case RIGHT -> row.append("-".repeat(Math.max(0, maxColLength) - 1)).append(": ");
                case NOALIGNMENT -> row.append("-".repeat(Math.max(0, maxColLength))).append(" ");
            }
            row.append("|");
        }
        rows.add(row.toString());
    }

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        Collection<String> rows = new LinkedHashSet<>();
        Collection<String> columnNames = table.getColumnNames();

        printHeader(table, rows);
        printAllignments(table, rows, alignments);

        for (int i = 1; i < table.getRowsCount(); i++) {
            StringBuilder row = new StringBuilder("|");
            for (String colName : columnNames) {
                int rowIndex = 0;
                for (String data : table.getColumnData(colName)) {
                    if (rowIndex != i - 1) {
                        rowIndex++;
                        continue;
                    }
                    Collection<String> colWithName = new ArrayList<>(table.getColumnData(colName));
                    colWithName.add(colName);

                    int maxColLength = getColumnMaxLength(colWithName);
                    int wordSize = data.length();
                    row.append(" ").append(data).append(" ".repeat(Math.max(0, maxColLength - wordSize) + 1));
                    row.append("|");
                    break;
                }
            }
            rows.add(row.toString());
        }
        return rows;
    }
}
