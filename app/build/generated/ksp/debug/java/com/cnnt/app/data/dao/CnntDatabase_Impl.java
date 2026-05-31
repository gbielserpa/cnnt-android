package com.cnnt.app.data.dao;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CnntDatabase_Impl extends CnntDatabase {
  private volatile NotebookDao _notebookDao;

  private volatile BoardDao _boardDao;

  private volatile StrokeDao _strokeDao;

  private volatile SpatialObjectDao _spatialObjectDao;

  private volatile FlashcardDao _flashcardDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `notebooks` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `coverColor` INTEGER NOT NULL, `tags` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `boards` (`id` TEXT NOT NULL, `notebookId` TEXT NOT NULL, `name` TEXT NOT NULL, `order` INTEGER NOT NULL, `backgroundColor` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`notebookId`) REFERENCES `notebooks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_boards_notebookId` ON `boards` (`notebookId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `layers` (`id` TEXT NOT NULL, `boardId` TEXT NOT NULL, `name` TEXT NOT NULL, `visible` INTEGER NOT NULL, `locked` INTEGER NOT NULL, `opacity` REAL NOT NULL, `order` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`boardId`) REFERENCES `boards`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_layers_boardId` ON `layers` (`boardId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `strokes` (`id` TEXT NOT NULL, `layerId` TEXT NOT NULL, `brushId` TEXT NOT NULL, `color` INTEGER NOT NULL, `size` REAL NOT NULL, `opacity` REAL NOT NULL, `pointsData` BLOB NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`layerId`) REFERENCES `layers`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_strokes_layerId` ON `strokes` (`layerId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `spatial_objects` (`id` TEXT NOT NULL, `layerId` TEXT NOT NULL, `type` TEXT NOT NULL, `x` REAL NOT NULL, `y` REAL NOT NULL, `width` REAL NOT NULL, `height` REAL NOT NULL, `rotation` REAL NOT NULL, `zIndex` INTEGER NOT NULL, `locked` INTEGER NOT NULL, `groupId` TEXT, `contentJson` TEXT NOT NULL, `styleJson` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`layerId`) REFERENCES `layers`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_spatial_objects_layerId` ON `spatial_objects` (`layerId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `flashcards` (`id` TEXT NOT NULL, `front` TEXT NOT NULL, `back` TEXT NOT NULL, `tags` TEXT NOT NULL, `difficulty` TEXT NOT NULL, `linkedRegionId` TEXT, `boardId` TEXT, `reviewHistoryJson` TEXT NOT NULL, `nextReview` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `palettes` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `colorsJson` TEXT NOT NULL, `isDefault` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `brush_presets` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `category` TEXT NOT NULL, `configJson` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '42362b6e724a454c6c22cf4d25b16e17')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `notebooks`");
        db.execSQL("DROP TABLE IF EXISTS `boards`");
        db.execSQL("DROP TABLE IF EXISTS `layers`");
        db.execSQL("DROP TABLE IF EXISTS `strokes`");
        db.execSQL("DROP TABLE IF EXISTS `spatial_objects`");
        db.execSQL("DROP TABLE IF EXISTS `flashcards`");
        db.execSQL("DROP TABLE IF EXISTS `palettes`");
        db.execSQL("DROP TABLE IF EXISTS `brush_presets`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsNotebooks = new HashMap<String, TableInfo.Column>(6);
        _columnsNotebooks.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotebooks.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotebooks.put("coverColor", new TableInfo.Column("coverColor", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotebooks.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotebooks.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotebooks.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotebooks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotebooks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotebooks = new TableInfo("notebooks", _columnsNotebooks, _foreignKeysNotebooks, _indicesNotebooks);
        final TableInfo _existingNotebooks = TableInfo.read(db, "notebooks");
        if (!_infoNotebooks.equals(_existingNotebooks)) {
          return new RoomOpenHelper.ValidationResult(false, "notebooks(com.cnnt.app.data.dao.NotebookEntity).\n"
                  + " Expected:\n" + _infoNotebooks + "\n"
                  + " Found:\n" + _existingNotebooks);
        }
        final HashMap<String, TableInfo.Column> _columnsBoards = new HashMap<String, TableInfo.Column>(7);
        _columnsBoards.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBoards.put("notebookId", new TableInfo.Column("notebookId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBoards.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBoards.put("order", new TableInfo.Column("order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBoards.put("backgroundColor", new TableInfo.Column("backgroundColor", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBoards.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBoards.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBoards = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBoards.add(new TableInfo.ForeignKey("notebooks", "CASCADE", "NO ACTION", Arrays.asList("notebookId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBoards = new HashSet<TableInfo.Index>(1);
        _indicesBoards.add(new TableInfo.Index("index_boards_notebookId", false, Arrays.asList("notebookId"), Arrays.asList("ASC")));
        final TableInfo _infoBoards = new TableInfo("boards", _columnsBoards, _foreignKeysBoards, _indicesBoards);
        final TableInfo _existingBoards = TableInfo.read(db, "boards");
        if (!_infoBoards.equals(_existingBoards)) {
          return new RoomOpenHelper.ValidationResult(false, "boards(com.cnnt.app.data.dao.BoardEntity).\n"
                  + " Expected:\n" + _infoBoards + "\n"
                  + " Found:\n" + _existingBoards);
        }
        final HashMap<String, TableInfo.Column> _columnsLayers = new HashMap<String, TableInfo.Column>(7);
        _columnsLayers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayers.put("boardId", new TableInfo.Column("boardId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayers.put("visible", new TableInfo.Column("visible", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayers.put("locked", new TableInfo.Column("locked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayers.put("opacity", new TableInfo.Column("opacity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayers.put("order", new TableInfo.Column("order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLayers = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysLayers.add(new TableInfo.ForeignKey("boards", "CASCADE", "NO ACTION", Arrays.asList("boardId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesLayers = new HashSet<TableInfo.Index>(1);
        _indicesLayers.add(new TableInfo.Index("index_layers_boardId", false, Arrays.asList("boardId"), Arrays.asList("ASC")));
        final TableInfo _infoLayers = new TableInfo("layers", _columnsLayers, _foreignKeysLayers, _indicesLayers);
        final TableInfo _existingLayers = TableInfo.read(db, "layers");
        if (!_infoLayers.equals(_existingLayers)) {
          return new RoomOpenHelper.ValidationResult(false, "layers(com.cnnt.app.data.dao.LayerEntity).\n"
                  + " Expected:\n" + _infoLayers + "\n"
                  + " Found:\n" + _existingLayers);
        }
        final HashMap<String, TableInfo.Column> _columnsStrokes = new HashMap<String, TableInfo.Column>(8);
        _columnsStrokes.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStrokes.put("layerId", new TableInfo.Column("layerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStrokes.put("brushId", new TableInfo.Column("brushId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStrokes.put("color", new TableInfo.Column("color", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStrokes.put("size", new TableInfo.Column("size", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStrokes.put("opacity", new TableInfo.Column("opacity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStrokes.put("pointsData", new TableInfo.Column("pointsData", "BLOB", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStrokes.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStrokes = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysStrokes.add(new TableInfo.ForeignKey("layers", "CASCADE", "NO ACTION", Arrays.asList("layerId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesStrokes = new HashSet<TableInfo.Index>(1);
        _indicesStrokes.add(new TableInfo.Index("index_strokes_layerId", false, Arrays.asList("layerId"), Arrays.asList("ASC")));
        final TableInfo _infoStrokes = new TableInfo("strokes", _columnsStrokes, _foreignKeysStrokes, _indicesStrokes);
        final TableInfo _existingStrokes = TableInfo.read(db, "strokes");
        if (!_infoStrokes.equals(_existingStrokes)) {
          return new RoomOpenHelper.ValidationResult(false, "strokes(com.cnnt.app.data.dao.StrokeEntity).\n"
                  + " Expected:\n" + _infoStrokes + "\n"
                  + " Found:\n" + _existingStrokes);
        }
        final HashMap<String, TableInfo.Column> _columnsSpatialObjects = new HashMap<String, TableInfo.Column>(15);
        _columnsSpatialObjects.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("layerId", new TableInfo.Column("layerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("x", new TableInfo.Column("x", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("y", new TableInfo.Column("y", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("width", new TableInfo.Column("width", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("height", new TableInfo.Column("height", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("rotation", new TableInfo.Column("rotation", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("zIndex", new TableInfo.Column("zIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("locked", new TableInfo.Column("locked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("groupId", new TableInfo.Column("groupId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("contentJson", new TableInfo.Column("contentJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("styleJson", new TableInfo.Column("styleJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpatialObjects.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpatialObjects = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSpatialObjects.add(new TableInfo.ForeignKey("layers", "CASCADE", "NO ACTION", Arrays.asList("layerId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSpatialObjects = new HashSet<TableInfo.Index>(1);
        _indicesSpatialObjects.add(new TableInfo.Index("index_spatial_objects_layerId", false, Arrays.asList("layerId"), Arrays.asList("ASC")));
        final TableInfo _infoSpatialObjects = new TableInfo("spatial_objects", _columnsSpatialObjects, _foreignKeysSpatialObjects, _indicesSpatialObjects);
        final TableInfo _existingSpatialObjects = TableInfo.read(db, "spatial_objects");
        if (!_infoSpatialObjects.equals(_existingSpatialObjects)) {
          return new RoomOpenHelper.ValidationResult(false, "spatial_objects(com.cnnt.app.data.dao.SpatialObjectEntity).\n"
                  + " Expected:\n" + _infoSpatialObjects + "\n"
                  + " Found:\n" + _existingSpatialObjects);
        }
        final HashMap<String, TableInfo.Column> _columnsFlashcards = new HashMap<String, TableInfo.Column>(10);
        _columnsFlashcards.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlashcards.put("front", new TableInfo.Column("front", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlashcards.put("back", new TableInfo.Column("back", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlashcards.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlashcards.put("difficulty", new TableInfo.Column("difficulty", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlashcards.put("linkedRegionId", new TableInfo.Column("linkedRegionId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlashcards.put("boardId", new TableInfo.Column("boardId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlashcards.put("reviewHistoryJson", new TableInfo.Column("reviewHistoryJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlashcards.put("nextReview", new TableInfo.Column("nextReview", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlashcards.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFlashcards = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFlashcards = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFlashcards = new TableInfo("flashcards", _columnsFlashcards, _foreignKeysFlashcards, _indicesFlashcards);
        final TableInfo _existingFlashcards = TableInfo.read(db, "flashcards");
        if (!_infoFlashcards.equals(_existingFlashcards)) {
          return new RoomOpenHelper.ValidationResult(false, "flashcards(com.cnnt.app.data.dao.FlashcardEntity).\n"
                  + " Expected:\n" + _infoFlashcards + "\n"
                  + " Found:\n" + _existingFlashcards);
        }
        final HashMap<String, TableInfo.Column> _columnsPalettes = new HashMap<String, TableInfo.Column>(4);
        _columnsPalettes.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPalettes.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPalettes.put("colorsJson", new TableInfo.Column("colorsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPalettes.put("isDefault", new TableInfo.Column("isDefault", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPalettes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPalettes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPalettes = new TableInfo("palettes", _columnsPalettes, _foreignKeysPalettes, _indicesPalettes);
        final TableInfo _existingPalettes = TableInfo.read(db, "palettes");
        if (!_infoPalettes.equals(_existingPalettes)) {
          return new RoomOpenHelper.ValidationResult(false, "palettes(com.cnnt.app.data.dao.PaletteEntity).\n"
                  + " Expected:\n" + _infoPalettes + "\n"
                  + " Found:\n" + _existingPalettes);
        }
        final HashMap<String, TableInfo.Column> _columnsBrushPresets = new HashMap<String, TableInfo.Column>(4);
        _columnsBrushPresets.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrushPresets.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrushPresets.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrushPresets.put("configJson", new TableInfo.Column("configJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBrushPresets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBrushPresets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBrushPresets = new TableInfo("brush_presets", _columnsBrushPresets, _foreignKeysBrushPresets, _indicesBrushPresets);
        final TableInfo _existingBrushPresets = TableInfo.read(db, "brush_presets");
        if (!_infoBrushPresets.equals(_existingBrushPresets)) {
          return new RoomOpenHelper.ValidationResult(false, "brush_presets(com.cnnt.app.data.dao.BrushPresetEntity).\n"
                  + " Expected:\n" + _infoBrushPresets + "\n"
                  + " Found:\n" + _existingBrushPresets);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "42362b6e724a454c6c22cf4d25b16e17", "686350fb478d296ad6d6de7b36f7cc52");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "notebooks","boards","layers","strokes","spatial_objects","flashcards","palettes","brush_presets");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `notebooks`");
      _db.execSQL("DELETE FROM `boards`");
      _db.execSQL("DELETE FROM `layers`");
      _db.execSQL("DELETE FROM `strokes`");
      _db.execSQL("DELETE FROM `spatial_objects`");
      _db.execSQL("DELETE FROM `flashcards`");
      _db.execSQL("DELETE FROM `palettes`");
      _db.execSQL("DELETE FROM `brush_presets`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(NotebookDao.class, NotebookDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BoardDao.class, BoardDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StrokeDao.class, StrokeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SpatialObjectDao.class, SpatialObjectDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FlashcardDao.class, FlashcardDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public NotebookDao notebookDao() {
    if (_notebookDao != null) {
      return _notebookDao;
    } else {
      synchronized(this) {
        if(_notebookDao == null) {
          _notebookDao = new NotebookDao_Impl(this);
        }
        return _notebookDao;
      }
    }
  }

  @Override
  public BoardDao boardDao() {
    if (_boardDao != null) {
      return _boardDao;
    } else {
      synchronized(this) {
        if(_boardDao == null) {
          _boardDao = new BoardDao_Impl(this);
        }
        return _boardDao;
      }
    }
  }

  @Override
  public StrokeDao strokeDao() {
    if (_strokeDao != null) {
      return _strokeDao;
    } else {
      synchronized(this) {
        if(_strokeDao == null) {
          _strokeDao = new StrokeDao_Impl(this);
        }
        return _strokeDao;
      }
    }
  }

  @Override
  public SpatialObjectDao spatialObjectDao() {
    if (_spatialObjectDao != null) {
      return _spatialObjectDao;
    } else {
      synchronized(this) {
        if(_spatialObjectDao == null) {
          _spatialObjectDao = new SpatialObjectDao_Impl(this);
        }
        return _spatialObjectDao;
      }
    }
  }

  @Override
  public FlashcardDao flashcardDao() {
    if (_flashcardDao != null) {
      return _flashcardDao;
    } else {
      synchronized(this) {
        if(_flashcardDao == null) {
          _flashcardDao = new FlashcardDao_Impl(this);
        }
        return _flashcardDao;
      }
    }
  }
}
