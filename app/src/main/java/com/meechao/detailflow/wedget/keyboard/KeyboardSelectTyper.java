package com.meechao.detailflow.wedget.keyboard;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.meechao.detailflow.wedget.keyboard.KeyboardSelectTyper.def;
import static com.meechao.detailflow.wedget.keyboard.KeyboardSelectTyper.emoji;
import static com.meechao.detailflow.wedget.keyboard.KeyboardSelectTyper.input;
import static com.meechao.detailflow.wedget.keyboard.KeyboardSelectTyper.living;
import static com.meechao.detailflow.wedget.keyboard.KeyboardSelectTyper.send;
import static com.meechao.detailflow.wedget.keyboard.KeyboardSelectTyper.topic;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-16 09:51
 * Mail：jihaifeng@meechao.com
 */
@IntDef({emoji,topic,living,send,input,def})
@Retention(RetentionPolicy.SOURCE)
public @interface KeyboardSelectTyper {
  int emoji = 0xf0f0;
  int topic = 0xf0f1;
  int living = 0xf0f2;
  int send = 0xf0f3;
  int input = 0xf0f4;
  int def = 0xf0ff;
}
