# SmartKeyboardManager

[![](https://jitpack.io/v/HelloVass/LeaveMessageBoardDemo.svg)](https://jitpack.io/#HelloVass/LeaveMessageBoardDemo)
## 优雅地切换表情键盘和软键盘

> Thanks for [Android: 实现表情输入键盘的另外一种思路](http://www.dss886.com/android/2015/12/16/14-01/) 这篇文章！



## 改进

1. 使用 Android 原生属性动画 Api 解决了**锁定** LinearLayout 高度的问题

2. 加入 Alpha 属性动画改进**切换键盘**时候的用户体验

3. **过滤快速点击事件**，机智如我

4. 增加 `OnContentViewScrollListener`,在键盘弹出或隐藏时，优雅地滚动你的 `ContentView`
 

## 使用
1.根布局 `LinearLayout`，高度可变化的 `ContentView`（例如 RecyclerView、ListView...），将 `ContentView` 的属性设置为

``` 
    <android.support.v7.widget.RecyclerView
      android:id="@+id/rcv_list"
      android:layout_width="match_parent"
      android:layout_height="0dp"
      android:layout_weight="1" />
```

2.自定义**表情键盘**部分

``` 
    <include
      android:id="@+id/reply_layout"
      layout="@layout/include_msg_board_reply_layout"
      android:layout_width="match_parent"
      android:layout_height="wrap_content" />
```

3.使用 `SmartKeyboardManager`

``` 

mSmartKeyboardManager = new SmartKeyboardManager.Builder(this).setContentView(mRecyclerView)
        .setEmotionKeyboard(mFaceTextInputLayout) // 表情键盘View
        .setEditText(mFaceTextEmotionEditText) // 输入框
        .setFaceTextEmotionTrigger(mFaceTextEmotionTrigger) // 表情键盘和软键盘的切换按钮
        .addOnContentViewScrollListener(new SmartKeyboardManager.OnContentViewScrollListener() {
          @Override public void shouldScroll(int distance) {
            mRecyclerView.scrollBy(0, distance); // 将 recyclerview 滚动相应的距离，内部已经把滚动的方向问题处理好了，大胆使用吧
          }
        .create();
```

## Tips

重写 Activity 的 onBackPressed 方法，使用 mSmartKeyboardManager.interceptBackPressed() 方法判断是否**拦截**返回键操作！

``` 
 @Override public void onBackPressed() {
    if (!mSmartKeyboardManager.interceptBackPressed()) {
      super.onBackPressed();
    }
  }
```


