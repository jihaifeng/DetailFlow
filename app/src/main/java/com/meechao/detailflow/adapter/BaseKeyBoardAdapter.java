package com.meechao.detailflow.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-10 13:47
 * Mail：jihaifeng@meechao.com
 */
public abstract class BaseKeyBoardAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

  private List<T> list;
  /**
   * 页数下标,从0开始(当前是第几页)
   */
  private int curIndex;
  /**
   * 每一页显示的个数
   */
  private int pageSize;

  public BaseKeyBoardAdapter(int layoutResId, List<T> emojiData, int curIndex, int pageSize) {
    super(layoutResId,emojiData);
    this.list = emojiData;
    this.curIndex = curIndex;
    this.pageSize = pageSize;
  }

  @Override public int getItemCount() {
    if (null == list) {
      return 0;
    } else {
      return list.size() > (curIndex + 1) * pageSize ? pageSize : (list.size() - curIndex * pageSize);
    }
  }

  @Nullable @Override public T getItem(int position) {
    return list.get(position + curIndex * pageSize);
  }

  @Override public long getItemId(int position) {
    return position + curIndex * pageSize;
  }

}
