package com.meechao.detailflow.wedget.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Func：自动适应高度的ViewPager，配合{@link AutoHeightViewPagerAdapter}使用
 * Desc: 在 viewPagerAdapter 的 instantiateItem 方法中改成如 AutoHeightViewPagerAdapter 写法
 * Author：JHF
 * Date：2017-11-29-0029 13:55
 * Mail：jihaifeng@meechao.com
 */
public class AutoHeightViewPager extends ViewPager {
  private static final String TAG = "AutofitViewPager";

  public AutoHeightViewPager(Context context) {
    this(context, null);
  }

  public AutoHeightViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    addOnPageChangeListener(new OnPageChangeListener() {
      @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override public void onPageSelected(int position) {
        requestLayout();
      }

      @Override public void onPageScrollStateChanged(int state) {
      }
    });
  }

  /**
   * getChildAt(getCurrentItem())改为findViewById(getCurrentItem())，解决配合{@link AutoHeightViewPagerAdapter}
   * 使用时出现当ViewPager滑动的position大于3时
   * AutoHeightViewPager中getChildAt(getCurrentItem())返回为空的bug;
   */
  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    // find the current child view
    //View view = getChildAt(getCurrentItem());
    View view = findViewById(getCurrentItem());
    if (view != null) {
      // measure the current child view with the specified measure spec
      view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
  }

  /**
   * 高度测量
   */
  private int measureHeight(int measureSpec, View view) {
    int result = 0;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    if (specMode == MeasureSpec.EXACTLY) {
      result = specSize;
    } else {
      // set the height from the base view if available
      if (view != null) {
        result = view.getMeasuredHeight();
      }
      if (specMode == MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize);
      }
    }
    return result;
  }
}
