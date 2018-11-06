package cn.hzmeurasia.poetryweather.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.adapter.OwnPoetryAdapter;
import cn.hzmeurasia.poetryweather.db.PoetryDb;

/**
 * 类名: PoetryActivity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/4 21:30
 */
public class PoetryActivity extends AppCompatActivity {
    private static final String TAG = "PoetryActivity";
    @BindView(R.id.qmui_group_list_view)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_ownPoetry)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.ib_photo)
    FloatingActionButton ibPhoto;
    private List<PoetryDb> poetryDbList = new ArrayList<>();
    private OwnPoetryAdapter ownPoetryAdapter;

    QMUICommonListItemView officialPoetryNumber;
    QMUICommonListItemView ownPoetryNumber;
    QMUICommonListItemView addOwnPoetry;
    Animation rotate;
    @OnClick({R.id.ib_photo})
    void onClick(View v) {
        switch(v.getId()) {
            case R.id.ib_photo:
                poetryDbList.clear();
                List<PoetryDb> poetryDbList1 = LitePal.where("poetryDb_id >= ?", "200").find(PoetryDb.class);
                for (PoetryDb poetryDb : poetryDbList1) {
                    poetryDbList.add(poetryDb);
                }
                ownPoetryAdapter.notifyDataSetChanged();
                rotate = AnimationUtils.loadAnimation(PoetryActivity.this,R.anim.widget_rotate);
                rotate.setFillAfter(true);
                ibPhoto.startAnimation(rotate);
                int officialNumbers = LitePal.where("poetryDb_id<?","200").count(PoetryDb.class);
                int ownNumbers = LitePal.where("poetryDb_id >= ?", "200").count(PoetryDb.class);
                officialPoetryNumber.setDetailText(String.valueOf(officialNumbers));
                ownPoetryNumber.setDetailText(String.valueOf(ownNumbers));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PoetryActivity.this,"已同步数据库数据",Toast.LENGTH_SHORT).show();
                    }
                },2000);
                 break;
            default:
                break;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poetry_activity);
        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initGroupListView();
        showSwipeRecycleView();
    }


    /**
     * swipeRecycleView处理
     */
    private void showSwipeRecycleView() {
        //初始化自定义诗词列表
        poetryDbList = LitePal.where("poetryDb_id >= ?", "200").find(PoetryDb.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ownPoetryAdapter = new OwnPoetryAdapter(poetryDbList);

        //获取手机屏幕高度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        recyclerView.addItemDecoration(new DefaultItemDecoration(getResources().getColor(R.color.gray),width,1));
        //滑动菜单
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                SwipeMenuItem editItem = new SwipeMenuItem(PoetryActivity.this)
                        .setText("编辑")
                        .setWidth(200)
                        .setHeight(height)
                        .setTextSize(16)
                        .setBackground(R.color.bluishWhite);
                SwipeMenuItem deleteItem = new SwipeMenuItem(PoetryActivity.this)
                        .setText("删除")
                        .setTextColorResource(R.color.qmui_config_color_white)
                        .setWidth(200)
                        .setHeight(height)
                        .setTextSize(16)
                        .setBackground(R.color.qmui_config_color_red);
                swipeLeftMenu.addMenuItem(editItem);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        };
        //设置监听
        SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                // 左侧还是右侧菜单。
                int direction = menuBridge.getDirection();
                // RecyclerView的Item的position。
                int adapterPosition = menuBridge.getAdapterPosition();
                // 菜单在RecyclerView的Item中的Position。
                int menuPosition = menuBridge.getPosition();
                Log.d(TAG, "onItemClick: 位置"+adapterPosition);
                if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                    Intent intent = new Intent(PoetryActivity.this, AddAndEditPoetryActivity.class);
                    intent.putExtra("addOrEdit","edit");
                    int editId = poetryDbList.get(adapterPosition).getPoetryDb_id();
                    intent.putExtra("editId",editId+"");
                    startActivity(intent);
                } else if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                    final boolean[] isCancle = {false};
                    Handler handler = new Handler();
                    Snackbar.make(recyclerView,"正在删除该条诗词数据",Snackbar.LENGTH_SHORT)
                            .setAction("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isCancle[0] = true;
                                    Toast.makeText(PoetryActivity.this,"已取消删除",Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isCancle[0]) {
                                int deleteId = poetryDbList.get(adapterPosition).getPoetryDb_id();
                                LitePal.deleteAll(PoetryDb.class, "poetryDb_id = ?", String.valueOf(deleteId));
                                poetryDbList.remove(adapterPosition);
                                ownPoetryAdapter.notifyItemRemoved(adapterPosition);
                            }
                        }
                    },1800);

                }

            }
        };
        recyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        recyclerView.setAdapter(ownPoetryAdapter);
    }


    /**
     * 返回按钮
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化GroupList
     */
    private void initGroupListView() {
        int officialNumbers = LitePal.where("poetryDb_id<?","200").count(PoetryDb.class);
        int ownNumbers = LitePal.where("poetryDb_id >= ?", "200").count(PoetryDb.class);
        officialPoetryNumber = mGroupListView.createItemView(
                ContextCompat.getDrawable(PoetryActivity.this,R.drawable.item_icon05),
                "官方诗词数",
                String.valueOf(officialNumbers),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        officialPoetryNumber.setTag(R.id.listitem_tag_1);

        ownPoetryNumber = mGroupListView.createItemView(
                ContextCompat.getDrawable(PoetryActivity.this,R.drawable.item_icon06),
                "自定义诗词数",
                String.valueOf(ownNumbers),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        ownPoetryNumber.setTag(R.id.listitem_tag_2);

        addOwnPoetry = mGroupListView.createItemView(
                ContextCompat.getDrawable(PoetryActivity.this,R.drawable.item_icon07),
                "添加自定义诗词",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        addOwnPoetry.setTag(R.id.listitem_tag_3);

        QMUIGroupListView.newSection(this)
                .setTitle("本地诗词数据库")
                .addItemView(officialPoetryNumber,null)
                .addItemView(ownPoetryNumber,null)
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(this)
                .setTitle("自定义诗词数据库")
                .addItemView(addOwnPoetry,onClickListener)
                .addTo(mGroupListView);

    }

    /**
     * 点击事件监听
     */
    View.OnClickListener onClickListener = view -> {
        QMUICommonListItemView listItemView = (QMUICommonListItemView) view;
        int tag = (int) listItemView.getTag();
        switch(tag) {
            case R.id.listitem_tag_3:
                Intent intent = new Intent(PoetryActivity.this, AddAndEditPoetryActivity.class);
                intent.putExtra("addOrEdit","add");
                startActivity(intent);
                break;
            default:
                break;
        }
    };
}
