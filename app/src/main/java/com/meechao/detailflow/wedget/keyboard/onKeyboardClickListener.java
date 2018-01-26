package com.meechao.detailflow.wedget.keyboard;

import android.widget.RadioButton;
import com.meechao.detailflow.entity.LivingLabelBean;

/**
 * Func：
 * Desc: 点击事件接口实现的抽象类，避免每次都实现所有方法
 * Author：JHF
 * Date：2018-01-17 09:13
 * Mail：jihaifeng@meechao.com
 */
public abstract   class onKeyboardClickListener implements KeyBoardChildClickListener{

  @Override public void onTopicItemClick(String topicStr) {

  }

  @Override public void onEmojiItemClick(String emojiStr) {

  }

  @Override public void onLivingItemClick(LivingLabelBean livingLabelBean) {

  }

  @Override public void onSend(String content) {

  }

  @Override public void onBtnStateChanged(RadioButton rbEmojiBtn, RadioButton rbTopicBtn, RadioButton rbLivingBtn) {

  }

  @Override public void hideAllKeyboard() {

  }

  @Override public void showKeyboard() {

  }
}
