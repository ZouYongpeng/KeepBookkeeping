package com.example.keepbookkeeping.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.keepbookkeeping.bean.BmobBean.User;
import com.example.keepbookkeeping.bean.SingleDataBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author 邹永鹏
 * @date 2019/2/24
 * @description :
 */
public class UserUtil {

    private static volatile UserUtil instance;

    public synchronized static UserUtil getInstance(){
        if (instance==null){
            instance=new UserUtil();
        }
        return instance;
    }
    /**
     * 获取当前用户
     * */
    public User getCurrentUser(){
        return BmobUser.getCurrentUser(User.class);
    }

    public String getCurrentUserId(){
        if (getCurrentUser()!=null){
            return getCurrentUser().getObjectId();
        }else {
            return "local";
        }
    }

    /**
     * 登陆
     * @param username
     * @param password
     * @param listener
     */
    public void login(String username, String password, final LogInListener listener){
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    listener.done(getCurrentUser(), null);
                } else {
                    listener.done(user, e);
                }
            }
        });
    }

    /**
     * 同步数据至云端
     * @param userId
     */
    public void uploadSingleDataList(List<SingleDataBean> list,String userId){
        if (TextUtils.isEmpty(userId) ||
                TextUtils.equals(userId,"local")){
            return;
        }
        for (final SingleDataBean bean : list) {
            if (!TextUtils.isEmpty(bean.getUserId()) && bean.getCanUpload()==SingleDataBean.TYPE_NEW_DATA){
                bean.setUserId(userId);
                bean.save(new SaveListener<String>(){
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                            LogUtil.d("cloud","添加数据成功 ：objectId = "+s+" , "+bean.toString());
                        }else {
                            LogUtil.d("cloud","创建数据失败：" + e.getMessage());
                        }
                    }
                });
            }if (!TextUtils.isEmpty(bean.getUserId()) && bean.getUserId()!=null && bean.getCanUpload()==SingleDataBean.TYPE_HAS_CHANGE){
                bean.setUserId(userId);
                if (!TextUtils.isEmpty(bean.getObjectId())){
                    bean.update(bean.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                LogUtil.d("cloud","修改数据成功 ：objectId = "+bean.getObjectId()+" , "+bean.toString());
                            }else {
                                LogUtil.d("cloud","修改数据失败：" + e.getMessage());
                            }
                        }
                    });
                }
            }
        }
        AllDataTableUtil.updateUpload();
        downLoadCloudSingleData(userId);
    }

    public static void downLoadCloudSingleData(String userId){
        BmobQuery<SingleDataBean> query=new BmobQuery<>();
        query.addWhereEqualTo("userId",userId);
        query.setLimit(500);
        query.findObjects(new FindListener<SingleDataBean>() {
            @Override
            public void done(List<SingleDataBean> list, BmobException e) {
                if (e==null){
                    LogUtil.d("cloud","查询成功：共"+list.size()+"条数据。");
                    //先清除本地的 TYPE_HAS_UPLOAD 数据
                    AllDataTableUtil.deleteAllHasUploadData();
                    //把数据插入本地数据库
                    for (SingleDataBean bean:list){
                        Log.d("cloud", "查询: "+bean.getObjectId()+bean.toString());
                        AllDataTableUtil.insertSingleDataToAllData(bean,SingleDataBean.TYPE_HAS_UPLOAD);
                    }

                }else {
                    LogUtil.d("cloud","查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public static void deleteDataInCloud(String objectId){
        if (TextUtils.isEmpty(objectId)){
            return;
        }
        SingleDataBean bean=new SingleDataBean();
        bean.setObjectId(objectId);
        bean.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUtil.error("已从云端删除数据");
                }else{
                    LogUtil.d("cloud","删除失败："+e.getMessage()+","+e.getErrorCode());
                    ToastUtil.error("云端删除数据失败");
                }
            }
        });
    }

}
