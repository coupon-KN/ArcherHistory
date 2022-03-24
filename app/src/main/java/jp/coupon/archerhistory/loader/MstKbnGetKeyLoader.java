package jp.coupon.archerhistory.loader;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import jp.coupon.archerhistory.activity.MainActivity;
import jp.coupon.archerhistory.dao.MstKbnDao;
import jp.coupon.archerhistory.database.ArcherDBSingleton;
import jp.coupon.archerhistory.entity.MstKbnEntity;


/**
 * マスタ区分をkeyで取得
 */
public class MstKbnGetKeyLoader extends AsyncTaskLoader<MstKbnEntity> {

    Context mContext;
    String mId;
    String mKbnDefault;

    public MstKbnGetKeyLoader(@NonNull Context context, String id, String defaultVal) {
        super(context);
        mContext = context;
        mId = id;
        mKbnDefault = defaultVal;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public MstKbnEntity loadInBackground() {
        MstKbnDao mstKbnDao = ArcherDBSingleton.getInstance(mContext).mstKbnDao();
        MstKbnEntity rtnEntity = mstKbnDao.getKey(mId);
        if(rtnEntity == null){
            // なければ作成
            MstKbnEntity insertEnt = new MstKbnEntity();
            insertEnt.id = mId;
            insertEnt.value = mKbnDefault;
            mstKbnDao.insertAll(insertEnt);

            rtnEntity = insertEnt;
        }

        return rtnEntity;
    }

}
