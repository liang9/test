package com.itheima.imclient101.fragment.Plugin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.itheima.imclient101.R;
import com.itheima.imclient101.activity.login.LoginActivity;
import com.itheima.imclient101.fragment.BaseFragment;
import com.itheima.imclient101.utils.SPUtils;
import com.itheima.imclient101.utils.ThreadUtils;
import com.itheima.imclient101.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginFragment extends BaseFragment implements PluginContract.View {
    @InjectView(R.id.btn_logout)
    Button btnLogout;
    private PluginContract.Presenter mPresnter;


    public PluginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plugin, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String username  = SPUtils.getString(getContext(),"username");
        btnLogout.setText("退("+username+")出");
        new LogoutPresenter(this);
    }

    @Override
    public void onGetLogoutResult(boolean isSuccess, String errMsg) {
            if(isSuccess){
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }else{
                ToastUtils.showToast(getContext(),"退出失败"+errMsg);
            }
    }

    @Override
    public void setPresenter(PluginContract.Presenter presenter) {
        this.mPresnter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.btn_logout)
    public void onClick() {
        mPresnter.logout();
    }
}
