package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {

    Map<String, E> buildables = new HashMap<>();
    int buildableArea;
    public Plot(int buildableArea) {
        this.buildableArea = buildableArea;
    }

    @Override
    public void construct(String address, E buildable) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Invalid address!");
        }

        if (buildable == null) {
            throw new IllegalArgumentException("Invalid buildable!");
        }

        if (buildables.containsKey(address)) {
            throw new BuildableAlreadyExistsException("This buildable already exists!");
        }

        if (buildable.getArea() > buildableArea) {
            throw new InsufficientPlotAreaException("Too large!");
        }

        buildableArea -= buildable.getArea();

        buildables.put(address, buildable);
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("Invalid parameters!");
        }

        for (String s : buildables.keySet()) {
            if (this.buildables.containsKey(s)) {
                throw new BuildableAlreadyExistsException();
            }
        }

        int area = 0;
        for (String s : buildables.keySet()) {
            area += buildables.get(s).getArea();
            if (area > buildableArea) {
                throw new InsufficientPlotAreaException();
            }
        }

        for (String s : buildables.keySet()) {
            construct(s, buildables.get(s));
        }
    }

    @Override
    public void demolish(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Invalid parameters!");
        }

        if (!buildables.containsKey(address)) {
            throw new BuildableNotFoundException();
        }

        buildableArea += buildables.get(address).getArea();

        E object = buildables.get(address);

        if (object != null) {
            buildables.remove(address);
        }
    }

    @Override
    public void demolishAll() {
        int area = 0;
        for (String s : buildables.keySet()) {
            area += buildables.get(s).getArea();
        }

        buildableArea += area;
        buildables.clear();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        Map<String, E> mapToReturn = Map.copyOf(buildables);
        return mapToReturn;
    }

    @Override
    public int getRemainingBuildableArea() {
        return buildableArea;
    }
}
