package com.meechao.detailflow.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Bind;
import com.meechao.detailflow.R;
import com.meechao.detailflow.adapter.RcvCollectionTopicAdapter;
import com.meechao.detailflow.layoutManeger.flowLayout.FlowLayoutManager;
import com.meechao.detailflow.utils.LogUtils;
import com.meechao.detailflow.utils.RcvInitUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Func：
 * Desc:
 * Author：JHF
 * Date：2018-01-23 15:21
 * Mail：jihaifeng@meechao.com
 */
public class RecyclerViewActivity extends BaseActivity {
  private static final String TAG = RecyclerViewActivity.class.getSimpleName().trim();
  @Bind (R.id.rcv_comment_topic) RecyclerView rcvCommentTopic;
  List<String> list = new ArrayList<>();

  @Override protected int getLayoutId() {
    return R.layout.activity_recycler_view;
  }

  @Override protected void initViewAndEvent() {

    View head = LayoutInflater.from(this).inflate(R.layout.activity_recycler_view, null, false);

    RcvCollectionTopicAdapter collectionTopicAdapter = new RcvCollectionTopicAdapter(list);
    RcvInitUtils.initFlowRcv(this, rcvCommentTopic, collectionTopicAdapter);

    collectionTopicAdapter.addHeaderView(head);


    list.add("#80后[858|话题]#");
    list.add("#单身贵族[860|话题]#");
    list.add("#90后[857|话题]#");
    list.add("#已婚[861|话题]#");
    RcvCollectionTopicAdapter adapter = new RcvCollectionTopicAdapter(list);
    RecyclerView rcv = head.findViewById(R.id.rcv_comment_topic);
    RcvInitUtils.initFlowRcv(this, rcv, adapter);

    collectionTopicAdapter.setNewData(list);
    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rcv.getLayoutParams();
    layoutParams.height = rcv.getMeasuredHeight();
    LogUtils.i(TAG, "initViewAndEvent: " + rcv.getLayoutManager().getHeight() + rcv.getHeight() + rcv.getMeasuredHeight());
    LogUtils.i(TAG, "initViewAndEvent: " + rcvCommentTopic.getLayoutManager().getHeight() + rcvCommentTopic.getHeight() + rcvCommentTopic.getMeasuredHeight());
    head.setLayoutParams(layoutParams);

    FlowLayoutManager layoutManager = (FlowLayoutManager) rcv.getLayoutManager();
    int lineNum = layoutManager.getLineRows();

    //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rcv.getLayoutParams();
    //layoutParams.height = ScreenUtil.dp2px(35)*lineNum;
    //rcvCollectionTopics.setLayoutParams(layoutParams);
  }
}
