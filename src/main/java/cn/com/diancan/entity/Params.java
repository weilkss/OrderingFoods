package cn.com.diancan.entity;

import java.io.Serializable;

public class Params implements Serializable {
    /**
     * 就餐人数
     */
    private int peopleNum;
    /**
     * 大菜数
     */
    private int maxNum;
    /**
     * 荤菜数
     */
    private int meatNum;
    /**
     * 素菜数
     */
    private int cheapNum;
    /**
     * 汤类
     */
    private int soupNum;

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMeatNum() {
        return meatNum;
    }

    public void setMeatNum(int meatNum) {
        this.meatNum = meatNum;
    }

    public int getCheapNum() {
        return cheapNum;
    }

    public void setCheapNum(int cheapNum) {
        this.cheapNum = cheapNum;
    }

    public int getSoupNum() {
        return soupNum;
    }

    public void setSoupNum(int soupNum) {
        this.soupNum = soupNum;
    }

    @Override
    public String toString() {
        return "Params{" +
                "peopleNum=" + peopleNum +
                ", maxNum=" + maxNum +
                ", meatNum=" + meatNum +
                ", cheapNum=" + cheapNum +
                ", soupNum=" + soupNum +
                '}';
    }
}
