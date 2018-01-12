package com.meechao.detailflow.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.meechao.detailflow.R;
import com.meechao.detailflow.adapter.EmojiKeyboardAdapter;
import com.meechao.detailflow.entity.LivingLabelBean;
import com.meechao.detailflow.entity.TopicBean;
import com.meechao.detailflow.richText.TEditText;
import com.meechao.detailflow.utils.AssetUtils;
import com.meechao.detailflow.utils.RcvInitUtils;
import com.meechao.detailflow.wedget.viewPager.AutoHeightViewPagerAdapter;
import com.meechao.smartkeyboard.OnContentViewScrollListener;
import com.meechao.smartkeyboard.SmartKeyboardManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-08 13:26
 * Mail：jihaifeng@meechao.com
 */
public class KeyBoardActivity extends AppCompatActivity {

  private static final String TAG = "EmojiKeyboard";

  @Bind (R.id.view_keyboard) View viewKeyboard;
  @Bind (R.id.rcv_list) RecyclerView rcvList;
  @Bind (R.id.rb_keybord_emoji) RadioButton rbKeybordEmoji;
  @Bind (R.id.keyboard_rich_input) TEditText keyboardRichInput;
  @Bind (R.id.rb_keybord_topic_tag) RadioButton rbKeybordTopicTag;
  @Bind (R.id.rb_send) RadioButton rbSend;
  @Bind (R.id.rg_keyboard) RadioGroup rgKeyboard;
  @Bind (R.id.text_length) TextView textLength;
  @Bind (R.id.emoji_viewpager) ViewPager emojiViewpager;
  @Bind (R.id.ll_dot) LinearLayout llDot;
  @Bind (R.id.ll_keyboard_emoji) LinearLayout llKeyboardEmoji;
  @Bind (R.id.tag_viewpager) ViewPager tagViewpager;
  @Bind (R.id.ll_keyboard_tag) LinearLayout llKeyboardTag;
  @Bind (R.id.topic_viewpager) ViewPager topicViewpager;
  @Bind (R.id.ll_keyboard_topic) LinearLayout llKeyboardTopic;

  private List<String> emojiData;
  private List<LivingLabelBean> livingLabelDatas;
  private List<TopicBean> topicDatas;


  private int pageCount;
  private List<View> viewPagerList;
  private int rows = 4, columns = 6;
  /**
   * 当前显示的是第几页
   */
  private int curIndex = 0;

  private SmartKeyboardManager mSmartKeyboardManager;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.view_keyboard);
    ButterKnife.bind(this);
    emojiData = AssetUtils.getEmojiData();
    if (emojiData != null) {
      // 总的页数
      pageCount = (int) Math.ceil(emojiData.size() * 1.0 / (rows * columns));
    }

    if (null == viewPagerList) {
      viewPagerList = new ArrayList<>();
    }

    if (viewPagerList.size() > 0) {
      viewPagerList.clear();
    }

    for (int i = 0; i < pageCount; i++) {
      View view = LayoutInflater.from(this).inflate(R.layout.keyboard_view, null, false);
      RecyclerView recyclerView = view.findViewById(R.id.rcv_keyboard);
      EmojiKeyboardAdapter emojiKeyboardAdapter = new EmojiKeyboardAdapter(emojiData, i, rows * columns);
      RcvInitUtils.initGridRcv(this, recyclerView, columns, emojiKeyboardAdapter);
      viewPagerList.add(view);
    }
    emojiViewpager.setAdapter(new AutoHeightViewPagerAdapter(viewPagerList));

    mSmartKeyboardManager = new SmartKeyboardManager.Builder(this).setContentView(rcvList)
        .addKeyboard(rbKeybordEmoji, llKeyboardEmoji)
        .addKeyboard(rbKeybordTopicTag, llKeyboardTag)
        .addKeyboard(rbKeybordTopicTag, llKeyboardTag)
        .setEditText(keyboardRichInput)
        .addOnContentViewScrollListener(new OnContentViewScrollListener() {
          @Override public void needScroll(int distance) {
            rcvList.scrollBy(0, distance);
          }
        })
        .create();

    livingLabelDatas = AssetUtils.getLivingTag();

    topicDatas = AssetUtils.getTopic();

  }

  /**
   * 设置圆点
   */
  public void setOvalLayout() {
    if (null == llDot) {
      return;
    }
    if (llDot.getChildCount() != 0) {
      llDot.removeAllViews();
    }
    for (int i = 0; i < pageCount; i++) {
      ImageView dotView = new ImageView(this);
      dotView.setBackgroundResource(R.drawable.shape_circle_indicator_gray);
      llDot.addView(LayoutInflater.from(this).inflate(R.layout.keyboard_dot, null));
    }
    // 默认显示第一页
    llDot.getChildAt(0).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.shape_circle_indicator_orange);
    emojiViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        // 取消圆点选中
        llDot.getChildAt(curIndex)
            .findViewById(R.id.v_dot)
            .setBackgroundResource(R.drawable.shape_circle_indicator_gray);
        // 圆点选中
        llDot.getChildAt(position)
            .findViewById(R.id.v_dot)
            .setBackgroundResource(R.drawable.shape_circle_indicator_orange);
        curIndex = position;
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }
}
