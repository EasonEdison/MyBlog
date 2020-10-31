package com.example.demo.bean;


// 响应，这是我之前没注意的
public class ResponBean {
    private String status;
    private String msg;

    public ResponBean() {
    }

    public ResponBean(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
