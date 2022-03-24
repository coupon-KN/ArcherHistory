package jp.coupon.archerhistory.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import jp.coupon.archerhistory.entity.TblScoreEntity;


/**
 * 区分マスタDao
 */
@Dao
public interface TblScoreDao {
    @Query("SELECT * FROM tbl_score WHERE date = (:date) ORDER BY seq ASC")
    List<TblScoreEntity> getListSortByAsc(String date);

    @Query("SELECT * FROM tbl_score WHERE date = (:date) ORDER BY seq DESC")
    List<TblScoreEntity> getListSortByDesc(String date);

    @Query("SELECT TOTAL(sub_total) / count(sub_total) as avg FROM tbl_score WHERE date = (:date)")
    Integer getAverage(String date);

    @Query("SELECT TOTAL(sub_total) as total FROM tbl_score WHERE date = (:date)")
    Integer getTotal(String date);

    @Query("SELECT date FROM tbl_score GROUP BY date ORDER BY date DESC")
    List<String> getDateList();

    @Query("SELECT distance FROM tbl_score GROUP BY distance ORDER BY distance DESC")
    List<String> getDistanceList();

    @Insert
    void insert(TblScoreEntity entity);

}
