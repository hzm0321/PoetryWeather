package cn.hzmeurasia.poetryweather.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.adapter.PageAdapter;
import cn.hzmeurasia.poetryweather.adapter.PoetryTypeAdapter;
import cn.hzmeurasia.poetryweather.entity.PoetryDetail;
import cn.hzmeurasia.poetryweather.util.PrefUtils;

public class PoetryDetailActivity extends AppCompatActivity {


    private static final String TAG = "PoetryDetailActivity";
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_poetry_title)
    TextView tvTitle;
    @BindView(R.id.tv_poetry_write)
    TextView tvWrite;
    @BindView(R.id.tv_poetry_content)
    TextView tvContent;
    @BindView(R.id.tab_poetry)
    TabLayout tabLayout;
    @BindView(R.id.vp_poetry)
    ViewPager viewPager;
//    @BindView(R.id.tv_poetry_detail_translation)
//    TextView tvTranslation;
//    @BindView(R.id.tv_poetry_detail_remark)
//    TextView tvRemark;
//    @BindView(R.id.tv_poetry_detail_shangxi)
//    TextView tvShangxi;

    PoetryDetail poetryDetail;//拿到的详细诗词数据


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poetry_detail);
        //绑定初始化BufferKnife
        ButterKnife.bind(this);
        //初始化状态栏
        poetryDetail = PrefUtils.getPoetryDetail(PoetryDetailActivity.this);
        Log.d(TAG, "拿到的关键词" + poetryDetail.type);
        initTopBar();
        initText();
        initType();
        initTab();
    }

    //初始化状态栏
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTopBar.setTitle("诗词鉴赏");
        mTopBar.setBackgroundColor(getResources().getColor(R.color.bluishWhite,getTheme()));
    }

    private void initType() {
        RecyclerView recyclerView = findViewById(R.id.rv_keyWord);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        PoetryTypeAdapter adapter = new PoetryTypeAdapter(poetryDetail.type);
        recyclerView.setAdapter(adapter);
    }

    private void initText(){
        tvTitle.setText(poetryDetail.title);
        tvWrite.setText("["+poetryDetail.dynasty+"]"+" "+poetryDetail.writer);
        tvContent.setText(poetryDetail.content);
//        tvTranslation.setText(poetryDetail.translation);
//        tvRemark.setText(poetryDetail.remark);
//        tvShangxi.setText(poetryDetail.shangxi);
    }

    private void initTab() {
        //往tab中添加内容
        tabLayout.addTab(tabLayout.newTab().setText("译文"));
        tabLayout.addTab(tabLayout.newTab().setText("注释"));
        tabLayout.addTab(tabLayout.newTab().setText("赏析"));
        //设置adapter滑动时间
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),poetryDetail));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //绑定tab点击事件
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }





}
