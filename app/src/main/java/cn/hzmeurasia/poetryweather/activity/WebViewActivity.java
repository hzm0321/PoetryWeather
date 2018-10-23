package cn.hzmeurasia.poetryweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


import com.yanzhenjie.loading.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hzmeurasia.poetryweather.R;
import ezy.ui.layout.LoadingLayout;

/**
 * 类名: WebViewActivity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/21 16:20
 */
public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.webView_loading)
    LoadingLayout loading;

    private String url=null;

    @OnClick({R.id.btn_back})
    void onClick(View v){
        switch(v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        //绑定初始化BufferKnife
        ButterKnife.bind(this);
        getUrl();
        init();

    }

    private void init() {
        mWebView.loadUrl(url);
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                view.loadUrl("javascript:function setTop(){document.querySelector('#pic_container').style.display='none';}setTop();");
                if (newProgress == 100) {

                } else {

                }
            }


        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:function setTop(){document.querySelector('#pic_container').style.display='none';}setTop();");
                Log.d(TAG, "run: 执行了过滤的方法");

                loading.showContent();
            }
        });

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    /**
     * 改写物理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 读取网址
     */
    private void getUrl() {
        SharedPreferences sharedPreferences = getSharedPreferences("poetry_detail", MODE_PRIVATE);
        url = sharedPreferences.getString("poetry_link","http://www.shicimingju.com");
    }
}
