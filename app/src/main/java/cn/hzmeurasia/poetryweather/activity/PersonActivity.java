package cn.hzmeurasia.poetryweather.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.entity.PersonEntity;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.hzmeurasia.poetryweather.MyApplication.getContext;

/**
 * 类名: PersonActivity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/3 11:48
 */
public class PersonActivity extends AppCompatActivity {
    private static final String TAG = "PersonActivity";
    @BindView(R.id.qmui_group_list_view)
    QMUIGroupListView mGroupListView;

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    QMUICommonListItemView nameListView;
    QMUICommonListItemView sexListView;
    QMUICommonListItemView signatureListView;
    QMUICommonListItemView preferenceListView;
    QMUICommonListItemView refreshForWeatherListView;
    private String name;
    private String sex;
    private String signature;
    private String preference;
    private String refreshForWeather;

    private int sexFlag = 2;
    private int refreshFlag = 3;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ib_photo)
    FloatingActionButton ib_photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);
        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.head).into(ib_photo);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        nameListView.setTag(R.id.listitem_tag_1);
        nameListView.setDetailText(name);

        sexListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(PersonActivity.this, R.drawable.test),
                "性别",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        sexListView.setTag(R.id.listitem_tag_2);
        sexListView.setDetailText(sex);

        signatureListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(PersonActivity.this, R.drawable.test),
                "个性签名",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        signatureListView.setTag(R.id.listitem_tag_3);
        signatureListView.setDetailText(signature);

        preferenceListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(PersonActivity.this, R.drawable.test),
                "诗词偏爱",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        preferenceListView.setTag(R.id.listitem_tag_4);
        preferenceListView.setDetailText(preference);

        refreshForWeatherListView = mGroupListView.createItemView(
                ContextCompat.getDrawable(PersonActivity.this,R.drawable.test),
                "自动刷新间隔",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        refreshForWeatherListView.setTag(R.id.listitem_tag_5);
        refreshForWeatherListView.setDetailText(refreshForWeather);



        QMUIGroupListView.newSection(this)
                .setTitle("个人信息")
                .setDescription("诗词示例:\n" +
                        "衣带渐宽终不悔，\n" +
                        "为伊消得人憔悴。")
                .addItemView(nameListView,onClickListener)
                .addItemView(sexListView,onClickListener)
                .addItemView(signatureListView,onClickListener)
                .addItemView(preferenceListView,onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(this)
                .setTitle("偏好设置")
                .addItemView(refreshForWeatherListView, onClickListener)
                .addTo(mGroupListView);

    }

    /**
     * 查询person数据本地缓存
     */
    private void showPerson() {
        SharedPreferences sharedPreferences = getSharedPreferences("person", MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");
        sex = sharedPreferences.getString("sex", "保密");
        sexFlag = sharedPreferences.getInt("sexFlag", 2);
        signature = sharedPreferences.getString("signature", "");
        preference = sharedPreferences.getString("preference", "");
        refreshFlag = sharedPreferences.getInt("refreshFlag",3);
        refreshForWeather = sharedPreferences.getString("refreshForWeather","每8小时");
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
            case R.id.listitem_tag_3:
                showLongEditDialog();
                break;
            case R.id.listitem_tag_4:
                showMultiChoiceDialog();
                break;
            case R.id.listitem_tag_5:
                showRefreshSingleDialog();
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
        final String[] items = new String[]{"男", "女", "保密"};
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
                        SharedPreferences.Editor editor = getSharedPreferences("person", MODE_PRIVATE).edit();
                        editor.putInt("sexFlag", sexFlag);
                        editor.apply();
                        dialog.dismiss();
                        Toast.makeText(PersonActivity.this,"您的性别已修改为"+sex,Toast.LENGTH_SHORT).show();

                    }
                })
                .create().show();
    }

    /**
     * 长文本输入对话框
     */
    private void showLongEditDialog() {
        QMAutoTestDialogBuilder autoTestDialogBuilder = new QMAutoTestDialogBuilder(PersonActivity.this);
        autoTestDialogBuilder
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    signature = autoTestDialogBuilder.getEditText().getText().toString();
                    if (signature.length() < 50) {
                        Toast.makeText(PersonActivity.this, "您的个性签名已修改", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "showLongEditDialog: signature" + signature);
                        sharedPreferencesEdit("signature", signature);
                        signatureListView.setDetailText(signature);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(PersonActivity.this,"您输入的字数超出限制,请重新输入",Toast.LENGTH_SHORT).show();
                    }
                });

        autoTestDialogBuilder.create(mCurrentDialogStyle).show();
        QMUIKeyboardHelper.showKeyboard(autoTestDialogBuilder.getEditText(),true);
    }

    class QMAutoTestDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
        private Context mContext;
        private EditText mEditText;
        public QMAutoTestDialogBuilder(Context context) {
            super(context);
            mContext = context;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout.setPadding(padding, padding, padding, padding);
            mEditText = new EditText(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mEditText, QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            mEditText.setHint("在此输入您的个性签名,字数限制在50个以内");
            mEditText.setText(signature
            );
            LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(100));
            editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(getContext(), 15);
            mEditText.setLayoutParams(editTextLP);
            layout.addView(mEditText);
            TextView textView = new TextView(mContext);
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext(), 4), 1.0f);
            textView.setText("推荐输入诗词作为个性签名。\n" +
                    "例如:\n" +
                    "众里寻他千百度,蓦然回首,那人却在灯火阑珊处。");
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(textView);
            return layout;
        }
    }

    /**
     * 多选框弹窗
     */
    private void showMultiChoiceDialog() {
        final String[] items = new String[]{"平实", "含蓄", "清新", "飘逸", "豪放", "沉郁","缠绵"};
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(PersonActivity.this)
                .addItems(items, (dialog, which) -> {});
        builder.addAction("取消", (dialog, index) -> dialog.dismiss());
        builder.addAction("提交", (dialog, index) -> {
            String result = "您选择了 ";
            preference = "";
            for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                result += "" + items[builder.getCheckedItemIndexes()[i]] + " ";
                preference += items[builder.getCheckedItemIndexes()[i]] + " ";
            }
            sharedPreferencesEdit("preference",preference);
            preferenceListView.setDetailText(preference);
            Toast.makeText(PersonActivity.this, result, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        builder.create(mCurrentDialogStyle).show();
    }

    /**
     * 单选对话框
     */
    private void showRefreshSingleDialog() {
        final String[] items = new String[]{"每1小时", "每2小时", "每4小时","每8小时"};
        final int checkedIndex = refreshFlag;
        new QMUIDialog.CheckableDialogBuilder(PersonActivity.this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refreshForWeather = items[which];
                        sharedPreferencesEdit("refreshForWeather",refreshForWeather);
                        refreshForWeatherListView.setDetailText(refreshForWeather);
                        refreshFlag = which;
                        SharedPreferences.Editor editor = getSharedPreferences("person", MODE_PRIVATE).edit();
                        editor.putInt("refreshFlag", refreshFlag);
                        editor.apply();
                        dialog.dismiss();
                        Toast.makeText(PersonActivity.this,"城市列表天气自动刷新间隔为"+refreshForWeather,Toast.LENGTH_SHORT).show();

                    }
                })
                .create().show();
    }

}
