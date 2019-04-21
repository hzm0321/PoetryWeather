package cn.hzmeurasia.poetryweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xujiaji.happybubble.BubbleDialog;

import java.time.Instant;
import java.util.List;

import cn.hzmeurasia.poetryweather.activity.WeatherActivity;
import cn.hzmeurasia.poetryweather.activity.WebViewActivity;
import cn.hzmeurasia.poetryweather.entity.PoetryDetail;

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

    static PoetryDetail poetryDetail;
    static String keyWord = "";

    private String poetry_link;

    private String author_link;

    public PoetryDialog(Context context, PoetryDetail p, List<String> list) {
        super(context);
        calBar(true);
        poetryDetail = p;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            stringBuilder.append("、");
        }
        if (stringBuilder.length() > 0) {
            keyWord = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        } else  {
            keyWord = "自定义诗词";
        }
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
        TextView tv1, tv2, tv3, tv4;

        public ViewHolder(View rootView) {
            tv1 = rootView.findViewById(R.id.tv1);
            tv2 = rootView.findViewById(R.id.tv2);
            tv3 = rootView.findViewById(R.id.tv3);
            tv4 = rootView.findViewById(R.id.tv4);
            tv1.setText(poetryDetail.title);
            tv2.setText("["+poetryDetail.dynasty+"]"+" "+poetryDetail.writer);
            tv3.setText(poetryDetail.content);
            tv4.setText("关键词:"+keyWord);

//            SharedPreferences preferences = getContext().getSharedPreferences("poetry_detail", MODE_PRIVATE);
//            author = preferences.getString("author", "暂未获取到作者数据,请刷新网络后重试");
//            annotation = preferences.getString("annotation", "暂未获取到注释数据,请刷新网络后重试");
//            poetry_link = preferences.getString("poetry_link", null);
//            tv1.setText("作者:"+author);
//            tv2.setText("注释:"+annotation);
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



}
