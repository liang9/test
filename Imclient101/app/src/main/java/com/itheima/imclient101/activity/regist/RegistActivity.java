package com.itheima.imclient101.activity.regist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.itheima.imclient101.R;
import com.itheima.imclient101.activity.BaseActivity;
import com.itheima.imclient101.activity.login.LoginActivity;
import com.itheima.imclient101.utils.SPUtils;
import com.itheima.imclient101.utils.ThreadUtils;
import com.itheima.imclient101.utils.ToastUtils;
import com.itheima.imclient101.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class RegistActivity extends BaseActivity implements RegistContract.View {
    RegistContract.Presenter mPresenter;
    @InjectView(R.id.iv_avatar)
    ImageView ivAvatar;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.til_username)
    TextInputLayout tilUsername;
    @InjectView(R.id.et_pwd)
    EditText etPwd;
    @InjectView(R.id.til_pwd)
    TextInputLayout tilPwd;
    @InjectView(R.id.btn_regist)
    Button btnRegist;
    private String username;
    private String pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);
        new RegistPresenter(this);
    }

    @Override
    public void onGetRegistResult(boolean isSuccess, String errorMsg) {
        progressDialog.dismiss();
        if(isSuccess){
            ToastUtils.showToast(getApplicationContext(),"注册成功");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
           // intent.putExtra("username",username);
           // intent.putExtra("pwd",pwd);
            SPUtils.putString(getApplicationContext(),"username",username);
            SPUtils.putString(getApplicationContext(),"pwd",pwd);
            startActivity(intent);
            //finish();
        }else{
            ToastUtils.showToast(getApplicationContext(),"注册失败:"+errorMsg);
        }
    }

    @Override
    public void setPresenter(RegistContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @OnClick(R.id.btn_regist)
    public void onClick() {
        //点击按钮注册用户
        //获取用户的输入
        username = etUsername.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        if(!Utils.checkUsername(username)){
            //打开显示错误信息的布局
            tilUsername.setErrorEnabled(true);
            tilUsername.setError("用户名输入不合法");
            return;
        }else{
            //关闭显示错误信息的布局
            tilUsername.setErrorEnabled(false);
        }

        if(!Utils.checkPwd(pwd)){
            tilPwd.setErrorEnabled(true);
            tilPwd.setError("密码输入不合法");
            return;
        }else{
            tilPwd.setErrorEnabled(false);
        }

        mPresenter.registNewUser(username, pwd);
        showProgressDialog("正在注册中...");
    }
}
