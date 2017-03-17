package com.ins.chef.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ins.chef.R;
import com.ins.middle.common.AppData;
import com.ins.middle.common.AppVali;
import com.ins.middle.common.CommonNet;
import com.ins.middle.entity.User;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.ins.middle.ui.dialog.DialogLoading;
import com.sobey.common.utils.MD5Util;

import org.xutils.http.RequestParams;

public class LoginActivity extends BaseFeastActivity implements View.OnClickListener {

    private EditText edit_login_phone;
    private EditText edit_login_psw;
    private View btn_go_forgetpsw;
    private View btn_go;

    private DialogLoading dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setToolbar();
        setNeedDoubleClickExit(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogLoading != null) dialogLoading.dismiss();
    }

    private void initBase() {
        dialogLoading = new DialogLoading(this);
    }

    private void initView() {

        edit_login_phone = (EditText) findViewById(R.id.edit_login_phone);
        edit_login_psw = (EditText) findViewById(R.id.edit_login_psw);
        btn_go_forgetpsw = findViewById(R.id.btn_go_forgetpsw);
        btn_go = findViewById(R.id.btn_go);

        btn_go_forgetpsw.setOnClickListener(this);
        btn_go.setOnClickListener(this);
    }

    private void initData() {
    }

    private void initCtrl() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_forgetpsw:
                CommonWebActivity.start(this, AppData.Url.FEAST_CHEF_FORGETPSW);
                break;
            case R.id.btn_go:
                String phone = edit_login_phone.getText().toString();
                String psw = edit_login_psw.getText().toString();
                String msg = AppVali.login_go(phone, psw);
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    netLogin(phone, psw);
                }
                break;
        }
    }

    private void netLogin(String phone, String psw) {
        RequestParams params = new RequestParams(AppData.Url.login);
        params.addBodyParameter("phoneNumber", phone);
        params.addBodyParameter("password", MD5Util.md5(psw));
        params.addBodyParameter("flag", "6");
        params.addBodyParameter("deviceToken", AppData.App.getJpushId());
        params.addBodyParameter("deviceType", "0");
        CommonNet.samplepost(params, User.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, final Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    User user = (User) pojo;
                    AppData.App.removeUser();
                    AppData.App.saveUser(user);
                    AppData.App.saveToken(user.getToken());
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                AppData.App.removeUser();
                AppData.App.removeToken();
            }

            @Override
            public void netStart(int status) {
                dialogLoading.show();
            }

            @Override
            public void netEnd(int status) {
                dialogLoading.hide();
            }
        });
    }
}
