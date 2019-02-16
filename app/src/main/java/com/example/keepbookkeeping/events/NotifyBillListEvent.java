package com.example.keepbookkeeping.events;

/**
 * @author 邹永鹏
 * @date 2019/2/17
 * @description :
 */
public class NotifyBillListEvent {

    private int msg;

    public NotifyBillListEvent(int msg){
        this.msg=msg;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

}
