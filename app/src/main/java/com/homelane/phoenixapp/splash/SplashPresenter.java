package com.homelane.phoenixapp.splash;

import android.content.Intent;
import android.os.Handler;

import com.hl.hlcorelib.mvp.presenters.HLCoreActivityPresenter;
import com.homelane.phoenixapp.login.LoginPresenter;

public class SplashPresenter extends HLCoreActivityPresenter<SplashView> {

    @Override
    protected void onBindView() {
        super.onBindView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashPresenter.this, LoginPresenter.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }

    /**
     * Function which return the enclosing view class, this will be used to
     * create the respective view bind it to the Context
     *
     * @return return the enclosed view class
     */

    @Override
    protected Class<SplashView> getVuClass() {
        return SplashView.class;
    }
}
