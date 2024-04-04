package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BaseColumn implements Column {
    private Collection<String> values;

    public BaseColumn() {
        this(new LinkedHashSet<>());
    }

    public BaseColumn(Set<String> values) {
        this.values = values;
    }

    @Override
    public void addData(String data) {
        if (data == null || data.isBlank()) {
            throw new IllegalArgumentException("Invalid data!");
        }

        values.add(data);
    }

    @Override
    public Collection<String> getData() {
        return List.copyOf(values);
    }
}
