package com.example.keepbookkeeping.events;

/**
 * @author 邹永鹏
 * @date 2019/1/29
 * @description :
 */
public class ChangeFragmentTypeEvent {

    private int msg;

    public ChangeFragmentTypeEvent(int msg){
        this.msg=msg;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
