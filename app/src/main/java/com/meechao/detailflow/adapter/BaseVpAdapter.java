package com.meechao.detailflow.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-12 16:24
 * Mail：jihaifeng@meechao.com
 */
public abstract class BaseVpAdapter extends PagerAdapter {
  private List<View> viewList;

  public BaseVpAdapter(List<View> viewList) {
    this.viewList = viewList;
  }

  @Override
  public boolean isViewFromObject(View arg0, Object arg1) {
    return arg0 == arg1;
  }

  @Override
  public int getCount() {
    return viewList.size();
  }

  @Override
  public void destroyItem(ViewGroup container, int position,
      Object object) {
    container.removeView(viewList.get(position));
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    container.addView(viewList.get(position));
    return viewList.get(position);
  }

  public List<View> getViewList() {
    return viewList;
  }
}
