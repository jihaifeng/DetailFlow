package com.meechao.detailflow.wedget.keyboard.smartKeyboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.meechao.detailflow.utils.InputMethodUtils;
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

  private Activity mActivity;

  private View mContentView;

  private Hashtable<View, View> keyboardTable = new Hashtable<>();

  private EditText mEditText;

  private View sendView;

  private InputMethodManager mInputMethodManager;

  private OnContentViewScrollListener mOnContentViewScrollListener;

  public SmartKeyboardManager(SmartKeyboardManager.Builder builder) {
    mActivity = builder.mNestedActivity;
    mContentView = builder.mNestedContentView;
    keyboardTable = builder.keyboardTable;
    mEditText = builder.mNestedEditText;
    mInputMethodManager = builder.mNestedInputMethodManager;
    mOnContentViewScrollListener = builder.mOnNestedContentViewScrollListener;
    setUpCallbacks();
  }

  private void setUpCallbacks() {
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
    mContentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
      @Override public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
          int oldRight, int oldBottom) {

        if (oldBottom - bottom == 0) {
          Log.i(TAG, "不用滚动");
          return;
        }

        Log.i(TAG, "滚动距离 -->>>" + (oldBottom - bottom));

        if (mOnContentViewScrollListener != null) {
          mOnContentViewScrollListener.needScroll(oldBottom - bottom);
        }
      }
    });

    // 为“文字表情键盘“切换按钮设置监听器
    for (Map.Entry<View, View> entry : keyboardTable.entrySet()) {
      entry.getValue().setVisibility(View.GONE);
      addTouchListener(entry.getKey(), entry.getValue());
    }
  }

  /**
   * 隐藏所有自定义的键盘
   */
  private void hideAllCustomKeyboard(boolean needSoftKeyboard) {
    for (Map.Entry<View, View> entry : keyboardTable.entrySet()) {
      if (entry.getValue().isShown()) {
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
            hideCustomKeyboard(keyboardViewShown, false);
            // step.2 显示选中的自定义键盘,并且锁定 "ContentView" 的高度
            showSelectCustomKeyboard(value, true);
          } else {
            // 没有自定义键盘显示
            if (InputMethodUtils.isSoftKeyboardShown(mActivity)) {
              // 系统软键盘正在显示
              showSelectCustomKeyboard(value, true);
            } else {
              // 系统软键盘没有显示
              showSelectCustomKeyboard(value, false);
            }
          }
        }
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
   * @param needLockContentViewHeight 是否需要锁定 "ContentView" 的高度 ， true 锁定 false 不锁定
   */
  private void showSelectCustomKeyboard(View view, final boolean needLockContentViewHeight) {
    view.setVisibility(View.VISIBLE);
    view.getLayoutParams().height = InputMethodUtils.getSupportSoftKeyboardHeight(mActivity);
    ObjectAnimator showAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.0F, 1.0F);
    showAnimator.setDuration(DURATION_SWITCH_KEYBOARD);
    showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    showAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        hideSoftKeyboard();
      }
    });

    showAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        if (needLockContentViewHeight) {
          lockContentViewHeight();
        }
        hideSoftKeyboard();
      }

      @Override public void onAnimationEnd(Animator animation) {
        if (needLockContentViewHeight) {
          unlockContentViewHeight();
        }
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
        if (needSoftKeyboard) {
          lockContentViewHeight();
          showSoftKeyboard();
        }
      }

      @Override public void onAnimationEnd(Animator animation) {
        view.setVisibility(View.GONE);
        if (needSoftKeyboard) {
          unlockContentViewHeight();
        }
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

  /**
   * 显示软键盘
   */
  private void showSoftKeyboard() {
    mEditText.requestFocus();
    mInputMethodManager.showSoftInput(mEditText, 0);
  }

  /**
   * 锁定 ContentView 的高度
   */
  private void lockContentViewHeight() {
    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
    layoutParams.height = mContentView.getHeight();
    layoutParams.weight = 0.0F;
  }

  /**
   * 解锁 ContentView 的高度
   */
  private void unlockContentViewHeight() {
    ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
  }

  public static class Builder {

    private Activity mNestedActivity;

    // 高度可变化的 `ContentView`（例如 RecyclerView、ListView...）
    private View mNestedContentView;

    private Hashtable<View, View> keyboardTable = new Hashtable<>();

    private EditText mNestedEditText;

    private InputMethodManager mNestedInputMethodManager;

    private OnContentViewScrollListener mOnNestedContentViewScrollListener;

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
}
