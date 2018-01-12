package com.meechao.detailflow.wedget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Func：
 * Desc: 水平无限滑动的文字跑马灯效果
 * Author：JHF
 * Date：2018-01-11 15:36
 * Mail：jihaifeng@meechao.com
 */
public class HorizontalMarqueeTextView extends AppCompatTextView {

  public HorizontalMarqueeTextView(Context context) {
    this(context,null);
  }

  public HorizontalMarqueeTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initMarqueeConfig();
  }

  private void initMarqueeConfig() {
    // 设置单行显示
    this.setSingleLine();
    // 设置水平滚动效果
    this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    // 设置水平滚动效果
    this.setHorizontallyScrolling(true);
    // 设置滚动次数，-1 表示无限次数
    this.setMarqueeRepeatLimit(-1);
  }

  @Override public boolean isFocused() {
    return true;
  }
}