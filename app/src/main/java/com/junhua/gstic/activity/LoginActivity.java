package com.junhua.gstic.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.junhua.gstic.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText et_user;
    private EditText et_pwd;
    private String user;
    private String pwd;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case 0x002:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    Toast.makeText(LoginActivity.this, userInfo.getUserName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    break;
                case 0x003:
                    Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                    break;
                case 0x004:
                    Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_user = (EditText) findViewById(R.id.et_user);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        userInfo = JMessageClient.getMyInfo();
        if (userInfo!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    public void MyClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reg:
                user = et_user.getText().toString().trim();
                pwd = et_pwd.getText().toString().trim();
                if (user.length() > 4) {
                    if (pwd.length() > 4) {
                        registe();
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("注册失败")
                                .setMessage("请核对密码（必须大于4个字符长度）")
                                .setPositiveButton("确定", null)
                                .show();
                    }
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("注册失败")
                            .setMessage("请核对账号（必须大于4个字符长度）")
                            .setPositiveButton("确定", null)
                            .show();
                }
                break;
            case R.id.btn_login:
                user = et_user.getText().toString().trim();
                pwd = et_pwd.getText().toString().trim();
                if (user.length() > 4) {
                    if (pwd.length() > 4) {
                        login();
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("登录失败")
                                .setMessage("请核对密码（必须大于4个字符长度）")
                                .setPositiveButton("确定", null)
                                .show();
                    }
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("登录失败")
                            .setMessage("请核对账号（必须大于4个字符长度）")
                            .setPositiveButton("确定", null)
                            .show();
                }

                break;
        }
    }

    private void login() {
        JMessageClient.login(user, pwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.e("login", i + "-----" + s);
                if (i == 801003) {
                    handler.sendEmptyMessage(0x003);
                } else if (i == 801004) {
                    handler.sendEmptyMessage(0x001);
                } else if (i == 0) {
                    JMessageClient.getUserInfo(user, new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {

                            Log.e("login", i + "++++++" + s + userInfo);
                            Message msg = new Message();
                            msg.what = 0x002;
                            msg.obj = userInfo;
                            handler.sendMessage(msg);
                        }
                    });
                }

            }
        });
    }

    private void registe() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JMessageClient.register(et_user.getText().toString().trim(), et_pwd.getText().toString().trim(), new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.e("user", i + "+++++" + s);
                        if (i == 0) {
                            handler.sendEmptyMessage(0x004);
                        } else if (i == 898001) {
                            handler.sendEmptyMessage(0x005);
                        }
                    }

                });
            }
        }).start();
    }
}
