package com.junhua.gstic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.junhua.gstic.R;

import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class MainActivity extends AppCompatActivity {

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInfo = JMessageClient.getMyInfo();
        if (userInfo!=null){
            Log.e("info",userInfo.toString());

            List<Conversation> list= JMessageClient.getConversationList();
            for (int i=0;i<list.size();i++){
                Log.e("list", list.get(i).toString());
            }

            ContactManager.sendInvitationRequest("12345678", null, "hello", new BasicCallback(false) {
                @Override
                public void gotResult(int responseCode, String responseMessage) {
                    if (0 == responseCode) {
                        //好友请求请求发送成功
                        Log.e("send",responseMessage);
                    } else {
                        //好友请求发送失败
                        Log.e("send","发送失败");
                    }
                }
            });

            ContactManager.getFriendList( new GetUserInfoListCallback(false) {
                @Override
                public void gotResult(int i, String s, List<UserInfo> list) {
                    Log.e("fi  +-  ",i+s);
                    if (i==0&list!=null){
                        for (int x=0;x<list.size();x++){
                            Log.e("firends",list.get(x).toString());
                        }
                    }
                }
            });
            JMessageClient.logout();
        }
    }
}
