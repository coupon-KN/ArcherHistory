package jp.coupon.archerhistory.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

import jp.coupon.archerhistory.dao.MstKbnDao;
import jp.coupon.archerhistory.database.ArcherDBSingleton;
import jp.coupon.archerhistory.entity.MstKbnEntity;


/**
 * マスタ区分を取得
 */
public class MstKbnGetAllLoader extends AsyncTaskLoader<List<MstKbnEntity>> {

    Context mContext;

    public MstKbnGetAllLoader(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<MstKbnEntity> loadInBackground() {
        MstKbnDao mstKbnDao = ArcherDBSingleton.getInstance(mContext).mstKbnDao();
        return mstKbnDao.getAll();
    }

}
