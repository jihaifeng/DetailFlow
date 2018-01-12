package com.meechao.detailflow.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.meechao.detailflow.R;
import com.meechao.detailflow.entity.LivingLabelBean;
import com.meechao.detailflow.utils.ImageUtils;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-12-27 10:17
 * Mail：jihaifeng@meechao.com
 */
public class LivingKeyboardAdapter extends BaseQuickAdapter<LivingLabelBean, BaseViewHolder> {

  private int itemWidth = 0;

  public LivingKeyboardAdapter() {
    super(R.layout.publish_tag_item);
  }

  @Override protected void convert(BaseViewHolder helper, LivingLabelBean item) {
    ImageUtils.load(item.labelImage, helper.getView(R.id.iv_publish_tag));
    helper.setText(R.id.tv_publish_tag, item.labelValue);
  }
  public void setItemWidth(int itemWidth) {
    this.itemWidth = itemWidth;
  }
}
