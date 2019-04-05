package cn.hzmeurasia.poetryweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.entity.PoetryDetail;
import cn.hzmeurasia.poetryweather.util.PrefUtils;

public class FMPoetryDetailTranslation extends Fragment {

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.translation_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        PoetryDetail poetryDetail = PrefUtils.getPoetryDetail(MyApplication.getContext());
        TextView tvTranslation = view.findViewById(R.id.tv_poetry_detail_translation);
        tvTranslation.setText(poetryDetail.translation);
    }
}
