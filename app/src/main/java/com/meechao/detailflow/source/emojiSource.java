package com.meechao.detailflow.source;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.meechao.detailflow.utils.AssetUtils;
import java.util.ArrayList;
import java.util.List;
import la.juju.android.ftil.entities.FaceText;
import la.juju.android.ftil.source.FaceTextProvider;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-11 10:03
 * Mail：jihaifeng@meechao.com
 */
public class emojiSource implements FaceTextProvider {
  private List<String> emojiDatas;

  public emojiSource() {
    String _emojiData = AssetUtils.getAssetsData("emoji_datas.json");
    if (!TextUtils.isEmpty(_emojiData)) {
      emojiDatas = JSON.parseArray(_emojiData, String.class);
    }
  }

  @Override public List<FaceText> provideFaceTextList() {
    List<FaceText> faceTextList = new ArrayList<>();
    if (null != emojiDatas && emojiDatas.size() > 0) {
      for (String s : emojiDatas) {
        int id = Integer.parseInt(s,16);
        FaceText faceText = new FaceText();
        faceText.content = new String(Character.toChars(id));
        faceTextList.add(faceText);
      }
    }
    return faceTextList;
  }
}
