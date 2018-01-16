package com.meechao.detailflow.wedget.keyboard;

import android.widget.RadioButton;
import com.meechao.detailflow.entity.LivingLabelBean;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-15 17:23
 * Mail：jihaifeng@meechao.com
 */
public interface KeyBoardChildClickListener {

  void onTopicItemClick(String topicStr);

  void onEmojiItemClick(String emojiStr);

  void onLivingItemClick(LivingLabelBean livingLabelBean);

  void onSend(String content);

  void onBtnStateChanged(RadioButton rbEmojiBtn,RadioButton rbTopicBtn,RadioButton rbLivingBtn);
}
