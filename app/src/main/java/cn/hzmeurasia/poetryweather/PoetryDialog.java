package cn.hzmeurasia.poetryweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xujiaji.happybubble.BubbleDialog;

import cn.hzmeurasia.poetryweather.activity.WeatherActivity;
import cn.hzmeurasia.poetryweather.activity.WebViewActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * 类名: PoetryDialog<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/21 13:59
 */
public class PoetryDialog extends BubbleDialog implements View.OnClickListener {
    private ViewHolder mViewHolder;
    private OnClickCustomButtonListener mListener;

    private String poetry_link;

    private String author_link;

    public PoetryDialog(Context context) {
        super(context);
        calBar(true);
//        initText();
        setTransParentBackground();
        setPosition(Position.RIGHT);
        setOffsetX(100);
        View rootView = LayoutInflater.from(context).inflate(R.layout.weather_pop, null);
        mViewHolder = new ViewHolder(rootView);
        addContentView(rootView);
        mViewHolder.tv1.setOnClickListener(this);
        mViewHolder.tv2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null)
        {
            mListener.onClick(
                    poetry_link
            );
        }
    }

    private class ViewHolder {
        private String author;
        private String annotation;
        TextView tv1, tv2;

        public ViewHolder(View rootView) {
            tv1 = rootView.findViewById(R.id.tv1);
            tv2 = rootView.findViewById(R.id.tv2);
            SharedPreferences preferences = getContext().getSharedPreferences("poetry_detail", MODE_PRIVATE);
            author = preferences.getString("author", "暂未获取到作者数据,请刷新网络后重试");
            annotation = preferences.getString("annotation", "暂未获取到注释数据,请刷新网络后重试");
            poetry_link = preferences.getString("poetry_link", null);
            tv1.setText("作者:"+author);
            tv2.setText("注释:"+annotation);
        }
    }

    public void setClickListener(OnClickCustomButtonListener l)
    {
        this.mListener = l;
    }


    public interface OnClickCustomButtonListener
    {
        void onClick(String string);
    }

//    private void initText() {
//        SharedPreferences preferences = getContext().getSharedPreferences("poetry_detail", MODE_PRIVATE);
//        author = preferences.getString("author", "暂未获取到作者数据,请刷新网络后重试");
//        annotation = preferences.getString("annotation", "暂未获取到注释数据,请刷新网络后重试");
//
//    }
}
