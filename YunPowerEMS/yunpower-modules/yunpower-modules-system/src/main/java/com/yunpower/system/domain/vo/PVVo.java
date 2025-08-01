package com.yunpower.system.domain.vo;

/**
 * @title: 光伏发电相关信息
 * @Author: Jiajiaglam
 * @date: 2023-12-06 10:40
 * @description:
 */
public class PVVo {
    /** 变量名称 */
    private String name;

    /** 电流 */
    private String I;

    /** 电压 */
    private String U;

    /** 功率 */
    private String W;

    /** 频率 */
    private String Hz;

    /** 时间 */
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getI() {
        return I;
    }

    public void setI(String i) {
        I = i;
    }

    public String getU() {
        return U;
    }

    public void setU(String u) {
        U = u;
    }

    public String getW() {
        return W;
    }

    public void setW(String w) {
        W = w;
    }

    public String getHz() {
        return Hz;
    }

    public void setHz(String hz) {
        Hz = hz;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
