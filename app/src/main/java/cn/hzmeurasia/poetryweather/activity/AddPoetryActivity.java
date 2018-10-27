package cn.hzmeurasia.poetryweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hzmeurasia.poetryweather.R;

/**
 * 类名: AddPoetryActivity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/27 22:02
 */
public class AddPoetryActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
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
        setContentView(R.layout.poetry_add_poetry_activity);
        //绑定初始化BufferKnife
        ButterKnife.bind(this);
    }
}
