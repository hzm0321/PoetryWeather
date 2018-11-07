package cn.hzmeurasia.poetryweather.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hzmeurasia.poetryweather.R;

/**
 * 类名: AboutActivity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/11/7 12:56
 */
public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.version)
    TextView mVersionTextView;
    @BindView(R.id.about_list)
    QMUIGroupListView mAboutGroupListView;
    @BindView(R.id.copyright)
    TextView mCopyrightTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        View root = LayoutInflater.from(this).inflate(R.layout.about_activity, null);
        ButterKnife.bind(this, root);
        //初始化状态栏
        initTopBar();
        //设置view
        setContentView(root);
        //初始化about列表内容
        initAboutList();
    }

    //初始化状态栏
    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTopBar.setTitle(getString(R.string.app_name));
        mTopBar.setBackgroundColor(getResources().getColor(R.color.bluishWhite,getTheme()));
    }

    //列表项点击事件处理
    View.OnClickListener mOnClickListenerListItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //region 列表项点击事件
            QMUICommonListItemView listItemView = (QMUICommonListItemView) view;
            int tag = (int) listItemView.getTag();
            Intent intent;

            switch (tag) {
                case R.id.listitem_tag_1:
                    copyToClipboard("微信", listItemView.getDetailText().toString());
                    break;
                case R.id.listitem_tag_2:
                    copyToClipboard("QQ", listItemView.getDetailText().toString());
                    break;
                case R.id.listitem_tag_3:
                    openWebsite(listItemView.getDetailText().toString());
                    break;
                default:
                    break;
            }
            //endregion
        }
    };

    //访问网址
    private void openWebsite(String websiteUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(websiteUrl));
        startActivity(intent);
    }

    //复制数据到剪贴板
    private void copyToClipboard(String label, String string) {
        //复制
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(label, string);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "成功复制" + label, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "获取剪贴板失败，无法复制", Toast.LENGTH_SHORT).show();
        }
    }

    //初始化QMUIGroupListView
    private void initAboutList() {
        //初始化LOGO下面的名称和版本号
        mVersionTextView.setText("诗语天气 V1.0.1");

        //作者
        QMUICommonListItemView itemAuthor = mAboutGroupListView.createItemView("作者");
        itemAuthor.setDetailText("黄振敏");
        //微信
        QMUICommonListItemView itemWechat = mAboutGroupListView.createItemView("微信");
        itemWechat.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemWechat.setDetailText("hzm1998hzm");
        itemWechat.setTag(R.id.listitem_tag_1);
        //QQ
        QMUICommonListItemView itemQQ = mAboutGroupListView.createItemView("QQ");
        itemQQ.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemQQ.setDetailText("934585316");
        itemQQ.setTag(R.id.listitem_tag_2);
        //GitHub主页
        QMUICommonListItemView itemGitHub = mAboutGroupListView.createItemView("作者GitHub主页");
        itemGitHub.setOrientation(QMUICommonListItemView.VERTICAL);
        itemGitHub.setDetailText("https://github.com/hzm0321");
        itemGitHub.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemGitHub.setTag(R.id.listitem_tag_3);
        QMUIGroupListView.newSection(this)
                .setDescription(getString(R.string.about_description))
                .addItemView(itemAuthor, null)
                .addItemView(itemWechat, mOnClickListenerListItem)
                .addItemView(itemQQ, mOnClickListenerListItem)
                .addItemView(itemGitHub, mOnClickListenerListItem)
                .addTo(mAboutGroupListView);

        //初始化页面底部的版权
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
        String currentYear = dateFormat.format(new java.util.Date());
        mCopyrightTextView.setText(String.format(getResources().getString(R.string.about_copyright), currentYear));

    }
}
