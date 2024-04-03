package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class EducationalAccount extends AccountBase{

    public EducationalAccount(String username, double balance){
        super(username, balance);
        type = AccountType.EDUCATION;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if(buyedCourses >= 100){
            throw new MaxCourseCapacityReachedException();
        }

        boolean isDiscount = false;
        double avgGrade = 0.0;
        int counter = 0;
        for(int i = buyedCourses - 1; i >= 0; i--){
                if(courses[i].isCompleted()){
                    avgGrade += courses[i].getGrade();
                    counter++;
                }
                else{
                    break;
                }
        }

        if(counter == 5){
            avgGrade /= 5.0;
            if(avgGrade >= 4.5) {
                isDiscount = true;
            }
        }

        double newPrice;
        if(isDiscount)
            newPrice = course.getPrice() - (course.getPrice() * type.getDiscount());
        else
            newPrice = course.getPrice();

        if(newPrice > balance){
            throw new InsufficientBalanceException();
        }

        for(Course a : courses){
            if(a == course)
                throw new CourseAlreadyPurchasedException();
        }

        courses[buyedCourses++] = course;
        balance -= newPrice;
        courses[buyedCourses - 1].purchase();
    }
}
