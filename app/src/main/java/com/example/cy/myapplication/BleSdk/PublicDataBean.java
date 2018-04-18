package com.example.cy.myapplication.BleSdk;

/**
 * 邮电部协议解析接收对象
 * Created by cxj on 2017/5/25.
 */

public class PublicDataBean<T> {
    private String version;
    private String address;
    private String commandID1;
    private String commandID2;
    private String length;
    private String info;
    private String checkSum;
    private String RTN;
    private T data;

    public PublicDataBean() {
    }

    public PublicDataBean(String version, String address, String commandID1, String commandID2, String length, String info, String checkSum, String RTN) {
        this.version = version;
        this.address = address;
        this.commandID1 = commandID1;
        this.commandID2 = commandID2;
        this.length = length;
        this.info = info;
        this.checkSum = checkSum;
        this.RTN = RTN;
    }

    @Override
    public String toString() {
        return "PublicDataBean{" +
                "version='" + version + '\'' +
                ", address='" + address + '\'' +
                ", commandID1='" + commandID1 + '\'' +
                ", commandID2='" + commandID2 + '\'' +
                ", length='" + length + '\'' +
                ", info='" + info + '\'' +
                ", checkSum='" + checkSum + '\'' +
                ", RTN='" + RTN + '\'' +
                ", data=" + data +
                '}';
    }

    public String getRTN() {
        return RTN;
    }

    public void setRTN(String RTN) {
        this.RTN = RTN;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommandID1() {
        return commandID1;
    }

    public void setCommandID1(String commandID1) {
        this.commandID1 = commandID1;
    }

    public String getCommandID2() {
        return commandID2;
    }

    public void setCommandID2(String commandID2) {
        this.commandID2 = commandID2;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
