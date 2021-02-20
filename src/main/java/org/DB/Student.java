package org.DB;

import java.util.Random;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/2/1 10:19
 */
public class Student {

    private  String  name;

    private double  score;

    private String  sex;

    private  int age;

    public Student(String name, double score, String sex, int age) {
        this.name = name;
        this.score = score;
        this.sex = sex;
        this.age = age;
    }

    public Student() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +"age="+ age +
                ","+ "score=" + score +",sex="+sex+
                '}';
    }

    public String randSex() {
        int randNum = new Random().nextInt(2) + 1;//该方法的作用是生成一个随机的int值，该值介于[0,n)的区间，也就是0到n之间的随机int值，包含0而不包含n。


        return randNum == 1 ? "男" : "女";
    }
}
