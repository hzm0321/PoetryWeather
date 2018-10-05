package cn.hzmeurasia.poetryweather.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.activity.WeatherActivity;
import cn.hzmeurasia.poetryweather.db.CityDb;
import cn.hzmeurasia.poetryweather.entity.CardEntity;

/**
 * 类名: CardAdapter<br>
 * 功能:(主页面Card布局内容适配)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/9/15 14:48
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

    private static final String TAG = "CardAdapter";

    private Context mcontext;
    private List<CityDb> mCityDbList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        @BindView(R.id.iv_bg)
        ImageView bgImage;
        @BindView(R.id.tv_CityName)
        TextView cityName;
        @BindView(R.id.tv_weather)
        TextView weatherMessage;
        @BindView(R.id.tv_temperature)
        TextView temperature;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            //绑定初始化BufferKnife
            ButterKnife.bind(this, view);
        }
    }

    public CardAdapter(List<CityDb> cityDbList) {
        mCityDbList = cityDbList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mcontext == null) {
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.card_item, parent, false);
        //点击事件
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //位置
                int position = holder.getAdapterPosition();
                Log.d(TAG, "onClick: "+position);
                CityDb cityDb = mCityDbList.get(position);
                Log.d(TAG, "onClick: "+cityDb.getCityDb_cid());
                Intent intent = new Intent(mcontext, WeatherActivity.class);
                intent.putExtra("cityCode", cityDb.getCityDb_cid());
                mcontext.startActivity(intent);

            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CityDb cityDb = mCityDbList.get(position);
        holder.cityName.setText(cityDb.getCityDb_cityName());
        holder.weatherMessage.setText(cityDb.getCityDb_txt());
        holder.temperature.setText(cityDb.getCityDb_temperature());
        Glide.with(mcontext).load(cityDb.getCityDb_imageId()).into(holder.bgImage);
    }

    @Override
    public int getItemCount() {
        return mCityDbList.size();
    }

}
