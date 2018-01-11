package la.juju.android.ftil.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-11 10:40
 * Mail：jihaifeng@meechao.com
 */
public class TextPagerAdapter extends PagerAdapter {

  private List<View> viewList;

  public TextPagerAdapter() {

  }

  public TextPagerAdapter(List<View> recyclerViewList) {
    viewList = recyclerViewList;
  }

  @Override public int getCount() {
    return viewList.size();
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    container.addView(viewList.get(position));
    return viewList.get(position);
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView(viewList.get(position));
  }

  public void setFaceTextInputPageList(List<View> faceTextInputPageList) {
    viewList = faceTextInputPageList;
  }
}
