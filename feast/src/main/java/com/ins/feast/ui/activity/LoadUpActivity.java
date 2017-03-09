package com.ins.feast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ins.feast.R;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.ins.middle.entity.User;
import com.ins.feast.utils.AppHelper;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.PermissionsUtil;
import com.sobey.common.utils.StrUtils;

import org.xutils.http.RequestParams;

import cn.jpush.android.api.JPushInterface;


public class LoadUpActivity extends BaseFeastActivity {

    private Handler mHandler = new Handler();

    private String token;
    private boolean startup;

    private long lasttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadup);

        initBase();
        initData();

        lasttime = System.currentTimeMillis();

        //测试获取token成功
//        AppData.App.saveToken("xxxx");
        //测试获取token失败
//        AppData.App.removeToken();
        //测试移除startup
//        AppData.App.removeStartUp();

        //获取token
        token = AppData.App.getToken();
        startup = AppHelper.getStartUp();

        if (token == null || "".equals(token)) {
            //无token 等待2秒 去登录页
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goLoginActivity();
                }
            }, 2000);
        } else {
            //有token 执行登录
            login();
        }
    }

    private void initBase() {
        //检查并申请权限
        PermissionsUtil.checkAndRequestPermissions(this);
        String registrationID = JPushInterface.getRegistrationID(ApplicationHelp.getApplicationContext());
        if (!StrUtils.isEmpty(registrationID)) {
            AppData.App.saveJpushId(registrationID);
        }
    }

    private void initData(){
        //netGetPhone();
    }

    private void login() {
        Log.e("liao", "token:" + token);
        RequestParams params = new RequestParams(AppData.Url.getInfo);
        params.addHeader("token", token);
        CommonNet.samplepost(params, User.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, final Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    User user = (User) pojo;
                    AppData.App.removeUser();
                    AppData.App.saveUser(user);

                    long time = System.currentTimeMillis() - lasttime;
                    if (time < 2000) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                goHomeActivity();
                            }
                        }, 2000 - time);
                    } else {
                        goHomeActivity();
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(LoadUpActivity.this, text, Toast.LENGTH_SHORT).show();
                AppData.App.removeUser();
                AppData.App.removeToken();
                long time = System.currentTimeMillis() - lasttime;
                if (time < 2000) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goLoginActivity();
                        }
                    }, 2000 - time);
                } else {
                    goLoginActivity();
                }
            }
        });
    }

    private void goLoginActivity() {
        //又不需要轮播页了，我操
//        if (startup) {
        //软登录，还是去home页
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
//        } else {
//            Intent intent = new Intent(this, StartUpActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    private void goHomeActivity() {
        //又不需要轮播页了fuck
//        if (startup) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
//        } else {
//            Intent intent = new Intent(this, StartUpActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    //获取客服电话
//    private void netGetPhone() {
//        RequestParams params = new RequestParams(AppData.Url.readContactUs);
//        params.addHeader("token", AppData.App.getToken());
//        CommonNet.samplepost(params, String.class, new CommonNet.SampleNetHander() {
//            @Override
//            public void netGo(int code, Object pojo, String text, Object obj) {
//                if (pojo == null) netSetError(code, "接口异常");
//                else {
//                    String phone = (String)pojo;
//                    if (!StrUtils.isEmpty(phone)) {
//                        AppData.App.removePhone();
//                        AppData.App.savePhone(phone);
//                    }
//                }
//            }
//
//            @Override
//            public void netSetError(int code, String text) {
//            }
//        });
//    }
}
