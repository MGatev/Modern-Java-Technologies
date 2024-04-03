import bg.sofia.uni.fmi.mjt.udemy.LearningPlatform;
import bg.sofia.uni.fmi.mjt.udemy.Udemy;
import bg.sofia.uni.fmi.mjt.udemy.account.BusinessAccount;
import bg.sofia.uni.fmi.mjt.udemy.account.EducationalAccount;
import bg.sofia.uni.fmi.mjt.udemy.account.StandardAccount;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;

public class Main {
    public static void main(String... args){
        Category[] allowed = new Category[2];
        allowed[0] = Category.DESIGN;
        allowed[1] = Category.DEVELOPMENT;


        Account acc1 = new EducationalAccount("Marti", 2000);
        Account acc2 = new EducationalAccount("Veni", 200);
        Account acc3 = new EducationalAccount("Vili", 169);
        Account acc4 = new BusinessAccount("Lazi", 17.5, allowed);
        Account acc5 = new StandardAccount("Ani", 50.5);

        Account[] accounts = new Account[5];
        accounts[0] = acc1;
        accounts[1] = acc2;
        accounts[2] = acc3;
        accounts[3] = acc4;
        accounts[4] = acc5;

        Course[] courses = new Course[8];

        Resource[] rDIS = new Resource[2];
        rDIS[0] = new Resource("Funkcii", new ResourceDuration(10));
        rDIS[1] = new Resource("Integrali", new ResourceDuration(15));

        Course c1 = new Course("DIS", "Mn qko", 7.0, rDIS, Category.SOFTWARE_ENGINEERING);

        Resource[] rDSTR = new Resource[2];
        rDSTR[0] = new Resource("relacii", new ResourceDuration(60));
        rDSTR[1] = new Resource("durveta", new ResourceDuration(30));
        Course c2 = new Course("DSTR", "Ne e Mn qko", 12.0, rDSTR, Category.DEVELOPMENT);

        courses[0] = c1;
        courses[1] = c2;

        Resource[] rALG = new Resource[2];
        rALG[0] = new Resource("kompleksni", new ResourceDuration(60));
        rALG[1] = new Resource("matrici", new ResourceDuration(30));
        Course c3 = new Course("ALG", "Mn qko", 5.0, rALG, Category.HEALTH_AND_FITNESS);

        Resource[] rUP   = new Resource[2];
        rUP[0] = new Resource("operacii", new ResourceDuration(60));
        rUP[1] = new Resource("if statement", new ResourceDuration(30));
        Course c4 = new Course("UP", "Mn qko", 14.0, rUP, Category.SOFTWARE_ENGINEERING);

        Resource[] rOOP   = new Resource[2];
        rOOP[0] = new Resource("polimorfizum", new ResourceDuration(60));
        rOOP[1] = new Resource("design pattern", new ResourceDuration(30));
        Course c5 = new Course("OOP", "Mn qko", 20.0, rOOP, Category.SOFTWARE_ENGINEERING);

        Resource[] rGeom   = new Resource[2];
        rGeom[0] = new Resource("e3*", new ResourceDuration(60));
        rGeom[1] = new Resource("ravnini", new ResourceDuration(30));
        Course c6 = new Course("Geom", "Mn qko", 3.0, rGeom, Category.DESIGN);

        Resource[] rKVARTIRA   = new Resource[2];
        rKVARTIRA[0] = new Resource("SAVOY", new ResourceDuration(60));
        rKVARTIRA[1] = new Resource("CHASHA", new ResourceDuration(30));
        Course c7 = new Course("KVARTIRA", "Mn qko", 40.0, rKVARTIRA, Category.SOFTWARE_ENGINEERING);

        Resource[] rVKIS   = new Resource[2];
        rKVARTIRA[0] = new Resource("BUTILKA", new ResourceDuration(60));
        rKVARTIRA[1] = new Resource("VKIS VKIS", new ResourceDuration(30));
        Course c8 = new Course("VKIS", "Mn qko", 1.0, rVKIS, Category.SOFTWARE_ENGINEERING);

        courses[2] = c3;
        courses[3] = c4;
        courses[4] = c5;
        courses[5] = c6;
        courses[6] = c7;
        courses[7] = c8;





        LearningPlatform marti = new Udemy(accounts, courses);
      // System.out.println(marti.getLongestCourse());
        try{
            marti.getAccount("Marti").buyCourse(c1);
            marti.getAccount("Marti").buyCourse(c2);
            marti.getAccount("Marti").buyCourse(c3);
            marti.getAccount("Marti").buyCourse(c4);
            marti.getAccount("Marti").buyCourse(c5);
            marti.getAccount("Marti").buyCourse(c6);
            marti.getAccount("Marti").buyCourse(c7);

            marti.getAccount("Marti").completeResourcesFromCourse(c1, rDIS);
            marti.getAccount("Marti").completeResourcesFromCourse(c2, rDSTR);
            marti.getAccount("Marti").completeResourcesFromCourse(c3, rALG);
            marti.getAccount("Marti").completeResourcesFromCourse(c4, rUP);
            marti.getAccount("Marti").completeResourcesFromCourse(c5, rOOP);
            marti.getAccount("Marti").completeResourcesFromCourse(c7, rKVARTIRA);




            marti.getAccount("Marti").completeCourse(c1, 6.0);
            marti.getAccount("Marti").completeCourse(c2, 6.0);
            marti.getAccount("Marti").completeCourse(c3, 2.0);
            marti.getAccount("Marti").completeCourse(c4, 4.5);
            marti.getAccount("Marti").completeCourse(c5, 5.0);
            marti.getAccount("Marti").completeCourse(c7, 5.0);

            marti.getAccount("Marti").buyCourse(c8);


            marti.getAccount("Marti").completeResourcesFromCourse(c1, rDSTR);
        }
        catch (AccountNotFoundException e){
            return;
        }
        catch (CourseNotPurchasedException e){
            return;
        }
        catch(Throwable e){
            return;
        }




    }
}
