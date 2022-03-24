package jp.coupon.archerhistory.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import jp.coupon.archerhistory.dao.MstKbnDao;
import jp.coupon.archerhistory.dao.TblScoreDao;
import jp.coupon.archerhistory.entity.MstKbnEntity;
import jp.coupon.archerhistory.entity.TblScoreEntity;


/**
 * データベース
 */
@Database(entities = {MstKbnEntity.class, TblScoreEntity.class}, version = 1, exportSchema = false)
public abstract class ArcherDatabase extends RoomDatabase {
    // 区分マスタ
    public abstract MstKbnDao mstKbnDao();
    // スコアテーブル
    public abstract TblScoreDao tblScoreDao();
}

