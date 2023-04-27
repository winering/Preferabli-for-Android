//
//  Tools_Database.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/30/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.ringit.datasdk.BuildConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Internal class for managing the SQLite database.
 */
class Tools_Database {

    // constants for searches
    private static final String KEY_SEARCHLOCALID = "SearchLocalId";
    private static final String KEY_SEARCHCOUNT = "SearchCount";
    private static final String KEY_SEARCHLAST = "SearchLast";
    private static final String KEY_SEARCHTEXT = "SearchText";

    // constants for products
    private static final String KEY_PRODUCTLOCALID = "WineLocalId";
    private static final String KEY_PRODUCTID = "PRODUCTID";
    private static final String KEY_PRODUCTIMAGE = "WineImage";
    private static final String KEY_PRODUCTBACKIMAGE = "WineBackImage";
    private static final String KEY_PRODUCTCA = "WineCA";
    private static final String KEY_PRODUCTUA = "WineUA";
    private static final String KEY_PRODUCTNAME = "WineName";
    private static final String KEY_PRODUCTGRAPE = "WineGrape";
    private static final String KEY_PRODUCTDECANT = "WineDecant";
    private static final String KEY_PRODUCTNV = "WineNV";
    private static final String KEY_PRODUCTTYPE = "WineType";
    private static final String KEY_PRODUCTBRAND = "WineBrand";
    private static final String KEY_PRODUCTBRANDLAT = "WineBrandLat";
    private static final String KEY_PRODUCTBRANDLON = "WineBrandLon";
    private static final String KEY_PRODUCTREGION = "WineRegion";
    private static final String KEY_PRODUCTDIRTY = "WineDirty";
    private static final String KEY_PRODUCTCAT = "WineCategory";
    private static final String KEY_PRODUCTRSL = "WineRateSourceLocation";
    private static final String KEY_PRODUCTSC = "WineSubCat";
    private static final String KEY_PRODUCTHASH = "WineHash";

    // constants for variants
    private static final String KEY_VARIANTLOCALID = "VariantLocalId";
    private static final String KEY_VARIANTID = "VariantId";
    private static final String KEY_VARIANTPRODUCTID = "VariantPRODUCTID";
    private static final String KEY_VARIANTIMAGE = "VariantImage";
    private static final String KEY_VARIANTCA = "VariantCA";
    private static final String KEY_VARIANTUA = "VariantUA";
    private static final String KEY_VARIANTYEAR = "VariantYear";
    private static final String KEY_VARIANTFRESH = "VariantFresh";
    private static final String KEY_VARIANTRECOMMENDABLE = "VariantRecommendable";
    private static final String KEY_VARIANTPRICE = "VariantPrice";
    private static final String KEY_VARIANTDOLLARSIGNS = "VariantDollarSigns";

    // constants for tags
    private static final String KEY_TAGLOCALID = "TagLocalId";
    private static final String KEY_TAGID = "TagId";
    private static final String KEY_TAGUSERID = "TagUserId";
    private static final String KEY_TAGVARIANTID = "TagVariantId";
    private static final String KEY_TAGPRODUCTID = "TagPRODUCTID";
    private static final String KEY_TAGCOLLECTIONID = "TagCollectionId";
    private static final String KEY_TAGCHANNELID = "TagChannelId";
    private static final String KEY_TAGGEDINCID = "TaggedInCID";
    private static final String KEY_TAGGEDINCVID = "TaggedInCVID";
    private static final String KEY_TAGTYPE = "TagType";
    private static final String KEY_TAGVALUE = "TagValue";
    private static final String KEY_TAGCOMMENT = "TagComment";
    private static final String KEY_TAGLOCATION = "TagLocation";
    private static final String KEY_TAGSHARING = "TagSharing";
    private static final String KEY_TAGCA = "TagCA";
    private static final String KEY_TAGUA = "TagUA";
    private static final String KEY_TAGDIRTY = "TagDirty";
    private static final String KEY_TAGBADGE = "TagBadge";
    private static final String KEY_TAGPARAMS = "TagParams";
    private static final String KEY_TAGORDERINGID = "TagOrderingId";
    private static final String KEY_TAGCHNAME = "TagChannelName";
    private static final String KEY_TAGFML = "TagFormatML";
    private static final String KEY_TAGPRICE = "TagPrice";
    private static final String KEY_TAGQUANTITY = "TagQuantity";
    private static final String KEY_TAGBIN = "TagBin";
    private static final String KEY_TAGCUSTOMERID = "TagCustId";

    // constants for collections
    private static final String KEY_COLLECTIONLOCALID = "CollectionLocalId";
    private static final String KEY_COLLECTIONID = "CollectionId";
    private static final String KEY_COLLECTIONCID = "CollectionChannelId";
    private static final String KEY_COLLECTIONSCID = "CollectionSortChannelId";
    private static final String KEY_COLLECTIONDESC = "CollectionDescription";
    private static final String KEY_COLLECTIONIMAGE = "CollectionImage";
    private static final String KEY_COLLECTIONORDER = "CollectionOrder";
    private static final String KEY_COLLECTIONVENUE = "CollectionVenue";
    private static final String KEY_COLLECTIONTRAITS = "CollectionTraits";
    private static final String KEY_COLLECTIONNAME = "CollectionName";
    private static final String KEY_COLLECTIONDN = "CollectionDisplayName";
    private static final String KEY_COLLECTIONCODE = "CollectionCodeActivity";
    private static final String KEY_COLLECTIONPUBLISHED = "CollectionPublished";
    private static final String KEY_COLLECTIONSD = "CollectionSD";
    private static final String KEY_COLLECTIONED = "CollectionED";
    private static final String KEY_COLLECTIONCOMMENT = "CollectionComment";
    private static final String KEY_COLLECTIONCA = "CollectionCA";
    private static final String KEY_COLLECTIONUA = "CollectionUA";
    private static final String KEY_COLLECTIONAUTOWILI = "CollectionAutoWili";
    private static final String KEY_COLLECTIONCHANNELNAME = "CollectionChannelName";
    private static final String KEY_COLLECTIONBLIND = "CollectionBlind";
    private static final String KEY_COLLECTIONCURRENCY = "CollectionCurrency";
    private static final String KEY_COLLECTIONTIMEZONE = "CollectionTimeZone";
    private static final String KEY_COLLECTIONBADGE = "CollectionBadge";
    private static final String KEY_COLLECTIONDQ = "CollectionDQ";
    private static final String KEY_COLLECTIONDP = "CollectionDP";
    private static final String KEY_COLLECTIONDB = "CollectionDB";
    private static final String KEY_COLLECTIONDT = "CollectionDT";
    private static final String KEY_COLLECTIONDGH = "CollectionDGH";
    private static final String KEY_COLLECTIONHPO = "CollectionHPO";
    private static final String KEY_COLLECTIONWC = "CollectionWC";
    private static final String KEY_COLLECTIONPINNED = "CollectionPinned";
    private static final String KEY_COLLECTIONBROWSE = "CollectionBrowsable";
    private static final String KEY_COLLECTIONARCHIVE = "CollectionArchive";

    // constants for versions
    private static final String KEY_VERSIONLOCALID = "VersionLocalId";
    private static final String KEY_VERSIONID = "VersionId";
    private static final String KEY_VERSIONCID = "VersionCollectionId";

    // constants for groups
    private static final String KEY_GROUPLOCALID = "GroupLocalId";
    private static final String KEY_GROUPID = "GroupId";
    private static final String KEY_GROUPVID = "GroupVersionId";
    private static final String KEY_GROUPOC = "GroupOrderingsCount";
    private static final String KEY_GROUPNAME = "GroupName";
    private static final String KEY_GROUPORDER = "GroupOrder";

    // constants for orderings
    private static final String KEY_ORDERINGLOCALID = "OrderingLocalId";
    private static final String KEY_ORDERINGID = "OrderingId";
    private static final String KEY_ORDERINGGID = "OrderingGroupId";
    private static final String KEY_ORDERINGCVTID = "OrderingCollectionVariantTagId";
    private static final String KEY_ORDERINGORDER = "OrderingOrder";
    private static final String KEY_ORDERINGDIRTY = "OrderingDirty";

    // constants for styles
    private static final String KEY_STYLELOCALID = "StyleLocalId";
    private static final String KEY_STYLEID = "StyleId";
    private static final String KEY_STYLEOR = "StyleOR";
    private static final String KEY_STYLEOP = "StyleOP";
    private static final String KEY_STYLEREFINE = "StyleRefine";
    private static final String KEY_STYLECONFLICT = "StyleConflict";
    private static final String KEY_STYLEKEYWORDS = "StyleKeywords";
    private static final String KEY_STYLENAME = "StyleName";
    private static final String KEY_STYLETYPE = "StyleType";
    private static final String KEY_STYLEDESCRIPTION = "StyleDescription";
    private static final String KEY_STYLERATING = "StyleRating";
    private static final String KEY_STYLEFOODS = "StyleFoods";
    private static final String KEY_STYLEIMAGE = "StyleImage";
    private static final String KEY_STYLECAT = "StyleCategory";
    private static final String KEY_STYLELOC = "StyleLocations";
    private static final String KEY_STYLESTRENGTH = "StyleStrength";
    private static final String KEY_STYLECREATED = "StyleCreated";
    private static final String KEY_STYLECID = "StyleCID";

    // constants for usercollections
    private static final String KEY_UCOLLECTIONLOCALID = "UserCollectionLocalId";
    private static final String KEY_UCOLLECTIONID = "UserCollectionId";
    private static final String KEY_UCOLLECTIONRT = "UserCollectionRelationshipType";
    private static final String KEY_UCOLLECTIONVIEWER = "UserCollectionViewer";
    private static final String KEY_UCOLLECTIONPINNED = "UserCollectionPinned";
    private static final String KEY_UCOLLECTIONEDITOR = "UserCollectionEditor";
    private static final String KEY_UCOLLECTIONADMIN = "UserCollectionAdmin";
    private static final String KEY_UCOLLECTIONCA = "UserCollectionCreatedAt";
    private static final String KEY_UCOLLECTIONUA = "UserCollectionUpdatedAt";
    private static final String KEY_UCOLLECTIONCID = "UserCollectionCollectionId";
    private static final String KEY_UCOLLECTIONAT = "UserCollectionArchivedAt";

    // constants for customers
    private static final String KEY_CUSTOMERLOCALID = "CustomerLocalId";
    private static final String KEY_CUSTOMERID = "CustomerId";
    private static final String KEY_CUSTOMERAVA = "CustomerAvatar";
    private static final String KEY_CUSTOMERMEMAIL = "CustomerMerchantEmail";
    private static final String KEY_CUSTOMERMUID = "CustomerMerchantUserId";
    private static final String KEY_CUSTOMERMUN = "CustomerMerchantUsername";
    private static final String KEY_CUSTOMERROLE = "CustomerRole";
    private static final String KEY_CUSTOMERUID = "CustomerUserId";
    private static final String KEY_CUSTOMERCHID = "CustomerChannelId";
    private static final String KEY_CUSTOMERHP = "CustomerHasProfile";
    private static final String KEY_CUSTOMERMUDN = "CustomerMUDN";
    private static final String KEY_CUSTOMERCC = "CustomerClaimCode";
    private static final String KEY_CUSTOMERORDER = "CustomerOrder";

    // database constants
    private static final String DATABASE_NAME = "PreferabliDB";
    private static final String TABLE_PRODUCTS = "Products";
    private static final String TABLE_VARIANTS = "Variants";
    private static final String TABLE_TAGS = "Tags";
    private static final String TABLE_COLLECTIONS = "Collections";
    private static final String TABLE_STYLES = "Styles";
    private static final String TABLE_VERSIONS = "Versions";
    private static final String TABLE_GROUPS = "Groups";
    private static final String TABLE_ORDERINGS = "Orderings";
    private static final String TABLE_USERCOLLECTIONS = "UserCollections";
    private static final String TABLE_SEARCHES = "Searches";
    private static final String TABLE_CUSTOMERS = "Customers";

    private int mOpenCounter;
    private static Tools_Database instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    static class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

        public SQLiteOpenHelper() {
            super(Tools_PreferabliApp.getAppContext(), DATABASE_NAME, null, BuildConfig.DBVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" + KEY_PRODUCTLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_PRODUCTID + " LONG UNIQUE, " + KEY_PRODUCTNAME + " TEXT, " + KEY_PRODUCTCAT + " TEXT, " + KEY_PRODUCTHASH + " TEXT, " + KEY_PRODUCTSC + " TEXT, " + KEY_PRODUCTBACKIMAGE + " TEXT, " + KEY_PRODUCTGRAPE + " TEXT, " + KEY_PRODUCTDECANT + " INTEGER, " + KEY_PRODUCTNV + " INTEGER, " + KEY_PRODUCTDIRTY + " INTEGER, " + KEY_PRODUCTTYPE + " TEXT, " + KEY_PRODUCTRSL + " TEXT, " + KEY_PRODUCTBRANDLAT + " DOUBLE, " + KEY_PRODUCTBRANDLON + " DOUBLE, " + KEY_PRODUCTBRAND + " TEXT, " + KEY_PRODUCTREGION + " TEXT, " + KEY_PRODUCTUA + " TEXT, " + KEY_PRODUCTIMAGE + " TEXT, " + KEY_PRODUCTCA + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_VARIANTS + " (" + KEY_VARIANTLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_VARIANTID + " LONG UNIQUE, " + KEY_VARIANTCA + " TEXT, " + KEY_VARIANTDOLLARSIGNS + " INTEGER, " + KEY_VARIANTFRESH + " INTEGER, " + KEY_VARIANTIMAGE + " TEXT, " + KEY_VARIANTPRICE + " DOUBLE, " + KEY_VARIANTRECOMMENDABLE + " INTEGER, " + KEY_VARIANTUA + " TEXT, " + KEY_VARIANTPRODUCTID + " LONG, " + KEY_VARIANTYEAR + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_TAGS + " (" + KEY_TAGLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_TAGID + " LONG UNIQUE, " + KEY_TAGCA + " TEXT, " + KEY_TAGCOLLECTIONID + " LONG, " + KEY_TAGCHANNELID + " LONG, " + KEY_TAGORDERINGID + " LONG, " + KEY_TAGCUSTOMERID + " LONG, " + KEY_TAGPRODUCTID + " LONG, " + KEY_TAGCOMMENT + " TEXT, " + KEY_TAGBADGE + " TEXT, " + KEY_TAGCHNAME + " TEXT, " + KEY_TAGPARAMS + " TEXT, " + KEY_TAGFML + " INTEGER, " + KEY_TAGPRICE + " DOUBLE, " + KEY_TAGQUANTITY + " INTEGER, " + KEY_TAGBIN + " TEXT, "+ KEY_TAGGEDINCID + " LONG, " + KEY_TAGDIRTY + " INTEGER, " + KEY_TAGGEDINCVID + " LONG, " + KEY_TAGLOCATION + " TEXT, " + KEY_TAGSHARING + " TEXT, " + KEY_TAGTYPE + " TEXT, " + KEY_TAGUSERID + " LONG, " + KEY_TAGVALUE + " TEXT, " + KEY_TAGVARIANTID + " LONG, " + KEY_TAGUA + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_COLLECTIONS + " (" + KEY_COLLECTIONLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_COLLECTIONID + " LONG UNIQUE, " + KEY_COLLECTIONCA + " TEXT, " + KEY_COLLECTIONCID + " LONG, " + KEY_COLLECTIONSCID + " LONG, " + KEY_COLLECTIONCODE + " TEXT, " + KEY_COLLECTIONCOMMENT + " TEXT, "  + KEY_COLLECTIONBROWSE + " INTEGER, " + KEY_COLLECTIONARCHIVE + " INTEGER, " + KEY_COLLECTIONCHANNELNAME + " TEXT, " + KEY_COLLECTIONDESC + " TEXT, " + KEY_COLLECTIONDN + " TEXT, " + KEY_COLLECTIONED + " TEXT, " + KEY_COLLECTIONIMAGE + " TEXT, " + KEY_COLLECTIONNAME + " TEXT, " + KEY_COLLECTIONORDER + " INTEGER, " + KEY_COLLECTIONDGH + " INTEGER, " + KEY_COLLECTIONPINNED + " INTEGER, " + KEY_COLLECTIONHPO + " INTEGER, " + KEY_COLLECTIONWC + " INTEGER, " + KEY_COLLECTIONAUTOWILI + " INTEGER, " + KEY_COLLECTIONPUBLISHED + " INTEGER, " + KEY_COLLECTIONVENUE + " TEXT, " + KEY_COLLECTIONCURRENCY + " TEXT, " + KEY_COLLECTIONTIMEZONE + " TEXT, " + KEY_COLLECTIONBADGE + " TEXT, " + KEY_COLLECTIONDB + " TEXT, " + KEY_COLLECTIONDP + " TEXT, " + KEY_COLLECTIONDQ + " TEXT, " + KEY_COLLECTIONDT + " TEXT, " + KEY_COLLECTIONBLIND + " TEXT, " + KEY_COLLECTIONTRAITS + " TEXT, " + KEY_COLLECTIONUA + " TEXT, " + KEY_COLLECTIONSD + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_STYLES + " (" + KEY_STYLELOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_STYLEID + " LONG UNIQUE, " + KEY_STYLEDESCRIPTION + " TEXT, " + KEY_STYLESTRENGTH + " TEXT, " + KEY_STYLECREATED + " TEXT, " + KEY_STYLEIMAGE + " TEXT, " + KEY_STYLELOC + " TEXT, " + KEY_STYLECAT + " TEXT, " + KEY_STYLEKEYWORDS + " TEXT, " + KEY_STYLECID + " LONG, " + KEY_STYLEFOODS + " TEXT, " + KEY_STYLENAME + " TEXT, " + KEY_STYLETYPE + " TEXT, " + KEY_STYLECONFLICT + " INTEGER, " + KEY_STYLERATING + " INTEGER, " + KEY_STYLEOP + " INTEGER, " + KEY_STYLEOR + " INTEGER, " + KEY_STYLEREFINE + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_VERSIONS + " (" + KEY_VERSIONLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_VERSIONID + " LONG UNIQUE, " + KEY_VERSIONCID + " LONG);");
            db.execSQL("CREATE TABLE " + TABLE_GROUPS + " (" + KEY_GROUPLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_GROUPID + " LONG UNIQUE, " + KEY_GROUPNAME + " TEXT, " + KEY_GROUPVID + " LONG, " + KEY_GROUPOC + " INTEGER, " + KEY_GROUPORDER + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_ORDERINGS + " (" + KEY_ORDERINGLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_ORDERINGID + " LONG UNIQUE, " + KEY_ORDERINGCVTID + " LONG, " + KEY_ORDERINGGID + " LONG, " + KEY_ORDERINGDIRTY + " INTEGER, " + KEY_ORDERINGORDER + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_USERCOLLECTIONS + " (" + KEY_UCOLLECTIONLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_UCOLLECTIONID + " LONG UNIQUE, " + KEY_UCOLLECTIONRT + " TEXT, " + KEY_UCOLLECTIONVIEWER + " INTEGER, " + KEY_UCOLLECTIONPINNED + " INTEGER, " + KEY_UCOLLECTIONEDITOR + " INTEGER, " + KEY_UCOLLECTIONADMIN + " INTEGER, " + KEY_UCOLLECTIONCID + " LONG, " + KEY_UCOLLECTIONCA + " TEXT, " + KEY_UCOLLECTIONAT + " TEXT, " + KEY_UCOLLECTIONUA + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_SEARCHES + " (" + KEY_SEARCHLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_SEARCHTEXT + " TEXT UNIQUE, " +  KEY_SEARCHLAST + " TEXT, " + KEY_SEARCHCOUNT + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_CUSTOMERS + " (" + KEY_CUSTOMERLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_CUSTOMERID + " LONG UNIQUE, " + KEY_CUSTOMERMEMAIL + " TEXT, " + KEY_CUSTOMERCC + " TEXT, " + KEY_CUSTOMERORDER + " INTEGER, " + KEY_CUSTOMERMUDN + " TEXT, " + KEY_CUSTOMERMUID + " TEXT, " + KEY_CUSTOMERMUN + " TEXT, " + KEY_CUSTOMERROLE + " TEXT, " + KEY_CUSTOMERUID + " LONG, " + KEY_CUSTOMERCHID + " LONG, " + KEY_CUSTOMERHP + " INTEGER, " + KEY_CUSTOMERAVA + " TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            resetDatabase(db);
            API_Singleton.getSharedInstance().clearCache();
            Map<String, ?> allEntries = Tools_Preferabli.getKeyStore().getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (entry.getKey().startsWith("hasLoaded") || entry.getKey().startsWith("lastCalled") || entry.getKey().startsWith("collection_etags_")) {
                    Tools_Preferabli.getKeyStore().edit().remove(entry.getKey()).apply();
                }
            }
        }

        public void resetDatabase(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIANTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STYLES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERINGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERCOLLECTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCHES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
            onCreate(db);
        }
    }

    static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new Tools_Database();
            mDatabaseHelper = helper;
        }
    }

    static synchronized Tools_Database getInstance() {
        if (instance == null) {
            throw new IllegalStateException(Tools_Database.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    synchronized void openDatabase() {
        mOpenCounter++;
        if (mOpenCounter == 1 || mDatabase == null) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
    }

    synchronized void closeDatabase() {
        mOpenCounter--;
        if (mOpenCounter == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    synchronized void makeSureWeUpgradeDB() {
        while (mOpenCounter > 0) closeDatabase();
        mDatabase = null;
        openDatabase();
        closeDatabase();
    }

    synchronized void deleteDatabase() {
        clearCollectionTable();
        clearProductTable();
        clearVariantTable();
        clearStyleTable();
        clearVersionTable();
        clearGroupTable();
        clearOrderingTable();
        clearTagTable();
        clearUserCollectionTable();
        clearSearchesTable();
        clearCustomerTable();
    }

    int updateSearchesTable(Object_Search search) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_SEARCHCOUNT, search.getCount());
        cv.put(KEY_SEARCHLAST, search.getLastSearched());
        cv.put(KEY_SEARCHTEXT, search.getText());

        try {
            return (int) getOurDatabase().insertOrThrow(TABLE_SEARCHES, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                return getOurDatabase().update(TABLE_SEARCHES, cv, KEY_SEARCHTEXT + " = ?", new String[]{search.getText()});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating searches!", e);
                return 0;
            }
        } finally {
            cv.clear();
        }
    }

    private SQLiteDatabase getOurDatabase() {
        return mDatabase;
    }

    int updateProductTable(Object_Product product) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_PRODUCTID, product.getId());
        cv.put(KEY_PRODUCTNAME, product.getName());
        cv.put(KEY_PRODUCTBRAND, product.getBrand());
        cv.put(KEY_PRODUCTBRANDLAT, product.getBrandLat());
        cv.put(KEY_PRODUCTBRANDLON, product.getBrandLon());
        cv.put(KEY_PRODUCTCA, product.getCreatedAt());
        cv.put(KEY_PRODUCTGRAPE, product.getGrape());
        cv.put(KEY_PRODUCTUA, product.getUpdatedAt());
        cv.put(KEY_PRODUCTNV, product.isNonVariant());
        cv.put(KEY_PRODUCTDECANT, product.isDecant());
        cv.put(KEY_PRODUCTIMAGE, Tools_Preferabli.getGson().toJson(product.getPrimaryImage()));
        cv.put(KEY_PRODUCTBACKIMAGE, Tools_Preferabli.getGson().toJson(product.getBackImage()));
        cv.put(KEY_PRODUCTREGION, product.getRegion());
        cv.put(KEY_PRODUCTTYPE, product.getProductType().getName());
        cv.put(KEY_PRODUCTCAT, product.getProductCategory().getName());
        cv.put(KEY_PRODUCTSC, product.getSubcategory());
        cv.put(KEY_PRODUCTHASH, product.getHash());

        if (product.getVariants() != null) {
            // Copying array of variants here to prevent concurrent modification crash.
            ArrayList<Object_Variant> variantsCopy = new ArrayList<>(product.getVariants());
            for (Object_Variant variant : variantsCopy)
                updateVariantTable(product.getId(), variant);
        }

        try {
            return (int) getOurDatabase().insertOrThrow(TABLE_PRODUCTS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                return getOurDatabase().update(TABLE_PRODUCTS, cv, KEY_PRODUCTID + " = ?", new String[]{Long.toString(product.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Wine!", e);
                return 0;
            }
        } finally {
            cv.clear();
        }
    }

    void clearProductTable() {
        getOurDatabase().delete(TABLE_PRODUCTS, null, null);
    }

    void updateVariantTable(long product_id, Object_Variant variant) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_VARIANTCA, variant.getCreatedAt());
        cv.put(KEY_VARIANTID, variant.getId());
        cv.put(KEY_VARIANTIMAGE, Tools_Preferabli.getGson().toJson(variant.getPrimaryImage()));
        cv.put(KEY_VARIANTPRICE, variant.getPrice());
        cv.put(KEY_VARIANTUA, variant.getUpdatedAt());
        cv.put(KEY_VARIANTPRODUCTID, product_id);
        cv.put(KEY_VARIANTYEAR, variant.getYear());
        cv.put(KEY_VARIANTFRESH, variant.isFresh());
        cv.put(KEY_VARIANTRECOMMENDABLE, variant.isRecommendable());
        cv.put(KEY_VARIANTDOLLARSIGNS, variant.getNumDollarSigns());

        if (variant.getTags() != null) for (Object_Tag tag : variant.getTags())
            updateTagTable(tag);

        try {
            getOurDatabase().insertOrThrow(TABLE_VARIANTS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_VARIANTS, cv, KEY_VARIANTID + " = ?", new String[]{Long.toString(variant.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Variant!", e);
            }
        } finally {
            cv.clear();
        }
    }

    void updateCustomerTable(long channelId, Object_Customer customer) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_CUSTOMERAVA, customer.getAvatarUrl());
        cv.put(KEY_CUSTOMERID, customer.getId());
        cv.put(KEY_CUSTOMERMEMAIL, customer.getEmail());
        cv.put(KEY_CUSTOMERMUID, customer.getMerchantUserId());
        cv.put(KEY_CUSTOMERMUN, customer.getMerchantUserName());
        cv.put(KEY_CUSTOMERROLE, customer.getRole());
        cv.put(KEY_CUSTOMERCHID, channelId);
        cv.put(KEY_CUSTOMERHP, customer.hasProfile());
        cv.put(KEY_CUSTOMERMUDN, customer.getMerchantUserDisplayName());
        cv.put(KEY_CUSTOMERCC, customer.getClaimCode());

        try {
            getOurDatabase().insertOrThrow(TABLE_CUSTOMERS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_CUSTOMERS, cv, KEY_CUSTOMERID + " = ?", new String[]{Long.toString(customer.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Customer!", e);
            }
        }
    }

    public void clearCustomerTable() {
        getOurDatabase().delete(TABLE_CUSTOMERS, null, null);
    }

    public void clearVariantTable() {
        getOurDatabase().delete(TABLE_VARIANTS, null, null);
    }

    public void clearVersionTable() {
        getOurDatabase().delete(TABLE_VERSIONS, null, null);
    }

    public void clearSearchesTable() {
        getOurDatabase().delete(TABLE_SEARCHES, null, null);
    }

    public void clearGroupTable() {
        getOurDatabase().delete(TABLE_GROUPS, null, null);
    }

    public void clearOrderingTable() {
        getOurDatabase().delete(TABLE_ORDERINGS, null, null);
    }

    public void clearStyleTable() {
        getOurDatabase().delete(TABLE_STYLES, null, null);
    }

    public void updateTagTable(Object_Tag tag) {
        updateTagTable(null, tag);
    }

    void updateTagTable(Object_Collection.Object_CollectionOrder ordering, Object_Tag tag) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TAGCA, tag.getCreatedAt());
        cv.put(KEY_TAGGEDINCID, tag.getTaggedInCollectionId());
        cv.put(KEY_TAGGEDINCVID, tag.getTaggedInCollectionVersionId());
        cv.put(KEY_TAGID, tag.getId());
        cv.put(KEY_TAGPRODUCTID, tag.getProductId());
        cv.put(KEY_TAGTYPE, tag.getType());
        cv.put(KEY_TAGUA, tag.getUpdatedAt());
        cv.put(KEY_TAGVARIANTID, tag.getVariantId());
        cv.put(KEY_TAGCOLLECTIONID, tag.getCollectionId());
        cv.put(KEY_TAGCHANNELID, tag.getChannelId());
        cv.put(KEY_TAGCOMMENT, tag.getComment());
        cv.put(KEY_TAGLOCATION, tag.getLocation());
        cv.put(KEY_TAGUSERID, tag.getUserId());
        cv.put(KEY_TAGVALUE, tag.getValue());
        cv.put(KEY_TAGBADGE, tag.getBadge());
        cv.put(KEY_TAGCHNAME, tag.getTaggedInChannelName());
        cv.put(KEY_TAGFML, tag.getFormatMl());
        cv.put(KEY_TAGQUANTITY, tag.getQuantity());
        cv.put(KEY_TAGPRICE, tag.getPrice());
        cv.put(KEY_TAGBIN, tag.getBin());

        if (ordering != null) {
            cv.put(KEY_TAGORDERINGID, ordering.getId());
        }
        if (tag.getOrdering() != null) {
            cv.put(KEY_TAGORDERINGID, tag.getOrdering().getId());
        }

        try {
            getOurDatabase().insertOrThrow(TABLE_TAGS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_TAGS, cv, KEY_TAGID + " = ?", new String[]{Long.toString(tag.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Tag!", e);
            }
        } finally {
            cv.clear();
        }
    }

    void deleteTag(long tagId) {
        getOurDatabase().delete(TABLE_TAGS, KEY_TAGID + " = ?", new String[]{Long.toString(tagId)});
    }

    void clearTagTable() {
        getOurDatabase().delete(TABLE_TAGS, null, null);
    }

    void clearUserCollectionTable() {
        getOurDatabase().delete(TABLE_USERCOLLECTIONS, null, null);
    }

    void clearTagTable(long collectionId, boolean keepDirtyTags) {
        if (keepDirtyTags)
            getOurDatabase().delete(TABLE_TAGS, KEY_TAGCOLLECTIONID + " = ? AND " + KEY_TAGDIRTY + " = ?", new String[]{Long.toString(collectionId), "0"});
        else
            getOurDatabase().delete(TABLE_TAGS, KEY_TAGCOLLECTIONID + " = ?", new String[]{Long.toString(collectionId)});
    }

    void updateUserCollectionTable(Object_UserCollection userCollection) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_UCOLLECTIONID, userCollection.getId());
        cv.put(KEY_UCOLLECTIONADMIN, userCollection.isAdmin());
        cv.put(KEY_UCOLLECTIONAT, userCollection.getArchivedAt());
        cv.put(KEY_UCOLLECTIONCA, userCollection.getCreatedAt());
        cv.put(KEY_UCOLLECTIONCID, userCollection.getCollectionId());
        cv.put(KEY_UCOLLECTIONEDITOR, userCollection.isEditor());
        cv.put(KEY_UCOLLECTIONPINNED, userCollection.isPinned());
        cv.put(KEY_UCOLLECTIONRT, userCollection.getRelationshipType());
        cv.put(KEY_UCOLLECTIONUA, userCollection.getUpdatedAt());
        cv.put(KEY_UCOLLECTIONVIEWER, userCollection.isViewer());

        try {
            getOurDatabase().insertOrThrow(TABLE_USERCOLLECTIONS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_USERCOLLECTIONS, cv, KEY_UCOLLECTIONID + " = ?", new String[]{Long.toString(userCollection.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Collection!", e);
            }
        }
    }

    void updateCollectionTable(Object_Collection collection) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_COLLECTIONCA, collection.getCreatedAt());
        cv.put(KEY_COLLECTIONCID, collection.getChannelId());
        cv.put(KEY_COLLECTIONSCID, collection.getSortChannelId());
        cv.put(KEY_COLLECTIONCODE, collection.getCode());
        cv.put(KEY_COLLECTIONCOMMENT, collection.getComment());
        cv.put(KEY_COLLECTIONDESC, collection.getDescription());
        cv.put(KEY_COLLECTIONED, collection.getEndDate());
        cv.put(KEY_COLLECTIONID, collection.getId());
        cv.put(KEY_COLLECTIONIMAGE, Tools_Preferabli.getGson().toJson(collection.getPrimaryImage()));
        cv.put(KEY_COLLECTIONNAME, collection.getName());
        cv.put(KEY_COLLECTIONPUBLISHED, collection.isPublished());
        cv.put(KEY_COLLECTIONSD, collection.getStartDate());
        cv.put(KEY_COLLECTIONTRAITS, Tools_Preferabli.getGson().toJson(collection.getTraits()));
        cv.put(KEY_COLLECTIONUA, collection.getUpdatedAt());
        cv.put(KEY_COLLECTIONVENUE, Tools_Preferabli.getGson().toJson(collection.getVenue()));
        cv.put(KEY_COLLECTIONCHANNELNAME, collection.getSortChannelName());
        cv.put(KEY_COLLECTIONBLIND, collection.isBlind());
        cv.put(KEY_COLLECTIONBADGE, collection.getBadgeMethod());
        cv.put(KEY_COLLECTIONCURRENCY, collection.getCurrency());
        cv.put(KEY_COLLECTIONDB, collection.isDisplayBin());
        cv.put(KEY_COLLECTIONDP, collection.isDisplayPrice());
        cv.put(KEY_COLLECTIONDQ, collection.isDisplayQuantity());
        cv.put(KEY_COLLECTIONDT, collection.isDisplayTime());
        cv.put(KEY_COLLECTIONDGH, collection.isDisplayGroupHeadings());
        cv.put(KEY_COLLECTIONTIMEZONE, collection.getTimezone());
        cv.put(KEY_COLLECTIONHPO, collection.hasPredictOrder());
        cv.put(KEY_COLLECTIONWC, collection.getProductCount());
        cv.put(KEY_COLLECTIONBROWSE, collection.isBrowsable());
        cv.put(KEY_COLLECTIONARCHIVE, collection.isArchived());

        if (collection.getVersions() != null) {
            for (Object_Collection.Object_CollectionVersion version : collection.getVersions()) {
                updateVersionTable(collection, version);
            }
        }

        try {
            getOurDatabase().insertOrThrow(TABLE_COLLECTIONS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_COLLECTIONS, cv, KEY_COLLECTIONID + " = ?", new String[]{Long.toString(collection.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Collection!", e);
            }
        }
    }

    void updateVersionTable(Object_Collection collection, Object_Collection.Object_CollectionVersion version) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_VERSIONID, version.getId());
        cv.put(KEY_VERSIONCID, collection.getId());

        if (version.getGroups() != null) {
            for (Object_Collection.Object_CollectionGroup group : version.getGroups()) {
                updateGroupTable(version, group);
            }
        }

        try {
            getOurDatabase().insertOrThrow(TABLE_VERSIONS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_VERSIONS, cv, KEY_VERSIONID + " = ?", new String[]{Long.toString(version.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Version!", e);
            }
        }
    }

    void updateGroupTable(Object_Collection.Object_CollectionVersion version, Object_Collection.Object_CollectionGroup group) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_GROUPID, group.getId());
        cv.put(KEY_GROUPNAME, group.getName());
        cv.put(KEY_GROUPOC, group.getOrderingsCount());
        cv.put(KEY_GROUPVID, version.getId());
        cv.put(KEY_GROUPORDER, group.getOrder());

        if (group.getOrderings() != null) {
            for (Object_Collection.Object_CollectionOrder ordering : group.getOrderings()) {
                updateOrderingTable(group.getId(), ordering);
            }
        }

        try {
            getOurDatabase().insertOrThrow(TABLE_GROUPS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_GROUPS, cv, KEY_GROUPID + " = ?", new String[]{Long.toString(group.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Version!", e);
            }
        }
    }

    void updateTagTable(long customerId, Object_Collection.Object_CollectionOrder ordering, Object_Tag tag) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TAGCA, tag.getCreatedAt());
        cv.put(KEY_TAGGEDINCID, tag.getTaggedInCollectionId());
        cv.put(KEY_TAGGEDINCVID, tag.getTaggedInCollectionVersionId());
        cv.put(KEY_TAGID, tag.getId());
        cv.put(KEY_TAGPRODUCTID, tag.getProductId());
        cv.put(KEY_TAGCUSTOMERID, customerId);
        cv.put(KEY_TAGTYPE, tag.getType());
        cv.put(KEY_TAGUA, tag.getUpdatedAt());
        cv.put(KEY_TAGVARIANTID, tag.getVariantId());
        cv.put(KEY_TAGCOLLECTIONID, tag.getCollectionId());
        cv.put(KEY_TAGCOMMENT, tag.getComment());
        cv.put(KEY_TAGLOCATION, tag.getLocation());
        cv.put(KEY_TAGUSERID, tag.getUserId());
        cv.put(KEY_TAGVALUE, tag.getValue());
        cv.put(KEY_TAGBADGE, tag.getBadge());
        if (ordering != null) {
            cv.put(KEY_TAGORDERINGID, ordering.getId());
        }


        try {
            getOurDatabase().insertOrThrow(TABLE_TAGS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_TAGS, cv, KEY_TAGID + " = ?", new String[]{Long.toString(tag.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Tag!", e);
            }
        } finally {
            cv.clear();
        }
    }

    void updateOrderingTable(long groupId, Object_Collection.Object_CollectionOrder ordering) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ORDERINGID, ordering.getId());
        cv.put(KEY_ORDERINGCVTID, ordering.getTagId());
        cv.put(KEY_ORDERINGGID, groupId);
        cv.put(KEY_ORDERINGORDER, ordering.getOrder());

        if (ordering.getTag() != null) {
            updateTagTable(ordering, ordering.getTag());
        }

        try {
            getOurDatabase().insertOrThrow(TABLE_ORDERINGS, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_ORDERINGS, cv, KEY_ORDERINGID + " = ?", new String[]{Long.toString(ordering.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Version!", e1);
            }
        }
    }

    void clearCollectionTable() {
        getOurDatabase().delete(TABLE_COLLECTIONS, null, null);
    }
    void clearGroupOrderings(Object_Collection.Object_CollectionGroup group) {
        getOurDatabase().delete(TABLE_ORDERINGS, KEY_ORDERINGGID + " = ? AND " + KEY_ORDERINGDIRTY + " = ?", new String[]{Long.toString(group.getId()), "0"});
    }

    void deleteCollection(Object_Collection collection) {
        getOurDatabase().delete(TABLE_COLLECTIONS, KEY_COLLECTIONID + " = ?", new String[]{Long.toString(collection.getId())});

        for (Object_Collection.Object_CollectionVersion version : collection.getVersions()) {
            getOurDatabase().delete(TABLE_VERSIONS, KEY_VERSIONID + " = ?", new String[]{Long.toString(version.getId())});
            getOurDatabase().delete(TABLE_GROUPS, KEY_GROUPVID + " = ?", new String[]{Long.toString(version.getId())});
        }
    }

    void updateOrderingTable(ArrayList<Object_Collection.Object_CollectionOrder> orderings) {
        getOurDatabase().beginTransaction();
        for (Object_Collection.Object_CollectionOrder ordering : orderings) {
            ContentValues cv = new ContentValues();
            cv.put(KEY_ORDERINGID, ordering.getId());
            cv.put(KEY_ORDERINGCVTID, ordering.getTagId());
            cv.put(KEY_ORDERINGGID, ordering.getGroupId());
            cv.put(KEY_ORDERINGORDER, ordering.getOrder());
            getOurDatabase().insertWithOnConflict(TABLE_ORDERINGS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        getOurDatabase().setTransactionSuccessful();
        getOurDatabase().endTransaction();
    }

    void deleteUserCollection(Object_UserCollection userCollection) {
        getOurDatabase().delete(TABLE_USERCOLLECTIONS, KEY_UCOLLECTIONID + " = ?", new String[]{Long.toString(userCollection.getId())});
    }

    void updateStyleTable(long customerId, Object_ProfileStyle style) {
        ContentValues cv = new ContentValues();
        if (style.getStyle() != null) {
            cv.put(KEY_STYLEID, style.getStyle().getId());
            cv.put(KEY_STYLEDESCRIPTION, style.getStyle().getDescription());
            cv.put(KEY_STYLENAME, style.getName());
            cv.put(KEY_STYLETYPE, style.getStyle().getProductType().getName());
            cv.put(KEY_STYLEIMAGE, style.getStyle().getImage());
            cv.put(KEY_STYLECAT, style.getStyle().getProductCategory().getName());
            cv.put(KEY_STYLELOC, Tools_Preferabli.getGson().toJson(style.getStyle().getLocations()));
        }

        cv.put(KEY_STYLECONFLICT, style.isConflict());
        cv.put(KEY_STYLEKEYWORDS, style.getKeywords());
        cv.put(KEY_STYLEOP, style.getOrderProfile());
        cv.put(KEY_STYLERATING, style.getRating());
        cv.put(KEY_STYLEOR, style.getOrderRecommend());
        cv.put(KEY_STYLEREFINE, style.isRefine());
        cv.put(KEY_STYLECREATED, style.getCreatedAt());
        cv.put(KEY_STYLECID, customerId);

        try {
            getOurDatabase().insertOrThrow(TABLE_STYLES, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_STYLES, cv, KEY_STYLEID + " = ?", new String[]{Long.toString(style.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Style!", e);
            }
        }
    }

    void updateStyleTable(Object_ProfileStyle style) {
        updateStyleTable(0, style);
    }

    ArrayList<Object_Product> getPurchasedProducts(boolean lock_to_integration) {
        Set<Object_Product> products = new HashSet<>();
        HashMap<Long, Object_Product> productsHash = new HashMap<>();
        HashMap<Long, Object_Variant> variantsHash = new HashMap<>();

        ArrayList<Object_UserCollection> userCollections = getUserCollections("purchase");
        for (Object_UserCollection userCollection : userCollections) {
            if (!lock_to_integration || (userCollection.getCollection().getChannelId() == Preferabli.CHANNEL_ID)) {
                getProductsFromCollection(userCollection.getCollectionId(), products, productsHash, variantsHash);
            }
        }

        // add all our personal cellar tags in
        ArrayList<Object_Tag> personalCellarTags = getPersonalCellarTags();
        for (Object_Product product : products) {
            for (Object_Variant variant : product.getVariants()) {
                for (Object_Tag tag : personalCellarTags) {
                    if (variant.getId() == tag.getVariantId()) {
                        variant.addTag(tag);
                        tag.setVariant(variant);
                    }
                }
            }
        }

        return new ArrayList<>(products);
    }

    @SuppressLint("Range")
    void getProductsFromCollection(long collectionId, Set<Object_Product> products, HashMap<Long, Object_Product> productsHash, HashMap<Long, Object_Variant> variantsHash) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " INNER JOIN " + TABLE_VARIANTS + " ON " + KEY_TAGPRODUCTID + " = " + KEY_VARIANTPRODUCTID + " INNER JOIN " + TABLE_PRODUCTS + " ON " + KEY_VARIANTPRODUCTID + " = " + KEY_PRODUCTID + " WHERE " + KEY_TAGCOLLECTIONID + " = ?", new String[]{ Long.toString(collectionId)} );
        if(c instanceof SQLiteCursor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // fixed bug where cursor is not big enough for getting certain collections.
            ((SQLiteCursor) c).setWindow(new CursorWindow(null, 1024*1024*100));
        }

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Product product;
            if (productsHash.containsKey(c.getLong(c.getColumnIndex(KEY_PRODUCTID)))) {
                product = productsHash.get(c.getLong(c.getColumnIndex(KEY_PRODUCTID)));
            } else {
                product = getProductFromCursor(c);
                productsHash.put(product.getId(), product);
            }

            Object_Variant variant;
            if (variantsHash.containsKey(c.getLong(c.getColumnIndex(KEY_VARIANTID)))) {
                variant = variantsHash.get(c.getLong(c.getColumnIndex(KEY_VARIANTID)));
            } else {
                variant = getVariantFromCursor(c);
                product.addVariant(variant);
                variantsHash.put(variant.getId(), variant);
            }
            variant.setProduct(product);

            Object_Tag tag = getTagFromCursor(c);
            Object_Variant variantInQuestion = variantsHash.get(tag.getVariantId());
            if (variantInQuestion != null) {
                tag.setVariant(variantInQuestion);
                variantInQuestion.addTag(tag);
            }
            products.add(product);
        }
        c.close();
    }

    void deleteOrdering(long ordering_id) {
        getOurDatabase().delete(TABLE_ORDERINGS, KEY_ORDERINGID + " = ?", new String[]{Long.toString(ordering_id)});
    }

    Object_Variant getVariant(long variant_id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_VARIANTS + " WHERE " + KEY_VARIANTID + " = ?", new String[]{Long.toString(variant_id)});

        Object_Variant variant = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            variant = getVariantFromCursor(c);
        }
        c.close();

        return variant;
    }

    @SuppressLint("Range")
    ArrayList<Object_Product> getCustomerTags(long customer_id, String tag_type) {
        Cursor c;
        if (tag_type == null) {
            c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " INNER JOIN " + TABLE_VARIANTS + " ON " + KEY_TAGVARIANTID + " = " + KEY_VARIANTID + " INNER JOIN " + TABLE_PRODUCTS + " ON " + KEY_VARIANTPRODUCTID + " = " + KEY_PRODUCTID + " WHERE " + KEY_TAGCUSTOMERID + " = ?", new String[]{Long.toString(customer_id)});

        } else {
            c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " INNER JOIN " + TABLE_VARIANTS + " ON " + KEY_TAGVARIANTID + " = " + KEY_VARIANTID + " INNER JOIN " + TABLE_PRODUCTS + " ON " + KEY_VARIANTPRODUCTID + " = " + KEY_PRODUCTID + " WHERE " + KEY_TAGCUSTOMERID + " = ? AND " + KEY_TAGTYPE + " = ?", new String[]{Long.toString(customer_id), tag_type});
        }

        ArrayList<Object_Product> products = new ArrayList<>();
        HashMap<Long, Object_Product> productHash = new HashMap<>();
        HashMap<Long, Object_Variant> variantHash = new HashMap<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Product product;
            if (productHash.containsKey(c.getLong(c.getColumnIndex(KEY_PRODUCTID)))) {
                product = productHash.get(c.getLong(c.getColumnIndex(KEY_PRODUCTID)));
            } else {
                product = getProductFromCursor(c);
                productHash.put(product.getId(), product);
            }

            Object_Variant variant;
            if (variantHash.containsKey(c.getLong(c.getColumnIndex(KEY_VARIANTID)))) {
                variant = variantHash.get(c.getLong(c.getColumnIndex(KEY_VARIANTID)));
            } else {
                variant = getVariantFromCursor(c);
                variant.setProduct(product);
                product.addVariant(variant);
                variantHash.put(variant.getId(), variant);
            }

            Object_Tag tag = getTagFromCursor(c);
            tag.setVariant(variant);
            variant.addTag(tag);
            products.add(product);
        }
        c.close();

        ArrayList<Object_Tag> tags = getPersonalTags();
        for (Object_Tag tag : tags) {
            for (Object_Variant variant : variantHash.values()) {
                if (variant.getId() == tag.getVariantId()) {
                    variant.addTag(tag);
                    tag.setVariant(variant);
                }
            }
        }

        return products;
    }

    void deleteGroup(Object_Collection.Object_CollectionGroup group) {
        getOurDatabase().delete(TABLE_GROUPS, KEY_GROUPID + " = ?", new String[]{Long.toString(group.getId())});
    }

    @SuppressLint("Range")
    ArrayList<Object_UserCollection> getUserCollections(String relationshipType) {
        // we need associated collections since this is in the journal
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_USERCOLLECTIONS + " LEFT OUTER JOIN " + TABLE_COLLECTIONS + " ON " + KEY_UCOLLECTIONCID + " = " + KEY_COLLECTIONID, null);

        ArrayList<Object_UserCollection> rts = new ArrayList<>();
        HashMap<Long, Object_Collection> collections = new HashMap<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_UserCollection userCollection = getUserCollectionFromCursor(c);
            if (userCollection.getRelationshipType().equalsIgnoreCase(relationshipType)) {
                rts.add(userCollection);
            }
            Object_Collection collection;
            if (collections.containsKey((c.getLong(c.getColumnIndex(KEY_COLLECTIONID))))) {
                collection = collections.get(c.getLong(c.getColumnIndex(KEY_COLLECTIONID)));
            } else {
                collection = getCollectionFromCursor(c);
                collections.put(collection.getId(), collection);
            }
            userCollection.setCollection(collection);
        }

        c.close();

        return rts;
    }

    @SuppressLint("Range")
    Object_Collection getCollection(long collectionId) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_COLLECTIONS + " LEFT OUTER JOIN " + TABLE_VERSIONS + " ON " + KEY_COLLECTIONID + " = " + KEY_VERSIONCID + " LEFT OUTER JOIN " + TABLE_GROUPS + " ON " + KEY_VERSIONID + " = " + KEY_GROUPVID + " LEFT OUTER JOIN " + TABLE_ORDERINGS + " ON " + KEY_GROUPID + " = " + KEY_ORDERINGGID + " LEFT OUTER JOIN " + TABLE_TAGS + " ON " + KEY_ORDERINGID + " = " + KEY_TAGORDERINGID + " LEFT OUTER JOIN " + TABLE_VARIANTS + " ON " + KEY_TAGPRODUCTID + " = " + KEY_VARIANTPRODUCTID + " LEFT OUTER JOIN " + TABLE_PRODUCTS + " ON " + KEY_VARIANTPRODUCTID + " = " + KEY_PRODUCTID + " WHERE " + KEY_COLLECTIONID + " = ?", new String[]{Long.toString(collectionId)});


        Object_Collection collection = null;
        Object_Collection.Object_CollectionVersion version = null;
        HashMap<Long, Object_Collection.Object_CollectionGroup> groupHash = new HashMap<>();
        HashMap<Long, Object_Collection.Object_CollectionOrder> orderingHash = new HashMap<>();
        HashMap<Long, Object_Product> productHash = new HashMap<>();
        HashMap<Long, Object_Variant> variantHash = new HashMap<>();
        HashMap<Long, Object_Tag> tagHash = new HashMap<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (collection == null) {
                collection = getCollectionFromCursor(c);
            }
            if (version == null) {
                version = getVersionFromCursor(c);
                collection.addVersion(version);
            }

            Object_Collection.Object_CollectionGroup group;
            if (groupHash.containsKey(c.getLong(c.getColumnIndex(KEY_GROUPID)))) {
                group = groupHash.get(c.getLong(c.getColumnIndex(KEY_GROUPID)));
            } else {
                group = getGroupFromCursor(c);
                version.addGroup(group);
                groupHash.put(group.getId(), group);
            }

            Object_Collection.Object_CollectionOrder ordering;
            if (c.getLong(c.getColumnIndex(KEY_ORDERINGID)) == 0 || c.getLong(c.getColumnIndex(KEY_TAGID)) == 0 || c.getLong(c.getColumnIndex(KEY_VARIANTID)) == 0 || c.getLong(c.getColumnIndex(KEY_PRODUCTID)) == 0)
                continue;
            if (orderingHash.containsKey(c.getLong(c.getColumnIndex(KEY_ORDERINGID)))) {
                ordering = orderingHash.get(c.getLong(c.getColumnIndex(KEY_ORDERINGID)));
            } else {
                ordering = getOrderingFromCursor(c);
                group.addOrdering(ordering);
                orderingHash.put(ordering.getId(), ordering);
            }

            Object_Tag tag;
            if (tagHash.containsKey(c.getLong(c.getColumnIndex(KEY_TAGID)))) {
                tag = tagHash.get(c.getLong(c.getColumnIndex(KEY_TAGID)));
            } else {
                tag = getTagFromCursor(c);
                ordering.setTag(tag);
                tag.setOrdering(ordering);
                tagHash.put(tag.getId(), tag);
            }

            Object_Product product;
            if (productHash.containsKey(c.getLong(c.getColumnIndex(KEY_PRODUCTID)))) {
                product = productHash.get(c.getLong(c.getColumnIndex(KEY_PRODUCTID)));
            } else {
                product = getProductFromCursor(c);
                productHash.put(product.getId(), product);
            }

            Object_Variant variant;
            if (variantHash.containsKey(c.getLong(c.getColumnIndex(KEY_VARIANTID))))
                variant = variantHash.get(c.getLong(c.getColumnIndex(KEY_VARIANTID)));
            else {
                variant = getVariantFromCursor(c);
                variant.setProduct(product);
                product.addVariant(variant);
                variantHash.put(variant.getId(), variant);
            }

            variant.setProduct(product);
            if (variant.getId() == tag.getVariantId()) {
                variant.addTag(tag);
                tag.setVariant(variant);
            }
        }
        c.close();

        ArrayList<Object_Tag> tags = getPersonalTags();
        for (Object_Tag tag : tags) {
            for (Object_Variant variant : variantHash.values()) {
                if (tag.getTagType() == Object_Tag.Other_TagType.WISHLIST && tag.getProductId() == variant.getProductId()) {
                    variant.addTag(tag);
                    tag.setVariant(variant);
                } else if (tag.getType().equalsIgnoreCase("skipped") && tag.getProductId() == variant.getProductId()) {
                    variant.addTag(tag);
                    tag.setVariant(variant);
                } else if (variant.getId() == tag.getVariantId()) {
                    variant.addTag(tag);
                    tag.setVariant(variant);
                }
            }
        }

        if (collection != null) {
            Object_Collection.Object_CollectionGroup.sortGroups(version.getGroups());
            for (Object_Collection.Object_CollectionGroup group : version.getGroups()) {
                Object_Collection.Object_CollectionOrder.sortOrders(group.getOrderings());
            }
        }

        return collection;
    }

    @SuppressLint("Range")
    Object_Product getProductWithId(long product_id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " LEFT JOIN " + TABLE_VARIANTS + " ON " + KEY_PRODUCTID + " = " + KEY_VARIANTPRODUCTID + " LEFT JOIN " + TABLE_TAGS + " ON " + KEY_VARIANTID + " = " + KEY_TAGVARIANTID + " WHERE " + KEY_PRODUCTID + " = ?", new String[]{Long.toString(product_id)});

        Object_Product product = null;
        HashMap<Long, Object_Variant> variantHash = new HashMap<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (product == null) {
                product = getProductFromCursor(c);
            }

            Object_Variant variant;
            if (variantHash.containsKey(c.getLong(c.getColumnIndex(KEY_VARIANTID)))) {
                variant = variantHash.get(c.getLong(c.getColumnIndex(KEY_VARIANTID)));
            } else {
                variant = getVariantFromCursor(c);
                variant.setProduct(product);
                product.addVariant(variant);
                variantHash.put(variant.getId(), variant);
            }

            Object_Tag tag = getTagFromCursor(c);
            tag.setVariant(variant);
            variant.addTag(tag);
        }
        c.close();

        return product;
    }

    ArrayList<Object_Search> getSearches() {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_SEARCHES, null);

        ArrayList<Object_Search> searches = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Search search = getSearchFromCursor(c);
            searches.add(search);
        }
        c.close();

        return searches;
    }

    Object_Search getSearch(String text) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_SEARCHES + " WHERE " + KEY_SEARCHTEXT + " = ?", new String[]{text});

        Object_Search search = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            search = getSearchFromCursor(c);
        }
        c.close();

        return search;
    }

    Object_Collection.Object_CollectionGroup getGroup(long group_id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_GROUPS + " LEFT OUTER JOIN " + TABLE_ORDERINGS + " ON " + KEY_GROUPID + " = " + KEY_ORDERINGGID + " WHERE " + KEY_GROUPID + " = ?", new String[]{Long.toString(group_id)});

        Object_Collection.Object_CollectionGroup group = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (group == null) {
                group = getGroupFromCursor(c);
            }
            Object_Collection.Object_CollectionOrder ordering = getOrderingFromCursor(c);
            group.addOrdering(ordering);

        }
        c.close();

        return group;
    }

    Object_Tag getTag(long tag_id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " WHERE " + KEY_TAGID + " = ?", new String[]{Long.toString(tag_id)});

        Object_Tag tag = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            tag = getTagFromCursor(c);

        }
        c.close();

        return tag;
    }

    ArrayList<Object_Tag> getPersonalTags() {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " WHERE " + KEY_TAGCOLLECTIONID + " = ? OR " + KEY_TAGCOLLECTIONID + "= ? OR " + KEY_TAGCOLLECTIONID + "= ?", new String[]{Long.toString(Tools_Preferabli.getKeyStore().getLong("ratings_id", 0)), Long.toString(Tools_Preferabli.getKeyStore().getLong("wishlist_id", 0))});

        ArrayList<Object_Tag> tags = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Tag tag = getTagFromCursor(c);
            tags.add(tag);
        }
        c.close();

        return tags;
    }

    ArrayList<Object_Tag> getPersonalCellarTags() {
        ArrayList<Object_UserCollection> userCollections = getUserCollections("mycellar");
        ArrayList<Object_Tag> tags = new ArrayList<>();

        for (Object_UserCollection userCollection : userCollections) {
            Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " WHERE " + KEY_TAGCOLLECTIONID + " = ?", new String[]{Long.toString(userCollection.getCollectionId())});
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                Object_Tag tag = getTagFromCursor(c);
                tags.add(tag);
            }
            c.close();
        }

        return tags;
    }

    Object_ProfileStyle getStyleById(long id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_STYLES + " WHERE " + KEY_STYLEID + " = ?", new String[]{Long.toString(id)});

        Object_ProfileStyle style = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            style = getStyleFromCursor(c);
        }
        c.close();

        return style;
    }

    ArrayList<Object_ProfileStyle> getStyles() {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_STYLES, null);

        ArrayList<Object_ProfileStyle> styles = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_ProfileStyle style = getStyleFromCursor(c);
            styles.add(style);
        }
        c.close();

        return styles;
    }

    Object_Customer getCustomer(long customerId) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_CUSTOMERS + " WHERE " + KEY_CUSTOMERID + " = ?", new String[]{Long.toString(customerId)});

        Object_Customer customer = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            customer = getCustomerFromCursor(c);
        }
        c.close();

        return customer;
    }

    private Object_Customer getCustomerFromCursor(Cursor c) {
        int iAV = c.getColumnIndex(KEY_CUSTOMERAVA);
        int iId = c.getColumnIndex(KEY_CUSTOMERID);
        int iEM = c.getColumnIndex(KEY_CUSTOMERMEMAIL);
        int iMID = c.getColumnIndex(KEY_CUSTOMERMUID);
        int iMUN = c.getColumnIndex(KEY_CUSTOMERMUN);
        int iROL = c.getColumnIndex(KEY_CUSTOMERROLE);
        int iUID = c.getColumnIndex(KEY_CUSTOMERUID);
        int iHP = c.getColumnIndex(KEY_CUSTOMERHP);
        int iMUDN = c.getColumnIndex(KEY_CUSTOMERMUDN);
        int iCC = c.getColumnIndex(KEY_CUSTOMERCC);
        int iCO = c.getColumnIndex(KEY_CUSTOMERORDER);

        return new Object_Customer(c.getLong(iId), c.getString(iAV), c.getString(iEM), c.getString(iMID), c.getString(iMUN), c.getString(iROL), c.getInt(iHP) == 1, c.getString(iMUDN), c.getString(iCC), c.getInt(iCO));
    }

    private Object_ProfileStyle getStyleFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_STYLEID);
        int iCon = c.getColumnIndex(KEY_STYLECONFLICT);
        int iDes = c.getColumnIndex(KEY_STYLEDESCRIPTION);
        int iKey = c.getColumnIndex(KEY_STYLEKEYWORDS);
        int iName = c.getColumnIndex(KEY_STYLENAME);
        int iOP = c.getColumnIndex(KEY_STYLEOP);
        int iOR = c.getColumnIndex(KEY_STYLEOR);
        int iRefine = c.getColumnIndex(KEY_STYLEREFINE);
        int iType = c.getColumnIndex(KEY_STYLETYPE);
        int iRating = c.getColumnIndex(KEY_STYLERATING);
        int iFoods = c.getColumnIndex(KEY_STYLEFOODS);
        int iImage = c.getColumnIndex(KEY_STYLEIMAGE);
        int iCat = c.getColumnIndex(KEY_STYLECAT);
        int iLoc = c.getColumnIndex(KEY_STYLELOC);
        int iCr = c.getColumnIndex(KEY_STYLECREATED);

        return new Object_ProfileStyle(c.getLong(iId), c.getInt(iRefine) == 1, c.getInt(iCon) == 1, c.getInt(iOR), c.getInt(iOP), c.getString(iKey), c.getString(iName), c.getString(iDes), c.getString(iType), c.getInt(iRating), c.getString(iFoods), c.getString(iImage), c.getString(iCat), c.getString(iLoc), c.getString(iCr));
    }

    private Object_Product getProductFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_PRODUCTID);
        int iName = c.getColumnIndex(KEY_PRODUCTNAME);
        int iBrand = c.getColumnIndex(KEY_PRODUCTBRAND);
        int iBrandLat = c.getColumnIndex(KEY_PRODUCTBRANDLAT);
        int iBrandLon = c.getColumnIndex(KEY_PRODUCTBRANDLON);
        int iCA = c.getColumnIndex(KEY_PRODUCTCA);
        int iGrape = c.getColumnIndex(KEY_PRODUCTGRAPE);
        int iUA = c.getColumnIndex(KEY_PRODUCTUA);
        int iNV = c.getColumnIndex(KEY_PRODUCTNV);
        int iDecant = c.getColumnIndex(KEY_PRODUCTDECANT);
        int iRegion = c.getColumnIndex(KEY_PRODUCTREGION);
        int iType = c.getColumnIndex(KEY_PRODUCTTYPE);
        int iImage = c.getColumnIndex(KEY_PRODUCTIMAGE);
        int iBackImage = c.getColumnIndex(KEY_PRODUCTBACKIMAGE);
        int iLocal = c.getColumnIndex(KEY_PRODUCTLOCALID);
        int iDirty = c.getColumnIndex(KEY_PRODUCTDIRTY);
        int iCat = c.getColumnIndex(KEY_PRODUCTCAT);
        int iRSL = c.getColumnIndex(KEY_PRODUCTRSL);
        int iSC = c.getColumnIndex(KEY_PRODUCTSC);
        int iHASH = c.getColumnIndex(KEY_PRODUCTHASH);

        return new Object_Product(c.getLong(iId), c.getString(iName), c.getString(iGrape), c.getInt(iDecant) == 1, c.getInt(iNV) == 1, c.getString(iType), c.getString(iImage), c.getString(iBackImage), c.getString(iBrand), c.getString(iRegion), c.getString(iCA), c.getString(iUA), c.getInt(iDirty) == 1, c.getDouble(iBrandLat), c.getDouble(iBrandLon), c.getString(iCat), c.getString(iRSL), c.getString(iSC), c.getString(iHASH));
    }

    private Object_Variant getVariantFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_VARIANTID);
        int iCA = c.getColumnIndex(KEY_VARIANTCA);
        int iDS = c.getColumnIndex(KEY_VARIANTDOLLARSIGNS);
        int iFresh = c.getColumnIndex(KEY_VARIANTFRESH);
        int iImage = c.getColumnIndex(KEY_VARIANTIMAGE);
        int iPrice = c.getColumnIndex(KEY_VARIANTPRICE);
        int iUA = c.getColumnIndex(KEY_VARIANTUA);
        int iRec = c.getColumnIndex(KEY_VARIANTRECOMMENDABLE);
        int iYear = c.getColumnIndex(KEY_VARIANTYEAR);
        int iLocal = c.getColumnIndex(KEY_VARIANTLOCALID);
        int iVWID = c.getColumnIndex(KEY_VARIANTPRODUCTID);
        return new Object_Variant(c.getLong(iId), c.getInt(iYear), c.getInt(iFresh) == 1, c.getInt(iRec) == 1, c.getDouble(iPrice), c.getInt(iDS), c.getString(iImage), c.getString(iCA), c.getString(iUA), c.getLong(iVWID));
    }

    private Object_Tag getTagFromCursor(Cursor c) {
        int iCA = c.getColumnIndex(KEY_TAGCA);
        int iTCID = c.getColumnIndex(KEY_TAGCOLLECTIONID);
        int iTCHID = c.getColumnIndex(KEY_TAGCHANNELID);
        int iComment = c.getColumnIndex(KEY_TAGCOMMENT);
        int iTICID = c.getColumnIndex(KEY_TAGGEDINCID);
        int iTICVID = c.getColumnIndex(KEY_TAGGEDINCVID);
        int iId = c.getColumnIndex(KEY_TAGID);
        int iLoc = c.getColumnIndex(KEY_TAGLOCATION);
        int iShare = c.getColumnIndex(KEY_TAGSHARING);
        int iType = c.getColumnIndex(KEY_TAGTYPE);
        int iUA = c.getColumnIndex(KEY_TAGUA);
        int iUID = c.getColumnIndex(KEY_TAGUSERID);
        int iValue = c.getColumnIndex(KEY_TAGVALUE);
        int iVID = c.getColumnIndex(KEY_TAGVARIANTID);
        int iDIRTY = c.getColumnIndex(KEY_TAGDIRTY);
        int iLocal = c.getColumnIndex(KEY_TAGLOCALID);
        int iBadge = c.getColumnIndex(KEY_TAGBADGE);
        int iParams = c.getColumnIndex(KEY_TAGPARAMS);
        int iWID = c.getColumnIndex(KEY_TAGPRODUCTID);
        int iCHN = c.getColumnIndex(KEY_TAGCHNAME);
        int iTP = c.getColumnIndex(KEY_TAGPRICE);
        int iTQ = c.getColumnIndex(KEY_TAGQUANTITY);
        int iTFML = c.getColumnIndex(KEY_TAGFML);
        int iBin = c.getColumnIndex(KEY_TAGBIN);

        return new Object_Tag(c.getLong(iId), c.getLong(iUID), c.getLong(iTCHID), c.getLong(iVID), c.getLong(iWID), c.getLong(iTCID), c.getLong(iTICID), c.getLong(iTICVID), c.getString(iType), c.getString(iValue), c.getString(iComment), c.getString(iLoc), c.getString(iCA), c.getString(iUA), c.getString(iShare), c.getInt(iDIRTY) == 1, c.getString(iBadge), c.getString(iParams), c.getString(iCHN), c.getInt(iTFML), c.getDouble(iTP), c.getInt(iTQ), c.getString(iBin));
    }

    private Object_Collection getCollectionFromCursor(Cursor c) {
        int iCA = c.getColumnIndex(KEY_COLLECTIONCA);
        int iCID = c.getColumnIndex(KEY_COLLECTIONCID);
        int iCSID = c.getColumnIndex(KEY_COLLECTIONSCID);
        int iCode = c.getColumnIndex(KEY_COLLECTIONCODE);
        int iComment = c.getColumnIndex(KEY_COLLECTIONCOMMENT);
        int iDesc = c.getColumnIndex(KEY_COLLECTIONDESC);
        int iDN = c.getColumnIndex(KEY_COLLECTIONDN);
        int iED = c.getColumnIndex(KEY_COLLECTIONED);
        int iID = c.getColumnIndex(KEY_COLLECTIONID);
        int iImage = c.getColumnIndex(KEY_COLLECTIONIMAGE);
        int iName = c.getColumnIndex(KEY_COLLECTIONNAME);
        int iOrder = c.getColumnIndex(KEY_COLLECTIONORDER);
        int iPub = c.getColumnIndex(KEY_COLLECTIONPUBLISHED);
        int iSD = c.getColumnIndex(KEY_COLLECTIONSD);
        int iWILI = c.getColumnIndex(KEY_COLLECTIONAUTOWILI);
        int iTraits = c.getColumnIndex(KEY_COLLECTIONTRAITS);
        int iUA = c.getColumnIndex(KEY_COLLECTIONUA);
        int iVenue = c.getColumnIndex(KEY_COLLECTIONVENUE);
        int iLocal = c.getColumnIndex(KEY_COLLECTIONLOCALID);
        int iSCN = c.getColumnIndex(KEY_COLLECTIONCHANNELNAME);
        int iBadge = c.getColumnIndex(KEY_COLLECTIONBADGE);
        int iBlind = c.getColumnIndex(KEY_COLLECTIONBLIND);
        int iTime = c.getColumnIndex(KEY_COLLECTIONTIMEZONE);
        int iCurr = c.getColumnIndex(KEY_COLLECTIONCURRENCY);
        int iDP = c.getColumnIndex(KEY_COLLECTIONDP);
        int iDQ = c.getColumnIndex(KEY_COLLECTIONDQ);
        int iDB = c.getColumnIndex(KEY_COLLECTIONDB);
        int iDT = c.getColumnIndex(KEY_COLLECTIONDT);
        int iDGH = c.getColumnIndex(KEY_COLLECTIONDGH);
        int iHPO = c.getColumnIndex(KEY_COLLECTIONHPO);
        int iWC = c.getColumnIndex(KEY_COLLECTIONWC);
        int iPIN = c.getColumnIndex(KEY_COLLECTIONPINNED);
        int iBro = c.getColumnIndex(KEY_COLLECTIONBROWSE);
        int iArc = c.getColumnIndex(KEY_COLLECTIONARCHIVE);

        return new Object_Collection(c.getLong(iID), c.getLong(iCID), c.getLong(iCSID), c.getString(iName), c.getString(iDN), c.getString(iDesc), c.getString(iCode), c.getInt(iPub) == 1, c.getString(iSD), c.getString(iED), c.getString(iComment), c.getString(iCA), c.getString(iUA), c.getString(iTraits), c.getString(iImage), c.getString(iVenue), c.getInt(iOrder), c.getInt(iWILI) == 1, c.getString(iSCN), c.getString(iBadge), c.getString(iCurr), c.getString(iTime), c.getInt(iDT) == 1, c.getInt(iDP) == 1, c.getInt(iDQ) == 1, c.getInt(iDB) == 1, c.getInt(iBlind) == 1, c.getInt(iDGH) == 1, c.getInt(iHPO) == 1, c.getInt(iWC), c.getInt(iPIN) == 1, c.getInt(iBro) == 1, c.getInt(iArc) == 1);
    }

    private Object_UserCollection getUserCollectionFromCursor(Cursor c) {
        int iAdmin = c.getColumnIndex(KEY_UCOLLECTIONADMIN);
        int iAT = c.getColumnIndex(KEY_UCOLLECTIONAT);
        int iCA = c.getColumnIndex(KEY_UCOLLECTIONCA);
        int iCID = c.getColumnIndex(KEY_UCOLLECTIONCID);
        int iEdit = c.getColumnIndex(KEY_UCOLLECTIONEDITOR);
        int iID = c.getColumnIndex(KEY_UCOLLECTIONID);
        int iPin = c.getColumnIndex(KEY_UCOLLECTIONPINNED);
        int iRT = c.getColumnIndex(KEY_UCOLLECTIONRT);
        int iUA = c.getColumnIndex(KEY_UCOLLECTIONUA);
        int iViewer = c.getColumnIndex(KEY_UCOLLECTIONVIEWER);

        return new Object_UserCollection(c.getLong(iID), c.getString(iCA), c.getString(iUA), c.getString(iAT), c.getLong(iCID), c.getInt(iAdmin) == 1, c.getInt(iEdit) == 1, c.getInt(iPin) == 1, c.getInt(iViewer) == 1, c.getString(iRT));
    }

    private Object_Collection.Object_CollectionVersion getVersionFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_VERSIONID);
        return new Object_Collection.Object_CollectionVersion(c.getLong(iId));
    }

    private Object_Collection.Object_CollectionGroup getGroupFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_GROUPID);
        int iName = c.getColumnIndex(KEY_GROUPNAME);
        int iOC = c.getColumnIndex(KEY_GROUPOC);
        int iOrder = c.getColumnIndex(KEY_GROUPORDER);
        int iVID = c.getColumnIndex(KEY_GROUPVID);
        return new Object_Collection.Object_CollectionGroup(c.getLong(iId), c.getString(iName), c.getInt(iOC), c.getInt(iOrder), c.getLong(iVID));
    }

    private Object_Collection.Object_CollectionOrder getOrderingFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_ORDERINGID);
        int iCVTGID = c.getColumnIndex(KEY_ORDERINGCVTID);
        int iOrder = c.getColumnIndex(KEY_ORDERINGORDER);
        int iOrderDirty = c.getColumnIndex(KEY_ORDERINGDIRTY);
        int iGroupId = c.getColumnIndex(KEY_ORDERINGGID);
        return new Object_Collection.Object_CollectionOrder(c.getLong(iId), c.getLong(iCVTGID), c.getInt(iOrder), c.getLong(iGroupId));
    }

    private Object_Search getSearchFromCursor(Cursor c) {
        int iSC = c.getColumnIndex(KEY_SEARCHCOUNT);
        int iSL = c.getColumnIndex(KEY_SEARCHLAST);
        int iST = c.getColumnIndex(KEY_SEARCHTEXT);

        return new Object_Search(c.getInt(iSC), c.getString(iSL), c.getString(iST));
    }
}