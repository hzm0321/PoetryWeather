package cn.hzmeurasia.poetryweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.List;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;

/**
 * 详细诗词页面,type适配类
 */
public class PoetryTypeAdapter extends RecyclerView.Adapter<PoetryTypeAdapter.ViewHolder> {

    private List<String> typeLists;

    public PoetryTypeAdapter(List<String> list) {
        typeLists = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.type_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String type = typeLists.get(position);
        holder.rb.setText(type);
    }

    @Override
    public int getItemCount() {
        return typeLists.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        QMUIRoundButton rb;
        public ViewHolder(View itemView) {
            super(itemView);
            rb = itemView.findViewById(R.id.rbtn_type);
        }
    }
}
