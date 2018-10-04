package cn.hzmeurasia.poetryweather.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.List;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.entity.PersonEntity;

import static cn.hzmeurasia.poetryweather.MyApplication.getContext;

/**
 * 类名: PersonActivity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/3 11:48
 */
public class PersonActivity extends AppCompatActivity {
    private static final String TAG = "PersonActivity";
    private QMUIGroupListView mGroupListView;

    QMUICommonListItemView nameListView;
    QMUICommonListItemView sexListView;

    private String name;
    private String sex;
    private int sexFlag = 2;


    Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mGroupListView = findViewById(R.id.qmui_group_list_view);
        showPerson();
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


    /**
     * 初始化GroupListView
     */
    private void initGroupListView() {
        nameListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(PersonActivity.this,R.drawable.test),
                "姓名",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        nameListView.setTag(R.id.listitem_tag_1);
        nameListView.setDetailText(name);

        sexListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(PersonActivity.this, R.drawable.test),
                "性别",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        sexListView.setTag(R.id.listitem_tag_2);
        sexListView.setDetailText(sex);

        QMUIGroupListView.newSection(this)
                .addItemView(nameListView,onClickListener)
                .addItemView(sexListView,onClickListener)
                .addTo(mGroupListView);
    }

    /**
     * 查询person数据本地缓存
     */
    private void showPerson() {
        SharedPreferences sharedPreferences = getSharedPreferences("person", MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");
        sex = sharedPreferences.getString("sex", "");
    }

    /**
     * Person 单项数据写入
     * @param name
     * @param data
     */
    private void sharedPreferencesEdit(String name,String data) {
        SharedPreferences.Editor editor = getSharedPreferences("person", MODE_PRIVATE).edit();
        editor.putString(name, data);
        editor.apply();

    }

    /**
     * 点击事件监听
     */
    View.OnClickListener onClickListener = view -> {
        QMUICommonListItemView listItemView = (QMUICommonListItemView) view;
        int tag = (int) listItemView.getTag();
        switch(tag) {
            case R.id.listitem_tag_1:
                showEditTextDialog();
                break;
            case R.id.listitem_tag_2:
                showSingleDialog();
                break;
            default:
                break;
        }
    };

    /**
     * 可输入对话框
     */
    private void showEditTextDialog() {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(PersonActivity.this);
        builder.setTitle("修改姓名")
                .setPlaceholder("输入您希望修改的姓名")
                .setDefaultText(name)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    CharSequence name = builder.getEditText().getText();
                    Log.d(TAG, "name字符长度: "+name.length());
                    if (name != null && name.length() > 0 && name.length() < 10) {
                        sharedPreferencesEdit("name",name.toString());
                        dialog.dismiss();
                        nameListView.setDetailText(name.toString());
                        Toast.makeText(PersonActivity.this, "您的姓名已修改为" + name.toString(), Toast.LENGTH_SHORT).show();
                    } else if (name.length() >= 10) {
                        Toast.makeText(PersonActivity.this, "您输入的姓名过长,请限制在10个汉字以内", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PersonActivity.this, "您未输入任何内容,请重新输入", Toast.LENGTH_SHORT).show();
                    }
                })
                .create().show();

    }

    /**
     * 单选对话框
     */
    private void showSingleDialog() {
        final String[] items = new String[]{"男", "女", "无可奉告"};
        final int checkedIndex = sexFlag;
        new QMUIDialog.CheckableDialogBuilder(PersonActivity.this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sex = items[which];
                        sharedPreferencesEdit("sex",sex);
                        sexListView.setDetailText(sex);
                        sexFlag = which;
                        dialog.dismiss();
                        Toast.makeText(PersonActivity.this,"您的性别已修改为"+sex,Toast.LENGTH_SHORT).show();

                    }
                })
                .create().show();
    }
}
