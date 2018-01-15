package com.meechao.detailflow.wedget.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-12 12:33
 * Mail：jihaifeng@meechao.com
 */
public class AutoCenterViewPager extends ViewPager {
  private static final String TAG = AutoCenterViewPager.class.getSimpleName().trim();
  private int maxHeight = 0;
  private int maxWidth = 0;
  private int paddingX = 0;
  private int paddingY = 0;

  public AutoCenterViewPager(Context context) {
    this(context, null);
  }

  public AutoCenterViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //下面遍历所有child的高度
    if (maxWidth <= 0 || maxHeight <= 0) {
      for (int i = 0; i < getChildCount(); i++) {
        View child = getChildAt(i);
        child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        //采用最大的view的宽度
        maxWidth = Math.max(child.getMeasuredWidth(), maxWidth);
        //采用最大的view的高度
        maxHeight = Math.max(child.getMeasuredHeight(), maxHeight);
      }
    }
    // 获取上下的padding
    if (maxWidth > 0) {
      paddingX = Math.max((getMeasuredWidth() - maxWidth) / 2, paddingX);
    }
    // 获取左右的padding
    if (maxHeight > 0) {
      paddingY = Math.max((getMeasuredHeight() - maxHeight) / 2, paddingY);
    }
    // 设置子View居中
    this.setPadding(paddingX, paddingY, paddingX, paddingY);
  }
}
