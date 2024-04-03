package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public class Course implements Completable, Purchasable {

    private String name;
    private String description;
    private double price;
   private Resource[] content;
   private int contentSize;
    private Category category;
    private boolean isPurchased = false;
    private boolean isCompleted = false;

    private double grade;


    public Course(String name, String description, double price, Resource[] content, Category category){
        this.name = name;
        this.description = description;
        this.price = price;
        this.content = content;
        this.category = category;
        contentSize = content.length;
        grade = 0.0;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public Resource[] getContent() {
        return content;
    }

    public int getContentSize(){return contentSize;}

    public void setGrade(double grade){
        this.grade = grade;
    }

    public double getGrade(){
        return grade;
    }

    public CourseDuration getTotalTime() {
        return CourseDuration.of(content);
    }

    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException {
        if(resourceToComplete == null)
            throw new IllegalArgumentException("Should enter a resource");

        for(int i = 0; i < contentSize; i++){
            if(content[i].equals(resourceToComplete)){
                content[i].complete();
                return;
            }
        }

            throw new ResourceNotFoundException();
    }

    public void completeCourse(){
        for(Resource r : content){
            r.complete();
        }
        isCompleted = true;
    }

    @Override
    public boolean isCompleted() {
        for(Resource r : content){
            if(!r.isCompleted()){
                return false;
            }
        }

        return true;
    }

    @Override
    public int getCompletionPercentage() {
        if(contentSize == 0){
            return 0;
        }
        double averageComplete = 0.0;
        for(int i = 0; i < contentSize; i++){
            averageComplete += content[i].getCompletionPercentage();
        }

        averageComplete /= contentSize;

        int intValue = (int) Math.round(averageComplete);

        return intValue;
    }

    @Override
    public void purchase() {
        isPurchased = true;
    }

    @Override
    public boolean isPurchased() {
        return isPurchased;
    }
}
