package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

public class Udemy implements LearningPlatform {

    private Account[] accounts;
    private Course[] courses;

    public static boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    public Udemy(Account[] accounts, Course[] courses){
        this.accounts = accounts;
        this.courses = courses;
    }


    @Override
    public Course findByName(String name) throws CourseNotFoundException {
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Invalid name!");

        for(Course c : courses){
            if(c.getName().equals(name)){
                return c;
            }
        }

        throw new CourseNotFoundException();

    }

    @Override
    public Course[] findByKeyword(String keyword) {
        if(keyword == null || keyword.isBlank() || !isAlpha(keyword)){
            throw new IllegalArgumentException("Not a keyword!");
        }

        int counter = 0;
        for(Course c : courses){
            if(c.getName().contains(keyword) || c.getDescription().contains(keyword))
                counter++;
        }

        Course[] newCourses = new Course[counter];
        int index = 0;
        for(Course c : courses){
            if(c.getName().contains(keyword) || c.getDescription().contains(keyword))
                newCourses[index++] = c;
        }

        return newCourses;

    }

    @Override
    public Course[] getAllCoursesByCategory(Category category) {
       if(category == null)
           throw new IllegalArgumentException("Not a valid category!");

       int counter = 0;
       for(Course c : courses){
           if(c.getCategory().equals(category))
               counter++;
       }

       Course[] newCourses = new Course[counter];
       int index = 0;
        for(Course c : courses){
            if(c.getCategory().equals(category))
                newCourses[index++] = c;
        }

        return newCourses;


    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException {
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Invalid name!");

        for(Account a : accounts){
            if(a.getUsername().equals(name)){
                return a;
            }
        }

        throw new AccountNotFoundException();
    }

    @Override
    public Course getLongestCourse() {
        if(courses == null){
            return null;
        }

        int minutes = 0;
        for(Course c : courses){
            int newMinutes = (c.getTotalTime().hours() * 60) + c.getTotalTime().minutes();
            if(newMinutes > minutes)
                minutes = newMinutes;
        }

        for(Course c : courses){
            int newMinutes = (c.getTotalTime().hours() * 60) + c.getTotalTime().minutes();
            if(newMinutes == minutes)
                return c;
        }

        return null;
    }

    @Override
    public Course getCheapestByCategory(Category category) {
        if(courses == null)
            return null;

        if(category == null){
            throw new IllegalArgumentException("Invalid category!");
        }

        double minPrice = Double.MAX_VALUE;
        for(Course c : courses){
            if(c.getCategory().equals(category)) {
                if(c.getPrice() < minPrice){
                    minPrice = c.getPrice();
                }
            }
        }

        for(Course c : courses){
            if(c.getCategory().equals(category)) {
                if(c.getPrice() == minPrice){
                    return c;
                }
            }
        }
        return null;
    }
}
