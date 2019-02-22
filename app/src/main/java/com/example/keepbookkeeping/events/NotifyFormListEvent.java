package com.example.keepbookkeeping.events;

/**
 * @author 邹永鹏
 * @date 2019/2/21
 * @description :
 */
public class NotifyFormListEvent {

    private int msg;

    public NotifyFormListEvent(int msg){
        this.msg=msg;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

}
