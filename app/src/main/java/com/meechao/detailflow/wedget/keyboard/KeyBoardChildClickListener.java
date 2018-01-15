package com.meechao.detailflow.wedget.keyboard;

import android.widget.RadioButton;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-15 17:23
 * Mail：jihaifeng@meechao.com
 */
public interface KeyBoardChildClickListener {

  void onTopicClick(String topicStr);

  void onEmojiClick(String emojiStr);

  void onSend(String content);

  void onEmojiStateChanged(RadioButton rbEmojiBtn);

  void onTopicStateChanged(RadioButton rbTopicBtn);
}
