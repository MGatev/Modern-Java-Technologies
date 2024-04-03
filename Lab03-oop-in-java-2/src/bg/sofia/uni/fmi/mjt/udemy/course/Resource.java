package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

public class Resource implements Completable {

    private String name;
    private ResourceDuration duration;

    private int completionPercentage;
    public Resource(String name, ResourceDuration duration){
        this.name = name;
        this.duration = duration;
        completionPercentage = 0;
    }

    public String getName() {
        return name;
    }

    public ResourceDuration getDuration() {
        return duration;
    }

    public void complete() {
        completionPercentage = 100;
    }


    @Override
    public boolean isCompleted() {
        return completionPercentage == 100;
    }

    @Override
    public int getCompletionPercentage() {
        return completionPercentage;
    }
}
