package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

public class BaseTable implements Table {
    Collection<Column> columns = new LinkedHashSet<>();
    int rowsCount = 0;
    int columnsCount = 0;

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null) {
            throw new IllegalArgumentException("There cannot be an empty row!");
        }

        if (columnsCount == 0) {
            for (String s : data) {
                Column header = new BaseColumn();
                header.addData(s);
                columns.add(header);
                columnsCount++;
            }
        } else {
            if (data.length != columnsCount) {
                throw new CsvDataNotCorrectException();
            }

            int index = 0;
            for (Column c : columns) {
                c.addData(data[index++]);
            }
        }
        rowsCount++;
    }

    @Override
    public Collection<String> getColumnNames() {
        Collection<String> columnNames = new LinkedHashSet<>();
        for (Column c : columns) {
            for (String s : c.getData()) {
                columnNames.add(s);
                break;
            }
        }

        return Collections.unmodifiableCollection(columnNames);
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if (column == null || column.isBlank()) {
            throw new IllegalArgumentException("Invalid column!");
        }

        for (Column c : columns) {
            String columnName = "";
            for (String s : c.getData()) {
                columnName = s;
                break;
            }
            if (column.equals(columnName)) {
                Collection<String> columnData = new ArrayList<>(c.getData());
                columnData.remove(columnName);
                return Collections.unmodifiableCollection(columnData);
            }
        }

        throw new IllegalArgumentException("There is no such column!");
    }

    @Override
    public int getRowsCount() {
        return rowsCount;
    }
}
