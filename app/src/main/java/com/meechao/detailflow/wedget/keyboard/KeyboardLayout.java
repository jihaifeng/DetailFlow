package com.meechao.detailflow.wedget.keyboard;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.meechao.detailflow.R;
import com.meechao.detailflow.adapter.CollectionClassKeyboardAdapter;
import com.meechao.detailflow.adapter.EmojiKeyboardAdapter;
import com.meechao.detailflow.adapter.keyBoardVpAdapter;
import com.meechao.detailflow.entity.CollectionTopic;
import com.meechao.detailflow.entity.TopicBean;
import com.meechao.detailflow.richText.TEditText;
import com.meechao.detailflow.utils.AssetUtils;
import com.meechao.detailflow.utils.InputMethodUtils;
import com.meechao.detailflow.utils.RcvInitUtils;
import com.meechao.detailflow.wedget.flowView.PageFlowView;
import com.meechao.detailflow.wedget.keyboard.keyboardManager.SmartKeyboardManager;
import com.meechao.detailflow.wedget.keyboard.listener.OnContentViewScrollListener;
import com.meechao.detailflow.wedget.keyboard.listener.OnKeyboardChildClickListener;
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
public class KeyboardLayout extends FrameLayout {

  /*****************************键盘头部*******************************/
  // 键盘切换按钮的跟布局
  @Bind (R.id.rg_keyboard) RadioGroup rgKeyboard;
  // 表情键盘切换按钮
  @Bind (R.id.rb_keybord_emoji) RadioButton rbKeybordEmoji;
  // 话题键盘切换按钮
  @Bind (R.id.rb_keybord_topic_tag) RadioButton rbKeybordTopicTag;
  // 场景标签切换按钮
  @Bind (R.id.rb_keybord_living_tag) RadioButton rbKeybordLivingTag;
  // 输入框
  @Bind (R.id.keyboard_rich_input) TEditText keyboardRichInput;
  // 发送按钮
  @Bind (R.id.rb_send) RadioButton rbSend;
  // 显示输入框文字长度的TextView
  @Bind (R.id.text_length) TextView textLength;
  // 键盘的头部根布局
  @Bind (R.id.ll_keyboard_view_title) LinearLayout llKeyboardViewTitle;

  /***************************** emoji表情键盘*******************************/
  // 自适应内容居中的ViewPager
  @Bind (R.id.emoji_viewpager) AutoCenterViewPager emojiViewpager;
  // emoji表情键盘的圆点指示器
  @Bind (R.id.ll_emoji_dot) LinearLayout llEmojiDot;
  // emoji表情键盘的根布局
  @Bind (R.id.ll_keyboard_emoji) LinearLayout llKeyboardEmoji;

  /*****************************场景标签键盘*******************************/
  // 自适应内容居中的ViewPager
  @Bind (R.id.living_tag_viewpager) AutoCenterViewPager livingTagViewpager;
  // 场景标签键盘的圆点指示器
  @Bind (R.id.ll_living_tag_dot) LinearLayout llLivingTagDot;
  // 场景标签的根布局
  @Bind (R.id.ll_keyboard_living_tag) LinearLayout llKeyboardLivingTag;

  /*****************************话题标签键盘*******************************/
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

  private OnKeyboardChildClickListener onChildClickListener;

  private Activity mActivity;
  private TEditText et;

  // 限制最大的话题数量, ，-1 不限制个数
  private int maxTopicNum = -1;

  private boolean isNeedInput = true, isNeedEmoji = true, isNeedLiving = false, isNeedTopic = true, isNeedSend = true,
      isNeedTextLength = false;

  private int[] topicBg = new int[] { R.drawable.keyboard_topic_normal, R.drawable.keyboard_normal };
  private int[] emojiBg = new int[] { R.drawable.keyboard_expression_normal, R.drawable.keyboard_normal };
  private int[] livingBg = new int[] { R.drawable.keyboard_tag_normal, R.drawable.keyboard_normal };

  public KeyboardLayout(Context context) {
    this(context, null);
  }

  public KeyboardLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initializeConfig();
    initView();
    initEmojiKeyboard();
    initCollectionKeyboard();
    //initLivingKeyboard();
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

    resetRb();

    et = keyboardRichInput;
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
      emojiKeyboardAdapter.setOnItemClickListener((adapter, view1, position) -> {
        if (null != onChildClickListener) {
          onChildClickListener.onEmojiItemClick((String) adapter.getItem(position));
        }
        et.getEditableText().insert(et.getSelectionStart(), (CharSequence) adapter.getItem(position));
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
    llDot.getChildAt(0).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.shape_keyboard_indicator_select);
    emojiViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        // 取消圆点选中
        llDot.getChildAt(curIndex)
            .findViewById(R.id.v_dot)
            .setBackgroundResource(R.drawable.shape_keyboard_indicator_normal);
        // 圆点选中
        llDot.getChildAt(position)
            .findViewById(R.id.v_dot)
            .setBackgroundResource(R.drawable.shape_keyboard_indicator_select);
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
    collectionClassKeyboardAdapter = new CollectionClassKeyboardAdapter(collectionTopics);
    RcvInitUtils.initHorizontalRcv(getContext(), rcvTopicClass, collectionClassKeyboardAdapter);
    collectionClassKeyboardAdapter.setOnItemClickListener((adapter, view, position) -> {
      collectionClassKeyboardAdapter.setItemSelect(position);
      CollectionTopic collectionTopic = (CollectionTopic) adapter.getItem(position);
      if (null != collectionTopic && null != collectionTopic.details) {
        updateTopicKeyboard(collectionTopic.details);
      }
    });
    if (collectionClassKeyboardAdapter.getData().size() > 0) {
      updateTopicKeyboard(collectionClassKeyboardAdapter.getData().get(0).details);
    }
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
      if (null != t && !TextUtils.isEmpty(t.labelTopicValue)) {
        topicList.add(t.labelTopicValue);
      }
    }
    pfvTopic.setData(topicList);
    pfvTopic.setOnTagClickListener((view, obj) -> {
      if (null != onChildClickListener) {
        onChildClickListener.onTopicItemClick(obj);
      }
      if (maxTopicNum >= 0 && et.getTopicList().size() >= maxTopicNum) {
        Toast.makeText(mActivity, "最多选择" + maxTopicNum + "个标签", Toast.LENGTH_SHORT).show();
        return;
      }
      et.insertTopic(obj);
    });
  }

  /**
   * 点击事件
   *
   * @param view 点击的View
   */
  @OnClick ({
      R.id.rb_keybord_emoji, R.id.keyboard_rich_input, R.id.rb_keybord_living_tag, R.id.rb_keybord_topic_tag,
      R.id.rb_send
  }) public void onClick(View view) {
    view.setSelected(!view.isSelected());
    switch (view.getId()) {
      case R.id.rb_keybord_emoji:
        changeSelect(KeyboardSelectTyper.emoji);
        break;
      case R.id.keyboard_rich_input:
        changeSelect(KeyboardSelectTyper.input);
        break;
      case R.id.rb_keybord_living_tag:
        changeSelect(KeyboardSelectTyper.living);
        break;
      case R.id.rb_keybord_topic_tag:
        changeSelect(KeyboardSelectTyper.topic);
        break;
      case R.id.rb_send:
        if (null != onChildClickListener) {
          onChildClickListener.onSend(et.getText().toString());
        }
        hideAll();
        break;
    }
  }

  /**
   * 切换键盘状态
   *
   * @param type 键盘状态
   */
  private void changeSelect(@KeyboardSelectTyper int type) {
    switch (type) {
      case KeyboardSelectTyper.emoji:
        rbKeybordEmoji.setBackgroundResource(!rbKeybordEmoji.isSelected() ? emojiBg[0] : emojiBg[1]);

        rbKeybordTopicTag.setBackgroundResource(topicBg[0]);
        rbKeybordTopicTag.setSelected(false);

        rbKeybordLivingTag.setBackgroundResource(livingBg[0]);
        rbKeybordLivingTag.setSelected(false);
        break;
      case KeyboardSelectTyper.topic:
        rbKeybordTopicTag.setBackgroundResource(!rbKeybordTopicTag.isSelected() ? topicBg[0] : topicBg[1]);

        rbKeybordEmoji.setBackgroundResource(emojiBg[0]);
        rbKeybordEmoji.setSelected(false);

        rbKeybordLivingTag.setBackgroundResource(livingBg[0]);
        rbKeybordLivingTag.setSelected(false);
        break;
      case KeyboardSelectTyper.living:
        rbKeybordLivingTag.setBackgroundResource(!rbKeybordLivingTag.isSelected() ? livingBg[0] : livingBg[1]);

        rbKeybordEmoji.setBackgroundResource(emojiBg[0]);
        rbKeybordEmoji.setSelected(false);

        rbKeybordTopicTag.setBackgroundResource(topicBg[0]);
        rbKeybordTopicTag.setSelected(false);
        break;

      case KeyboardSelectTyper.input:
        resetRb();
        break;

      case KeyboardSelectTyper.send:
        resetRb();
        break;

      case KeyboardSelectTyper.def:
        rbKeybordTopicTag.setBackgroundResource(topicBg[0]);
        rbKeybordTopicTag.setSelected(false);
        llKeyboardTopic.setVisibility(GONE);

        rbKeybordEmoji.setBackgroundResource(emojiBg[0]);
        rbKeybordEmoji.setSelected(false);
        llKeyboardEmoji.setVisibility(GONE);

        rbKeybordLivingTag.setBackgroundResource(livingBg[0]);
        rbKeybordLivingTag.setSelected(false);
        llKeyboardLivingTag.setVisibility(GONE);

        textLength.setVisibility(GONE);
        textLength.setText("");

        rbKeybordEmoji.setVisibility(isNeedEmoji ? VISIBLE : GONE);
        rbKeybordLivingTag.setVisibility(isNeedLiving ? VISIBLE : GONE);
        rbKeybordTopicTag.setVisibility(isNeedTopic ? VISIBLE : GONE);
        rbSend.setVisibility(isNeedSend ? VISIBLE : GONE);
        keyboardRichInput.setVisibility(isNeedInput ? VISIBLE : GONE);
        textLength.setVisibility(isNeedTextLength ? VISIBLE : GONE);

        break;
    }
    if (null != onChildClickListener) {
      onChildClickListener.onBtnStateChanged(rbKeybordEmoji, rbKeybordTopicTag, rbKeybordLivingTag);
    }
  }

  /**
   * 话题标签键盘是否显示
   *
   * @return true 显示 false 隐藏
   */
  private boolean isTopicShowing() {
    return llKeyboardTopic.getVisibility() == VISIBLE;
  }

  /**
   * emoji表情键盘是否显示
   *
   * @return true 显示 false 隐藏
   */
  private boolean isEmojiShowing() {
    return llKeyboardEmoji.getVisibility() == VISIBLE;
  }

  /**
   * 场景标签键盘是否显示
   *
   * @return true 显示 false 隐藏
   */
  private boolean isLivingTagShowing() {
    return llKeyboardLivingTag.getVisibility() == VISIBLE;
  }


/*##############################以下是暴露出来的方法#######################################*/

  /**
   * 隐藏所有键盘
   */
  public void hideAll() {
    InputMethodUtils.hideKeyboard(null != mActivity ? mActivity.getCurrentFocus() : llKeyboardViewTitle);
    resetRb();
    if (null != onChildClickListener) {
      onChildClickListener.hideAllKeyboard();
    }
  }

  /**
   * 设置键盘智能切换
   *
   * @param activity 当前Activity
   */
  public KeyboardLayout setKeyBoardManager(Activity activity) {
    return this.setKeyBoardManager(activity, null);
  }

  /**
   * 设置键盘智能切换
   *
   * @param activity 当前Activity
   * @param contentView 可滑动的内容布局，一般在输入框上方
   */
  public KeyboardLayout setKeyBoardManager(Activity activity, View contentView) {
    return this.setKeyBoardManager(activity, contentView, null, true);
  }

  /**
   * 设置键盘智能切换
   *
   * @param activity 当前Activity
   * @param contentView 可滑动的内容布局，一般在输入框上方
   * @param editText 输入框，如果null 则使用 默认的
   */
  public KeyboardLayout setKeyBoardManager(Activity activity, View contentView, TEditText editText,
      boolean needScroll) {
    mActivity = activity;
    et = null == editText ? keyboardRichInput : editText;
    mSmartKeyboardManager = new SmartKeyboardManager.Builder(activity).setContentView(contentView)
        .addKeyboard(rbKeybordEmoji, llKeyboardEmoji)
        .addKeyboard(rbKeybordTopicTag, llKeyboardTopic)
        //.addKeyboard(rbKeybordLivingTag, llKeyboardLivingTag)
        .setEditText(et)
        .addOnContentViewScrollListener(new OnContentViewScrollListener() {
          @Override public void needScroll(int distance) {
            if (null != contentView && needScroll) {
              contentView.scrollBy(0, distance);
            }
          }
        })
        .addKeyboardChangeListener(new SmartKeyboardManager.KeyboardChangeListener() {
          @Override public void onKeyboardChange(boolean isShow, int keyboardHeight) {
            if (isShow) {
              resetRb();
              if (null != onChildClickListener) {
                onChildClickListener.showKeyboard();
              }
              ViewGroup.LayoutParams params1 = llKeyboardEmoji.getLayoutParams();
              if (params1 != null && params1.height != keyboardHeight) {
                params1.height = keyboardHeight;
                llKeyboardEmoji.setLayoutParams(params1);
              }
            } else {
              if (!isEmojiShowing() && !isTopicShowing()) {
                hideAll();
              }
            }
          }
        })
        .create();
    return this;
  }

  /**
   * 键盘点击事件
   *
   * @param childClickListener 点击事件监听器
   */
  public KeyboardLayout setOnChildClick(OnKeyboardChildClickListener childClickListener) {
    this.onChildClickListener = childClickListener;
    return this;
  }

  /**
   * 设置话题标签的数据， 带分类的
   *
   * @param collectionTopics 带分类的话题标签数据
   */
  public KeyboardLayout setCollectionTopics(List<CollectionTopic> collectionTopics) {
    this.collectionTopics = collectionTopics;
    if (null == collectionTopics || collectionTopics.size() <= 0) {
      return this;
    }
    if (null == collectionClassKeyboardAdapter) {
      initCollectionKeyboard();
    } else {
      collectionClassKeyboardAdapter.setNewData(collectionTopics);
      collectionClassKeyboardAdapter.notifyDataSetChanged();
      updateTopicKeyboard(collectionTopics.get(0).details);
    }
    return this;
  }

  /**
   * 设置话题标签的数据，没有分类
   *
   * @param details 话题标签数据
   */
  public KeyboardLayout setTopics(List<TopicBean> details) {
    updateTopicKeyboard(details);
    return this;
  }

  /**
   * 设置话题键盘切换按钮背景图
   *
   * @param topicBg 话题
   */
  public KeyboardLayout setTopicBg(int[] topicBg) {
    this.topicBg = topicBg;
    return this;
  }

  /**
   * 设置表情键盘切换按钮背景图
   *
   * @param emojiBg 表情
   */
  public KeyboardLayout setEmojiBg(int[] emojiBg) {
    this.emojiBg = emojiBg;
    return this;
  }

  /**
   * 设置场景键盘切换按钮背景图
   *
   * @param livingBg 场景
   */
  public KeyboardLayout setLivingBg(int[] livingBg) {
    this.livingBg = livingBg;
    return this;
  }

  public KeyboardLayout setMaxTopicNum(int maxTopicNum) {
    this.maxTopicNum = maxTopicNum;
    return this;
  }

  /**
   * 设置输入字符提示文字
   *
   * @param len 提示文字
   */
  public KeyboardLayout updateTextLength(String len) {
    isNeedTextLength = !TextUtils.isEmpty(len);
    textLength.setVisibility(TextUtils.isEmpty(len) ? GONE : VISIBLE);
    textLength.setText(len);
    return this;
  }

  public KeyboardLayout showInput(boolean isShow) {
    isNeedInput = isShow;
    return this;
  }

  public KeyboardLayout showEmoji(boolean isShow) {
    isNeedEmoji = isShow;
    return this;
  }

  public KeyboardLayout showLiving(boolean isShow) {
    isNeedLiving = isShow;
    return this;
  }

  public KeyboardLayout showTopic(boolean isShow) {
    isNeedTopic = isShow;
    return this;
  }

  public KeyboardLayout showSend(boolean isShow) {
    isNeedSend = isShow;
    return this;
  }

  public KeyboardLayout showTextLength(boolean isShow) {
    isNeedTextLength = isShow;
    return this;
  }

  public KeyboardLayout setEditText(TEditText et) {
    this.et = et;
    mSmartKeyboardManager.updateEditText(et);
    return this;
  }

  /**
   * 如果自定义键盘还在显示，中断"back"操作
   */
  public boolean interceptBackPressed() {
    if (null == mSmartKeyboardManager) {
      return true;
    }
    return mSmartKeyboardManager.interceptBackPressed();
  }

  /**
   * 设置输入框的hint提示语
   *
   * @param s 提示语
   */
  public KeyboardLayout setInputHint(String s) {
    SpannableString spannableString = new SpannableString(s);
    spannableString.setSpan(new AbsoluteSizeSpan(12, true), 0, s.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    if (null != et) {
      et.setHint(spannableString);
    }
    return this;
  }

  /**
   * 重置radioButton的背景图和选中状态
   */
  public void resetRb() {
    changeSelect(KeyboardSelectTyper.def);
  }

  public void showSoftKeyboard() {
    mSmartKeyboardManager.showSoftKeyboard();
  }

  public TEditText getInputEdit() {
    return et;
  }

  /**
   * 清空输入框的内容
   */
  public void clearInputContent() {
    if (null != et) {
      et.setText("");
    }
  }
}
