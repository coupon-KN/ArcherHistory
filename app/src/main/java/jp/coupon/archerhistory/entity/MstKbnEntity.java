package jp.coupon.archerhistory.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;


/**
 * 区分マスタ
 */
@Entity(tableName = "mst_kbn")
public class MstKbnEntity {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "value")
    public String value;

}
