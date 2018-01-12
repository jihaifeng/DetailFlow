package com.meechao.detailflow.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.meechao.detailflow.R;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2017-11-04-0004 13:23
 * Mail：jihaifeng@meechao.com
 */
@SuppressLint ("StaticFieldLeak") public class ImageUtils {
  private static Context mContext;

  public static void init(Context context) {
    mContext = context;
  }

  public static void load(String imageUrl, ImageView view) {
    Glide.with(mContext)
        .load(TextUtils.isEmpty(imageUrl) ? R.drawable.ic_image_loading : imageUrl)
        .centerCrop()
        .error(R.drawable.ic_image_load_error)
        .placeholder(R.drawable.ic_image_loading)
        .into(view);
  }
}