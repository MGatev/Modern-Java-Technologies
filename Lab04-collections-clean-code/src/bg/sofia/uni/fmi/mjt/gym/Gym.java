package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Comparator;
import java.util.TreeSet;

public class Gym implements GymAPI {
    private int capacity;
    private Address address;

    SortedSet<GymMember> members = new TreeSet<>();
    public Gym(int capacity, Address address) {
        this.capacity = capacity;
        this.address = address;
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        return Collections.unmodifiableSortedSet(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        return Collections.unmodifiableSortedSet(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        SortedSet<GymMember> temp = new TreeSet<>(new MemberByProximity());
        temp.addAll(members);

        return temp;
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Member is null!");
        }

        if (members.size() >= capacity) {
            throw new GymCapacityExceededException();
        }

        members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Members is null!");
        }

        if (members.size() + this.members.size() >= capacity) {
            throw new GymCapacityExceededException();
        }

        this.members.addAll(members);
    }

    @Override
    public boolean isMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException("Member is null!");
        }

        return members.contains(member);
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if (exerciseName == null || exerciseName.isEmpty() || day == null) {
            throw new IllegalArgumentException("Ïllegal argument!");
        }

        for (var member : members) {
            var workoutOnDay = member.getTrainingProgram().get(day);
            if (workoutOnDay != null) {
                for (var e : workoutOnDay.exercises()) {
                    if (e.name().equals(exerciseName)) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Empty name or invalid name");
        }

        Map<DayOfWeek, List<String>> temp = new EnumMap<>(DayOfWeek.class);

        for (var day : DayOfWeek.values()) {
            for (var member : members) {
                var workoutOnDay = member.getTrainingProgram().get(day);
                if (workoutOnDay != null) {
                    for (var e : workoutOnDay.exercises()) {
                        if (e.name().equals(exerciseName)) {
                            temp.putIfAbsent(day, new ArrayList<>());
                            temp.get(day).add(member.getName());
                        }
                    }
                }
            }
        }

        return temp;
    }

    public class MemberByProximity implements Comparator<GymMember> {
        @Override
        public int compare(GymMember first, GymMember second) {
            return Double.compare(address.getDistanceTo(first.getAddress()),
                                  address.getDistanceTo(second.getAddress()));
        }

    }
}
