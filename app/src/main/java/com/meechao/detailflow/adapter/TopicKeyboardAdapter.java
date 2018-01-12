package com.meechao.detailflow.adapter;

import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.meechao.detailflow.R;
import com.meechao.detailflow.entity.TopicBean;
import java.util.List;

/**
 * Func：
 * Desc: 标签键盘适配器
 * Author：JHF
 * Date：2017-12-27 10:24
 * Mail：jihaifeng@meechao.com
 */
public class TopicKeyboardAdapter extends BaseQuickAdapter<TopicBean, BaseViewHolder> {
  private static final String TAG = TopicKeyboardAdapter.class.getSimpleName().trim();

  public TopicKeyboardAdapter(List<TopicBean> list) {
    super(R.layout.item_topic_keyboard, list);
  }

  @Override protected void convert(BaseViewHolder helper, TopicBean item) {
    helper.setText(R.id.tv_tag, item.labelTopicValue);
    helper.itemView.setOnClickListener(v -> {
      Toast.makeText(mContext, item.labelTopicValue, Toast.LENGTH_SHORT).show();
    });
  }
}
