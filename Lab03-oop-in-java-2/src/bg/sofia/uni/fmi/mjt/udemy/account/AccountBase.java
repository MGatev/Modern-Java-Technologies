package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public abstract class AccountBase implements Account {

    protected String username;
    protected double balance;

    protected Course[] courses = new Course[100];

    protected int buyedCourses = 0;

   protected AccountType type;

    public AccountBase(String username, double balance){
        this.username = username;
        this.balance = balance;
    }

    public String getUsername(){
        return username;
    }

    public void addToBalance(double amount){
        if(amount < 0)
            throw new IllegalArgumentException("You cannot add negative amount");

        balance+=amount;
    }

    public double getBalance(){
        return balance;
    }

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException {
        if(course == null || resourcesToComplete == null)
            throw new IllegalArgumentException("The arguments should not be null");

        boolean isCourseFound = false;
        for(int i = 0; i < buyedCourses; i++){
            if(courses[i].equals(course)) {
                isCourseFound = true;
                for(int j = 0; j < course.getContentSize(); j++){
                    boolean isFound = false;
                    for(int k = 0; k < courses[i].getContentSize(); k++){
                        if(courses[i].getContent()[k].equals(resourcesToComplete[j])){
                            isFound = true;
                            courses[i].completeResource(courses[i].getContent()[k]);
                        }
                    }
                    if(!isFound){
                        throw new ResourceNotFoundException();
                    }
                }
            }
        }
        if(!isCourseFound) {
            throw new CourseNotPurchasedException();
        }

    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        if(grade < 2.0 || grade > 6.0 || course == null){
            throw new IllegalArgumentException("Illegal arguments!");
        }

        for(Resource r : course.getContent()){
            if(!r.isCompleted())
                throw new CourseNotCompletedException();
        }

        boolean isCourseFound = false;
        for(int i = 0; i < buyedCourses; i++) {
            if (courses[i].equals(course)) {
                isCourseFound = true;
                courses[i].setGrade(grade);
                courses[i].completeCourse();
            }
        }

        if(!isCourseFound)
            throw new CourseNotPurchasedException();
    }


    @Override
    public Course getLeastCompletedCourse(){
        if(buyedCourses == 0)
            return null;

        int leastPercentage = 101;
        for(int i = 0; i < buyedCourses; i++){
            int candidate = courses[i].getCompletionPercentage();
            if(candidate < leastPercentage){
                leastPercentage = candidate;
            }
        }

        for(int i = 0; i < buyedCourses; i++){
            if(courses[i].getCompletionPercentage() == leastPercentage){
                return courses[i];
            }
        }
        return null;
    }
}
