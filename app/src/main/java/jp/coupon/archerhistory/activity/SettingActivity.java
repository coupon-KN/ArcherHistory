package jp.coupon.archerhistory.activity;

import static jp.coupon.archerhistory.common.Constants.MST_KBN_ID_DISTANCE;
import static jp.coupon.archerhistory.common.Constants.MST_KBN_ID_DISTANCE_DEFAULT;
import static jp.coupon.archerhistory.common.Constants.MST_KBN_ID_SUM_ROUND;
import static jp.coupon.archerhistory.common.Constants.MST_KBN_ID_SUM_ROUND_DEFAULT;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import jp.coupon.archerhistory.R;
import jp.coupon.archerhistory.databinding.ActivityRecordBinding;
import jp.coupon.archerhistory.databinding.ActivitySettingBinding;
import jp.coupon.archerhistory.entity.MstKbnEntity;
import jp.coupon.archerhistory.loader.MstKbnGetKeyLoader;
import jp.coupon.archerhistory.loader.MstKbnUpdateAllLoader;

/**
 * 設定画面
 */
public class SettingActivity extends AppCompatActivity {

    // ローダーID
    private final Integer LOADER_ID_DISTANCE = 1;
    private final Integer LOADER_ID_SUM_ROUND = 2;
    private final Integer LOADER_ID_UPDATE = 3;

    // バインディング
    private ActivitySettingBinding mBinding;

    // 取得後リスナー
    interface GetLoaderListener extends Serializable {
        void onFinished(MstKbnEntity entity);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // バインディング
            mBinding = ActivitySettingBinding.inflate(getLayoutInflater());
            View view = mBinding.getRoot();
            setContentView(view);

            // 設定
            mBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<MstKbnEntity> lstMstKbn = new ArrayList<MstKbnEntity>();

                    MstKbnEntity entity1 = new MstKbnEntity();
                    entity1.id = MST_KBN_ID_DISTANCE;
                    entity1.value = mBinding.txtDistance.getText().toString();
                    lstMstKbn.add(entity1);

                    MstKbnEntity entity2 = new MstKbnEntity();
                    entity2.id = MST_KBN_ID_SUM_ROUND;
                    entity2.value = mBinding.txtSumRoundNum.getText().toString();
                    lstMstKbn.add(entity2);

                    // アップデート
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dataArray", lstMstKbn.toArray(new MstKbnEntity[lstMstKbn.size()]));
                    LoaderManager.getInstance(SettingActivity.this).initLoader(LOADER_ID_UPDATE, bundle, new MstKbnUpdateAsyncTaskLoaderCallback());
                }
            });

            // 初期処理
            init();


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 初期処理
     */
    private void init(){
        Bundle bundle;

        // 区分マスタ(射程距離)取得
        GetLoaderListener distanceListener = new GetLoaderListener() {
            @Override
            public void onFinished(MstKbnEntity entity) {
                mBinding.txtDistance.setText(entity.value);
            }
        };
        bundle = new Bundle();
        bundle.putString("id", MST_KBN_ID_DISTANCE);
        bundle.putString("default", MST_KBN_ID_DISTANCE_DEFAULT);
        bundle.putSerializable("listener", distanceListener);
        LoaderManager.getInstance(this).initLoader(LOADER_ID_DISTANCE, bundle, new GetMstKbnAsyncTaskLoaderCallback());

        // 区分マスタ(合計対象ラウンド数)取得
        GetLoaderListener sumRoundListener = new GetLoaderListener() {
            @Override
            public void onFinished(MstKbnEntity entity) {
                mBinding.txtSumRoundNum.setText(entity.value);
            }
        };
        bundle = new Bundle();
        bundle.putString("id", MST_KBN_ID_SUM_ROUND);
        bundle.putString("default", MST_KBN_ID_SUM_ROUND_DEFAULT);
        bundle.putSerializable("listener", sumRoundListener);
        LoaderManager.getInstance(this).initLoader(LOADER_ID_SUM_ROUND, bundle, new GetMstKbnAsyncTaskLoaderCallback());
    }


    /**
     * 区分マスタ取得コールバック
     */
    private class GetMstKbnAsyncTaskLoaderCallback implements LoaderManager.LoaderCallbacks<MstKbnEntity>{
        GetLoaderListener getListener;

        @NonNull
        @Override
        public Loader<MstKbnEntity> onCreateLoader(int id, @Nullable Bundle args) {
            String strID = args.getString("id");
            String strDefault = args.getString("default");
            getListener = (GetLoaderListener)args.getSerializable("listener");
            return new MstKbnGetKeyLoader(SettingActivity.this, strID, strDefault);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<MstKbnEntity> loader, MstKbnEntity data) {
            try {
                if(data != null){
                    getListener.onFinished(data);
                }
            }catch (Exception ex){
            }finally {
                LoaderManager.getInstance(SettingActivity.this).destroyLoader(loader.getId());
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<MstKbnEntity> loader) {
        }
    }

    /**
     * 区分マスタ更新コールバック
     */
    private class MstKbnUpdateAsyncTaskLoaderCallback implements LoaderManager.LoaderCallbacks<Integer>{
        @NonNull
        @Override
        public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {
            MstKbnEntity[] dataArray = (MstKbnEntity[])args.getSerializable("dataArray");
            return new MstKbnUpdateAllLoader(SettingActivity.this, dataArray);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Integer> loader, Integer count) {
            try {
            }catch (Exception ex){
            }finally {
                LoaderManager.getInstance(SettingActivity.this).destroyLoader(loader.getId());
                // 終了する
                finish();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Integer> loader) {
        }
    }

}