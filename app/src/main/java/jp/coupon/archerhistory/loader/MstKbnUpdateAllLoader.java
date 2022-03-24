package jp.coupon.archerhistory.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import jp.coupon.archerhistory.dao.MstKbnDao;
import jp.coupon.archerhistory.database.ArcherDBSingleton;
import jp.coupon.archerhistory.entity.MstKbnEntity;


/**
 * マスタ区分を更新
 */
public class MstKbnUpdateAllLoader extends AsyncTaskLoader<Integer> {

    Context mContext;
    MstKbnEntity[] mDataArray;

    public MstKbnUpdateAllLoader(@NonNull Context context, MstKbnEntity[] dataArray) {
        super(context);
        mContext = context;
        mDataArray = dataArray;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Integer loadInBackground() {
        MstKbnDao mstKbnDao = ArcherDBSingleton.getInstance(mContext).mstKbnDao();
        Integer count = mstKbnDao.updateAll(mDataArray);
        return count;
    }

}
