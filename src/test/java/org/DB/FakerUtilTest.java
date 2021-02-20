package org.DB;

import com.github.javafaker.Faker;
import org.DataProvider.FakerUtil;
import org.DataProvider.RandomName;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/1/29 10:30
 */
public class FakerUtilTest {


    private static int MIN_NUM = 4;
    private static int MAX_NUM = 10;

    //private Logger logger = (Logger) LoggerFactory.getLogger(FakerUtilTest.class);

    Faker faker = new Faker(Locale.CHINA);

    @Test
    public void testFaker() {

        int cycleFullNameNum = 10;
        for (int i = 0; i < cycleFullNameNum; i++) {
            System.out.println(faker.address().cityName());
        }
    }

    @Test
    public void test() {

        // 卢街62号
        System.out.println(faker.address().streetAddress());
        // 胡昊强
        System.out.println(faker.name().fullName());
        // By Grand Central Station I Sat Down and Wept
        System.out.println(faker.book().title());
        // 15128552972
        System.out.println(faker.phoneNumber().cellPhone());
        // Bytecard
        System.out.println(faker.app().name());
        // pink
        System.out.println(faker.color().name());
        // Fri Jan 29 16:04:50 CST 1960
        System.out.println(faker.date().birthday());
        // 000-63-7175
        System.out.println(faker.idNumber().invalid());
    }


    @Test
    public void testFakerUtil() {
        Logger logger = LoggerFactory.getLogger(FakerUtilTest.class);

        logger.info("                                ");
        logger.info("================ start test full name =====================");
        long cycleFullNameNum = FakerUtil.randomNum(FakerUtilTest.MIN_NUM, FakerUtilTest.MAX_NUM);
        for (int i = 0; i < cycleFullNameNum; i++) {
            logger.info("full name : [{}]", FakerUtil.fullName());
        }
        logger.info("================ start test full name =====================");

        logger.info("                                ");
        logger.info("================ start test name =====================");
        long cycleNameNum = FakerUtil.randomNum(FakerUtilTest.MIN_NUM, FakerUtilTest.MAX_NUM);
        for (int i = 0; i < cycleNameNum; i++) {
            logger.info("full name : [{}]", FakerUtil.name());
        }
        logger.info("================ end test name =====================");

        logger.info("                                ");
        logger.info("================ start test app name =====================");
        long cycleAppNameNum = FakerUtil.randomNum(FakerUtilTest.MIN_NUM, FakerUtilTest.MAX_NUM);
        for (int i = 0; i < cycleAppNameNum; i++) {
            logger.info("full name : [{}]", FakerUtil.appName());
        }
        logger.info("================ end test app name =====================");

        logger.info("                                ");
        logger.info("================ start test food =====================");
        long cycleFoodNum = FakerUtil.randomNum(FakerUtilTest.MIN_NUM, FakerUtilTest.MAX_NUM);
        for (int i = 0; i < cycleFoodNum; i++) {
            logger.info("full name : [{}]", FakerUtil.food());
        }
        logger.info("================ end test food =====================");
        logger.info("                                ");


        logger.info("================ start country test  =====================");
        long cycleCountryNum = FakerUtil.randomNum(FakerUtilTest.MIN_NUM, FakerUtilTest.MAX_NUM);
        for (int i = 0; i < cycleFoodNum; i++) {
            logger.info("full name : [{}]", faker.currency().name());
        }
        logger.info("================ end test food =====================");
        logger.info("                                ");

    }


    @Test
    public void  StudentTest(){

        Student student= new Student();

        for(int i=0;i<10;i++){
            student.setAge(faker.number().numberBetween(15,17));
            student.setName(faker.name().fullName());
            student.setScore(faker.number().randomDouble(1,60,100));
            student.setSex(student.randSex());

            System.out.println("userInfo = " + student);
        }


    }

    @Test
    public void RandomInfoTest(){

        Student student= new Student();
        RandomName randInfo = new RandomName();


        for (int i = 0; i < 10; i++) {

            // 姓氏随机生成
            String familyName = randInfo.randFamilyName();
            // 名字依托于性别产生
            String randName = randInfo.randName(randInfo.randSex());
            String[] fixed = randName.split("-");
            double score = randInfo.randScore();

            String name = fixed[0];
            String sex = fixed[1];
            int age = randInfo.randAge();
            student.setName(familyName.concat(name));
            student.setSex(sex);
            student.setAge(age);
            student.setScore(score);

            System.out.println(student);
        }

    }


}


