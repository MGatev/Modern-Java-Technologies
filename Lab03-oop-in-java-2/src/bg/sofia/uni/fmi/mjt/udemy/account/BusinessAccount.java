package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public class BusinessAccount extends AccountBase{

    Category[] allowedCategories;

    public BusinessAccount(String username, double balance, Category[] allowedCategories){
        super(username, balance);
        this.allowedCategories = allowedCategories;
        type = AccountType.BUSINESS;
    }
    @Override
   public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
       if(buyedCourses >= 100){
           throw new MaxCourseCapacityReachedException();
       }

        for(Category c: allowedCategories){
            if(c.equals(course.getCategory())){
                double newPrice = course.getPrice() - (course.getPrice() * type.getDiscount());

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
                return;
            }
        }

        throw new IllegalArgumentException("The category of the course is not allowed!");
    }
}
