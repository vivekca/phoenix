package com.homelane.phoenixapp;

import com.hl.hlcorelib.HLApplication;
import com.hl.hlcorelib.HLCoreLib;

/**
 * Created by hl0395 on 15/12/15.
 */
public class PhoenixApplication extends HLApplication{

    @Override
    public void onCreate() {
        super.onCreate();

        HLCoreLib.init(getApplicationContext(), true);
        HLCoreLib.initAppConfig(PhoenixConstants.AppConfig.HL_ENVIRONMENT_DEV);

    }
}
