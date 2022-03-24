package jp.coupon.archerhistory.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

import jp.coupon.archerhistory.dao.MstKbnDao;
import jp.coupon.archerhistory.dao.TblScoreDao;
import jp.coupon.archerhistory.database.ArcherDBSingleton;
import jp.coupon.archerhistory.entity.MstKbnEntity;
import jp.coupon.archerhistory.entity.TblScoreEntity;


/**
 * テーブルスコアから取得
 */
public class TblScoreListGetByDateLoader extends AsyncTaskLoader<List<TblScoreEntity>> {

    Context mContext;
    String mDate;
    Boolean mIsAsc;

    public TblScoreListGetByDateLoader(@NonNull Context context, String date, boolean isAsc) {
        super(context);
        mContext = context;
        mDate = date;
        mIsAsc = isAsc;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<TblScoreEntity> loadInBackground() {
        TblScoreDao tblScoreDao = ArcherDBSingleton.getInstance(mContext).tblScoreDao();
        List<TblScoreEntity> lstEntity;
        if(mIsAsc){
            lstEntity = tblScoreDao.getListSortByAsc(mDate);
        }else{
            lstEntity = tblScoreDao.getListSortByDesc(mDate);
        }
        return lstEntity;
    }

}
