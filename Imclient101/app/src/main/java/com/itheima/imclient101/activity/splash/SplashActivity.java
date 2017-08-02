package com.itheima.imclient101.activity.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.itheima.imclient101.R;
import com.itheima.imclient101.activity.MainActivity;
import com.itheima.imclient101.activity.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    @InjectView(R.id.iv_image)
    ImageView ivImage;
    @InjectView(R.id.activity_splash)
    RelativeLayout activitySplash;
    private SplashContract.Presenter mPresenter;
    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        //把传递进来的Presenter对象保存到成员变量
        mPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        //只需要new对象就可以了 不一定非要赋值给成员变量 成员变量的赋值在setPresenter方法中实现的
        new SplashPresenter(this);
        mPresenter.checkIsLogin();
    }

    @Override
    public void onGetIsLoginResult(boolean isLoginBefore) {
        if (isLoginBefore) {
            //不展示闪屏 直接进入主页面
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(ivImage,"Alpha",0,1);
            animator.setDuration(2000);
            animator.start();
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //不展示闪屏 直接进入主页面
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

}
