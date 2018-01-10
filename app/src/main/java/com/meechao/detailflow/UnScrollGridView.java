package com.meechao.detailflow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Func：
 * Desc: 禁止滑动的gridView
 * Author：JHF
 * Date：2018-01-08 13:26
 * Mail：jihaifeng@meechao.com
 */
public class UnScrollGridView extends GridView {

    public UnScrollGridView(Context context) {
        super(context);
    }

    public UnScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
