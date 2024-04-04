package bg.sofia.uni.fmi.mjt.cooking.api.util;

public interface Iterator<T> {

    boolean hasNext();

    T next();

    void increment(T newValue);
}
