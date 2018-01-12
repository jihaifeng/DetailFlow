package com.meechao.detailflow.wedget.viewPager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Func：自适应高度的ViewPagerAdapter，配合{@link AutoHeightViewPager}使用
 * Desc:
 * Author：JHF
 * Date：2017-11-02-0002 17:56
 * Mail：jihaifeng@meechao.com
 */
public class AutoHeightViewPagerAdapter extends PagerAdapter {
  private List<View> views;

  public AutoHeightViewPagerAdapter(List<View> views) {
    this.views = views;
  }

  @Override public int getCount() {
    return views.size();
  }

  @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  @Override public int getItemPosition(Object object) {//TODO 这是重点继续研究
    //return super.getItemPosition(object);//默认是不改变
    return POSITION_NONE;//可以即时刷新看源码
  }

  /**
   * 以position作为id值设置给当前 itemView ，解决配合{@link AutoHeightViewPager} 使用时出现当ViewPager滑动的position大于3时
   * AutoHeightViewPager中getChildAt(getCurrentItem())返回为空的bug,同时AutoHeightViewPager改为findViewById(getCurrentItem());
   */

  @NonNull @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View itemView = views.get(position);
    itemView.setId(position);
    container.addView(itemView);
    return itemView;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    ((ViewPager) container).removeView((View) object);
  }

  public void remove(int currentItem) {
    views.remove(currentItem);
    notifyDataSetChanged();
  }
}
