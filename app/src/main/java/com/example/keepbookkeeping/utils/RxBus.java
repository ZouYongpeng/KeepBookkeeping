package com.example.keepbookkeeping.utils;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;

/**
 * @author 邹永鹏
 * @date 2019/1/29
 * @description :基于 RxRelay ,有异常处理能力的 Rxbus
 */
public class RxBus {

    private static volatile RxBus instance;

    private final Relay<Object> mBus;

    public RxBus(){
        this.mBus= PublishRelay.create().toSerialized();
    }

    private static class Holder{
        private static final RxBus rxBus=new RxBus();
    }

    public static RxBus getInstance(){
        if (instance==null){
            synchronized (RxBus.class){
                if (instance==null){
                    instance=Holder.rxBus;
                }
            }
        }
        return instance;
    }

    /**
     * 提供了一个新的事件,单一类型
     * @param object 事件数据
     */
    public void post(Object object){
        mBus.accept(object);
    }

    public <T> Observable<T> toObservable(Class<T> tClass){
        return mBus.ofType(tClass);
    }

    public Observable<Object> toObservable(){
        return mBus;
    }

    public boolean hasObservers(){
        return mBus.hasObservers();
    }
}
