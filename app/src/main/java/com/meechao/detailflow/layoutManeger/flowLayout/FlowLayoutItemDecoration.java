package com.meechao.detailflow.layoutManeger.flowLayout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-11-24-0024 17:09
 * Mail：jihaifeng@meechao.com
 */
public class FlowLayoutItemDecoration extends RecyclerView.ItemDecoration {

  private int space;

  public FlowLayoutItemDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    outRect.top = space;
    outRect.left = space;
    outRect.right = space;
    outRect.bottom = space;
  }
}
