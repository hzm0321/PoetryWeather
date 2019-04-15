package cn.hzmeurasia.poetryweather.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.db.PoetryDb;

/**
 * 类名: AddAndEditPoetryActivity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/27 22:02
 */
public class AddAndEditPoetryActivity extends AppCompatActivity {

    private static final String TAG = "AddAndEditPoetryActivity";
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_finish)
    ImageButton btnFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.qmui_group_list_view)
    QMUIGroupListView mGroupListView;
    QMUITipDialog tipDialog;

    int[] weatherItems;
    int id ;
    String sFirstPoetry="",sSecondPotry="",sWeatherPoetry="";
    int qwxl=-1 ;
    int jygk=-1 ;
    int yyql=-1 ;
    String checkIntent,tipText;
    String[] items = new String[]{
            "晴","多云","少云","晴间多云","阴","阵雨","强阵雨",
            "雷阵雨","小雨","中雨","大雨","暴雨","大暴雨","特大暴雨",
            "冻雨","小到中雨","中到大雨","大到暴雨","雨","小雪","中雪",
            "大雪","暴雪","雨夹雪","阵雪","雪","薄雾","雾","沙尘暴","大雾"};

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
        if (isEdit()) {
            tvTitle.setText("编辑自定义诗词");
            tipText = "修改成功";
        } else {
            checkId();
            tvTitle.setText("添加自定义诗词");
            tipText = "添加成功";
        }
        //初始化列表
        initGroupListView();

    }



    @SuppressLint("LongLogTag")
    private boolean isEdit() {
        Intent intent = getIntent();
        checkIntent = intent.getStringExtra("addOrEdit");
        if ("edit".equals(checkIntent)) {
            String editId = intent.getStringExtra("editId");
            Log.d(TAG, "检查传入的: ID "+editId);
            List<PoetryDb> poetryDbs = LitePal.where("poetryDb_id=?",editId)
                                              .find(PoetryDb.class);
            Log.d(TAG, "isEdit: 传入查到的数据库"+poetryDbs.size());
            PoetryDb poetryDb = poetryDbs.get(0);
            Log.d(TAG, "isEdit: id " + poetryDb.getPoetryDb_id());
            id = poetryDb.getPoetryDb_id();
            String[] sPoetry = poetryDb.getPoetryDb_poetry().split(",");
            sFirstPoetry = sPoetry[0];
            sSecondPotry = sPoetry[1];
            sWeatherPoetry = poetryDb.getPoetryDb_weather();
//            String[] strings = sWeatherPoetry.split(" ");
//            Log.d(TAG, "isEdit: string长度 "+strings.length);
//            weatherItems = new int[strings.length];
//            for (int i = 0; i < strings.length; i++) {
//                weatherItems[i] =
//                Log.d(TAG, "isEdit: strings "+strings[i]);
//                Log.d(TAG, "isEdit: 返回的索引位置"+ Arrays.binarySearch(items,strings[i]));
//            }
//            qwxl = poetryDb.getPoetryDb_qwxl();
//            jygk = poetryDb.getPoetryDb_jygk();
//            yyql = poetryDb.getPoetryDb_yyql();
            return true;
        }
        return false;
    }

    /**
     * 初始化id
     */
    private void checkId() {
        PoetryDb poetryDb = LitePal.findLast(PoetryDb.class);
        if (poetryDb == null) {
            id = 1;
        } else {
            id = poetryDb.getPoetryDb_id() + 1;
        }
    }

    /**
     * 初始化列表
     */
    @SuppressLint("LongLogTag")
    private void initGroupListView() {
        idPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddAndEditPoetryActivity.this,R.drawable.item_icon00),
                "id",
                String.valueOf(id),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        idPoetryListView.setTag(R.id.listitem_tag_1);

        firstPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddAndEditPoetryActivity.this,R.drawable.item_icon01),
                "诗词上句",
                sFirstPoetry,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        firstPoetryListView.setTag(R.id.listitem_tag_2);

        secondPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddAndEditPoetryActivity.this,R.drawable.item_icon02),
                "诗词下句",
                sSecondPotry,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        secondPoetryListView.setTag(R.id.listitem_tag_3);

        weatherPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddAndEditPoetryActivity.this,R.drawable.item_icon03),
                "匹配天气",
                sWeatherPoetry,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        weatherPoetryListView.setTag(R.id.listitem_tag_4);

        isqwxlPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddAndEditPoetryActivity.this,R.drawable.item_icon04),
                "清婉秀丽",
                qwxl==-1?null:(qwxl==0?"否":"是"),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Log.d(TAG, "initGroupListView: 清婉秀丽"+qwxl);
        isqwxlPoetryListView.setTag(R.id.listitem_tag_5);

        isjygkPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddAndEditPoetryActivity.this,R.drawable.item_icon05),
                "激越高亢",
                jygk==-1?null:(jygk==0?"否":"是"),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        isjygkPoetryListView.setTag(R.id.listitem_tag_6);

        isyyqlPoetryListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(AddAndEditPoetryActivity.this,R.drawable.item_icon06),
                "语言绮丽",
                yyql==-1?null:(yyql==0?"否":"是"),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        isyyqlPoetryListView.setTag(R.id.listitem_tag_7);

        QMUIGroupListView.newSection(this)
                .addItemView(idPoetryListView,null)
                .addItemView(firstPoetryListView,onClickListener)
                .addItemView(secondPoetryListView,onClickListener)
                .addItemView(weatherPoetryListView,onClickListener)
//                .addItemView(isqwxlPoetryListView,onClickListener)
//                .addItemView(isjygkPoetryListView,onClickListener)
//                .addItemView(isyyqlPoetryListView,onClickListener)
                .addTo(mGroupListView);
    }

    /**
     * 诗词上句可输入对话框
     */
    private void showFirstPoetryEditTextDialog() {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(AddAndEditPoetryActivity.this);
        builder.setTitle("诗词上句")
                .setPlaceholder("请输入诗词,限制在10字以内")
                .setDefaultText(sFirstPoetry)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    CharSequence firstPoetry = builder.getEditText().getText();
                    if (firstPoetry != null && firstPoetry.length() > 0 && firstPoetry.length() <= 10) {
                        String str = stringFilter(firstPoetry.toString());
                        if (!firstPoetry.toString().equals(str)) {
                            Toast.makeText(AddAndEditPoetryActivity.this,"请输入纯汉字",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialog.dismiss();
                        sFirstPoetry = firstPoetry.toString();
                        firstPoetryListView.setDetailText(sFirstPoetry);
                    } else if (firstPoetry.length() >= 10) {
                        Toast.makeText(AddAndEditPoetryActivity.this, "您输入的诗词过长,请限制在10个汉字以内", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddAndEditPoetryActivity.this, "您未输入任何内容,请重新输入", Toast.LENGTH_SHORT).show();
                    }
                })
                .create().show();

    }

    /**
     * 诗词下句可输入对话框
     */
    private void showSecondPoetryEditTextDialog() {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(AddAndEditPoetryActivity.this);
        builder.setTitle("诗词下句")
                .setPlaceholder("请输入诗词,限制在10字以内")
                .setDefaultText(sSecondPotry)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    CharSequence secondPoetry = builder.getEditText().getText();
                    if (secondPoetry != null && secondPoetry.length() > 0 && secondPoetry.length() <= 10) {
                        String str = stringFilter(secondPoetry.toString());
                        if (!secondPoetry.toString().equals(str)) {
                            Toast.makeText(AddAndEditPoetryActivity.this,"请输入纯汉字",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialog.dismiss();
                        sSecondPotry = secondPoetry.toString();
                        secondPoetryListView.setDetailText(sSecondPotry);
                    } else if (secondPoetry.length() >= 10) {
                        Toast.makeText(AddAndEditPoetryActivity.this, "您输入的诗词过长,请限制在10个汉字以内", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddAndEditPoetryActivity.this, "您未输入任何内容,请重新输入", Toast.LENGTH_SHORT).show();
                    }
                })
                .create().show();

    }

    /**
     * 匹配天气多选输入框
     */
    @SuppressLint("LongLogTag")
    private void showWeatherPoetryMultiChoiceDialog(){
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(AddAndEditPoetryActivity.this)
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
                sWeatherPoetry += items[builder.getCheckedItemIndexes()[i]] + " ";
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
        QMUIDialog.CheckableDialogBuilder checkableDialogBuilder = new QMUIDialog.CheckableDialogBuilder(AddAndEditPoetryActivity.this);
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
        QMUIDialog.CheckableDialogBuilder checkableDialogBuilder = new QMUIDialog.CheckableDialogBuilder(AddAndEditPoetryActivity.this);
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
        QMUIDialog.CheckableDialogBuilder checkableDialogBuilder = new QMUIDialog.CheckableDialogBuilder(AddAndEditPoetryActivity.this);
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
//            case R.id.listitem_tag_5:
//                showQWXLSingleChoiceDialog();
//                break;
//            case R.id.listitem_tag_6:
//                showJYGKSingleChoiceDialog();
//                break;
//            case R.id.listitem_tag_7:
//                showYYQLSingleChoiceDialog();
//                break;
            default:
                break;
        }
    };



    @SuppressLint("LongLogTag")
    private void checkPoetry() {
        if (id >= 0 && !sFirstPoetry.isEmpty() && !sSecondPotry.trim().isEmpty() && !sWeatherPoetry.trim().isEmpty() ) {
            tipDialog = new QMUITipDialog.Builder(AddAndEditPoetryActivity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord(tipText)
                    .create();
            tipDialog.show();

                PoetryDb poetryDb = new PoetryDb();
                poetryDb.setPoetryDb_id(id);
                poetryDb.setPoetryDb_poetry(sFirstPoetry.trim() + "," + sSecondPotry.trim());
                poetryDb.setPoetryDb_weather(sWeatherPoetry);
//                poetryDb.setPoetryDb_qwxl(qwxl);
//                poetryDb.setPoetryDb_jygk(jygk);
//                poetryDb.setPoetryDb_yyql(yyql);

            Log.d(TAG, "checkPoetry: 清婉秀丽"+qwxl);
            if ("edit".equals(checkIntent)) {
                poetryDb.updateAll("poetryDb_id = ?", String.valueOf(id));
            } else {
                poetryDb.save();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tipDialog.dismiss();
                    finish();
                }
            }, 1000);
        } else {
            Toast.makeText(AddAndEditPoetryActivity.this,"提交失败,请检查每项是否已填写",Toast.LENGTH_SHORT).show();
        }
    }

    public static String stringFilter(String str)throws PatternSyntaxException {
        // 只允许字母、数字和汉字      
        String regEx = "[^\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
    return m.replaceAll("").trim();
    }

}
