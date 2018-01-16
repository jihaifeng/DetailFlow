package com.meechao.detailflow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.meechao.detailflow.R;
import com.meechao.detailflow.entity.LivingLabelBean;
import com.meechao.detailflow.richText.TEditText;
import com.meechao.detailflow.utils.AssetUtils;
import com.meechao.detailflow.wedget.keyboard.KeyBoardChildClickListener;
import com.meechao.detailflow.wedget.keyboard.KeyboardLayout;

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
  @Bind (R.id.rich_keyboard) KeyboardLayout richKeyboard;
  @Bind (R.id.rcv_list) RecyclerView rcvList;
  @Bind (R.id.edit_input) TEditText editInput;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    richKeyboard.setKeyBoardManager(this, rcvList).setTopicBg(new int[] {
        R.drawable.keyboard_question_normal, R.drawable.keyboard_normal
    }).resetRb();
    //richKeyboard.setCollectionTopics(AssetUtils.getCollectionTopic());
    richKeyboard.setTopics(AssetUtils.getArticleTopic());
    richKeyboard.setOnChildClick(new KeyBoardChildClickListener() {
      @Override public void onTopicItemClick(String topicStr) {
        Toast.makeText(MainActivity.this, topicStr, Toast.LENGTH_SHORT).show();
      }

      @Override public void onEmojiItemClick(String emojiStr) {
        Toast.makeText(MainActivity.this, emojiStr, Toast.LENGTH_SHORT).show();

      }

      @Override public void onLivingItemClick(LivingLabelBean livingLabelBean) {
        Toast.makeText(MainActivity.this, livingLabelBean.labelValue, Toast.LENGTH_SHORT).show();
      }

      @Override public void onSend(String content) {
        Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
      }

      @Override public void onBtnStateChanged(RadioButton rbEmojiBtn, RadioButton rbTopicBtn, RadioButton rbLivingBtn) {
        Toast.makeText(MainActivity.this, "aa", Toast.LENGTH_SHORT).show();
      }
    });
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
        //to(FaceLayoutDemoActivity.class);
        break;
      case R.id.btn_emoji:
        //to(EmojiLayoutDemoActivity.class);
        break;
    }
  }

  private void to(Class cls) {
    startActivity(new Intent(this, cls));
  }
}
