package com.meechao.detailflow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.meechao.detailflow.R;
import com.meechao.detailflow.utils.DisplayUtils;
import com.meechao.detailflow.utils.ScreenUtil;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-09 13:53
 * Mail：jihaifeng@meechao.com
 */
public class MainActivity extends AppCompatActivity {
  @Bind (R.id.btn_list) Button btnList;
  @Bind (R.id.btn_topic) Button btnTopic;
  @Bind (R.id.btn_keyboard) Button btnKeyboard;
  @Bind (R.id.btn_face_text) Button btnFaceText;
  @Bind (R.id.btn_emoji) Button btnEmoji;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    ScreenUtil.getInstance(this);
    DisplayUtils.init(this);
  }

  @OnClick ({ R.id.btn_list, R.id.btn_topic, R.id.btn_keyboard, R.id.btn_face_text, R.id.btn_emoji })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_list:
        to(ItemListActivity.class);
        break;
      case R.id.btn_topic:
        to(TopicActivity.class);
        break;
      case R.id.btn_keyboard:
        to(KeyBoardActivity.class);
        break;
      case R.id.btn_face_text:
        to(FaceLayoutDemoActivity.class);
        break;
      case R.id.btn_emoji:
        to(EmojiLayoutDemoActivity.class);
        break;
    }
  }

  private void to(Class cls) {
    startActivity(new Intent(this, cls));
  }
}
