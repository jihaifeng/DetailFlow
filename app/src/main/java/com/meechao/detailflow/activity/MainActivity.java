package com.meechao.detailflow.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.meechao.detailflow.R;
import com.meechao.detailflow.entity.LivingLabelBean;
import com.meechao.detailflow.richText.TEditText;
import com.meechao.detailflow.utils.AssetUtils;
import com.meechao.detailflow.wedget.WheelSelectDialog;
import com.meechao.detailflow.wedget.keyboard.KeyBoardChildClickListener;
import com.meechao.detailflow.wedget.keyboard.KeyboardLayout;
import java.util.Arrays;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-09 13:53
 * Mail：jihaifeng@meechao.com
 */
public class MainActivity extends BaseActivity {
  @Bind (R.id.btn_list) Button btnList;
  @Bind (R.id.btn_topic) Button btnTopic;
  @Bind (R.id.btn_keyboard) Button btnKeyboard;
  @Bind (R.id.btn_face_text) Button btnFaceText;
  @Bind (R.id.btn_emoji) Button btnEmoji;
  @Bind (R.id.rcv_list) ScrollView rcvList;
  @Bind (R.id.edit_input) TEditText editInput;
  @Bind (R.id.rich_keyboard) KeyboardLayout richKeyboard;

  //@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
  //  super.onCreate(savedInstanceState);
  //  setContentView(R.layout.activity_main);
  //
  //}
  String[] PLANETS = new String[] {
      "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto", "Venus", "Earth", "Mars", "Jupiter",
      "Uranus", "Neptune", "Pluto", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto", "Venus", "Earth",
      "Mars", "Jupiter", "Uranus", "Neptune", "Pluto", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"
  };
  private WheelSelectDialog wheelSelectDialog;

  @Override protected int getLayoutId() {
    return R.layout.activity_main;
  }

  @Override protected void initViewAndEvent() {
    richKeyboard.setKeyBoardManager(this, rcvList, editInput).setTopicBg(new int[] {
        R.drawable.keyboard_question_normal, R.drawable.keyboard_normal
    }).hideInput().hideSend().resetRb();
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

      @Override public void hideAllKeyboard() {
        richKeyboard.setVisibility(View.GONE);
      }

      @Override public void showKeyboard() {
        richKeyboard.setVisibility(View.VISIBLE);
      }
    });

    wheelSelectDialog = new WheelSelectDialog(this);
    wheelSelectDialog.setOnWheelListener(new WheelSelectDialog.onWheelListener() {
      @Override public void select(int index, String text) {
        Toast.makeText(MainActivity.this, text + "  index: " + index, Toast.LENGTH_SHORT).show();
      }

      @Override public void cancel() {

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
        to(RecyclerViewActivity.class);
        break;
      case R.id.btn_emoji:
        wheelSelectDialog.show();
        wheelSelectDialog.setData(Arrays.asList(PLANETS));
        wheelSelectDialog.setTitle("啊啊啊啊啊啊");
        break;
    }
  }

  private void to(Class cls) {
    startActivity(new Intent(this, cls));
  }
}
