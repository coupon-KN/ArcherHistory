package jp.coupon.archerhistory.activity;

import static jp.coupon.archerhistory.common.Constants.MST_KBN_ID_DISTANCE;
import static jp.coupon.archerhistory.common.Constants.MST_KBN_ID_SUM_ROUND;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import jp.coupon.archerhistory.R;
import jp.coupon.archerhistory.adapter.ScoreAdapter;
import jp.coupon.archerhistory.databinding.ActivityRecordBinding;
import jp.coupon.archerhistory.entity.MstKbnEntity;
import jp.coupon.archerhistory.entity.TblScoreEntity;
import jp.coupon.archerhistory.loader.MstKbnGetAllLoader;
import jp.coupon.archerhistory.loader.TblScoreInsertLoader;
import jp.coupon.archerhistory.loader.TblScoreListGetByDateLoader;

/**
 * 記録画面
 */
public class RecordActivity extends AppCompatActivity {

    // ローダーID
    private final Integer LOADER_ID_GET_MSTKBN = 1;
    private final Integer LOADER_ID_GET_SCORE = 2;
    private final Integer LOADER_ID_INSERT_SCORE = 3;

    private ActivityRecordBinding mBinding;
    private EditText mNowScoreText = null;
    private EditText[] mScoreTextArray;

    private ScoreAdapter mAdapter;

    private String mDistance;
    private String mTargetDate;
    private Integer mSumRound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // バインディング
            mBinding = ActivityRecordBinding.inflate(getLayoutInflater());
            View view = mBinding.getRoot();
            setContentView(view);

            // 日付
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            mTargetDate = sdf.format(calendar.getTime());

            // 平均
            mBinding.txtAverage.setText("0");

            // キーボードを非表示にする
            mBinding.txtScore1.setRawInputType(InputType.TYPE_NULL);
            mBinding.txtScore2.setRawInputType(InputType.TYPE_NULL);
            mBinding.txtScore3.setRawInputType(InputType.TYPE_NULL);
            mBinding.txtScore4.setRawInputType(InputType.TYPE_NULL);
            mBinding.txtScore5.setRawInputType(InputType.TYPE_NULL);
            mBinding.txtScore6.setRawInputType(InputType.TYPE_NULL);

            // 配列にテキストを追加する
            mScoreTextArray = new EditText[]{
                    mBinding.txtScore1,
                    mBinding.txtScore2,
                    mBinding.txtScore3,
                    mBinding.txtScore4,
                    mBinding.txtScore5,
                    mBinding.txtScore6
            };

            // リサイクラーの設定
            mAdapter = new ScoreAdapter(this, null);
            mBinding.listHistory.setLayoutManager(new LinearLayoutManager(this));
            mBinding.listHistory.setAdapter(mAdapter);

            // リスナーを設定
            setViewListener();

            // 区分マスタ取得
            LoaderManager.getInstance(this).initLoader(LOADER_ID_GET_MSTKBN, null, new MstKbnAsyncTaskLoaderCallback());

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * リスナーの設定
     */
    private void setViewListener(){
        // チェンジリスナー
        mBinding.txtScore1.setOnFocusChangeListener(createScoreChangeListener());
        mBinding.txtScore2.setOnFocusChangeListener(createScoreChangeListener());
        mBinding.txtScore3.setOnFocusChangeListener(createScoreChangeListener());
        mBinding.txtScore4.setOnFocusChangeListener(createScoreChangeListener());
        mBinding.txtScore5.setOnFocusChangeListener(createScoreChangeListener());
        mBinding.txtScore6.setOnFocusChangeListener(createScoreChangeListener());

        // ボタン
        mBinding.btnScoreX.setOnClickListener(createScoreClickListener("X"));
        mBinding.btnScore10.setOnClickListener(createScoreClickListener("10"));
        mBinding.btnScore9.setOnClickListener(createScoreClickListener("9"));
        mBinding.btnScore8.setOnClickListener(createScoreClickListener("8"));
        mBinding.btnScore7.setOnClickListener(createScoreClickListener("7"));
        mBinding.btnScore6.setOnClickListener(createScoreClickListener("6"));
        mBinding.btnScore5.setOnClickListener(createScoreClickListener("5"));
        mBinding.btnScore4.setOnClickListener(createScoreClickListener("4"));
        mBinding.btnScore3.setOnClickListener(createScoreClickListener("3"));
        mBinding.btnScore2.setOnClickListener(createScoreClickListener("2"));
        mBinding.btnScore1.setOnClickListener(createScoreClickListener("1"));
        mBinding.btnScoreM.setOnClickListener(createScoreClickListener("M"));

        // 決定ボタン
        mBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean insertNot = false;
                insertNot = insertNot | mBinding.txtScore1.getText().toString().isEmpty();
                insertNot = insertNot | mBinding.txtScore2.getText().toString().isEmpty();
                insertNot = insertNot | mBinding.txtScore3.getText().toString().isEmpty();
                insertNot = insertNot | mBinding.txtScore4.getText().toString().isEmpty();
                insertNot = insertNot | mBinding.txtScore5.getText().toString().isEmpty();
                insertNot = insertNot | mBinding.txtScore6.getText().toString().isEmpty();

                if(!insertNot){
                    // スコアを降順に並び変える
                    scoreDescOrder();

                    TblScoreEntity entity = new TblScoreEntity();
                    entity.date = mTargetDate;
                    entity.distance = mDistance;
                    entity.score1 = mBinding.txtScore1.getText().toString();
                    entity.score2 = mBinding.txtScore2.getText().toString();
                    entity.score3 = mBinding.txtScore3.getText().toString();
                    entity.score4 = mBinding.txtScore4.getText().toString();
                    entity.score5 = mBinding.txtScore5.getText().toString();
                    entity.score6 = mBinding.txtScore6.getText().toString();
                    entity.sub_total = Integer.parseInt(mBinding.txtSubTotal.getText().toString());

                    // アップデート
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("entity", entity);
                    LoaderManager.getInstance(RecordActivity.this).initLoader(LOADER_ID_INSERT_SCORE, bundle, new TblScoreInsertAsyncTaskLoaderCallback());
                }
            }
        });
    }

    /**
     * OnClickListenerを返す
     * @param score
     * @return
     */
    private View.OnClickListener createScoreClickListener(String score){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mNowScoreText != null){
                    mNowScoreText.setText(score);
                    // 小計を計算
                    calcSubTotal();
                    if(mNowScoreText.getId() == R.id.txt_score_1){
                        mBinding.txtScore2.requestFocus();
                    }
                    else if(mNowScoreText.getId() == R.id.txt_score_2){
                        mBinding.txtScore3.requestFocus();
                    }
                    else if(mNowScoreText.getId() == R.id.txt_score_3){
                        mBinding.txtScore4.requestFocus();
                    }
                    else if(mNowScoreText.getId() == R.id.txt_score_4){
                        mBinding.txtScore5.requestFocus();
                    }
                    else if(mNowScoreText.getId() == R.id.txt_score_5){
                        mBinding.txtScore6.requestFocus();
                    }
                }
            }
        };
        return listener;
    }

    /**
     * OnFocusChangeListenerを返す
     * @return
     */
    private View.OnFocusChangeListener createScoreChangeListener(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    mNowScoreText = (EditText) view;
                }
            }
        };
    }

    /**
     * 小計を計算する
     */
    private void calcSubTotal(){
        Integer score = 0;

        for(EditText txtScore : mScoreTextArray){
            String work = txtScore.getText().toString();
            if(!work.isEmpty()){
                if(work.equals("X")){
                    score += 10;
                }else if(!work.equals("M")){
                    score += Integer.parseInt(work);
                }
            }
        }
        mBinding.txtSubTotal.setText(score.toString());
    }

    /**
     * スコアを降順にする
     */
    private void scoreDescOrder(){
        List<Integer> lstNumber = new ArrayList<>();
        int x_num = 0;
        int m_num = 0;
        for(EditText txtScore : mScoreTextArray){
            String work = txtScore.getText().toString();
            if(!work.isEmpty()){
                if(work.equals("X")){
                    x_num ++;
                }else if(work.equals("M")){
                    m_num ++;
                }else{
                    lstNumber.add(Integer.parseInt(work));
                }
            }
        }
        // X
        int index = 0;
        for(int i=0; i<x_num; i++){
            mScoreTextArray[index].setText("X");
            index++;
        }
        // 数値
        if(lstNumber.size() > 0) {
            Collections.sort(lstNumber, Collections.reverseOrder());
            for(Integer num : lstNumber){
                mScoreTextArray[index].setText(num.toString());
                index++;
            }
        }
        // M
        for(int i=0; i<m_num; i++){
            mScoreTextArray[index].setText("M");
            index++;
        }
    }

    /**
     * 合計を計算する
     */
    private void calcTotal(){
        List<TblScoreEntity> list = mAdapter.getItems();
        Integer total = 0;
        if(list != null && list.size() > 0){
            int cnt = 1;
            for(TblScoreEntity entity : list){
                total += entity.sub_total;
                if(cnt >= mSumRound){
                    break;
                }
                cnt++;
            }
        }
        mBinding.txtTotal.setText(total.toString());
    }


    /**
     * 区分マスタ取得コールバック
     */
    private class MstKbnAsyncTaskLoaderCallback implements LoaderManager.LoaderCallbacks<List<MstKbnEntity>>{
        @NonNull
        @Override
        public Loader<List<MstKbnEntity>> onCreateLoader(int id, @Nullable Bundle args) {
            return new MstKbnGetAllLoader(RecordActivity.this);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<MstKbnEntity>> loader, List<MstKbnEntity> list) {
            try {
                if(list != null && list.size() > 0){
                    for(MstKbnEntity entity : list){
                        if(entity.id.equals(MST_KBN_ID_DISTANCE)){
                            mDistance = entity.value;
                            mBinding.txtDistance.setText(entity.value + "m");
                        }
                        else if(entity.id.equals(MST_KBN_ID_SUM_ROUND)){
                            mSumRound = Integer.parseInt(entity.value);
                            mAdapter.setSumRound(Integer.parseInt(entity.value));
                            mBinding.txtTotalTitle.setText(String.format("最新%s回合計", entity.value));
                        }
                    }
                }
            }catch (Exception ex){
            }finally {
                LoaderManager.getInstance(RecordActivity.this).destroyLoader(loader.getId());
                // スコアを取得
                LoaderManager.getInstance(RecordActivity.this).initLoader(LOADER_ID_GET_SCORE, null, new TblScoreListAsyncTaskLoaderCallback());
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<MstKbnEntity>> loader) {
        }
    }

    /**
     * スコアテーブル取得コールバック
     */
    private class TblScoreListAsyncTaskLoaderCallback implements LoaderManager.LoaderCallbacks<List<TblScoreEntity>>{
        @NonNull
        @Override
        public Loader<List<TblScoreEntity>> onCreateLoader(int id, @Nullable Bundle args) {
            return new TblScoreListGetByDateLoader(RecordActivity.this, mTargetDate, false);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<TblScoreEntity>> loader, List<TblScoreEntity> list) {
            try {
                if(list != null && list.size() > 0){
                    // リサイクラーに投げる
                    mAdapter.AddList(list);
                    // 平均を計算
                    Integer total = 0;
                    for(TblScoreEntity entity : list){
                        total += entity.sub_total;
                    }
                    Integer avg = total / list.size();
                    mBinding.txtAverage.setText(avg.toString());
                    // トータルを再計算
                    calcTotal();

                }
            }catch (Exception ex){
            }finally {
                LoaderManager.getInstance(RecordActivity.this).destroyLoader(loader.getId());
            }
        }
        @Override
        public void onLoaderReset(@NonNull Loader<List<TblScoreEntity>> loader) {
        }
    }

    /**
     * スコアテーブル追加コールバック
     */
    private class TblScoreInsertAsyncTaskLoaderCallback implements LoaderManager.LoaderCallbacks<Integer>{
        TblScoreEntity entity;

        @NonNull
        @Override
        public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {
            entity = (TblScoreEntity)args.getSerializable("entity");
            return new TblScoreInsertLoader(RecordActivity.this, entity);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Integer> loader, Integer average) {
            try {
                // 追加
                mAdapter.Add(entity);
                if(average != null){
                    mBinding.txtAverage.setText(average.toString());
                }
                // クリア
                mBinding.txtScore1.setText("");
                mBinding.txtScore2.setText("");
                mBinding.txtScore3.setText("");
                mBinding.txtScore4.setText("");
                mBinding.txtScore5.setText("");
                mBinding.txtScore6.setText("");
                mBinding.txtSubTotal.setText("");
                mBinding.txtScore1.requestFocus();
                // トータルを再計算
                calcTotal();

            }catch (Exception ex){
            }finally {
                LoaderManager.getInstance(RecordActivity.this).destroyLoader(loader.getId());
            }
        }
        @Override
        public void onLoaderReset(@NonNull Loader<Integer> loader) {
        }
    }

}