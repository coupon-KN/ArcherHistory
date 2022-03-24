package jp.coupon.archerhistory.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


/**
 * スコアテーブル
 */
@Entity(tableName = "tbl_score", primaryKeys = {"date", "seq"})
public class TblScoreEntity implements Serializable {
    @NonNull
    public String date;

    @NonNull
    public Integer seq;

    @ColumnInfo(name = "distance")
    public String distance;

    @ColumnInfo(name = "score1")
    public String score1;

    @ColumnInfo(name = "score2")
    public String score2;

    @ColumnInfo(name = "score3")
    public String score3;

    @ColumnInfo(name = "score4")
    public String score4;

    @ColumnInfo(name = "score5")
    public String score5;

    @ColumnInfo(name = "score6")
    public String score6;

    @ColumnInfo(name = "sub_total")
    public Integer sub_total;

}
