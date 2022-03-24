package jp.coupon.archerhistory.loader;

import static jp.coupon.archerhistory.common.Constants.MST_KBN_ID_SUM_ROUND;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

import jp.coupon.archerhistory.bean.HistoryInitDataBean;
import jp.coupon.archerhistory.dao.MstKbnDao;
import jp.coupon.archerhistory.dao.TblScoreDao;
import jp.coupon.archerhistory.database.ArcherDBSingleton;
import jp.coupon.archerhistory.entity.MstKbnEntity;
import jp.coupon.archerhistory.entity.TblScoreEntity;


/**
 * 履歴画面の初期データ所得
 */
public class GetHistoryInitDataLoader extends AsyncTaskLoader<HistoryInitDataBean> {

    Context mContext;
    HistoryInitDataBean mResultBean;

    public GetHistoryInitDataLoader(@NonNull Context context) {
        super(context);
        mContext = context;
        mResultBean = new HistoryInitDataBean();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public HistoryInitDataBean loadInBackground() {

        MstKbnDao mstKbnDao = ArcherDBSingleton.getInstance(mContext).mstKbnDao();
        MstKbnEntity entity = mstKbnDao.getKey(MST_KBN_ID_SUM_ROUND);
        if(entity != null) {
            mResultBean.setSumRoundNum(entity.value);
        }

        TblScoreDao tblScoreDao = ArcherDBSingleton.getInstance(mContext).tblScoreDao();
        List<String> lstDate = tblScoreDao.getDateList();
        if(lstDate != null){
            // スピナーデータ
            mResultBean.setLstDate(lstDate);
            mResultBean.setLstDistance(tblScoreDao.getDistanceList());
            // スコアデータ
            String date = mResultBean.getLstDate().get(0);
            mResultBean.setLstScoreEntity(tblScoreDao.getListSortByAsc(date));
        }

        return mResultBean;
    }

}
