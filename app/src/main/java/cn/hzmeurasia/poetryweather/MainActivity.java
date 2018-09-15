package cn.hzmeurasia.poetryweather;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import cn.hzmeurasia.poetryweather.adapter.CardAdapter;
import cn.hzmeurasia.poetryweather.entity.CardEntity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private CardEntity[] cardEntities = {new CardEntity("京兆府", "(西安)", "大雨", "16°",R.drawable.bg),
            new CardEntity("应天府", "(商丘)", "多云", "26°",R.drawable.bg)};
    private List<CardEntity> cardEntityList = new ArrayList<>();
    private CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //启用toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //载入左滑提示按钮
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //显示出按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            //载入按钮图片
            actionBar.setHomeAsUpIndicator(R.drawable.ic_person1);
        }
        //初始化Card布局
        initCard();
        RecyclerView recyclerView = findViewById(R.id.rv_main);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        cardAdapter = new CardAdapter(cardEntityList);
        recyclerView.setAdapter(cardAdapter);
    }

    private void initCard() {
        cardEntityList.clear();
        for (int i = 0; i < cardEntities.length; i++) {
            cardEntityList.add(cardEntities[i]);
        }
    }
}