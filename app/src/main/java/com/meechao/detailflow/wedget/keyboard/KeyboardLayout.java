package com.meechao.detailflow.wedget.keyboard;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meechao.detailflow.R;
import com.meechao.detailflow.adapter.CollectionClassKeyboardAdapter;
import com.meechao.detailflow.adapter.EmojiKeyboardAdapter;
import com.meechao.detailflow.adapter.keyBoardVpAdapter;
import com.meechao.detailflow.entity.CollectionTopic;
import com.meechao.detailflow.entity.TopicBean;
import com.meechao.detailflow.richText.TEditText;
import com.meechao.detailflow.utils.AssetUtils;
import com.meechao.detailflow.utils.InputMethodUtils;
import com.meechao.detailflow.utils.KeyboardChangeListener;
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
 * Date：2018-01-15 17:01
 * Mail：jihaifeng@meechao.com
 */
public class KeyboardLayout extends LinearLayout {
  // 键盘切换按钮的跟布局
  @Bind (R.id.rg_keyboard) RadioGroup rgKeyboard;
  // 表情键盘切换按钮
  @Bind (R.id.rb_keybord_emoji) RadioButton rbKeybordEmoji;
  // 话题键盘切换按钮
  @Bind (R.id.rb_keybord_topic_tag) RadioButton rbKeybordTopicTag;
  // 输入框
  @Bind (R.id.keyboard_rich_input) TEditText keyboardRichInput;
  // 发送按钮
  @Bind (R.id.rb_send) RadioButton rbSend;
  // 显示输入框文字长度的TextView
  @Bind (R.id.text_length) TextView textLength;
  // 键盘的头部根布局
  @Bind (R.id.ll_keyboard_view_title) LinearLayout llKeyboardViewTitle;
  // 自适应内容居中的ViewPager
  @Bind (R.id.emoji_viewpager) AutoCenterViewPager emojiViewpager;
  // emoji表情键盘的圆点指示器
  @Bind (R.id.ll_emoji_dot) LinearLayout llEmojiDot;
  // emoji表情键盘的根布局
  @Bind (R.id.ll_keyboard_emoji) LinearLayout llKeyboardEmoji;
  // 话题标签显示容器，支持分页
  @Bind (R.id.pfv_topic) PageFlowView pfvTopic;
  // 话题标签分类列表
  @Bind (R.id.rcv_topic_class) RecyclerView rcvTopicClass;
  // 话题标签根布局
  @Bind (R.id.ll_keyboard_topic) LinearLayout llKeyboardTopic;

  /*Emoji表情*/
  private int pageCount;
  private List<View> viewPagerList;
  private int curIndex = 0;

  // 话题标签
  private List<TopicBean> topicDatas;
  // 合集里面的话题标签，包括分类
  private List<CollectionTopic> collectionTopics;

  private CollectionClassKeyboardAdapter collectionClassKeyboardAdapter;

  private SmartKeyboardManager mSmartKeyboardManager;

  private KeyBoardChildClickListener onChildClickListener;

  private Activity mActivity;
  private EditText et;

  private int[] topicbg = new int[] { R.drawable.keyboard_topic_normal, R.drawable.keyboard_normal };
  private int[] emojibg = new int[] { R.drawable.keyboard_expression_normal, R.drawable.keyboard_normal };

  public KeyboardLayout(Context context) {
    this(context, null);
  }

  public KeyboardLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initializeConfig();
    initView();
    initEmojiKeyboard();
    initCollectionKeyboard();
  }

  private void initializeConfig() {
    LayoutParams layoutParams = (LayoutParams) this.getLayoutParams();
    if (null == layoutParams) {
      layoutParams = new LayoutParams(getMeasuredWidth(), LayoutParams.WRAP_CONTENT);
    } else {
      layoutParams.width = getMeasuredWidth();
      layoutParams.height = LayoutParams.WRAP_CONTENT;
    }
    this.setLayoutParams(layoutParams);
    this.setBackgroundColor(getResources().getColor(R.color.color_f5f5f7));
  }

  private void initView() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.view_rich_keyboard, null, false);
    ButterKnife.bind(this, view);
    this.addView(view);
    llKeyboardEmoji.setVisibility(GONE);
    llKeyboardTopic.setVisibility(GONE);

    et = keyboardRichInput;
    rbKeybordEmoji.setOnClickListener(viewClickListener);
    rbKeybordTopicTag.setOnClickListener(viewClickListener);
    keyboardRichInput.setOnClickListener(viewClickListener);
    rbSend.setOnClickListener(viewClickListener);
  }

  View.OnClickListener viewClickListener = new OnClickListener() {
    @Override public void onClick(View view) {
      view.setSelected(!view.isSelected());
      switch (view.getId()) {
        case R.id.rb_keybord_emoji:
          rbKeybordEmoji.setBackgroundResource(!rbKeybordEmoji.isSelected() ? emojibg[0] : emojibg[1]);
          rbKeybordTopicTag.setBackgroundResource(topicbg[0]);
          break;
        case R.id.rb_keybord_topic_tag:
          rbKeybordTopicTag.setBackgroundResource(!rbKeybordTopicTag.isSelected() ? topicbg[0] : topicbg[1]);
          rbKeybordEmoji.setBackgroundResource(emojibg[0]);
          break;
        case R.id.rb_send:
          if (null != onChildClickListener) {
            onChildClickListener.onSend(et.getText().toString());

          }
          hideAll();
          break;
        default:
          resetRb();
          break;
      }
    }
  };

  private void hideAll() {
    InputMethodUtils.hideKeyboard(null != mActivity ? mActivity.getCurrentFocus() : llKeyboardViewTitle);
    llKeyboardTopic.setVisibility(GONE);
    llKeyboardEmoji.setVisibility(GONE);
    resetRb();
  }

  /**
   * 初始化emoji表情键盘
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
      View view = LayoutInflater.from(getContext()).inflate(R.layout.keyboard_view, null, false);
      RecyclerView recyclerView = view.findViewById(R.id.rcv_keyboard);
      EmojiKeyboardAdapter emojiKeyboardAdapter = new EmojiKeyboardAdapter(emojiData, i, rows * columns);
      RcvInitUtils.initGridRcv(getContext(), recyclerView, columns, emojiKeyboardAdapter);
      emojiKeyboardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
        @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
          if (null != onChildClickListener) {
            onChildClickListener.onEmojiClick((String) adapter.getItem(position));
          }
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
      llDot.addView(LayoutInflater.from(getContext()).inflate(R.layout.keyboard_dot, null));
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
  private void initCollectionKeyboard() {
    if (null == collectionTopics) {
      return;
    }
    collectionClassKeyboardAdapter = new CollectionClassKeyboardAdapter(collectionTopics);
    RcvInitUtils.initHorizontalRcv(getContext(), rcvTopicClass, collectionClassKeyboardAdapter);
    collectionClassKeyboardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        collectionClassKeyboardAdapter.setItemSelect(position);
        CollectionTopic collectionTopic = (CollectionTopic) adapter.getItem(position);
        if (null != collectionTopic && null != collectionTopic.details) {
          updateTopicKeyboard(collectionTopic.details);
        }
      }
    });
    updateTopicKeyboard(collectionClassKeyboardAdapter.getData().get(0).details);
  }

  /**
   * 话题标签内容
   *
   * @param details 标签数据
   */
  private void updateTopicKeyboard(List<TopicBean> details) {
    if (null == details) {
      return;
    }
    List<String> topicList = new ArrayList<>();
    for (TopicBean t : details) {
      topicList.add(t.labelTopicValue);
    }
    pfvTopic.setData(topicList);
    pfvTopic.setOnTagClickListener(new PageScrollView.OnTagClickListener() {
      @Override public void onClick(View view, String obj) {
        if (null != onChildClickListener) {
          onChildClickListener.onTopicClick(obj);
        }
      }
    });
  }

  public void setKeyBoardManager(Activity activity, RecyclerView recyclerView) {
    this.setKeyBoardManager(activity, recyclerView, null);
  }

  public void setKeyBoardManager(Activity activity, RecyclerView recyclerView, TEditText editText) {
    mActivity = activity;
    et = null == editText ? keyboardRichInput : editText;
    mSmartKeyboardManager = new SmartKeyboardManager.Builder(activity).setContentView(recyclerView)
        .addKeyboard(rbKeybordEmoji, llKeyboardEmoji)
        .addKeyboard(rbKeybordTopicTag, llKeyboardTopic)
        .setEditText(et)
        .addOnContentViewScrollListener(new OnContentViewScrollListener() {
          @Override public void needScroll(int distance) {
            recyclerView.scrollBy(0, distance);
          }
        })
        .create();

     /*键盘监听*/
    new KeyboardChangeListener(activity).setKeyBoardListener((isShow, keyboardHeight) -> {
      if (isShow) {
        resetRb();
      } else {
        if (!isEmojiShowing() && !isTopicShowing()) {
          resetRb();
        }
      }
    });
  }

  private boolean isTopicShowing() {
    return llKeyboardTopic.getVisibility() == VISIBLE;
  }

  private boolean isEmojiShowing() {
    return llKeyboardEmoji.getVisibility() == VISIBLE;
  }

  private void resetRb() {
    rbKeybordTopicTag.setBackgroundResource(topicbg[0]);
    rbKeybordEmoji.setBackgroundResource(emojibg[0]);
  }

  public void setOnChildClick(KeyBoardChildClickListener childClickListener) {
    this.onChildClickListener = childClickListener;
  }

  public void setCollectionTopics(List<CollectionTopic> collectionTopics) {
    this.collectionTopics = collectionTopics;
    collectionClassKeyboardAdapter.setNewData(collectionTopics);
    collectionClassKeyboardAdapter.notifyDataSetChanged();
  }

  public void setTopics(List<TopicBean> details) {
    updateTopicKeyboard(details);
  }
}
