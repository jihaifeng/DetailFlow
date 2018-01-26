package com.meechao.detailflow.wedget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-23 18:36
 * Mail：jihaifeng@meechao.com
 */
public class WarpRecyclerView extends RecyclerView {
  public WarpRecyclerView(Context context) {
    super(context);
  }

  public WarpRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onMeasure(int widthSpec, int heightSpec) {
    //super.onMeasure(widthSpec, heightSpec);
    int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, // 设计一个较大的值和AT_MOST模式
        MeasureSpec.AT_MOST);
    super.onMeasure(widthSpec, newHeightMeasureSpec);//再调用原方法测量
  }
}
