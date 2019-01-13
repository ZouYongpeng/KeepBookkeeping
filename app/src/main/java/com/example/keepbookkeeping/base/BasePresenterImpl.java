package com.example.keepbookkeeping.base;

import java.lang.ref.WeakReference;

/**
 * @author 邹永鹏
 * @date 2019/1/13
 * @description :
 */
public class BasePresenterImpl<V extends BaseView> implements BasePresenter {

    protected WeakReference<V> mView;

    public BasePresenterImpl(V view){
        mView=new WeakReference<V>(view);
        view.setPresenter(this);
    }

    protected boolean isViewActive(){
        return mView!=null && mView.get().isActive();
    }

    public void detachView(){
        if (mView!=null){
            mView.clear();
            mView=null;
        }
    }

    @Override
    public void start() {

    }
}
