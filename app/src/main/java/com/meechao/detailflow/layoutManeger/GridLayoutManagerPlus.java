package com.meechao.detailflow.layoutManeger;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Func：
 * Desc:
 * Author：jihf
 * Data：2017-02-28 11:22
 * Mail：jihaifeng@meechao.com
 */
public class GridLayoutManagerPlus extends GridLayoutManager {

  // 总页数
  private int totalPages;

  public GridLayoutManagerPlus(Context context, int spanCount) {
    super(context, spanCount);
  }

  @Override
  public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    try {
      super.onLayoutChildren(recycler, state);
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }

  public GridLayoutManagerPlus(Context context, int spanCount, int orientation, boolean reverseLayout) {
    super(context, spanCount, orientation, reverseLayout);
  }

  private int[] mMeasuredDimension = new int[2];

  @Override
  public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
    final int widthMode = View.MeasureSpec.getMode(widthSpec);
    final int heightMode = View.MeasureSpec.getMode(heightSpec);
    final int widthSize = View.MeasureSpec.getSize(widthSpec);
    final int heightSize = View.MeasureSpec.getSize(heightSpec);

    int width = 0;
    int height = 0;
    int count = getItemCount();

    for (int i = 0; i < count; i++) {
      int span = getSpanCount() / getSpanSizeLookup().getSpanSize(i);
      measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
          View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), mMeasuredDimension);

      if (getOrientation() == HORIZONTAL) {
        if (i % span == 0) {
          width = width + mMeasuredDimension[0];
        }
        if (i == 0) {
          height = mMeasuredDimension[1];
        }
      } else {
        if (i % span == 0) {
          height = height + mMeasuredDimension[1];
        }
        if (i == 0) {
          width = mMeasuredDimension[0];
        }
      }
    }

    switch (widthMode) {
      case View.MeasureSpec.EXACTLY:
        width = widthSize;
      case View.MeasureSpec.AT_MOST:
      case View.MeasureSpec.UNSPECIFIED:
    }

    switch (heightMode) {
      case View.MeasureSpec.EXACTLY:
        height = heightSize;
      case View.MeasureSpec.AT_MOST:
      case View.MeasureSpec.UNSPECIFIED:
    }

    setMeasuredDimension(width, height);
  }

  private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec,
      int[] measuredDimension) {
    if (position < getItemCount()) {
      try {
        View view = recycler.getViewForPosition(position);//fix 动态添加时报IndexOutOfBoundsException
        if (view != null) {
          RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
          int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), p.width);
          int childHeightSpec =
              ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), p.height);
          view.measure(childWidthSpec, childHeightSpec);
          measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
          measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
          recycler.recycleView(view);
        }
      } catch (Exception e) {
        //e.printStackTrace();
      }
    }
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  @Override
  public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
    //如果页面总数大于 1 则可以滑动
    if (totalPages > 1) {
      return super.scrollHorizontallyBy(dx, recycler, state);
    } else {
      return 0;
    }
  }
}