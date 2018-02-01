package com.meechao.detailflow.wedget.keyboard.keyboardManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.meechao.detailflow.utils.DisplayUtils;
import com.meechao.detailflow.utils.InputMethodUtils;
import com.meechao.detailflow.utils.LogUtils;
import com.meechao.detailflow.wedget.keyboard.listener.OnContentViewScrollListener;
import java.util.Hashtable;
import java.util.Map;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-11 12:33
 * Mail：jihaifeng@meechao.com
 */
public class SmartKeyboardManager {
  private static final String TAG = SmartKeyboardManager.class.getSimpleName();

  // 键盘切换动画时长
  private final static long DURATION_SWITCH_KEYBOARD = 150L;

  // 绑定的Activity
  private Activity mActivity;

  private View mContentView;

  private Hashtable<View, View> keyboardTable = new Hashtable<>();

  private EditText mEditText;

  private View sendView;

  private InputMethodManager mInputMethodManager;

  private OnContentViewScrollListener mOnContentViewScrollListener;
  private KeyboardChangeListener mKeyboardChangeListener;

  // 键盘高度
  private int mKeyBoardHeight = 0;

  public SmartKeyboardManager(SmartKeyboardManager.Builder builder) {
    mActivity = builder.mNestedActivity;
    mContentView = builder.mNestedContentView;
    keyboardTable = builder.keyboardTable;
    mEditText = builder.mNestedEditText;
    mInputMethodManager = builder.mNestedInputMethodManager;
    mOnContentViewScrollListener = builder.mOnNestedContentViewScrollListener;
    mKeyboardChangeListener = builder.mKeyboardChangeListener;
    setUpCallbacks();
  }

  private void setUpCallbacks() {

    // 初始化屏幕工具类
    DisplayUtils.init(mActivity);
    // 默认键盘高度为267dp
    setKeyBoardHeight(DisplayUtils.dp2px(267));
    detectKeyboard();// 监听View树变化，以便监听键盘是否弹出
    enableCloseKeyboardOnTouchOutside(mActivity);

    // 设置 EditText 监听器
    mEditText.requestFocus();
    mEditText.setOnTouchListener(new ThrottleTouchListener() {
      @Override public void onThrottleTouch() {
        if (hasCustomKeyboardShown()) {
          hideAllCustomKeyboard(true);
        }
      }
    });

    // 设置内容滚动监听器
    mContentView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {

      if (oldBottom - bottom == 0) {
        Log.i(TAG, "不用滚动");
        return;
      }

      Log.i(TAG, "滚动距离 -->>>" + (oldBottom - bottom));

      if (mOnContentViewScrollListener != null) {
        mOnContentViewScrollListener.needScroll(oldBottom - bottom);
      }
    });

    // 为“文字表情键盘“切换按钮设置监听器
    for (Map.Entry<View, View> entry : keyboardTable.entrySet()) {
      entry.getValue().setVisibility(View.GONE);
      addTouchListener(entry.getKey(), entry.getValue());
    }
  }

  /**
   * 设置键盘的高度
   *
   * @param keyBoardHeight 键盘的高度(px单位)
   */
  private void setKeyBoardHeight(int keyBoardHeight) {
    mKeyBoardHeight = keyBoardHeight;
    updateAllPanelHeight(keyBoardHeight);
  }

  /**
   * 更新所有面板的高度
   *
   * @param height 具体高度
   */
  private void updateAllPanelHeight(int height) {

    for (Map.Entry<View, View> entry : keyboardTable.entrySet()) {
      ViewGroup.LayoutParams params = entry.getValue().getLayoutParams();
      params.height = height;
      entry.getValue().setLayoutParams(params);
    }
  }

  /**
   * 更新输入法的弹出模式
   *
   * @param softInputMode <br/>
   * 键盘弹出模式：WindowManager.LayoutParams的参数有：<br/>
   * &nbsp;&nbsp;&nbsp;&nbsp;可见状态： SOFT_INPUT_STATE_UNSPECIFIED,
   * SOFT_INPUT_STATE_UNCHANGED, SOFT_INPUT_STATE_HIDDEN,
   * SOFT_INPUT_STATE_ALWAYS_VISIBLE, or SOFT_INPUT_STATE_VISIBLE.<br/>
   * &nbsp;&nbsp;&nbsp;&nbsp;适配选项有： SOFT_INPUT_ADJUST_UNSPECIFIED,
   * SOFT_INPUT_ADJUST_RESIZE, or SOFT_INPUT_ADJUST_PAN.
   */
  public void updateSoftInputMethod(int softInputMode) {
    updateSoftInputMethod(mActivity, softInputMode);
  }

  /**
   * 更新输入法的弹出模式
   *
   * @param activity
   * @param softInputMode <br/>
   * 键盘弹出模式：WindowManager.LayoutParams的参数有：<br/>
   * &nbsp;&nbsp;&nbsp;&nbsp;可见状态： SOFT_INPUT_STATE_UNSPECIFIED,
   * SOFT_INPUT_STATE_UNCHANGED, SOFT_INPUT_STATE_HIDDEN,
   * SOFT_INPUT_STATE_ALWAYS_VISIBLE, or SOFT_INPUT_STATE_VISIBLE.<br/>
   * &nbsp;&nbsp;&nbsp;&nbsp;适配选项有： SOFT_INPUT_ADJUST_UNSPECIFIED,
   * SOFT_INPUT_ADJUST_RESIZE, or SOFT_INPUT_ADJUST_PAN.
   */
  public static void updateSoftInputMethod(Activity activity, int softInputMode) {
    if (!activity.isFinishing()) {
      WindowManager.LayoutParams params = activity.getWindow().getAttributes();
      if (params.softInputMode != softInputMode) {
        params.softInputMode = softInputMode;
        activity.getWindow().setAttributes(params);
      }
    }
  }

  /**
   * 设置View树监听，以便判断键盘是否弹出。<br/>
   * 【只有当Activity的windowSoftInputMode设置为adjustResize时才有效】
   */
  private void detectKeyboard() {
    final View activityRootView = mActivity.findViewById(android.R.id.content);
    if (activityRootView != null) {
      ViewTreeObserver observer = activityRootView.getViewTreeObserver();
      if (observer == null) {
        return;
      }
      observer.addOnGlobalLayoutListener(() -> {
        final Rect r = new Rect();
        activityRootView.getWindowVisibleDisplayFrame(r);
        int heightDiff = DisplayUtils.getScreenHeight() - (r.bottom - r.top);
        boolean show = heightDiff >= mKeyBoardHeight / 3;
        int keyboardHeight = 0;
        if (show) {
          keyboardHeight = heightDiff - DisplayUtils.getStatusBarHeight();
          // 设置新的键盘高度
          setKeyBoardHeight(keyboardHeight);
        }
        if (null != mKeyboardChangeListener) {
          mKeyboardChangeListener.onKeyboardChange(show, keyboardHeight);
        }
      });
    }
  }

  /**
   * 开启点击外部关闭键盘的功能
   *
   * @param activity
   */
  private void enableCloseKeyboardOnTouchOutside(Activity activity) {
    CloseKeyboardOnOutsideContainer frameLayout = new CloseKeyboardOnOutsideContainer(activity);
    activity.addContentView(frameLayout,
        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  /**
   * 隐藏所有自定义的键盘
   */
  private void hideAllCustomKeyboard(boolean needSoftKeyboard) {
    for (Map.Entry<View, View> entry : keyboardTable.entrySet()) {
      if (entry.getValue().getVisibility() == View.VISIBLE) {
        hideCustomKeyboard(entry.getValue(), needSoftKeyboard);
      }
    }
  }

  /**
   * 给键盘切换按钮添加touch事件，实现键盘的切换
   *
   * @param key 切换按钮
   * @param value 键盘布局
   */
  private void addTouchListener(View key, final View value) {
    key.setOnTouchListener(new ThrottleTouchListener() {
      @Override public void onThrottleTouch() {
        if (value.isShown()) {
          //当前对应的自定义键盘 "显示"
          hideCustomKeyboard(value, true);
        } else {
          //当前对应的自定义键盘 "隐藏"
          if (hasCustomKeyboardShown()) {
            // 有自定义键盘在显示
            // step.1 隐藏当前显示的自定义键盘
            View keyboardViewShown = getShownCustomKeyboard();
            if (null != keyboardViewShown) {
              keyboardViewShown.setVisibility(View.GONE);
            }
            // step.2 显示选中的自定义键盘,并且锁定 "ContentView" 的高度
            showSelectCustomKeyboard(value);
          } else {
            // 没有自定义键盘显示
            if (InputMethodUtils.isSoftKeyboardShown(mActivity)) {
              // 系统软键盘正在显示
              showSelectCustomKeyboard(value);
            } else {
              // 系统软键盘没有显示
              showSelectCustomKeyboard(value);
            }
          }
        }
        mEditText.requestFocus();
      }
    });
  }

  /**
   * 获取当前正在显示的自定义键盘
   *
   * @return view
   */
  private View getShownCustomKeyboard() {
    for (Map.Entry<View, View> entry : keyboardTable.entrySet()) {
      if (entry.getValue().isShown()) {
        return entry.getValue();
      }
    }
    return null;
  }

  /**
   * 如果自定义键盘还在显示，中断"back"操作
   */
  public boolean interceptBackPressed() {
    if (hasCustomKeyboardShown()) {
      hideAllCustomKeyboard(false);
      return true;
    }
    return false;
  }

  /**
   * 是否有自定义的键盘正在显示
   *
   * @return true 有 false 没有
   */
  private boolean hasCustomKeyboardShown() {
    for (Map.Entry<View, View> entry : keyboardTable.entrySet()) {
      if (entry.getValue().isShown()) {
        return true;
      }
    }
    return false;
  }

  /**
   * 显示指定的自定义键盘，不锁定 "ContentView"的高度
   *
   * @param view 需要显示的键盘布局
   */
  private void showSelectCustomKeyboard(View view) {
    view.setVisibility(View.VISIBLE);
    view.getLayoutParams().height = InputMethodUtils.getSupportSoftKeyboardHeight(mActivity);
    ObjectAnimator showAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.0F, 1.0F);
    showAnimator.setDuration(DURATION_SWITCH_KEYBOARD);
    showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

    showAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        updateSoftInputMethod(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        if (isSoftKeyboardShowing()) {
          hideSoftKeyboard();
        }
      }

      @Override public void onAnimationEnd(Animator animation) {
        updateSoftInputMethod(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
      }
    });
    showAnimator.start();
  }

  /**
   * 隐藏指定自定义键盘
   *
   * @param view 需要隐藏的自定义键盘布局
   * @param needSoftKeyboard true 锁定 "ContentView" 的高度并显示 "软键盘"  / false 不显示 "软键盘"
   */
  private void hideCustomKeyboard(final View view, final boolean needSoftKeyboard) {
    ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0F, 0.0F);
    hideAnimator.setDuration(DURATION_SWITCH_KEYBOARD);
    hideAnimator.setInterpolator(new AccelerateInterpolator());
    hideAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        updateSoftInputMethod(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        if (needSoftKeyboard) {
          showSoftKeyboard();
        }
      }

      @Override public void onAnimationEnd(Animator animation) {
        updateSoftInputMethod(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        view.setVisibility(View.GONE);
      }
    });
    hideAnimator.start();
  }

  /**
   * 隐藏软键盘
   */
  private void hideSoftKeyboard() {
    mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
  }

  private class CloseKeyboardOnOutsideContainer extends FrameLayout {

    public CloseKeyboardOnOutsideContainer(Context context) {
      this(context, null);
    }

    public CloseKeyboardOnOutsideContainer(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
    }

    public CloseKeyboardOnOutsideContainer(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
      if ((isSoftKeyboardShowing() || hasCustomKeyboardShown()) && event.getAction() == MotionEvent.ACTION_DOWN) {
        int touchY = (int) (event.getY());
        int touchX = (int) (event.getX());
        if (isTouchKeyboardOutside(touchY)) {
          if (isSoftKeyboardShowing()) {
            hideSoftKeyboard();
          }
          if (hasCustomKeyboardShown()) {
            hideAllCustomKeyboard(false);
          }
          //return true;
        }

        LogUtils.i(TAG, "dispatchTouchEvent: " + (mActivity.getCurrentFocus() instanceof EditText));
        if (isTouchedFocusView(touchX, touchY)) {
          // 如果点击的是输入框，那么延时折叠表情面板
          //InputMethodUtils.showInputMethod(mActivity.getCurrentFocus(), 0);
          postDelayed(new Runnable() {
            @Override public void run() {
              hideAllCustomKeyboard(true);
            }
          }, 500);
          //return true;
        }
      }
      return super.onTouchEvent(event);
    }
  }

  /**
   * 是否点击软键盘和输入法外面区域
   *
   * @param touchY 点击y坐标(不包括statusBar的高度)
   */
  private boolean isTouchKeyboardOutside(int touchY) {
    View focusView = mActivity.getCurrentFocus();
    if (focusView == null) {
      return false;
    }
    int[] location = new int[2];
    focusView.getLocationOnScreen(location);
    int editY = location[1] - DisplayUtils.getStatusBarHeight();
    int offset = touchY - editY;
    if (offset > 0) {// 输入框一下的有可能是表情面板，所以，我们算作范围内
      return false;
    }
    return true;
  }

  /**
   * 是否点击的是当前焦点View的范围
   *
   * @param x x方向坐标
   * @param y y方向坐标(不包括statusBar的高度)
   *
   * @return true表示点击的焦点View，false反之
   */
  private boolean isTouchedFocusView(int x, int y) {
    View focusView = mActivity.getCurrentFocus();
    if (focusView == null) {
      return false;
    }
    int[] location = new int[2];
    focusView.getLocationOnScreen(location);
    int focusViewTop = location[1] - DisplayUtils.getStatusBarHeight();
    int offsetY = y - focusViewTop;
    if (offsetY > 0 && offsetY < focusView.getMeasuredHeight()) {
      int focusViewLeft = location[0];
      int focusViewLength = focusView.getWidth();
      int offsetX = x - focusViewLeft;
      if (offsetX >= 0 && offsetX <= focusViewLength) {
        return true;
      }
    }
    return false;
  }

  /**
   * 显示软键盘
   */
  public void showSoftKeyboard() {
    if (null == mEditText) {
      return;
    }

    if (!mEditText.hasFocus()) {
      mEditText.requestFocus();
    }
    if (!isSoftKeyboardShowing()) {
      mInputMethodManager.showSoftInput(mEditText, 0);
    }
  }

  public void updateEditText(EditText editText) {
    this.mEditText = editText;
  }

  public boolean isSoftKeyboardShowing() {
    return null != mActivity && InputMethodUtils.isSoftKeyboardShown(mActivity);
  }

  public int getKeyBoardHeight() {
    return mKeyBoardHeight;
  }

  public static class Builder {

    private Activity mNestedActivity;

    // 高度可变化的 `ContentView`（例如 RecyclerView、ListView...）
    private View mNestedContentView;

    private Hashtable<View, View> keyboardTable = new Hashtable<>();

    private EditText mNestedEditText;

    private InputMethodManager mNestedInputMethodManager;

    private OnContentViewScrollListener mOnNestedContentViewScrollListener;
    private KeyboardChangeListener mKeyboardChangeListener;

    public Builder(Activity activity) {
      this.mNestedActivity = activity;
    }

    public SmartKeyboardManager.Builder setContentView(View contentView) {
      this.mNestedContentView = contentView;
      return this;
    }

    public SmartKeyboardManager.Builder addKeyboard(View clickView, View KeyboardContent) {
      if (null == clickView) {
        Log.w(TAG, "addKeyboard: your clickView is null.please check it.");
        return this;
      }
      if (null == KeyboardContent) {
        Log.w(TAG, "addKeyboard: your KeyboardContent is null.please check it.");
        return this;
      }
      keyboardTable.put(clickView, KeyboardContent);
      return this;
    }

    public SmartKeyboardManager.Builder setEditText(EditText editText) {
      this.mNestedEditText = editText;
      return this;
    }

    public SmartKeyboardManager.Builder addOnContentViewScrollListener(OnContentViewScrollListener listener) {
      this.mOnNestedContentViewScrollListener = listener;
      return this;
    }

    public SmartKeyboardManager.Builder addKeyboardChangeListener(KeyboardChangeListener keyboardChangeListener) {
      this.mKeyboardChangeListener = keyboardChangeListener;
      return this;
    }

    public SmartKeyboardManager create() {
      initFieldsWithDefaultValue();
      return new SmartKeyboardManager(this);
    }

    private void initFieldsWithDefaultValue() {
      this.mNestedInputMethodManager =
          (InputMethodManager) mNestedActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
      mNestedActivity.getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
              | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
  }

  public interface KeyboardChangeListener {
    /**
     * call back
     *
     * @param isShow true is show else hidden
     * @param keyboardHeight keyboard height
     */
    void onKeyboardChange(boolean isShow, int keyboardHeight);
  }
}
