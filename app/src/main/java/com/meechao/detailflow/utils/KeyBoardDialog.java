package com.meechao.detailflow.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.meechao.detailflow.R;
import com.meechao.detailflow.richText.TEditText;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-08 14:42
 * Mail：jihaifeng@meechao.com
 */
public class KeyBoardDialog extends AlertDialog {

  // 键盘切换按钮的根布局
  @Bind (R.id.rg_keyboard) RadioGroup rgKeyboard;
  // emoji表情键盘切换按钮
  @Bind (R.id.rb_keybord_emoji) RadioButton rbKeybordEmoji;
  // emoji表情键盘的视图
  @Bind (R.id.keyboard_emoji_rcv) RecyclerView keyboardEmojiRcv;
  // 话题键盘切换按钮
  @Bind (R.id.rb_keybord_topic_tag) RadioButton rbKeybordTopicTag;
  // 发送按钮
  @Bind (R.id.rb_send) RadioButton rbSend;
  // 键盘输入框
  @Bind (R.id.keyboard_rich_input) TEditText keyboardRichInput;

  // 话题键盘视图
  @Bind (R.id.text_length) TextView textLength;
  @Bind (R.id.view_pager) ViewPager viewPager;
  @Bind (R.id.indicator) LinearLayout indicator;
  @Bind (R.id.keyboard_tag_rcv) RecyclerView keyboardTagRcv;
  @Bind (R.id.fill_content) LinearLayout fillContent;
  @Bind (R.id.rl_tag_keyboard_root) RelativeLayout rlTagKeyboardRoot;

  protected KeyBoardDialog(Context context) {
    // 定制dialog主题
    super(context, R.style.keyboard_dialog_theme);
    // 设置dialog内容
    setContentView(R.layout.view_keyboard_list);
  }
}
