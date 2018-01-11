package com.meechao.detailflow.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meechao.detailflow.R;
import com.meechao.detailflow.utils.LogUtils;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-10 13:35
 * Mail：jihaifeng@meechao.com
 */
public class EmojiKeyboardAdapter extends BaseKeyBoardAdapter<String, BaseViewHolder> {

  private List<String> emojiDatas;

  public EmojiKeyboardAdapter(List<String> emojiData, int curIndex, int pageSize) {
    super(R.layout.item_emoji_keyboard,emojiData, curIndex, pageSize);
  }

  @Override protected void convert(BaseViewHolder helper, String item) {
    LogUtils.i(TAG, "convert: "+item);
    helper.setText(R.id.tv_emoji, item);
  }
}
