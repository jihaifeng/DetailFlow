package com.meechao.detailflow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.meechao.detailflow.R;
import com.meechao.detailflow.source.emojiSource;
import la.juju.android.ftil.entities.FaceText;
import la.juju.android.ftil.listener.OnTextClickListener;
import la.juju.android.ftil.widgets.EmojiTextInputLayout;

/**
 * Created by HelloVass on 15/12/31.
 */
public class EmojiLayoutDemoActivity extends AppCompatActivity {

  private static final String TAG = EmojiLayoutDemoActivity.class.getSimpleName();

  private EmojiTextInputLayout emojiTextInputLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_emoji_text_demo);
    initViews();
  }

  private void initViews() {

    // setup FaceTextInputLayout
    emojiTextInputLayout = findViewById(R.id.tv_emoji_text_input_layout);
    emojiTextInputLayout.setFaceTextSource(new emojiSource());
    emojiTextInputLayout.setOnFaceTextClickListener(new OnTextClickListener() {
      @Override public void onFaceTextClick(FaceText faceText) {
        Toast.makeText(EmojiLayoutDemoActivity.this, faceText.content, Toast.LENGTH_SHORT).show();
      }
    });
    emojiTextInputLayout.render();
    emojiTextInputLayout.setVisibility(View.VISIBLE);
  }
}
