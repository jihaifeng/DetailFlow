package com.meechao.detailflow.wedget.keyboard.smartKeyboard;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-25 09:48
 * Mail：jihaifeng@meechao.com
 */
public class DisplayUtils {
  // 四舍五入的偏移值
  private static final float ROUND_CEIL = 0.5f;
  // 屏幕矩阵对象
  private static DisplayMetrics mDisplayMetrics;
  // 资源对象（用于获取屏幕矩阵）
  private static Resources mResources;
  // statusBar的高度（由于这里获取statusBar的高度使用的反射，比较耗时，所以用变量记录）
  private static int statusBarHeight = -1;

  /**
   * 初始化操作
   *
   * @param context context上下文对象
   */
  public static void init(Context context) {
    mDisplayMetrics = context.getResources().getDisplayMetrics();
    mResources = context.getResources();
  }

  /**
   * 获取屏幕高度 单位：像素 px
   *
   * @return 屏幕高度
   */
  public static int getScreenHeight() {
    return mDisplayMetrics.heightPixels;
  }

  /**
   * 获取屏幕宽度 单位：像素 px
   *
   * @return 屏幕宽度
   */
  public static float getScreenWidth() {
    return mDisplayMetrics.widthPixels;
  }

  /**
   * 获取屏幕密度
   *
   * @return 屏幕密度
   */
  public static float getDensity() {
    return mDisplayMetrics.density;
  }

  /**
   * dp 转 px
   *
   * @param dp dp值
   *
   * @return 转换后的像素值
   */
  public static int dp2px(int dp) {
    return (int) (dp * getDensity() + ROUND_CEIL);
  }

  /**
   * px 转 dp
   *
   * @param px px值
   *
   * @return dp
   */
  public static int px2dp(float px) {
    return (int) (px / getDensity() + ROUND_CEIL);
  }

  /**
   * SP 转 px
   *
   * @param sp sp 字体大小
   *
   * @return pixels
   */
  public static float sp2px(float sp) {
    return sp * getDensity();
  }

  /**
   * 获取状态栏高度
   *
   * @return 状态栏高度
   */
  public static int getStatusBarHeight() {
    // 如果之前计算过，直接使用上次的计算结果
    if (statusBarHeight == -1) {
      final int defaultHeightInDp = 19;// statusBar默认19dp的高度
      statusBarHeight = DisplayUtils.dp2px(defaultHeightInDp);
      try {
        Class<?> c = Class.forName("com.android.internal.R$dimen");
        Object obj = c.newInstance();
        java.lang.reflect.Field field = c.getField("status_bar_height");
        statusBarHeight = mResources.getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return statusBarHeight;
  }
}
