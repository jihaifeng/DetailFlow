package com.meechao.detailflow.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meechao.detailflow.R;
import com.meechao.detailflow.adapter.CollectionClassKeyboardAdapter;
import com.meechao.detailflow.adapter.EmojiKeyboardAdapter;
import com.meechao.detailflow.adapter.keyBoardVpAdapter;
import com.meechao.detailflow.entity.CollectionTopic;
import com.meechao.detailflow.entity.LivingLabelBean;
import com.meechao.detailflow.entity.TopicBean;
import com.meechao.detailflow.richText.TEditText;
import com.meechao.detailflow.utils.AssetUtils;
import com.meechao.detailflow.utils.RcvInitUtils;
import com.meechao.detailflow.wedget.flowView.PageFlowView;
import com.meechao.detailflow.wedget.flowView.PageScrollView;
import com.meechao.detailflow.wedget.keyboard.smartKeyboard.OnContentViewScrollListener;
import com.meechao.detailflow.wedget.keyboard.smartKeyboard.SmartKeyboardManager;
import com.meechao.detailflow.wedget.viewPager.AutoCenterViewPager;
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
  @Bind (R.id.rcv_list) RecyclerView rcvList;
  @Bind (R.id.rb_keybord_emoji) RadioButton rbKeybordEmoji;
  @Bind (R.id.keyboard_rich_input) TEditText keyboardRichInput;
  @Bind (R.id.rb_keybord_topic_tag) RadioButton rbKeybordTopicTag;
  @Bind (R.id.rb_send) RadioButton rbSend;
  @Bind (R.id.rg_keyboard) RadioGroup rgKeyboard;
  @Bind (R.id.text_length) TextView textLength;
  @Bind (R.id.ll_keyboard_view_title) LinearLayout llKeyboardViewTitle;

  // emoji键盘
  @Bind (R.id.emoji_viewpager) AutoCenterViewPager emojiViewpager;
  @Bind (R.id.ll_emoji_dot) LinearLayout llEmojiDot;
  @Bind (R.id.ll_keyboard_emoji) LinearLayout llKeyboardEmoji;

  // 合集话题键盘
  @Bind (R.id.pfv_topic) PageFlowView pageFlowView;
  @Bind (R.id.rcv_collection_topic_class) RecyclerView rcvCollectionTopicClass;
  @Bind (R.id.ll_keyboard_collection_topic) LinearLayout llKeyboardCollectionTopic;

  private List<LivingLabelBean> livingLabelDatas;
  private List<TopicBean> topicDatas;
  private List<CollectionTopic> collectionTopics;

  private int pageCount;
  private List<View> viewPagerList;
  /**
   * 当前显示的是第几页
   */
  private int curIndex = 0;

  private SmartKeyboardManager mSmartKeyboardManager;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.view_keyboard);
    ButterKnife.bind(this);
    initData();
    initEmojiKeyboard();
    initCollectionKeyboard(collectionTopics);

    mSmartKeyboardManager = new SmartKeyboardManager.Builder(this).setContentView(rcvList)
        .addKeyboard(rbKeybordEmoji, llKeyboardEmoji)
        .addKeyboard(rbKeybordTopicTag, llKeyboardCollectionTopic)
        .setEditText(keyboardRichInput)
        .addOnContentViewScrollListener(new OnContentViewScrollListener() {
          @Override public void needScroll(int distance) {
            rcvList.scrollBy(0, distance);
          }
        })
        .create();
  }

  private void initData() {

    topicDatas = AssetUtils.getArticleTopic();

    collectionTopics = AssetUtils.getCollectionTopic();
  }

  /**
   * emoji表情键盘
   */
  private void initEmojiKeyboard() {
    List<String> emojiData = AssetUtils.getEmojiData();
    int columns = 6;
    int rows = 4;
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
      emojiKeyboardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
        @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
          Toast.makeText(KeyBoardActivity.this, "adapter.getItem(position):" + adapter.getItem(position),
              Toast.LENGTH_SHORT).show();
        }
      });
      viewPagerList.add(view);
    }
    emojiViewpager.setAdapter(new keyBoardVpAdapter(viewPagerList));
    setOvalLayout(llEmojiDot);
  }

  /**
   * 设置圆点
   */
  public void setOvalLayout(LinearLayout llDot) {
    if (null == llDot) {
      return;
    }
    if (llDot.getChildCount() != 0) {
      llDot.removeAllViews();
    }
    for (int i = 0; i < pageCount; i++) {
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

  /**
   * 合集话题标签 底部分类
   */
  private void initCollectionKeyboard(List<CollectionTopic> collectionTopics) {
    if (null == collectionTopics) {
      return;
    }
    CollectionClassKeyboardAdapter collectionClassKeyboardAdapter =
        new CollectionClassKeyboardAdapter(collectionTopics);
    RcvInitUtils.initHorizontalRcv(this, rcvCollectionTopicClass, collectionClassKeyboardAdapter);
    collectionClassKeyboardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        collectionClassKeyboardAdapter.setItemSelect(position);
        CollectionTopic collectionTopic = (CollectionTopic) adapter.getItem(position);
        if (null != collectionTopic && null != collectionTopic.details) {
          upDateTopicKeyboard(collectionTopic.details);
        }
      }
    });
    upDateTopicKeyboard(collectionClassKeyboardAdapter.getData().get(0).details);
  }

  /**
   * 话题标签内容
   *
   * @param details 标签数据
   */
  private void upDateTopicKeyboard(List<TopicBean> details) {
    if (null == details) {
      return;
    }
    List<String> topicList = new ArrayList<>();
    for (TopicBean t : details) {
      topicList.add(t.labelTopicValue);
    }
    pageFlowView.setData(topicList);
    pageFlowView.setOnTagClickListener(new PageScrollView.OnTagClickListener() {
      @Override public void onClick(View view, String obj) {
        Toast.makeText(KeyBoardActivity.this, obj, Toast.LENGTH_SHORT).show();
      }
    });
  }
}
