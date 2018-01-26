package com.meechao.detailflow.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.meechao.detailflow.R;
import com.meechao.detailflow.utils.LogUtils;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-23 13:09
 * Mail：jihaifeng@meechao.com
 */
public class RcvCollectionTopicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
  private static final String TAG = RcvCollectionTopicAdapter.class.getSimpleName().trim();

  public RcvCollectionTopicAdapter(List<String> list) {
    super(R.layout.item_collection_topics,list);
  }

  @Override protected void convert(BaseViewHolder helper, String item) {

    helper.setText(R.id.tv_topic_val, item);
    LogUtils.i(TAG, "convert: " + item);
  }
}
