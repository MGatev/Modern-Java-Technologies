package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Member implements GymMember, Comparable<Member> {
    private String personalIdNumber;
    private String name;
    private Address address;
    private int age;
    private Gender gender;
    private Map<DayOfWeek, Workout> trainingSplit = new HashMap<>();

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return trainingSplit;
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null || workout == null) {
            throw new IllegalArgumentException("Illegal arrgument!");
        }

        trainingSplit.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Illegal argument!");
        }

        List<DayOfWeek> days = new ArrayList<>();

        for (var day : DayOfWeek.values()) {
            var workoutOnDay = trainingSplit.get(day);
            if (workoutOnDay != null) {
                if (workoutOnDay.exercises().getLast().name().equals(exerciseName)) {
                    days.add(day);
                }
            }

        }

        return days;
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) {
        if (day == null || exercise == null) {
            throw new IllegalArgumentException("Illegal argument!");
        }

        if (trainingSplit.get(day) == null) {
            throw new DayOffException();
        }

        trainingSplit.get(day).exercises().addLast(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        if (day == null || exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Illegal argument!");
        }

        if (trainingSplit.get(day) == null) {
            throw new DayOffException();
        }

        for (Exercise e : exercises) {
            addExercise(day, e);
        }
    }

    @Override
    public int compareTo(Member other) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.name, other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(personalIdNumber, member.personalIdNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalIdNumber);
    }
}
