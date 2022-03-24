package jp.coupon.archerhistory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import jp.coupon.archerhistory.R;
import jp.coupon.archerhistory.entity.TblScoreEntity;
import jp.coupon.archerhistory.event.ScoreListSelectEvent;

/**
 * スコアアダプター
 */
public class ScoreAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<TblScoreEntity> mLstScore;
    Integer mSumRound = -1;
    Integer mSumStaIndex = 0;

    public ScoreAdapter(Context context, List<TblScoreEntity> lstScore) {
        mContext = context;
        mLstScore = lstScore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.list_score, parent,false);
        return new ScoreViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TblScoreEntity entity = mLstScore.get(position);
        ScoreViewHolder vHolder = (ScoreViewHolder) holder;

        int backgroundColor = mContext.getColor(R.color.white);
        if(position >= mSumStaIndex && position < (mSumStaIndex + mSumRound)){
            backgroundColor = mContext.getColor(R.color.ivory);
        }

        vHolder.txtNo.setText(entity.seq.toString());
        vHolder.txtNo.setBackgroundColor(backgroundColor);

        vHolder.txtScore1.setText(entity.score1);
        vHolder.txtScore1.setTextColor(getScoreFontColor(entity.score1));
        vHolder.txtScore1.setBackgroundColor(backgroundColor);

        vHolder.txtScore2.setText(entity.score2);
        vHolder.txtScore2.setTextColor(getScoreFontColor(entity.score2));
        vHolder.txtScore2.setBackgroundColor(backgroundColor);

        vHolder.txtScore3.setText(entity.score3);
        vHolder.txtScore3.setTextColor(getScoreFontColor(entity.score3));
        vHolder.txtScore3.setBackgroundColor(backgroundColor);

        vHolder.txtScore4.setText(entity.score4);
        vHolder.txtScore4.setTextColor(getScoreFontColor(entity.score4));
        vHolder.txtScore4.setBackgroundColor(backgroundColor);

        vHolder.txtScore5.setText(entity.score5);
        vHolder.txtScore5.setTextColor(getScoreFontColor(entity.score5));
        vHolder.txtScore5.setBackgroundColor(backgroundColor);

        vHolder.txtScore6.setText(entity.score6);
        vHolder.txtScore6.setTextColor(getScoreFontColor(entity.score6));
        vHolder.txtScore6.setBackgroundColor(backgroundColor);

        vHolder.txtSubTotal.setText(entity.sub_total.toString());
        vHolder.txtSubTotal.setBackgroundColor(backgroundColor);

        vHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScoreListSelectEvent event = new ScoreListSelectEvent();
                event.setSelectIndex(position);
                EventBus.getDefault().post(event);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mLstScore == null){
            return 0;
        }
        return mLstScore.size();
    }

    /**
     * 追加
     */
    public void Add(TblScoreEntity entity){
        if(mLstScore == null){
            mLstScore = new ArrayList<TblScoreEntity>();
        }
        mLstScore.add(0, entity);
        notifyDataSetChanged();
    }

    /**
     * 追加リスト
     */
    public void AddList(List<TblScoreEntity> lstEntity){
        if(mLstScore == null){
            mLstScore = new ArrayList<TblScoreEntity>();
        }
        mLstScore.addAll(lstEntity);
        notifyDataSetChanged();
    }

    /**
     * 再設定
     */
    public void ResetList(List<TblScoreEntity> lstEntity){
        mLstScore = lstEntity;
        notifyDataSetChanged();
    }
    /**
     * アイテムの取得
     */
    public List<TblScoreEntity> getItems(){
        return mLstScore;
    }

    /**
     * 背景色変更する行数
     */
    public void setSumRound(Integer round){
        mSumRound = round;
    }

    /**
     * 開始位置の設定
     */
    public void setSumStaIndex(Integer index){
        mSumStaIndex = index;
        notifyDataSetChanged();
    }

    /**
     * スコアの文字色取得
     */
    private int getScoreFontColor(String score){
        if(score.equals("X")){
            return mContext.getColor(R.color.blue);
        }else if(score.equals("M")){
            return mContext.getColor(R.color.red);
        }
        return mContext.getColor(R.color.black);
    }

    /**
     * ViewHolder
     */
    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        LinearLayout row;
        public TextView txtNo;
        public TextView txtScore1;
        public TextView txtScore2;
        public TextView txtScore3;
        public TextView txtScore4;
        public TextView txtScore5;
        public TextView txtScore6;
        public TextView txtSubTotal;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            row = (LinearLayout) itemView.findViewById(R.id.list_score_row);
            txtNo = (TextView) itemView.findViewById(R.id.list_score_no);
            txtScore1 = (TextView) itemView.findViewById(R.id.list_score_1);
            txtScore2 = (TextView) itemView.findViewById(R.id.list_score_2);
            txtScore3 = (TextView) itemView.findViewById(R.id.list_score_3);
            txtScore4 = (TextView) itemView.findViewById(R.id.list_score_4);
            txtScore5 = (TextView) itemView.findViewById(R.id.list_score_5);
            txtScore6 = (TextView) itemView.findViewById(R.id.list_score_6);
            txtSubTotal = (TextView) itemView.findViewById(R.id.list_score_sub_total);
        }
    }

}
