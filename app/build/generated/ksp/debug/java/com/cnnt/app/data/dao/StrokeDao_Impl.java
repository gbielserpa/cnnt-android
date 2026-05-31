package com.cnnt.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
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
public final class StrokeDao_Impl implements StrokeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StrokeEntity> __insertionAdapterOfStrokeEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllForLayer;

  public StrokeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStrokeEntity = new EntityInsertionAdapter<StrokeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `strokes` (`id`,`layerId`,`brushId`,`color`,`size`,`opacity`,`pointsData`,`createdAt`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StrokeEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getLayerId());
        statement.bindString(3, entity.getBrushId());
        statement.bindLong(4, entity.getColor());
        statement.bindDouble(5, entity.getSize());
        statement.bindDouble(6, entity.getOpacity());
        statement.bindBlob(7, entity.getPointsData());
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM strokes WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllForLayer = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM strokes WHERE layerId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final StrokeEntity stroke, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStrokeEntity.insert(stroke);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<StrokeEntity> strokes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStrokeEntity.insert(strokes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String strokeId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, strokeId);
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
  public Object deleteAllForLayer(final String layerId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllForLayer.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, layerId);
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
          __preparedStmtOfDeleteAllForLayer.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getStrokesForLayer(final String layerId,
      final Continuation<? super List<StrokeEntity>> $completion) {
    final String _sql = "SELECT * FROM strokes WHERE layerId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, layerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<StrokeEntity>>() {
      @Override
      @NonNull
      public List<StrokeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLayerId = CursorUtil.getColumnIndexOrThrow(_cursor, "layerId");
          final int _cursorIndexOfBrushId = CursorUtil.getColumnIndexOrThrow(_cursor, "brushId");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
          final int _cursorIndexOfOpacity = CursorUtil.getColumnIndexOrThrow(_cursor, "opacity");
          final int _cursorIndexOfPointsData = CursorUtil.getColumnIndexOrThrow(_cursor, "pointsData");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<StrokeEntity> _result = new ArrayList<StrokeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StrokeEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpLayerId;
            _tmpLayerId = _cursor.getString(_cursorIndexOfLayerId);
            final String _tmpBrushId;
            _tmpBrushId = _cursor.getString(_cursorIndexOfBrushId);
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final float _tmpSize;
            _tmpSize = _cursor.getFloat(_cursorIndexOfSize);
            final float _tmpOpacity;
            _tmpOpacity = _cursor.getFloat(_cursorIndexOfOpacity);
            final byte[] _tmpPointsData;
            _tmpPointsData = _cursor.getBlob(_cursorIndexOfPointsData);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new StrokeEntity(_tmpId,_tmpLayerId,_tmpBrushId,_tmpColor,_tmpSize,_tmpOpacity,_tmpPointsData,_tmpCreatedAt);
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
