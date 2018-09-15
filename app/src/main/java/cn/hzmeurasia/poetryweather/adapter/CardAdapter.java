package cn.hzmeurasia.poetryweather.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.entity.CardEntity;

/**
 * 类名: CardAdapter<br>
 * 功能:(主页面Card布局内容适配)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/9/15 14:48
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

    private Context mcontext;
    private List<CardEntity> mCardEntityList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView bgImage;
        TextView oldCityName;
        TextView nowCityName;
        TextView weatherMessage;
        TextView temperature;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            bgImage = view.findViewById(R.id.iv_bg);
            oldCityName = view.findViewById(R.id.tv_oldCityName);
            nowCityName = view.findViewById(R.id.tv_nowCityName);
            weatherMessage = view.findViewById(R.id.tv_weather);
            temperature = view.findViewById(R.id.tv_temperature);
        }
    }

    public CardAdapter(List<CardEntity> cardEntityList) {
        mCardEntityList = cardEntityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mcontext == null) {
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardEntity cardEntity = mCardEntityList.get(position);
        holder.oldCityName.setText(cardEntity.getAdress());
        holder.nowCityName.setText(cardEntity.getAdress2());
        holder.weatherMessage.setText(cardEntity.getWeatherMessage());
        holder.temperature.setText(cardEntity.getTemperature());
        Glide.with(mcontext).load(cardEntity.getImageId()).into(holder.bgImage);
    }

    @Override
    public int getItemCount() {
        return mCardEntityList.size();
    }

}
