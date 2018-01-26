package com.meechao.detailflow.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-16 18:04
 * Mail：jihaifeng@meechao.com
 */
public abstract class BaseActivity extends AppCompatActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    ButterKnife.bind(this);
    initViewAndEvent();
  }

  protected abstract int getLayoutId();

  protected abstract void initViewAndEvent();
}
