package com.meechao.detailflow.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.meechao.detailflow.R;
import com.meechao.detailflow.entity.CollectionTopic;
import com.meechao.detailflow.utils.LogUtils;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-10 13:35
 * Mail：jihaifeng@meechao.com
 */
public class CollectionClassKeyboardAdapter extends BaseQuickAdapter<CollectionTopic, BaseViewHolder> {

  private int selectPos = 0;

  public CollectionClassKeyboardAdapter(List<CollectionTopic> collectionTopics) {
    super(R.layout.item_collection_keyboard_class, collectionTopics);
  }

  @Override protected void convert(BaseViewHolder helper, CollectionTopic item) {
    LogUtils.i(TAG, "convert: " + item);
    if (helper.getLayoutPosition() == selectPos) {
      helper.setBackgroundColor(R.id.tv_class_name, mContext.getResources().getColor(R.color.color_f5f5f5));
    } else {
      helper.setBackgroundColor(R.id.tv_class_name, mContext.getResources().getColor(R.color.white));
    }
    helper.setText(R.id.tv_class_name, item.className);
    helper.setGone(R.id.view_line, helper.getLayoutPosition() != getData().size() - 1);
  }

  public void setItemSelect(int position) {
    selectPos = position;
    notifyDataSetChanged();
  }
}
