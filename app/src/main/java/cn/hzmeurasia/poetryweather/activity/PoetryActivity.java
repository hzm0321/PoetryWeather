package cn.hzmeurasia.poetryweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
    QMUIGroupListView mGroupListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poetry_activity);
        mGroupListView = findViewById(R.id.gg);
        QMUICommonListItemView qmuiCommonListItemView = mGroupListView.createItemView("item");
        qmuiCommonListItemView.setDetailText("hhahaa");

        QMUIGroupListView.newSection(this)
                .setDescription("wuwuwu")
                .addItemView(qmuiCommonListItemView,null)
                .addTo(mGroupListView);
    }
}
