package com.meechao.detailflow.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meechao.detailflow.R;
import com.meechao.detailflow.entity.TopicBean;
import com.meechao.detailflow.utils.LogUtils;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-10 13:35
 * Mail：jihaifeng@meechao.com
 */
public class CollectionTopicKeyboardAdapter extends BaseKeyBoardAdapter<TopicBean, BaseViewHolder> {

  public CollectionTopicKeyboardAdapter(List<TopicBean> collectionTopics, int curIndex, int pageSize) {
    super(R.layout.item_collection_keyboard_topic, collectionTopics, curIndex, pageSize);
  }

  @Override protected void convert(BaseViewHolder helper, TopicBean item) {
    LogUtils.i(TAG, "convert: " + item);
    helper.setText(R.id.tv_tag, item.labelTopicValue);
  }
}
