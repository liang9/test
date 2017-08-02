package com.itheima.imclient101.activity.login;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.imclient101.R;
import com.itheima.imclient101.activity.BaseActivity;
import com.itheima.imclient101.activity.MainActivity;
import com.itheima.imclient101.activity.regist.RegistActivity;
import com.itheima.imclient101.utils.SPUtils;
import com.itheima.imclient101.utils.ToastUtils;
import com.itheima.imclient101.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {

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
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.tv_newuser)
    TextView tvNewuser;

    private LoginContract.Presenter mPresenter = null;
    private String username;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
      //  Intent intent = getIntent();

        String username  = SPUtils.getString(getApplicationContext(),"username");
        String pwd  = SPUtils.getString(getApplicationContext(),"pwd");
        if(username !=null&&username.trim().length()>0){
            etUsername.setText(username);
            etPwd.setText(pwd);
        }
        new LoginPresenter(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        String username  = intent.getStringExtra("username");
//        String pwd  = intent.getStringExtra("pwd");
        String username  = SPUtils.getString(getApplicationContext(),"username");
        String pwd  = SPUtils.getString(getApplicationContext(),"pwd");
        if(username !=null&&username.trim().length()>0){
            etUsername.setText(username);
            etPwd.setText(pwd);
        }
    }

    @OnClick({R.id.btn_login, R.id.tv_newuser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
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
                //如果是6.0以后的设备
                if(Build.VERSION.SDK_INT>=23){
                    //先检查有没有权限
                   if( ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PermissionChecker.PERMISSION_GRANTED){
                       //如果没有权限先去申请
                       ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                       //直接返回
                       return;
                   }
                }
                // 如果有权限 或者 是23以下的版本 直接登录
                mPresenter.login(username,pwd);
                showProgressDialog("正在玩命登录中...");
                break;
            case R.id.tv_newuser:
                startActivity(new Intent(getApplicationContext(), RegistActivity.class));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PermissionChecker.PERMISSION_GRANTED){

        }else{
           ToastUtils.showToast(getApplicationContext(),"没有sd卡权限无法保存聊天记录");
        }
        mPresenter.login(username,pwd);
        showProgressDialog("正在玩命登录中...");
    }

    @Override
    public void onGetLoginResult(boolean isSuccess, String errorMessage) {
        progressDialog.dismiss();
        if(isSuccess){
            SPUtils.putString(getApplicationContext(),"username",username);
            SPUtils.putString(getApplicationContext(),"pwd",pwd);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            ToastUtils.showToast(this,"登录失败:"+errorMessage);
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
