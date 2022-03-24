package jp.coupon.archerhistory.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

import jp.coupon.archerhistory.dao.TblScoreDao;
import jp.coupon.archerhistory.database.ArcherDBSingleton;
import jp.coupon.archerhistory.entity.TblScoreEntity;


/**
 * テーブルスコアに追加
 */
public class TblScoreInsertLoader extends AsyncTaskLoader<Integer> {

    Context mContext;
    TblScoreEntity mEntity;

    public TblScoreInsertLoader(@NonNull Context context, TblScoreEntity entity) {
        super(context);
        mContext = context;
        mEntity = entity;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Integer loadInBackground() {
        TblScoreDao tblScoreDao = ArcherDBSingleton.getInstance(mContext).tblScoreDao();
        // 件数を取得
        List<TblScoreEntity> lstEntity = tblScoreDao.getListSortByDesc(mEntity.date);
        if(lstEntity != null){
            mEntity.seq = lstEntity.size() + 1;
        }else{
            mEntity.seq = 1;
        }
        tblScoreDao.insert(mEntity);
        // 平均を取得
        return tblScoreDao.getAverage(mEntity.date);
    }

}
