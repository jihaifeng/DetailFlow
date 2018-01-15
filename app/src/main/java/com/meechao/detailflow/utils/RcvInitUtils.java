package com.meechao.detailflow.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meechao.detailflow.layoutManeger.GridLayoutManagerPlus;
import com.meechao.detailflow.layoutManeger.flowLayout.FlowLayoutItemDecoration;
import com.meechao.detailflow.layoutManeger.flowLayout.FlowLayoutManager;

/**
 * author : admin
 * time   : 2017/11/13
 * desc   : RecyclerView初始化工具类
 */
public class RcvInitUtils {

  // 初始化一般瀑布流RecyclerView
  public static void initStaggeredGridRcv(RecyclerView mRcv, int spanCount, BaseQuickAdapter mAdapter) {

    mRcv.setHasFixedSize(true);
    mRcv.setNestedScrollingEnabled(false);
    mRcv.setOverScrollMode(View.OVER_SCROLL_NEVER);
    StaggeredGridLayoutManager layoutManager =
        new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
    layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
    mRcv.setLayoutManager(layoutManager);
    mRcv.setAdapter(mAdapter);
  }

  // 初始化一般网格RecyclerView
  public static void initGridRcv(Context context, RecyclerView mRcv, int spanCount, BaseQuickAdapter mAdapter) {
    mRcv.setHasFixedSize(true);
    mRcv.setNestedScrollingEnabled(false);
    mRcv.setOverScrollMode(View.OVER_SCROLL_NEVER);
    mRcv.setLayoutManager(new GridLayoutManagerPlus(context, spanCount));
    mRcv.setAdapter(mAdapter);
  }

  // 初始化标签键盘流式布局
  public static void initTagKeyboardFlowRcv(Context context, RecyclerView mRcv, BaseQuickAdapter mAdapter,
      FlowLayoutManager flowLayoutManager) {
    mRcv.setHasFixedSize(true);
    mRcv.setNestedScrollingEnabled(false);
    mRcv.setOverScrollMode(View.OVER_SCROLL_NEVER);
    mRcv.addItemDecoration(new FlowLayoutItemDecoration(ScreenUtil.dip2px(10)));
    mRcv.setLayoutManager(flowLayoutManager);
    mRcv.setAdapter(mAdapter);
  }

  // 初始化一般横向RecyclerView
  public static void initHorizontalRcv(Context context, RecyclerView mRcv, RecyclerView.Adapter mAdapter) {
    mRcv.setHasFixedSize(true);
    mRcv.setNestedScrollingEnabled(false);
    mRcv.setOverScrollMode(View.OVER_SCROLL_NEVER);
    mRcv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    mRcv.setAdapter(mAdapter);
  }
}
