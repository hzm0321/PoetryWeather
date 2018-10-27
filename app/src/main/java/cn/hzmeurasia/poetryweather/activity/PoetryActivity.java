package cn.hzmeurasia.poetryweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hzmeurasia.poetryweather.R;

/**
 * 类名: PoetryActivity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/4 21:30
 */
public class PoetryActivity extends AppCompatActivity {
    @BindView(R.id.qmui_group_list_view)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    QMUICommonListItemView officialPoetryNumber;
    QMUICommonListItemView ownPoetryNumber;
    QMUICommonListItemView addOwnPoetryNumber;
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

    private void initGroupListView() {
        officialPoetryNumber = mGroupListView.createItemView(
                ContextCompat.getDrawable(PoetryActivity.this,R.drawable.test),
                "官方诗词数",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        officialPoetryNumber.setTag(R.id.listitem_tag_1);

        ownPoetryNumber = mGroupListView.createItemView(
                ContextCompat.getDrawable(PoetryActivity.this,R.drawable.test),
                "自定义诗词数",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        ownPoetryNumber.setTag(R.id.listitem_tag_2);

        addOwnPoetryNumber = mGroupListView.createItemView(
                ContextCompat.getDrawable(PoetryActivity.this,R.drawable.test),
                "添加自定义诗词",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        addOwnPoetryNumber.setTag(R.id.listitem_tag_3);

        QMUIGroupListView.newSection(this)
                .setTitle("本地诗词数据库")
                .addItemView(officialPoetryNumber,null)
                .addItemView(ownPoetryNumber,null)
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(this)
                .setTitle("自定义诗词数据库")
                .addItemView(addOwnPoetryNumber,onClickListener)
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
                Intent intent = new Intent(PoetryActivity.this, AddPoetryActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    };
}
