package jp.coupon.archerhistory.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import jp.coupon.archerhistory.entity.MstKbnEntity;


/**
 * 区分マスタDao
 */
@Dao
public interface MstKbnDao {
    @Query("SELECT * FROM mst_kbn WHERE id = (:id)")
    MstKbnEntity getKey(String id);

    @Query("SELECT * FROM mst_kbn")
    List<MstKbnEntity> getAll();

    @Insert
    void insertAll(MstKbnEntity... kbn);

    @Update
    Integer updateAll(MstKbnEntity... kbn);

}
