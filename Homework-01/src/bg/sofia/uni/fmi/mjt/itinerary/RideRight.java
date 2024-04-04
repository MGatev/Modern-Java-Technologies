package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.pathfind.CheapestPath;
import bg.sofia.uni.fmi.mjt.itinerary.pathfind.Node;
import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

public class RideRight implements ItineraryPlanner {

    List<Journey> schedule;
    public RideRight(List<Journey> schedule) {
        this.schedule = schedule;
    }

    private boolean isCityInSchedule(City cityToCheck) {
        for (Journey journey : schedule) {
            if (journey.from().equals(cityToCheck) || journey.to().equals(cityToCheck)) {
                return true;
            }
        }
        return false;
    }

    private List<Journey> journeyWithoutTransfer(City start, City destination) {
        BigDecimal minPrice = new BigDecimal(Double.MAX_VALUE);

        for (Journey journey : schedule) {
            if (journey.from().equals(start) && journey.to().equals(destination) &&
                journey.getPriceWithTax().compareTo(minPrice) < 0) {
                minPrice = journey.getPriceWithTax();
            }
        }

        for (Journey journey : schedule) {
            if (journey.from().equals(start) && journey.to().equals(destination) &&
                journey.getPriceWithTax().equals(minPrice)) {
                List<Journey> listToReturn = new ArrayList<>();
                listToReturn.add(journey);
                return listToReturn;
            }
        }
        return new ArrayList<>();
    }

    private SequencedCollection<Journey> citiesToJourneys(SequencedCollection<City> cities) {
        SequencedCollection<Journey> journeys = new ArrayList<>();

        while (cities.size() >= 2) {
            City from = cities.getFirst();
            cities.removeFirst();

            City to = cities.getFirst();

            BigDecimal tempPrice = new BigDecimal(Double.MAX_VALUE);
            for (Journey journey : schedule) {
                if (journey.from().equals(from) && journey.to().equals(to) &&
                    journey.getPriceWithTax().compareTo(tempPrice) < 0) {
                    tempPrice = journey.getPriceWithTax();
                }
            }

            for (Journey journey : schedule) {
                if (journey.from().equals(from) && journey.to().equals(to) &&
                    journey.getPriceWithTax().equals(tempPrice)) {
                    journeys.add(journey);
                }
            }
        }
        return journeys;
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {
        if (!isCityInSchedule(start)) {
            throw new CityNotKnownException("Cannot find the start city in the schedule!");
        }
        if (!isCityInSchedule(destination)) {
            throw new CityNotKnownException("Cannot find the destination city in the schedule!");
        }

        if (!allowTransfer) {
            SequencedCollection<Journey> colToReturn = journeyWithoutTransfer(start, destination);
            if (colToReturn.isEmpty()) {
                throw new NoPathToDestinationException();
            }

            return colToReturn;
        } else {
            Node endNode = new Node(destination, new BigDecimal(0));
            Node startNode = new Node(start);

            SequencedCollection<City> cities = CheapestPath.reconstructPath(
                CheapestPath.findPathBetween(startNode, endNode, schedule));

            if (cities.isEmpty()) {
                throw new NoPathToDestinationException("There is not a trip from " + start.name() + " to " +
                    destination.name());
            }

            return citiesToJourneys(cities);
        }
    }
}
