package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public class StandardAccount extends AccountBase{

    public StandardAccount(String username, double balance){
        super(username, balance);
        type = AccountType.STANDARD;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if(buyedCourses >= 100){
            throw new MaxCourseCapacityReachedException();
        }

                if(course.getPrice() > balance){
                    throw new InsufficientBalanceException();
                }

                for(Course a : courses){
                    if(a == course)
                        throw new CourseAlreadyPurchasedException();
                }

                courses[buyedCourses++] = course;
                balance -= course.getPrice();
                courses[buyedCourses - 1].purchase();
    }
}
