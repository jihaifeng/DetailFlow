package com.meechao.detailflow;

import android.app.Application;
import butterknife.ButterKnife;
import com.meechao.detailflow.utils.DisplayUtils;
import com.meechao.detailflow.utils.ImageUtils;
import com.meechao.detailflow.utils.ScreenUtil;

/**
 * Func：
 * Desc:
 * Author：jihf
 * Data：2017-02-06 15:44
 * Mail：jihaifeng@meechao.com
 */
public class App extends Application {
  private static App instance;

  //private DbManager dbManager;

  public static App getInstance() {
    return instance;
  }

  private static void setInstance(App instance) {
    App.instance = instance;
  }

  @Override public void onCreate() {
    super.onCreate();
    init();
  }

  private void init() {
    setInstance(this);
    ScreenUtil.getInstance(this);
    DisplayUtils.init(this);
    ImageUtils.init(this);
    //// 异常日志搜集处理
    //CrashHandler.getInstance().init(getApplicationContext());
    //// 设置日志类是否显示
    //LogUtils.setLogSwitch(BuildConfig.DEBUG);
    ////屏幕工具类初始化
    //ScreenUtil.getInstance(getApplicationContext());
    //DisplayUtils.init(getApplicationContext());
    //// BaseUrl
    //UrlConstants.init(BuildConfig.DEBUG);
    //// http mode
    //HttpApiMethed.init();
    //// SP 持久化存储
    //SpUtils.init(getApplicationContext());
    //// ImageUtils
    //ImageUtils.init(getApplicationContext());
    //// 微信分享
    //ShareConfig.initWxShare(MeechaoConfig.WX_SHARE_APPID, MeechaoConfig.WX_SHARE_SECRET);
    //// Umeng统计
    //UMConfigure.init(getApplicationContext(), UMConfigure.DEVICE_TYPE_PHONE,"");
    //UMConfigure.setEncryptEnabled(true);
    //UMConfigure.setLogEnabled(true);
    //
    //MeechaoConfig.initHistory();
  }

}
