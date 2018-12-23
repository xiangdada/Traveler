package com.cijianyouqing.traveler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cijianyouqing.traveler.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

/**
 * Created by xiangpengfei on 2018/11/5.
 */
public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.relativelayout)
    RelativeLayout relativelayout;
    @BindView(R.id.textview)
    TextView textview;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //Bmob云服务提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(this, getResources().getString(R.string.bmobkey));
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textview.setText("跳过 " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                toNextActivity();
            }
        };
        countDownTimer.start();

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                toNextActivity();
            }
        });

    }

    private void toNextActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
