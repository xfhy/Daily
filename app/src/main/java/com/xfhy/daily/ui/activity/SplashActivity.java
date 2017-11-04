package com.xfhy.daily.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xfhy.daily.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.tv_splash_text)
    TextView tvSplashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(tvSplashText, "alpha", 0.1f, 1.0f);
        alpha.setDuration(2000);
        alpha.start();
    }
}
