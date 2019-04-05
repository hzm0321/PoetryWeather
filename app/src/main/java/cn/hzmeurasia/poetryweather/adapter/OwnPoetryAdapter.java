package cn.hzmeurasia.poetryweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.activity.PoetryActivity;
import cn.hzmeurasia.poetryweather.db.PoetryDb;

/**
 * 类名: OwnPoetryAdapter<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/29 22:10
 */
public class OwnPoetryAdapter extends RecyclerView.Adapter<OwnPoetryAdapter.ViewHolder> {

    private static final String TAG = "OwnPoetryAdapter";
    private Context mContext;
    private List<PoetryDb> mPoetryDbList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvId;
        TextView tvPoetry;
        public ViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_own_poetry_id);
            tvPoetry = itemView.findViewById(R.id.tv_own_poetry);
        }
    }

    public OwnPoetryAdapter(List<PoetryDb> poetryDbList) {
        mPoetryDbList = poetryDbList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext != null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.poetry_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoetryDb poetryDb = mPoetryDbList.get(position);
        holder.tvId.setText("Id:"+String.valueOf(poetryDb.getPoetryDb_id()));
        holder.tvPoetry.setText(poetryDb.getPoetryDb_poetry());
    }

    @Override
    public int getItemCount() {
        return mPoetryDbList.size();
    }




}
