package cn.hzmeurasia.poetryweather.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.db.PoetryDb;

/**
 * 类名: AddPoetryActivity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/27 22:02
 */
public class AddPoetryActivity extends AppCompatActivity {

    private static final String TAG = "AddPoetryActivity";
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.qmui_group_list_view)
    QMUIGroupListView mGroupListView;
    QMUITipDialog tipDialog;

    int[] weatherItems;
    int id ;
    String sFirstPoetry="",sSecondPotry="",sWeatherPoetry="";
    int qwxl=-1 ;
    int jygk=-1 ;
    int yyql=-1 ;

    @OnClick({R.id.btn_back,R.id.btn_finish})
    void onClick(View v){
        switch(v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_finish:
                checkPoetry();
                break;
            default:
                break;
        }
    }

    QMUICommonListItemView idPoetryListView;
    QMUICommonListItemView firstPoetryListView;
    QMUICommonListItemView secondPoetryListView;
    QMUICommonListItemView weatherPoetryListView;
    QMUICommonListItemView isqwxlPoetryListView;
    QMUICommonListItemView isjygkPoetryListView;
    QMUICommonListItemView isyyqlPoetryListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poetry_add_poetry_activity);
        //绑定初始化BufferKnife
        ButterKnife.bind(this);
        //初始化列表
        initGroupListView();
        checkId();
    }

    /**
     * 初始化列表
     */
    private void initGroupListView() {
        idPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddPoetryActivity.this,R.drawable.test),
                "id",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        idPoetryListView.setTag(R.id.listitem_tag_1);

        firstPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddPoetryActivity.this,R.drawable.test),
                "诗词上句",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        firstPoetryListView.setTag(R.id.listitem_tag_2);

        secondPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddPoetryActivity.this,R.drawable.test),
                "诗词下句",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        secondPoetryListView.setTag(R.id.listitem_tag_3);

        weatherPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddPoetryActivity.this,R.drawable.test),
                "匹配天气",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        weatherPoetryListView.setTag(R.id.listitem_tag_4);

        isqwxlPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddPoetryActivity.this,R.drawable.test),
                "清婉秀丽",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        isqwxlPoetryListView.setTag(R.id.listitem_tag_5);

        isjygkPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddPoetryActivity.this,R.drawable.test),
                "激越高亢",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        isjygkPoetryListView.setTag(R.id.listitem_tag_6);

        isyyqlPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddPoetryActivity.this,R.drawable.test),
                "语言绮丽",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        isyyqlPoetryListView.setTag(R.id.listitem_tag_7);

        QMUIGroupListView.newSection(this)
                .addItemView(idPoetryListView,null)
                .addItemView(firstPoetryListView,onClickListener)
                .addItemView(secondPoetryListView,onClickListener)
                .addItemView(weatherPoetryListView,onClickListener)
                .addItemView(isqwxlPoetryListView,onClickListener)
                .addItemView(isjygkPoetryListView,onClickListener)
                .addItemView(isyyqlPoetryListView,onClickListener)
                .addTo(mGroupListView);
    }

    /**
     * 诗词上句可输入对话框
     */
    private void showFirstPoetryEditTextDialog() {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(AddPoetryActivity.this);
        builder.setTitle("诗词上句")
                .setPlaceholder("请输入诗词,限制在10字以内")
                .setDefaultText(sFirstPoetry)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    CharSequence firstPoetry = builder.getEditText().getText();
                    if (firstPoetry != null && firstPoetry.length() > 0 && firstPoetry.length() <= 10) {
                        dialog.dismiss();
                        sFirstPoetry = firstPoetry.toString();
                        firstPoetryListView.setDetailText(sFirstPoetry);
                    } else if (firstPoetry.length() >= 10) {
                        Toast.makeText(AddPoetryActivity.this, "您输入的诗词过长,请限制在10个汉字以内", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddPoetryActivity.this, "您未输入任何内容,请重新输入", Toast.LENGTH_SHORT).show();
                    }
                })
                .create().show();

    }

    /**
     * 诗词上句可输入对话框
     */
    private void showSecondPoetryEditTextDialog() {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(AddPoetryActivity.this);
        builder.setTitle("诗词下句")
                .setPlaceholder("请输入诗词,限制在10字以内")
                .setDefaultText(sSecondPotry)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    CharSequence secondPoetry = builder.getEditText().getText();
                    if (secondPoetry != null && secondPoetry.length() > 0 && secondPoetry.length() <= 10) {
                        dialog.dismiss();
                        sSecondPotry = secondPoetry.toString();
                        secondPoetryListView.setDetailText(sSecondPotry);
                    } else if (secondPoetry.length() >= 10) {
                        Toast.makeText(AddPoetryActivity.this, "您输入的诗词过长,请限制在10个汉字以内", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddPoetryActivity.this, "您未输入任何内容,请重新输入", Toast.LENGTH_SHORT).show();
                    }
                })
                .create().show();

    }

    /**
     * 匹配天气多选输入框
     */
    private void showWeatherPoetryMultiChoiceDialog(){
        final String[] items = new String[]{
                "晴","多云","少云","晴间多云","阴","阵雨","强阵雨",
                "雷阵雨","小雨","中雨","大雨","暴雨","大暴雨","特大暴雨",
                "冻雨","小到中雨","中到大雨","大到暴雨","雨","小雪","中雪",
                "大雪","暴雪","雨夹雪","阵雪","雪","薄雾","雾","沙尘暴","大雾"};
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(AddPoetryActivity.this)
                .setCheckedItems(weatherItems)
                .addItems(items, (dialog, which) -> {

                });
        builder.addAction("取消", (dialog, index) -> dialog.dismiss());
        builder.addAction("提交", (dialog, index) -> {
            String text = "";
            sWeatherPoetry = "";
            weatherItems = builder.getCheckedItemIndexes();
            for (int i = 0; i < weatherItems.length; i++) {
                text += items[builder.getCheckedItemIndexes()[i]]+" ";
                sWeatherPoetry += items[builder.getCheckedItemIndexes()[i]];
            }
            Log.d(TAG, "sWeatherPoetry: "+sWeatherPoetry);
            weatherPoetryListView.setDetailText(text);
            dialog.dismiss();
        });
        builder.create().show();
    }

    /**
     * 清婉秀丽单选框
     */
    private void showQWXLSingleChoiceDialog(){
        final String[] items = new String[]{"否", "是"};
        QMUIDialog.CheckableDialogBuilder checkableDialogBuilder = new QMUIDialog.CheckableDialogBuilder(AddPoetryActivity.this);
        if (qwxl >= 0) {
            checkableDialogBuilder.setCheckedIndex(qwxl);
        }
        checkableDialogBuilder.addItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                qwxl = which;
                isqwxlPoetryListView.setDetailText(items[qwxl]);
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 激越高亢单选框
     */
    private void showJYGKSingleChoiceDialog(){
        final String[] items = new String[]{"否", "是"};
        QMUIDialog.CheckableDialogBuilder checkableDialogBuilder = new QMUIDialog.CheckableDialogBuilder(AddPoetryActivity.this);
        if (jygk >= 0) {
            checkableDialogBuilder.setCheckedIndex(jygk);
        }
        checkableDialogBuilder.addItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jygk = which;
                isjygkPoetryListView.setDetailText(items[jygk]);
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 语言绮丽单选框
     */
    private void showYYQLSingleChoiceDialog(){
        final String[] items = new String[]{"否", "是"};
        QMUIDialog.CheckableDialogBuilder checkableDialogBuilder = new QMUIDialog.CheckableDialogBuilder(AddPoetryActivity.this);
        if (yyql >= 0) {
            checkableDialogBuilder.setCheckedIndex(yyql);
        }
        checkableDialogBuilder.addItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yyql = which;
                isyyqlPoetryListView.setDetailText(items[yyql]);
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 点击事件监听
     */
    View.OnClickListener onClickListener = view -> {
        QMUICommonListItemView listItemView = (QMUICommonListItemView) view;
        int tag = (int) listItemView.getTag();
        switch(tag) {
            case R.id.listitem_tag_1:
                break;
            case R.id.listitem_tag_2:
                showFirstPoetryEditTextDialog();
                break;
            case R.id.listitem_tag_3:
                showSecondPoetryEditTextDialog();
                break;
            case R.id.listitem_tag_4:
                showWeatherPoetryMultiChoiceDialog();
                break;
            case R.id.listitem_tag_5:
                showQWXLSingleChoiceDialog();
                break;
            case R.id.listitem_tag_6:
                showJYGKSingleChoiceDialog();
                break;
            case R.id.listitem_tag_7:
                showYYQLSingleChoiceDialog();
                break;
            default:
                break;
        }
    };

    /**
     * 初始化id
     */
    private void checkId() {
        PoetryDb poetryDb = LitePal.findLast(PoetryDb.class);
        if (poetryDb.getPoetryDb_id() < 200) {
            id = 200;
            idPoetryListView.setDetailText("200");
        } else {
            id = poetryDb.getPoetryDb_id() + 1;
            idPoetryListView.setDetailText(String.valueOf(id));
        }
    }

    private void checkPoetry() {
        if (id >= 0 && !sFirstPoetry.isEmpty() && !sSecondPotry.isEmpty() && !sWeatherPoetry.isEmpty() &&
                qwxl >= 0 && jygk >= 0 && yyql >= 0) {
            tipDialog = new QMUITipDialog.Builder(AddPoetryActivity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord("添加成功")
                    .create();
            tipDialog.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tipDialog.dismiss();
                    finish();
                }
            }, 2000);
        } else {
            Toast.makeText(AddPoetryActivity.this,"提交失败,请检查每项是否已填写",Toast.LENGTH_SHORT).show();
        }
    }
}
