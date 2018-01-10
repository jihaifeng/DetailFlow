package com.meechao.detailflow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import com.meechao.detailflow.R;
import com.meechao.detailflow.richText.TEditText;
import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity implements View.OnClickListener {

  private TEditText et;

  private ArrayList<ForegroundColorSpan> mColorSpans = new ArrayList<>();
  private ArrayList<String> mTopicList = new ArrayList<>();
  // 正则表达式
  private static final String topicRegex = "#([^#]+?)#";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_topic);
    et = findViewById(R.id.et_content);
    findViewById(R.id.tv_0).setOnClickListener(this);
    findViewById(R.id.tv_1).setOnClickListener(this);
    findViewById(R.id.tv_2).setOnClickListener(this);
    findViewById(R.id.tv_3).setOnClickListener(this);
    findViewById(R.id.btn_set).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
et.setVisibility(View.VISIBLE);
      }
    });

    et.setOnKeyListener(new View.OnKeyListener() {
      @Override public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN ) {

          int selectionStart = et.getSelectionStart();
          int selectionEnd = et.getSelectionEnd();

          if (selectionStart != selectionEnd) {
            return false;
          }

          Editable editable = et.getText();
          String content = editable.toString();
          int lastPos = 0;
          int size = mTopicList.size();
          for (int i = 0; i < size; i++) {
            String topic = mTopicList.get(i);
            lastPos = content.indexOf(topic, lastPos);
            if (lastPos != -1) {
              if (selectionStart != 0 && selectionStart >= lastPos && selectionStart <= (lastPos + topic.length())) {
                //选中话题
                et.setSelection(lastPos, lastPos + topic.length());
                return true;
              }
            }
            lastPos += topic.length();
          }
        }
        return false;
      }
    });
  }

  @Override public void onClick(View v) {

    switch (v.getId()) {
      case R.id.tv_0:
        et.insertTopic(" #程序猿[1|话题]# ");
        break;
      case R.id.tv_1:
        et.insertTopic(" #设计喵[1|话题]# ");
        break;
      case R.id.tv_2:
        et.insertTopic(" #攻城狮[1|话题]# ");
        break;
      case R.id.tv_3:
        et.insertTopic(" #单身汪[1|话题]# ");
        break;
    }
  }

  public void insertContent(String content) {
    int selectStart = et.getSelectionStart();
    String con = et.getText().toString();
    String firstStr = con.substring(0, selectStart);
    String secondStr = con.substring(selectStart, con.length());

    et.setText(firstStr + content + secondStr);
    et.setSelection(selectStart + content.length());
  }
}