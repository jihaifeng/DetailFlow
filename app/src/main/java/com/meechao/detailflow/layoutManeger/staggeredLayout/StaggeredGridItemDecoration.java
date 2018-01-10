package com.meechao.detailflow.layoutManeger.staggeredLayout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-11-07-0007 9:58
 * Mail：jihaifeng@meechao.com
 */
public class StaggeredGridItemDecoration extends RecyclerView.ItemDecoration {
  private int space = 0;

  public StaggeredGridItemDecoration(int space) {
    this.space = space;
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

    outRect.left = space;
    outRect.right = space;
    outRect.bottom = space;
    //if (parent.getChildAdapterPosition(view) == 0) {
    outRect.top = space;
    //}
  }
}