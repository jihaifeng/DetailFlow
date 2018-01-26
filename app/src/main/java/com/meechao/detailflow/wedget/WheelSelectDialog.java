package com.meechao.detailflow.wedget;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.meechao.detailflow.R;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-26 12:14
 * Mail：jihaifeng@meechao.com
 */
public class WheelSelectDialog extends AlertDialog implements View.OnClickListener {

  private Button btnCancel;
  private TextView tvDialogTitle;
  private Button btnSure;
  private WheelView wheelView;
  private onWheelListener wheelListener;

  /**
   * 接口回调
   */
  public interface onWheelListener {

    void select(int index, String text);

    void cancel();
  }

  public WheelSelectDialog(Activity context) {
    super(context, R.style.dialog_custom);
    setOwnerActivity(context);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Window window = getWindow();
    if (null != window) {
      window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置为居中
      window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
    }
    setContentView(R.layout.view_wheel_select_dialog);
    if (null != window) {
      WindowManager windowManager = window.getWindowManager();
      Display display = windowManager.getDefaultDisplay();
      WindowManager.LayoutParams lp = getWindow().getAttributes();
      lp.width = display.getWidth(); // 设置dialog宽度为屏幕
      getWindow().setAttributes(lp);
    }

    intViewAndEvent();
  }

  /**
   * 控件初始化
   */
  private void intViewAndEvent() {
    btnCancel = findViewById(R.id.btn_cancel);
    tvDialogTitle = findViewById(R.id.tv_dialog_title);
    btnSure = findViewById(R.id.btn_sure);
    wheelView = findViewById(R.id.wv);

    btnCancel.setOnClickListener(this);
    btnSure.setOnClickListener(this);

    wheelView.setOnSelectListener(new WheelView.OnSelectListener() {
      @Override public void endSelect(int index, String text) {
        updateBtnState(wheelView.isScrolling());
      }

      @Override public void selecting(int index, String text) {
        updateBtnState(wheelView.isScrolling());
      }
    });
  }

  private void updateBtnState(boolean isScrolling) {
    btnCancel.setClickable(!isScrolling);
    btnSure.setClickable(!isScrolling);
  }

  @Override public void onClick(View view) {
    if (null != wheelListener
        && null != wheelView
        && !wheelView.isScrolling()
        && wheelView.getSelected() >= 0
        && !TextUtils.isEmpty(wheelView.getSelectedText())) {
      switch (view.getId()) {
        case R.id.btn_cancel:
          dismiss();
          wheelListener.cancel();
          break;
        case R.id.btn_sure:
          dismiss();
          wheelListener.select(wheelView.getSelected(), wheelView.getSelectedText());

          break;
      }
    }
  }

  /********************************一下是暴露出来的方法*************************************************************/
  /**
   * 设置数据
   *
   * 注意：该方法需在 show() 以后调用，否则无效
   *
   * @param data 数据集
   */
  public WheelSelectDialog setData(List<String> data) {
    if (null != wheelView) {
      wheelView.setData(data);
    }
    return this;
  }

  /**
   * 设置标题
   *
   * 注意：该方法需在 show() 以后调用，否则无效
   *
   * @param title 标题
   */
  public WheelSelectDialog setTitle(String title) {
    if (null != tvDialogTitle) {
      tvDialogTitle.setText(title);
    }
    return this;
  }

  /**
   * 设置监听器
   *
   * @param listener 监听器
   */
  public void setOnWheelListener(onWheelListener listener) {
    this.wheelListener = listener;
  }

  @Override public void show() {
    if (null != getOwnerActivity() && !getOwnerActivity().isFinishing()) {
      if (isShowing()) {
        dismiss();
      }
      super.show();
    }
  }

  @Override public void dismiss() {
    if (null != getOwnerActivity() && !getOwnerActivity().isFinishing() && isShowing()) {
      super.dismiss();
    }
  }
}
