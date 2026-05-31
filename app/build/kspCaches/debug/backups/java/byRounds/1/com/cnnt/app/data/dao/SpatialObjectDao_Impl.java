package com.cnnt.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SpatialObjectDao_Impl implements SpatialObjectDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SpatialObjectEntity> __insertionAdapterOfSpatialObjectEntity;

  private final EntityDeletionOrUpdateAdapter<SpatialObjectEntity> __updateAdapterOfSpatialObjectEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public SpatialObjectDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSpatialObjectEntity = new EntityInsertionAdapter<SpatialObjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `spatial_objects` (`id`,`layerId`,`type`,`x`,`y`,`width`,`height`,`rotation`,`zIndex`,`locked`,`groupId`,`contentJson`,`styleJson`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SpatialObjectEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getLayerId());
        statement.bindString(3, entity.getType());
        statement.bindDouble(4, entity.getX());
        statement.bindDouble(5, entity.getY());
        statement.bindDouble(6, entity.getWidth());
        statement.bindDouble(7, entity.getHeight());
        statement.bindDouble(8, entity.getRotation());
        statement.bindLong(9, entity.getZIndex());
        final int _tmp = entity.getLocked() ? 1 : 0;
        statement.bindLong(10, _tmp);
        if (entity.getGroupId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getGroupId());
        }
        statement.bindString(12, entity.getContentJson());
        statement.bindString(13, entity.getStyleJson());
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfSpatialObjectEntity = new EntityDeletionOrUpdateAdapter<SpatialObjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `spatial_objects` SET `id` = ?,`layerId` = ?,`type` = ?,`x` = ?,`y` = ?,`width` = ?,`height` = ?,`rotation` = ?,`zIndex` = ?,`locked` = ?,`groupId` = ?,`contentJson` = ?,`styleJson` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SpatialObjectEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getLayerId());
        statement.bindString(3, entity.getType());
        statement.bindDouble(4, entity.getX());
        statement.bindDouble(5, entity.getY());
        statement.bindDouble(6, entity.getWidth());
        statement.bindDouble(7, entity.getHeight());
        statement.bindDouble(8, entity.getRotation());
        statement.bindLong(9, entity.getZIndex());
        final int _tmp = entity.getLocked() ? 1 : 0;
        statement.bindLong(10, _tmp);
        if (entity.getGroupId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getGroupId());
        }
        statement.bindString(12, entity.getContentJson());
        statement.bindString(13, entity.getStyleJson());
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getUpdatedAt());
        statement.bindString(16, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM spatial_objects WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SpatialObjectEntity obj,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSpatialObjectEntity.insert(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SpatialObjectEntity obj,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSpatialObjectEntity.handle(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String objId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, objId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getObjectsForLayer(final String layerId,
      final Continuation<? super List<SpatialObjectEntity>> $completion) {
    final String _sql = "SELECT * FROM spatial_objects WHERE layerId = ? ORDER BY zIndex";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, layerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SpatialObjectEntity>>() {
      @Override
      @NonNull
      public List<SpatialObjectEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLayerId = CursorUtil.getColumnIndexOrThrow(_cursor, "layerId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfX = CursorUtil.getColumnIndexOrThrow(_cursor, "x");
          final int _cursorIndexOfY = CursorUtil.getColumnIndexOrThrow(_cursor, "y");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfRotation = CursorUtil.getColumnIndexOrThrow(_cursor, "rotation");
          final int _cursorIndexOfZIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "zIndex");
          final int _cursorIndexOfLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "locked");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfContentJson = CursorUtil.getColumnIndexOrThrow(_cursor, "contentJson");
          final int _cursorIndexOfStyleJson = CursorUtil.getColumnIndexOrThrow(_cursor, "styleJson");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<SpatialObjectEntity> _result = new ArrayList<SpatialObjectEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SpatialObjectEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpLayerId;
            _tmpLayerId = _cursor.getString(_cursorIndexOfLayerId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final float _tmpX;
            _tmpX = _cursor.getFloat(_cursorIndexOfX);
            final float _tmpY;
            _tmpY = _cursor.getFloat(_cursorIndexOfY);
            final float _tmpWidth;
            _tmpWidth = _cursor.getFloat(_cursorIndexOfWidth);
            final float _tmpHeight;
            _tmpHeight = _cursor.getFloat(_cursorIndexOfHeight);
            final float _tmpRotation;
            _tmpRotation = _cursor.getFloat(_cursorIndexOfRotation);
            final int _tmpZIndex;
            _tmpZIndex = _cursor.getInt(_cursorIndexOfZIndex);
            final boolean _tmpLocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfLocked);
            _tmpLocked = _tmp != 0;
            final String _tmpGroupId;
            if (_cursor.isNull(_cursorIndexOfGroupId)) {
              _tmpGroupId = null;
            } else {
              _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            }
            final String _tmpContentJson;
            _tmpContentJson = _cursor.getString(_cursorIndexOfContentJson);
            final String _tmpStyleJson;
            _tmpStyleJson = _cursor.getString(_cursorIndexOfStyleJson);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new SpatialObjectEntity(_tmpId,_tmpLayerId,_tmpType,_tmpX,_tmpY,_tmpWidth,_tmpHeight,_tmpRotation,_tmpZIndex,_tmpLocked,_tmpGroupId,_tmpContentJson,_tmpStyleJson,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
