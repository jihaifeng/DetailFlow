package com.meechao.detailflow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.meechao.detailflow.fragment.ItemDetailFragment;
import com.meechao.detailflow.R;
import com.meechao.detailflow.dummy.DummyContent;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;
  private LinearLayoutManager mLinearLayoutManager;
  private RecyclerView recyclerView;
  private boolean move = false;
  private int mIndex = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    final Button btn = findViewById(R.id.btn);

    if (findViewById(R.id.item_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;
    }

    recyclerView = findViewById(R.id.item_list);

    assert recyclerView != null;
    setupRecyclerView(recyclerView);
    mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //在这里进行第二次滚动（最后的100米！）
        if (move) {
          move = false;
          //最后的移动
          recyclerView.scrollBy(0, -(btn.getBottom() + 20));
        }
      }
    });
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        moveToPosition(2);
      }
    });
    btn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(ItemListActivity.this,KeyBoardActivity.class));
      }
    });
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
  }

  private void moveToPosition(int n) {
    mIndex = n;
    //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
    int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
    int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
    move = true;
    //然后区分情况
    if (n <= firstItem) {
      //当要置顶的项在当前显示的第一个项的前面时
      recyclerView.scrollToPosition(n);
    } else if (n <= lastItem) {
      //当要置顶的项已经在屏幕上显示时
      int top = recyclerView.getChildAt(n - firstItem).getTop();
      recyclerView.scrollBy(0, top);
    } else {
      //当要置顶的项在当前显示的最后一项的后面时
      recyclerView.scrollToPosition(n);
      //这里这个变量是用在RecyclerView滚动监听里面的
      move = true;
    }
  }

  public static class SimpleItemRecyclerViewAdapter
      extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final ItemListActivity mParentActivity;
    private final List<DummyContent.DummyItem> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
      @Override public void onClick(View view) {
        DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
        if (mTwoPane) {
          Bundle arguments = new Bundle();
          arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
          ItemDetailFragment fragment = new ItemDetailFragment();
          fragment.setArguments(arguments);
          mParentActivity.getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.item_detail_container, fragment)
              .commit();
        } else {
          Context context = view.getContext();
          Intent intent = new Intent(context, ItemDetailActivity.class);
          intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

          context.startActivity(intent);
        }
      }
    };

    SimpleItemRecyclerViewAdapter(ItemListActivity parent, List<DummyContent.DummyItem> items, boolean twoPane) {
      mValues = items;
      mParentActivity = parent;
      mTwoPane = twoPane;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
      holder.mIdView.setText(mValues.get(position).id);
      holder.mContentView.setText(mValues.get(position).content);

      holder.itemView.setTag(mValues.get(position));
      holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override public int getItemCount() {
      return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      final TextView mIdView;
      final TextView mContentView;

      ViewHolder(View view) {
        super(view);
        mIdView = (TextView) view.findViewById(R.id.id_text);
        mContentView = (TextView) view.findViewById(R.id.content);
      }
    }
  }
}
