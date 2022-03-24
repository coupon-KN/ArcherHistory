package jp.coupon.archerhistory.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import jp.coupon.archerhistory.R;
import jp.coupon.archerhistory.adapter.ScoreAdapter;
import jp.coupon.archerhistory.bean.HistoryInitDataBean;
import jp.coupon.archerhistory.databinding.ActivityHistoryBinding;
import jp.coupon.archerhistory.entity.TblScoreEntity;
import jp.coupon.archerhistory.event.ScoreListSelectEvent;
import jp.coupon.archerhistory.loader.GetHistoryInitDataLoader;
import jp.coupon.archerhistory.loader.TblScoreListGetByDateLoader;


/**
 * 履歴アクティビティ
 */
public class HistoryActivity extends AppCompatActivity {

    // ローダーID
    private final Integer LOADER_ID_GET_INIT = 1;
    private final Integer LOADER_ID_GET_SCORE = 2;

    private ActivityHistoryBinding mBinding;

    private ScoreAdapter mHistoryAdapter;
    private Integer mSumRound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // バインディング
            mBinding = ActivityHistoryBinding.inflate(getLayoutInflater());
            View view = mBinding.getRoot();
            setContentView(view);

            // リサイクラーの設定
            mHistoryAdapter = new ScoreAdapter(this, null);
            mBinding.listHistory.setLayoutManager(new LinearLayoutManager(this));
            mBinding.listHistory.setAdapter(mHistoryAdapter);

            // 検索ボタン
            mBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoaderManager.getInstance(HistoryActivity.this).initLoader(LOADER_ID_GET_SCORE, null, new HistoryActivity.SearchHistoryAsyncTaskLoaderCallback());
                }
            });

            // 初期データの取得
            LoaderManager.getInstance(this).initLoader(LOADER_ID_GET_INIT, null, new HistoryActivity.InitDataAsyncTaskLoaderCallback());

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 合計を計算する
     */
    private void calcAverageAndTotal(int staIdx){
        List<TblScoreEntity> list = mHistoryAdapter.getItems();
        Integer total = 0;
        Integer num = 0;
        if(list != null && list.size() > 0){
            Integer endIndex = staIdx + mSumRound;
            endIndex = (endIndex > list.size()) ? list.size() : endIndex;
            for(TblScoreEntity entity : list.subList(staIdx, endIndex)){
                total += entity.sub_total;
                num ++;
            }
        }
        Integer avg = total / num;
        mBinding.txtAverage.setText(avg.toString());
        mBinding.txtTotal.setText(total.toString());
    }

    /**
     * スコアの選択イベント
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScoreListSelectEvent(ScoreListSelectEvent event){
        mHistoryAdapter.setSumStaIndex(event.getSelectIndex());
        calcAverageAndTotal(event.getSelectIndex());
    }

    /**
     * 初期データ取得ローダー
     */
    private class InitDataAsyncTaskLoaderCallback implements LoaderManager.LoaderCallbacks<HistoryInitDataBean>{
        @NonNull
        @Override
        public Loader<HistoryInitDataBean> onCreateLoader(int id, @Nullable Bundle args) {
            return new GetHistoryInitDataLoader(HistoryActivity.this);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<HistoryInitDataBean> loader, HistoryInitDataBean bean) {
            try {
                if(bean != null){
                    // ラウンド
                    if(!bean.getSumRoundNum().isEmpty()){
                        mSumRound = Integer.parseInt(bean.getSumRoundNum());
                        mBinding.txtAggregateTitle.setText(String.format("選択%s回分", bean.getSumRoundNum()));
                        mHistoryAdapter.setSumRound(mSumRound);
                    }
                    // 日付スピナー
                    if(bean.getLstDate() != null){
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, R.layout.common_spinner, bean.getLstDate());
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mBinding.spnDate.setAdapter(adapter);
                    }
                    // 距離スピナー
                    if(bean.getLstDistance() != null){
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, R.layout.common_spinner, bean.getLstDistance());
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mBinding.spnDistance.setAdapter(adapter);
                    }
                    // 履歴
                    if(bean.getLstScoreEntity() != null){
                        mHistoryAdapter.AddList(bean.getLstScoreEntity());
                        // 平均と合計
                        calcAverageAndTotal(0);
                    }
                }
            }catch (Exception ex){
            }finally {
                LoaderManager.getInstance(HistoryActivity.this).destroyLoader(loader.getId());
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<HistoryInitDataBean> loader) {
        }
    }

    /**
     * 再検索ローダー
     */
    private class SearchHistoryAsyncTaskLoaderCallback implements LoaderManager.LoaderCallbacks<List<TblScoreEntity>>{
        @NonNull
        @Override
        public Loader<List<TblScoreEntity>> onCreateLoader(int id, @Nullable Bundle args) {
            String date = (String)mBinding.spnDate.getSelectedItem();
            return new TblScoreListGetByDateLoader(HistoryActivity.this, date, true);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<TblScoreEntity>> loader, List<TblScoreEntity> list) {
            try {
                if(list != null && list.size() > 0){
                    // 履歴
                    mHistoryAdapter.setSumStaIndex(0);
                    mHistoryAdapter.ResetList(list);
                    // 平均と合計
                    calcAverageAndTotal(0);
                }
            }catch (Exception ex){
            }finally {
                LoaderManager.getInstance(HistoryActivity.this).destroyLoader(loader.getId());
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<TblScoreEntity>> loader) {
        }
    }


}