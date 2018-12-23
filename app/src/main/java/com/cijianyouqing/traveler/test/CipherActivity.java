package com.cijianyouqing.traveler.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.activity.BaseActivity;
import com.cijianyouqing.traveler.util.CipherHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiangpengfei on 2018/11/9.
 */
public class CipherActivity extends BaseActivity {
    @BindView(R.id.et_input)
    EditText et_input;
    @BindView(R.id.jiami)
    Button jiami;
    @BindView(R.id.tv_jiami)
    TextView tv_jiami;
    @BindView(R.id.jiemi)
    Button jiemi;
    @BindView(R.id.tv_jiemi)
    TextView tv_jiemi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cipher);
        ButterKnife.bind(this);

        jiami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_input.getText().toString().trim())){
                    String jiami = CipherHelper.getInstence().encrypt(et_input.getText().toString().trim());
                    tv_jiami.setText(jiami);
                }
            }
        });
        jiemi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_jiami.getText().toString().trim())){
                    String jiemi = CipherHelper.getInstence().decrypt(tv_jiami.getText().toString().trim());
                    tv_jiemi.setText(jiemi);
                }
            }
        });

    }
}
