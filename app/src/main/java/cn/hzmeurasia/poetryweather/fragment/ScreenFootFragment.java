package cn.hzmeurasia.poetryweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.hzmeurasia.poetryweather.R;

/**
 * 类名: ScreenFootFragment<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/26 16:59
 */
public class ScreenFootFragment extends Fragment {
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_foot_fragment, container, false);
        textView = view.findViewById(R.id.tv_screen_foot);
        textView.setText("黄振敏");
        return view;
    }
}
