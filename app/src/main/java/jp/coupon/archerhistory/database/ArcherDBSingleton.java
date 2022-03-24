package jp.coupon.archerhistory.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.migration.Migration;

public class ArcherDBSingleton {
    private static ArcherDatabase instance = null;
    private ArcherDBSingleton() {}

    public static ArcherDatabase getInstance(Context context) {
        if (instance != null) {
            return instance;
        }

        instance = Room.databaseBuilder(context, ArcherDatabase.class, "archer-db").build();
        return instance;
    }
}