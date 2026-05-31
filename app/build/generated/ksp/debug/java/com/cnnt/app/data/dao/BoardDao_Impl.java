package com.cnnt.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BoardDao_Impl implements BoardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BoardEntity> __insertionAdapterOfBoardEntity;

  private final EntityInsertionAdapter<LayerEntity> __insertionAdapterOfLayerEntity;

  private final EntityDeletionOrUpdateAdapter<BoardEntity> __deletionAdapterOfBoardEntity;

  private final EntityDeletionOrUpdateAdapter<LayerEntity> __deletionAdapterOfLayerEntity;

  private final EntityDeletionOrUpdateAdapter<BoardEntity> __updateAdapterOfBoardEntity;

  private final EntityDeletionOrUpdateAdapter<LayerEntity> __updateAdapterOfLayerEntity;

  public BoardDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBoardEntity = new EntityInsertionAdapter<BoardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `boards` (`id`,`notebookId`,`name`,`order`,`backgroundColor`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BoardEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getNotebookId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getOrder());
        statement.bindLong(5, entity.getBackgroundColor());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfLayerEntity = new EntityInsertionAdapter<LayerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `layers` (`id`,`boardId`,`name`,`visible`,`locked`,`opacity`,`order`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LayerEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getBoardId());
        statement.bindString(3, entity.getName());
        final int _tmp = entity.getVisible() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getLocked() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindDouble(6, entity.getOpacity());
        statement.bindLong(7, entity.getOrder());
      }
    };
    this.__deletionAdapterOfBoardEntity = new EntityDeletionOrUpdateAdapter<BoardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `boards` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BoardEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__deletionAdapterOfLayerEntity = new EntityDeletionOrUpdateAdapter<LayerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `layers` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LayerEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfBoardEntity = new EntityDeletionOrUpdateAdapter<BoardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `boards` SET `id` = ?,`notebookId` = ?,`name` = ?,`order` = ?,`backgroundColor` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BoardEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getNotebookId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getOrder());
        statement.bindLong(5, entity.getBackgroundColor());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        statement.bindString(8, entity.getId());
      }
    };
    this.__updateAdapterOfLayerEntity = new EntityDeletionOrUpdateAdapter<LayerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `layers` SET `id` = ?,`boardId` = ?,`name` = ?,`visible` = ?,`locked` = ?,`opacity` = ?,`order` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LayerEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getBoardId());
        statement.bindString(3, entity.getName());
        final int _tmp = entity.getVisible() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getLocked() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindDouble(6, entity.getOpacity());
        statement.bindLong(7, entity.getOrder());
        statement.bindString(8, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final BoardEntity board, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBoardEntity.insert(board);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertLayer(final LayerEntity layer, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLayerEntity.insert(layer);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final BoardEntity board, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBoardEntity.handle(board);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLayer(final LayerEntity layer, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfLayerEntity.handle(layer);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BoardEntity board, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBoardEntity.handle(board);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLayer(final LayerEntity layer, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfLayerEntity.handle(layer);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BoardEntity>> getBoardsByNotebook(final String notebookId) {
    final String _sql = "SELECT * FROM boards WHERE notebookId = ? ORDER BY `order`";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, notebookId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"boards"}, new Callable<List<BoardEntity>>() {
      @Override
      @NonNull
      public List<BoardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNotebookId = CursorUtil.getColumnIndexOrThrow(_cursor, "notebookId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfBackgroundColor = CursorUtil.getColumnIndexOrThrow(_cursor, "backgroundColor");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<BoardEntity> _result = new ArrayList<BoardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BoardEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpNotebookId;
            _tmpNotebookId = _cursor.getString(_cursorIndexOfNotebookId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final int _tmpBackgroundColor;
            _tmpBackgroundColor = _cursor.getInt(_cursorIndexOfBackgroundColor);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new BoardEntity(_tmpId,_tmpNotebookId,_tmpName,_tmpOrder,_tmpBackgroundColor,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBoardById(final String id, final Continuation<? super BoardEntity> $completion) {
    final String _sql = "SELECT * FROM boards WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BoardEntity>() {
      @Override
      @Nullable
      public BoardEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNotebookId = CursorUtil.getColumnIndexOrThrow(_cursor, "notebookId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfBackgroundColor = CursorUtil.getColumnIndexOrThrow(_cursor, "backgroundColor");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final BoardEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpNotebookId;
            _tmpNotebookId = _cursor.getString(_cursorIndexOfNotebookId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final int _tmpBackgroundColor;
            _tmpBackgroundColor = _cursor.getInt(_cursorIndexOfBackgroundColor);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new BoardEntity(_tmpId,_tmpNotebookId,_tmpName,_tmpOrder,_tmpBackgroundColor,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLayersForBoard(final String boardId,
      final Continuation<? super List<LayerEntity>> $completion) {
    final String _sql = "SELECT * FROM layers WHERE boardId = ? ORDER BY `order`";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, boardId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<LayerEntity>>() {
      @Override
      @NonNull
      public List<LayerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBoardId = CursorUtil.getColumnIndexOrThrow(_cursor, "boardId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "visible");
          final int _cursorIndexOfLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "locked");
          final int _cursorIndexOfOpacity = CursorUtil.getColumnIndexOrThrow(_cursor, "opacity");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final List<LayerEntity> _result = new ArrayList<LayerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LayerEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpBoardId;
            _tmpBoardId = _cursor.getString(_cursorIndexOfBoardId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfVisible);
            _tmpVisible = _tmp != 0;
            final boolean _tmpLocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfLocked);
            _tmpLocked = _tmp_1 != 0;
            final float _tmpOpacity;
            _tmpOpacity = _cursor.getFloat(_cursorIndexOfOpacity);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            _item = new LayerEntity(_tmpId,_tmpBoardId,_tmpName,_tmpVisible,_tmpLocked,_tmpOpacity,_tmpOrder);
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
