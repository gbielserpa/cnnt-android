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
public final class FlashcardDao_Impl implements FlashcardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FlashcardEntity> __insertionAdapterOfFlashcardEntity;

  private final EntityDeletionOrUpdateAdapter<FlashcardEntity> __deletionAdapterOfFlashcardEntity;

  private final EntityDeletionOrUpdateAdapter<FlashcardEntity> __updateAdapterOfFlashcardEntity;

  public FlashcardDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFlashcardEntity = new EntityInsertionAdapter<FlashcardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `flashcards` (`id`,`front`,`back`,`tags`,`difficulty`,`linkedRegionId`,`boardId`,`reviewHistoryJson`,`nextReview`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FlashcardEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getFront());
        statement.bindString(3, entity.getBack());
        statement.bindString(4, entity.getTags());
        statement.bindString(5, entity.getDifficulty());
        if (entity.getLinkedRegionId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLinkedRegionId());
        }
        if (entity.getBoardId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getBoardId());
        }
        statement.bindString(8, entity.getReviewHistoryJson());
        statement.bindLong(9, entity.getNextReview());
        statement.bindLong(10, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfFlashcardEntity = new EntityDeletionOrUpdateAdapter<FlashcardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `flashcards` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FlashcardEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfFlashcardEntity = new EntityDeletionOrUpdateAdapter<FlashcardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `flashcards` SET `id` = ?,`front` = ?,`back` = ?,`tags` = ?,`difficulty` = ?,`linkedRegionId` = ?,`boardId` = ?,`reviewHistoryJson` = ?,`nextReview` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FlashcardEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getFront());
        statement.bindString(3, entity.getBack());
        statement.bindString(4, entity.getTags());
        statement.bindString(5, entity.getDifficulty());
        if (entity.getLinkedRegionId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLinkedRegionId());
        }
        if (entity.getBoardId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getBoardId());
        }
        statement.bindString(8, entity.getReviewHistoryJson());
        statement.bindLong(9, entity.getNextReview());
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindString(11, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final FlashcardEntity flashcard,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFlashcardEntity.insert(flashcard);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final FlashcardEntity flashcard,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFlashcardEntity.handle(flashcard);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final FlashcardEntity flashcard,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFlashcardEntity.handle(flashcard);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FlashcardEntity>> getAllFlashcards() {
    final String _sql = "SELECT * FROM flashcards ORDER BY nextReview ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"flashcards"}, new Callable<List<FlashcardEntity>>() {
      @Override
      @NonNull
      public List<FlashcardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
          final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfLinkedRegionId = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedRegionId");
          final int _cursorIndexOfBoardId = CursorUtil.getColumnIndexOrThrow(_cursor, "boardId");
          final int _cursorIndexOfReviewHistoryJson = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewHistoryJson");
          final int _cursorIndexOfNextReview = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReview");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<FlashcardEntity> _result = new ArrayList<FlashcardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FlashcardEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpFront;
            _tmpFront = _cursor.getString(_cursorIndexOfFront);
            final String _tmpBack;
            _tmpBack = _cursor.getString(_cursorIndexOfBack);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpLinkedRegionId;
            if (_cursor.isNull(_cursorIndexOfLinkedRegionId)) {
              _tmpLinkedRegionId = null;
            } else {
              _tmpLinkedRegionId = _cursor.getString(_cursorIndexOfLinkedRegionId);
            }
            final String _tmpBoardId;
            if (_cursor.isNull(_cursorIndexOfBoardId)) {
              _tmpBoardId = null;
            } else {
              _tmpBoardId = _cursor.getString(_cursorIndexOfBoardId);
            }
            final String _tmpReviewHistoryJson;
            _tmpReviewHistoryJson = _cursor.getString(_cursorIndexOfReviewHistoryJson);
            final long _tmpNextReview;
            _tmpNextReview = _cursor.getLong(_cursorIndexOfNextReview);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FlashcardEntity(_tmpId,_tmpFront,_tmpBack,_tmpTags,_tmpDifficulty,_tmpLinkedRegionId,_tmpBoardId,_tmpReviewHistoryJson,_tmpNextReview,_tmpCreatedAt);
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
  public Flow<List<FlashcardEntity>> getDueFlashcards(final long now) {
    final String _sql = "SELECT * FROM flashcards WHERE nextReview <= ? ORDER BY nextReview ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, now);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"flashcards"}, new Callable<List<FlashcardEntity>>() {
      @Override
      @NonNull
      public List<FlashcardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
          final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfLinkedRegionId = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedRegionId");
          final int _cursorIndexOfBoardId = CursorUtil.getColumnIndexOrThrow(_cursor, "boardId");
          final int _cursorIndexOfReviewHistoryJson = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewHistoryJson");
          final int _cursorIndexOfNextReview = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReview");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<FlashcardEntity> _result = new ArrayList<FlashcardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FlashcardEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpFront;
            _tmpFront = _cursor.getString(_cursorIndexOfFront);
            final String _tmpBack;
            _tmpBack = _cursor.getString(_cursorIndexOfBack);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpLinkedRegionId;
            if (_cursor.isNull(_cursorIndexOfLinkedRegionId)) {
              _tmpLinkedRegionId = null;
            } else {
              _tmpLinkedRegionId = _cursor.getString(_cursorIndexOfLinkedRegionId);
            }
            final String _tmpBoardId;
            if (_cursor.isNull(_cursorIndexOfBoardId)) {
              _tmpBoardId = null;
            } else {
              _tmpBoardId = _cursor.getString(_cursorIndexOfBoardId);
            }
            final String _tmpReviewHistoryJson;
            _tmpReviewHistoryJson = _cursor.getString(_cursorIndexOfReviewHistoryJson);
            final long _tmpNextReview;
            _tmpNextReview = _cursor.getLong(_cursorIndexOfNextReview);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FlashcardEntity(_tmpId,_tmpFront,_tmpBack,_tmpTags,_tmpDifficulty,_tmpLinkedRegionId,_tmpBoardId,_tmpReviewHistoryJson,_tmpNextReview,_tmpCreatedAt);
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
  public Object getFlashcardById(final String id,
      final Continuation<? super FlashcardEntity> $completion) {
    final String _sql = "SELECT * FROM flashcards WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FlashcardEntity>() {
      @Override
      @Nullable
      public FlashcardEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
          final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfLinkedRegionId = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedRegionId");
          final int _cursorIndexOfBoardId = CursorUtil.getColumnIndexOrThrow(_cursor, "boardId");
          final int _cursorIndexOfReviewHistoryJson = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewHistoryJson");
          final int _cursorIndexOfNextReview = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReview");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final FlashcardEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpFront;
            _tmpFront = _cursor.getString(_cursorIndexOfFront);
            final String _tmpBack;
            _tmpBack = _cursor.getString(_cursorIndexOfBack);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpLinkedRegionId;
            if (_cursor.isNull(_cursorIndexOfLinkedRegionId)) {
              _tmpLinkedRegionId = null;
            } else {
              _tmpLinkedRegionId = _cursor.getString(_cursorIndexOfLinkedRegionId);
            }
            final String _tmpBoardId;
            if (_cursor.isNull(_cursorIndexOfBoardId)) {
              _tmpBoardId = null;
            } else {
              _tmpBoardId = _cursor.getString(_cursorIndexOfBoardId);
            }
            final String _tmpReviewHistoryJson;
            _tmpReviewHistoryJson = _cursor.getString(_cursorIndexOfReviewHistoryJson);
            final long _tmpNextReview;
            _tmpNextReview = _cursor.getLong(_cursorIndexOfNextReview);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new FlashcardEntity(_tmpId,_tmpFront,_tmpBack,_tmpTags,_tmpDifficulty,_tmpLinkedRegionId,_tmpBoardId,_tmpReviewHistoryJson,_tmpNextReview,_tmpCreatedAt);
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
  public Flow<List<FlashcardEntity>> getFlashcardsByTag(final String tag) {
    final String _sql = "SELECT * FROM flashcards WHERE tags LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, tag);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"flashcards"}, new Callable<List<FlashcardEntity>>() {
      @Override
      @NonNull
      public List<FlashcardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
          final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfLinkedRegionId = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedRegionId");
          final int _cursorIndexOfBoardId = CursorUtil.getColumnIndexOrThrow(_cursor, "boardId");
          final int _cursorIndexOfReviewHistoryJson = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewHistoryJson");
          final int _cursorIndexOfNextReview = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReview");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<FlashcardEntity> _result = new ArrayList<FlashcardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FlashcardEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpFront;
            _tmpFront = _cursor.getString(_cursorIndexOfFront);
            final String _tmpBack;
            _tmpBack = _cursor.getString(_cursorIndexOfBack);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpLinkedRegionId;
            if (_cursor.isNull(_cursorIndexOfLinkedRegionId)) {
              _tmpLinkedRegionId = null;
            } else {
              _tmpLinkedRegionId = _cursor.getString(_cursorIndexOfLinkedRegionId);
            }
            final String _tmpBoardId;
            if (_cursor.isNull(_cursorIndexOfBoardId)) {
              _tmpBoardId = null;
            } else {
              _tmpBoardId = _cursor.getString(_cursorIndexOfBoardId);
            }
            final String _tmpReviewHistoryJson;
            _tmpReviewHistoryJson = _cursor.getString(_cursorIndexOfReviewHistoryJson);
            final long _tmpNextReview;
            _tmpNextReview = _cursor.getLong(_cursorIndexOfNextReview);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FlashcardEntity(_tmpId,_tmpFront,_tmpBack,_tmpTags,_tmpDifficulty,_tmpLinkedRegionId,_tmpBoardId,_tmpReviewHistoryJson,_tmpNextReview,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
