//
//  Tools_Database.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/30/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

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

public class Tools_Database {

    // constants for searches
    public static final String KEY_SEARCHLOCALID = "SearchLocalId";
    public static final String KEY_SEARCHCOUNT = "SearchCount";
    public static final String KEY_SEARCHLAST = "SearchLast";
    public static final String KEY_SEARCHTEXT = "SearchText";

    // constants for products
    public static final String KEY_WINELOCALID = "WineLocalId";
    public static final String KEY_WINEID = "WineId";
    public static final String KEY_WINEIMAGE = "WineImage";
    public static final String KEY_WINEBACKIMAGE = "WineBackImage";
    public static final String KEY_WINECA = "WineCA";
    public static final String KEY_WINEUA = "WineUA";
    public static final String KEY_WINENAME = "WineName";
    public static final String KEY_WINEGRAPE = "WineGrape";
    public static final String KEY_WINEDECANT = "WineDecant";
    public static final String KEY_WINENV = "WineNV";
    public static final String KEY_WINETYPE = "WineType";
    public static final String KEY_WINEBRAND = "WineBrand";
    public static final String KEY_WINEBRANDLAT = "WineBrandLat";
    public static final String KEY_WINEBRANDLON = "WineBrandLon";
    public static final String KEY_WINEREGION = "WineRegion";
    public static final String KEY_WINEDIRTY = "WineDirty";
    public static final String KEY_WINECAT = "WineCategory";
    public static final String KEY_WINERSL = "WineRateSourceLocation";
    public static final String KEY_WINESC = "WineSubCat";
    public static final String KEY_WINEHASH = "WineHash";

    // constants for variants
    public static final String KEY_VINTAGELOCALID = "VariantLocalId";
    public static final String KEY_VINTAGEID = "VariantId";
    public static final String KEY_VINTAGEWINEID = "VariantWineId";
    public static final String KEY_VINTAGEIMAGE = "VariantImage";
    public static final String KEY_VINTAGECA = "VariantCA";
    public static final String KEY_VINTAGEUA = "VariantUA";
    public static final String KEY_VINTAGEYEAR = "VariantYear";
    public static final String KEY_VINTAGEFRESH = "VariantFresh";
    public static final String KEY_VINTAGERECOMMENDABLE = "VariantRecommendable";
    public static final String KEY_VINTAGEPRICE = "VariantPrice";
    public static final String KEY_VINTAGEDOLLARSIGNS = "VariantDollarSigns";

    // constants for tags
    public static final String KEY_TAGLOCALID = "TagLocalId";
    public static final String KEY_TAGID = "TagId";
    public static final String KEY_TAGUSERID = "TagUserId";
    public static final String KEY_TAGVINTAGEID = "TagVariantId";
    public static final String KEY_TAGWINEID = "TagWineId";
    public static final String KEY_TAGCOLLECTIONID = "TagCollectionId";
    public static final String KEY_TAGCHANNELID = "TagChannelId";
    public static final String KEY_TAGGEDINCID = "TaggedInCID";
    public static final String KEY_TAGGEDINCVID = "TaggedInCVID";
    public static final String KEY_TAGTYPE = "TagType";
    public static final String KEY_TAGVALUE = "TagValue";
    public static final String KEY_TAGCOMMENT = "TagComment";
    public static final String KEY_TAGLOCATION = "TagLocation";
    public static final String KEY_TAGSHARING = "TagSharing";
    public static final String KEY_TAGCA = "TagCA";
    public static final String KEY_TAGUA = "TagUA";
    public static final String KEY_TAGDIRTY = "TagDirty";
    public static final String KEY_TAGBADGE = "TagBadge";
    public static final String KEY_TAGPARAMS = "TagParams";
    public static final String KEY_TAGORDERINGID = "TagOrderingId";
    public static final String KEY_TAGCHNAME = "TagChannelName";
    public static final String KEY_TAGFML = "TagFormatML";
    public static final String KEY_TAGPRICE = "TagPrice";
    public static final String KEY_TAGQUANTITY = "TagQuantity";
    public static final String KEY_TAGBIN = "TagBin";
    public static final String KEY_TAGCUSTOMERID = "TagCustId";


    // constants for collections
    public static final String KEY_COLLECTIONLOCALID = "CollectionLocalId";
    public static final String KEY_COLLECTIONID = "CollectionId";
    public static final String KEY_COLLECTIONCID = "CollectionChannelId";
    public static final String KEY_COLLECTIONSCID = "CollectionSortChannelId";
    public static final String KEY_COLLECTIONDESC = "CollectionDescription";
    public static final String KEY_COLLECTIONIMAGE = "CollectionImage";
    public static final String KEY_COLLECTIONORDER = "CollectionOrder";
    public static final String KEY_COLLECTIONVENUE = "CollectionVenue";
    public static final String KEY_COLLECTIONTRAITS = "CollectionTraits";
    public static final String KEY_COLLECTIONNAME = "CollectionName";
    public static final String KEY_COLLECTIONDN = "CollectionDisplayName";
    public static final String KEY_COLLECTIONCODE = "CollectionCodeActivity";
    public static final String KEY_COLLECTIONPUBLISHED = "CollectionPublished";
    public static final String KEY_COLLECTIONSD = "CollectionSD";
    public static final String KEY_COLLECTIONED = "CollectionED";
    public static final String KEY_COLLECTIONCOMMENT = "CollectionComment";
    public static final String KEY_COLLECTIONCA = "CollectionCA";
    public static final String KEY_COLLECTIONUA = "CollectionUA";
    public static final String KEY_COLLECTIONAUTOWILI = "CollectionAutoWili";
    public static final String KEY_COLLECTIONCHANNELNAME = "CollectionChannelName";
    public static final String KEY_COLLECTIONBLIND = "CollectionBlind";
    public static final String KEY_COLLECTIONCURRENCY = "CollectionCurrency";
    public static final String KEY_COLLECTIONTIMEZONE = "CollectionTimeZone";
    public static final String KEY_COLLECTIONBADGE = "CollectionBadge";
    public static final String KEY_COLLECTIONDQ = "CollectionDQ";
    public static final String KEY_COLLECTIONDP = "CollectionDP";
    public static final String KEY_COLLECTIONDB = "CollectionDB";
    public static final String KEY_COLLECTIONDT = "CollectionDT";
    public static final String KEY_COLLECTIONDGH = "CollectionDGH";
    public static final String KEY_COLLECTIONHPO = "CollectionHPO";
    public static final String KEY_COLLECTIONWC = "CollectionWC";
    public static final String KEY_COLLECTIONPINNED = "CollectionPinned";
    public static final String KEY_COLLECTIONBROWSE = "CollectionBrowsable";
    public static final String KEY_COLLECTIONARCHIVE = "CollectionArchive";


    // constants for versions
    public static final String KEY_VERSIONLOCALID = "VersionLocalId";
    public static final String KEY_VERSIONID = "VersionId";
    public static final String KEY_VERSIONCID = "VersionCollectionId";

    // constants for groups
    public static final String KEY_GROUPLOCALID = "GroupLocalId";
    public static final String KEY_GROUPID = "GroupId";
    public static final String KEY_GROUPVID = "GroupVersionId";
    public static final String KEY_GROUPOC = "GroupOrderingsCount";
    public static final String KEY_GROUPNAME = "GroupName";
    public static final String KEY_GROUPORDER = "GroupOrder";

    // constants for orderings
    public static final String KEY_ORDERINGLOCALID = "OrderingLocalId";
    public static final String KEY_ORDERINGID = "OrderingId";
    public static final String KEY_ORDERINGGID = "OrderingGroupId";
    public static final String KEY_ORDERINGCVTID = "OrderingCollectionVariantTagId";
    public static final String KEY_ORDERINGORDER = "OrderingOrder";
    public static final String KEY_ORDERINGDIRTY = "OrderingDirty";

    // constants for connections
    public static final String KEY_CONNECTIONLOCALID = "ConnectionLocalId";
    public static final String KEY_CONNECTIONID = "ConnectionId";
    public static final String KEY_CONNECTIONUSERID = "ConnectionUserId";
    public static final String KEY_CONNECTIONUSER = "ConnectionUser";
    public static final String KEY_CONNECTIONTYPE = "ConnectionType";

    // constants for channels
    public static final String KEY_CHANNELLOCALID = "ChannelLocalId";
    public static final String KEY_CHANNELID = "ChannelId";
    public static final String KEY_CHANNELNAME = "ChannelName";
    public static final String KEY_CHANNELORDER = "ChannelOrder";
    public static final String KEY_CHANNELIMAGE = "ChannelImage";
    public static final String KEY_CHANNELTRAITS = "ChannelTraits";
    public static final String KEY_CHANNELAVTRAITS = "ChannelAvTraits";
    public static final String KEY_CHANNELDT = "ChannelDT";
    public static final String KEY_CHANNELDC = "ChannelDC";
    public static final String KEY_CHANNELDBM = "ChannelDBM";
    public static final String KEY_CHANNELDP = "ChannelDP";
    public static final String KEY_CHANNELDQ = "ChannelDQ";
    public static final String KEY_CHANNELDB = "ChannelDB";
    public static final String KEY_CHANNELCSV = "ChannelCSV";
    public static final String KEY_CHANNELXLSX = "ChannelXLSX";
    public static final String KEY_CHANNELPDF = "ChannelPDF";
    public static final String KEY_CHANNELVENUES = "ChannelVenues";
    public static final String KEY_CHANNELMAXVENUES = "ChannelMaxVenues";
    public static final String KEY_CHANNELISPINNED = "ChannelIsPinned";
    public static final String KEY_CHANNELISVERIFIED = "ChannelIsVerified";
    public static final String KEY_CHANNELPIID = "ChannelPIID";
    public static final String KEY_CHANNELFCID = "ChannelFCID";
    public static final String KEY_CHANNELDESC = "ChannelDesc";
    public static final String KEY_CHANNELAID = "ChannelAID";

    // constants for styles
    public static final String KEY_STYLELOCALID = "StyleLocalId";
    public static final String KEY_STYLEID = "StyleId";
    public static final String KEY_STYLEOR = "StyleOR";
    public static final String KEY_STYLEOP = "StyleOP";
    public static final String KEY_STYLEREFINE = "StyleRefine";
    public static final String KEY_STYLECONFLICT = "StyleConflict";
    public static final String KEY_STYLEKEYWORDS = "StyleKeywords";
    public static final String KEY_STYLENAME = "StyleName";
    public static final String KEY_STYLETYPE = "StyleType";
    public static final String KEY_STYLEDESCRIPTION = "StyleDescription";
    public static final String KEY_STYLERATING = "StyleRating";
    public static final String KEY_STYLEFOODS = "StyleFoods";
    public static final String KEY_STYLEIMAGE = "StyleImage";
    public static final String KEY_STYLECAT = "StyleCategory";
    public static final String KEY_STYLELOC = "StyleLocations";
    public static final String KEY_STYLESTRENGTH = "StyleStrength";
    public static final String KEY_STYLECREATED = "StyleCreated";
    public static final String KEY_STYLECID = "StyleCID";

    // constants for feed messages
    public static final String KEY_FEEDLOCALID = "FeedLocalId";
    public static final String KEY_FEEDID = "FeedId";
    public static final String KEY_FEEDCA = "FeedCA";
    public static final String KEY_FEEDMT = "FeedMT";
    public static final String KEY_FEEDHIDE = "FeedHide";
    public static final String KEY_FEEDREADAT = "FeedReadAt";

    // constants for appliances
    public static final String KEY_APPLOCALID = "AppLocalId";
    public static final String KEY_APPID = "ApplianceId";
    public static final String KEY_TOPTEMP = "AppTopTemp";
    public static final String KEY_MIDTEMP = "AppMidTemp";
    public static final String KEY_BOTTOMTEMP = "AppBottomTemp";
    public static final String KEY_APPNAME = "AppName";
    public static final String KEY_APPSAB = "AppSabbath";
    public static final String KEY_APPHAID = "AppHAID";
    public static final String KEY_APPIMAGE = "AppImage";
    public static final String KEY_APPUNIT = "AppUnit";

    // constants for usercollections
    public static final String KEY_UCOLLECTIONLOCALID = "UserCollectionLocalId";
    public static final String KEY_UCOLLECTIONID = "UserCollectionId";
    public static final String KEY_UCOLLECTIONRT = "UserCollectionRelationshipType";
    public static final String KEY_UCOLLECTIONVIEWER = "UserCollectionViewer";
    public static final String KEY_UCOLLECTIONPINNED = "UserCollectionPinned";
    public static final String KEY_UCOLLECTIONEDITOR = "UserCollectionEditor";
    public static final String KEY_UCOLLECTIONADMIN = "UserCollectionAdmin";
    public static final String KEY_UCOLLECTIONCA = "UserCollectionCreatedAt";
    public static final String KEY_UCOLLECTIONUA = "UserCollectionUpdatedAt";
    public static final String KEY_UCOLLECTIONCID = "UserCollectionCollectionId";
    public static final String KEY_UCOLLECTIONAT = "UserCollectionArchivedAt";

    // constants for customers
    public static final String KEY_CUSTOMERLOCALID = "CustomerLocalId";
    public static final String KEY_CUSTOMERID = "CustomerId";
    public static final String KEY_CUSTOMERAVA = "CustomerAvatar";
    public static final String KEY_CUSTOMERMEMAIL = "CustomerMerchantEmail";
    public static final String KEY_CUSTOMERMUID = "CustomerMerchantUserId";
    public static final String KEY_CUSTOMERMUN = "CustomerMerchantUsername";
    public static final String KEY_CUSTOMERROLE = "CustomerRole";
    public static final String KEY_CUSTOMERUID = "CustomerUserId";
    public static final String KEY_CUSTOMERCHID = "CustomerChannelId";
    public static final String KEY_CUSTOMERHP = "CustomerHasProfile";
    public static final String KEY_CUSTOMERMUDN = "CustomerMUDN";
    public static final String KEY_CUSTOMERCC = "CustomerClaimCode";
    public static final String KEY_CUSTOMERORDER = "CustomerOrder";

    // database constants
    public static final String DATABASE_NAME = "WineRingDB";
    public static final String TABLE_WINES = "Products";
    public static final String TABLE_VINTAGES = "Variants";
    public static final String TABLE_TAGS = "Tags";
    public static final String TABLE_COLLECTIONS = "Collections";
    public static final String TABLE_CONNECTIONS = "Connections";
    public static final String TABLE_CHANNELS = "Channels";
    public static final String TABLE_STYLES = "Styles";
    public static final String TABLE_VERSIONS = "Versions";
    public static final String TABLE_GROUPS = "Groups";
    public static final String TABLE_ORDERINGS = "Orderings";
    public static final String TABLE_FEEDMESSAGES = "FeedMessages";
    public static final String TABLE_USERCOLLECTIONS = "UserCollections";
    public static final String TABLE_APPLIANCES = "Appliances";
    public static final String TABLE_SEARCHES = "Searches";
    public static final String TABLE_CUSTOMERS = "Customers";

    private int mOpenCounter;
    public static Tools_Database instance;
    public static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

        public SQLiteOpenHelper() {
            super(Tools_PreferabliApp.getAppContext(), DATABASE_NAME, null, BuildConfig.DBVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_WINES + " (" + KEY_WINELOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_WINEID + " LONG UNIQUE, " + KEY_WINENAME + " TEXT, " + KEY_WINECAT + " TEXT, " + KEY_WINEHASH + " TEXT, " + KEY_WINESC + " TEXT, " + KEY_WINEBACKIMAGE + " TEXT, " + KEY_WINEGRAPE + " TEXT, " + KEY_WINEDECANT + " INTEGER, " + KEY_WINENV + " INTEGER, " + KEY_WINEDIRTY + " INTEGER, " + KEY_WINETYPE + " TEXT, " + KEY_WINERSL + " TEXT, " + KEY_WINEBRANDLAT + " DOUBLE, " + KEY_WINEBRANDLON + " DOUBLE, " + KEY_WINEBRAND + " TEXT, " + KEY_WINEREGION + " TEXT, " + KEY_WINEUA + " TEXT, " + KEY_WINEIMAGE + " TEXT, " + KEY_WINECA + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_VINTAGES + " (" + KEY_VINTAGELOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_VINTAGEID + " LONG UNIQUE, " + KEY_VINTAGECA + " TEXT, " + KEY_VINTAGEDOLLARSIGNS + " INTEGER, " + KEY_VINTAGEFRESH + " INTEGER, " + KEY_VINTAGEIMAGE + " TEXT, " + KEY_VINTAGEPRICE + " DOUBLE, " + KEY_VINTAGERECOMMENDABLE + " INTEGER, " + KEY_VINTAGEUA + " TEXT, " + KEY_VINTAGEWINEID + " LONG, " + KEY_VINTAGEYEAR + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_TAGS + " (" + KEY_TAGLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_TAGID + " LONG UNIQUE, " + KEY_TAGCA + " TEXT, " + KEY_TAGCOLLECTIONID + " LONG, " + KEY_TAGCHANNELID + " LONG, " + KEY_TAGORDERINGID + " LONG, " + KEY_TAGCUSTOMERID + " LONG, " + KEY_TAGWINEID + " LONG, " + KEY_TAGCOMMENT + " TEXT, " + KEY_TAGBADGE + " TEXT, " + KEY_TAGCHNAME + " TEXT, " + KEY_TAGPARAMS + " TEXT, " + KEY_TAGFML + " INTEGER, " + KEY_TAGPRICE + " DOUBLE, " + KEY_TAGQUANTITY + " INTEGER, " + KEY_TAGBIN + " TEXT, "+ KEY_TAGGEDINCID + " LONG, " + KEY_TAGDIRTY + " INTEGER, " + KEY_TAGGEDINCVID + " LONG, " + KEY_TAGLOCATION + " TEXT, " + KEY_TAGSHARING + " TEXT, " + KEY_TAGTYPE + " TEXT, " + KEY_TAGUSERID + " LONG, " + KEY_TAGVALUE + " TEXT, " + KEY_TAGVINTAGEID + " LONG, " + KEY_TAGUA + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_COLLECTIONS + " (" + KEY_COLLECTIONLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_COLLECTIONID + " LONG UNIQUE, " + KEY_COLLECTIONCA + " TEXT, " + KEY_COLLECTIONCID + " LONG, " + KEY_COLLECTIONSCID + " LONG, " + KEY_COLLECTIONCODE + " TEXT, " + KEY_COLLECTIONCOMMENT + " TEXT, "  + KEY_COLLECTIONBROWSE + " INTEGER, " + KEY_COLLECTIONARCHIVE + " INTEGER, " + KEY_COLLECTIONCHANNELNAME + " TEXT, " + KEY_COLLECTIONDESC + " TEXT, " + KEY_COLLECTIONDN + " TEXT, " + KEY_COLLECTIONED + " TEXT, " + KEY_COLLECTIONIMAGE + " TEXT, " + KEY_COLLECTIONNAME + " TEXT, " + KEY_COLLECTIONORDER + " INTEGER, " + KEY_COLLECTIONDGH + " INTEGER, " + KEY_COLLECTIONPINNED + " INTEGER, " + KEY_COLLECTIONHPO + " INTEGER, " + KEY_COLLECTIONWC + " INTEGER, " + KEY_COLLECTIONAUTOWILI + " INTEGER, " + KEY_COLLECTIONPUBLISHED + " INTEGER, " + KEY_COLLECTIONVENUE + " TEXT, " + KEY_COLLECTIONCURRENCY + " TEXT, " + KEY_COLLECTIONTIMEZONE + " TEXT, " + KEY_COLLECTIONBADGE + " TEXT, " + KEY_COLLECTIONDB + " TEXT, " + KEY_COLLECTIONDP + " TEXT, " + KEY_COLLECTIONDQ + " TEXT, " + KEY_COLLECTIONDT + " TEXT, " + KEY_COLLECTIONBLIND + " TEXT, " + KEY_COLLECTIONTRAITS + " TEXT, " + KEY_COLLECTIONUA + " TEXT, " + KEY_COLLECTIONSD + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_CONNECTIONS + " (" + KEY_CONNECTIONLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_CONNECTIONID + " LONG UNIQUE, " + KEY_CONNECTIONTYPE + " TEXT, " + KEY_CONNECTIONUSER + " TEXT, " + KEY_CONNECTIONUSERID + " LONG);");
            db.execSQL("CREATE TABLE " + TABLE_CHANNELS + " (" + KEY_CHANNELLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_CHANNELID + " LONG UNIQUE, " + KEY_CHANNELNAME + " TEXT, " + KEY_CHANNELIMAGE + " TEXT, " + KEY_CHANNELAID + " INTEGER, " + KEY_CHANNELVENUES + " TEXT, " + KEY_CHANNELMAXVENUES + " INTEGER, " + KEY_CHANNELTRAITS + " TEXT, " + KEY_CHANNELAVTRAITS + " TEXT, " + KEY_CHANNELDESC + " TEXT, " + KEY_CHANNELDB + " INTEGER, " + KEY_CHANNELORDER + " INTEGER, " + KEY_CHANNELDBM + " TEXT, " + KEY_CHANNELPIID + " LONG, " + KEY_CHANNELFCID + " LONG, " + KEY_CHANNELDC + " TEXT, " + KEY_CHANNELDP + " INTEGER, " + KEY_CHANNELCSV + " INTEGER, " + KEY_CHANNELXLSX + " INTEGER, " + KEY_CHANNELPDF + " INTEGER, " + KEY_CHANNELISPINNED + " INTEGER, " + KEY_CHANNELISVERIFIED + " INTEGER, " + KEY_CHANNELDT + " TEXT, " + KEY_CHANNELDQ + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_STYLES + " (" + KEY_STYLELOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_STYLEID + " LONG UNIQUE, " + KEY_STYLEDESCRIPTION + " TEXT, " + KEY_STYLESTRENGTH + " TEXT, " + KEY_STYLECREATED + " TEXT, " + KEY_STYLEIMAGE + " TEXT, " + KEY_STYLELOC + " TEXT, " + KEY_STYLECAT + " TEXT, " + KEY_STYLEKEYWORDS + " TEXT, " + KEY_STYLECID + " LONG, " + KEY_STYLEFOODS + " TEXT, " + KEY_STYLENAME + " TEXT, " + KEY_STYLETYPE + " TEXT, " + KEY_STYLECONFLICT + " INTEGER, " + KEY_STYLERATING + " INTEGER, " + KEY_STYLEOP + " INTEGER, " + KEY_STYLEOR + " INTEGER, " + KEY_STYLEREFINE + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_VERSIONS + " (" + KEY_VERSIONLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_VERSIONID + " LONG UNIQUE, " + KEY_VERSIONCID + " LONG);");
            db.execSQL("CREATE TABLE " + TABLE_GROUPS + " (" + KEY_GROUPLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_GROUPID + " LONG UNIQUE, " + KEY_GROUPNAME + " TEXT, " + KEY_GROUPVID + " LONG, " + KEY_GROUPOC + " INTEGER, " + KEY_GROUPORDER + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_ORDERINGS + " (" + KEY_ORDERINGLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_ORDERINGID + " LONG UNIQUE, " + KEY_ORDERINGCVTID + " LONG, " + KEY_ORDERINGGID + " LONG, " + KEY_ORDERINGDIRTY + " INTEGER, " + KEY_ORDERINGORDER + " INTEGER);");
            db.execSQL("CREATE TABLE " + TABLE_FEEDMESSAGES + " (" + KEY_FEEDLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_FEEDID + " LONG UNIQUE, " + KEY_FEEDCA + " TEXT, " + KEY_FEEDREADAT + " TEXT, " + KEY_FEEDHIDE + " INTEGER, " + KEY_FEEDMT + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_APPLIANCES + " (" + KEY_APPLOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_APPID + " LONG UNIQUE, " + KEY_APPNAME + " TEXT, " + KEY_BOTTOMTEMP + " INTEGER, " + KEY_TOPTEMP + " INTEGER, " + KEY_MIDTEMP + " INTEGER, " + KEY_APPSAB + " INTEGER, " + KEY_APPHAID + " TEXT, " + KEY_APPUNIT + " TEXT, " + KEY_APPIMAGE + " TEXT);");
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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WINES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VINTAGES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNECTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNELS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STYLES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERINGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDMESSAGES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERCOLLECTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPLIANCES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCHES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
            onCreate(db);
        }
    }

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new Tools_Database();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized Tools_Database getInstance() {
        if (instance == null) {
            throw new IllegalStateException(Tools_Database.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized void openDatabase() {
        mOpenCounter++;
        if (mOpenCounter == 1 || mDatabase == null) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if (mOpenCounter == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    public synchronized void makeSureWeUpgradeDB() {
        while (mOpenCounter > 0) closeDatabase();
        mDatabase = null;
        openDatabase();
        closeDatabase();
    }

    public synchronized void deleteDatabase() {
        clearCollectionTable();
        clearWineTable();
        clearVariantTable();
        clearChannelTable();
        clearStyleTable();
        clearVersionTable();
        clearGroupTable();
        clearOrderingTable();
        clearTagTable();
        clearFeedMessageTable();
        clearUserCollectionTable();
        clearApplianceTable();
        clearSearchesTable();
        clearCustomerTable();
    }


    public int updateSearchesTable(Object_Search search) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_SEARCHCOUNT, search.getCount());
        cv.put(KEY_SEARCHLAST, search.getLast_searched());
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

    public SQLiteDatabase getOurDatabase() {
        return mDatabase;
    }

    public int updateProductTable(Object_Product product) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_WINEID, product.getId());
        cv.put(KEY_WINENAME, product.getName());
        cv.put(KEY_WINEBRAND, product.getBrand());
        cv.put(KEY_WINEBRANDLAT, product.getBrand_lat());
        cv.put(KEY_WINEBRANDLON, product.getBrand_lon());
        cv.put(KEY_WINECA, product.getCreatedAt());
        cv.put(KEY_WINEGRAPE, product.getGrapeForDB());
        cv.put(KEY_WINEUA, product.getUpdatedAt());
        cv.put(KEY_WINENV, product.isNonVariant());
        cv.put(KEY_WINEDIRTY, product.isDirty());
        cv.put(KEY_WINEDECANT, product.isDecant());
        cv.put(KEY_WINEIMAGE, Tools_Preferabli.getGson().toJson(product.getPrimaryImage()));
        cv.put(KEY_WINEBACKIMAGE, Tools_Preferabli.getGson().toJson(product.getBackImage()));
        cv.put(KEY_WINEREGION, product.getRegionForDB());
        cv.put(KEY_WINETYPE, product.getType());
        cv.put(KEY_WINECAT, product.getCategory());
        cv.put(KEY_WINERSL, product.getRateSourceLocation());
        cv.put(KEY_WINESC, product.getSubcategory());
        cv.put(KEY_WINEHASH, product.getHash());

        if (product.getVariants() != null) {
            // Copying array of variants here to prevent concurrent modiciation crash.
            ArrayList<Object_Variant> variantsCopy = new ArrayList<>(product.getVariants());
            for (Object_Variant variant : variantsCopy)
                updateVariantTable(product.getId(), variant);
        }

        try {
            return (int) getOurDatabase().insertOrThrow(TABLE_WINES, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                return getOurDatabase().update(TABLE_WINES, cv, KEY_WINEID + " = ?", new String[]{Long.toString(product.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Wine!", e);
                return 0;
            }
        } finally {
            cv.clear();
        }
    }

    public void clearWineTable() {
        getOurDatabase().delete(TABLE_WINES, null, null);
    }

    public void updateVariantTable(long wineId, Object_Variant variant) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_VINTAGECA, variant.getCreatedAt());
        cv.put(KEY_VINTAGEID, variant.getId());
        cv.put(KEY_VINTAGEIMAGE, Tools_Preferabli.getGson().toJson(variant.getPrimaryImage()));
        cv.put(KEY_VINTAGEPRICE, variant.getPrice());
        cv.put(KEY_VINTAGEUA, variant.getUpdatedAt());
        cv.put(KEY_VINTAGEWINEID, wineId);
        cv.put(KEY_VINTAGEYEAR, variant.getYear());
        cv.put(KEY_VINTAGEFRESH, variant.isFresh());
        cv.put(KEY_VINTAGERECOMMENDABLE, variant.isRecommendable());
        cv.put(KEY_VINTAGEDOLLARSIGNS, variant.getNumDollarSigns());

        if (variant.getTags() != null) for (Object_Tag tag : variant.getTags())
            updateTagTable(tag);

        try {
            getOurDatabase().insertOrThrow(TABLE_VINTAGES, null, cv);
        } catch (SQLiteConstraintException e) {
            try {
                getOurDatabase().update(TABLE_VINTAGES, cv, KEY_VINTAGEID + " = ?", new String[]{Long.toString(variant.getId())});
            } catch (SQLiteConstraintException e1) {
                Log.e(getClass().getSimpleName(), "Failed updating Variant!", e);
            }
        } finally {
            cv.clear();
        }
    }

    public void updateCustomerTable(long channelId, Object_Customer customer) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_CUSTOMERAVA, customer.getAvatarUrl());
        cv.put(KEY_CUSTOMERID, customer.getId());
        cv.put(KEY_CUSTOMERMEMAIL, customer.getEmail());
        cv.put(KEY_CUSTOMERMUID, customer.getMerchantUserId());
        cv.put(KEY_CUSTOMERMUN, customer.getMerchantUserName());
        cv.put(KEY_CUSTOMERROLE, customer.getRole());
        cv.put(KEY_CUSTOMERCHID, channelId);
        cv.put(KEY_CUSTOMERHP, customer.isHas_profile());
        cv.put(KEY_CUSTOMERMUDN, customer.getMerchantUserDisplayName());
        cv.put(KEY_CUSTOMERCC, customer.getClaimCode());
        cv.put(KEY_CUSTOMERORDER, customer.getOrder());

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
        getOurDatabase().delete(TABLE_VINTAGES, null, null);
    }

    public void clearChannelTable() {
        getOurDatabase().delete(TABLE_CHANNELS, null, null);
    }

    public void clearVersionTable() {
        getOurDatabase().delete(TABLE_VERSIONS, null, null);
    }

    public void clearApplianceTable() {
        getOurDatabase().delete(TABLE_APPLIANCES, null, null);
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

    public void updateTagTable(Object_Collection.Object_CollectionOrder ordering, Object_Tag tag) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TAGCA, tag.getCreatedAt());
        cv.put(KEY_TAGGEDINCID, tag.getTaggedInCollectionId());
        cv.put(KEY_TAGGEDINCVID, tag.getTaggedInCollectionVersionId());
        cv.put(KEY_TAGID, tag.getId());
        cv.put(KEY_TAGWINEID, tag.getProductId());
        cv.put(KEY_TAGTYPE, tag.getType());
        cv.put(KEY_TAGUA, tag.getUpdatedAt());
        cv.put(KEY_TAGVINTAGEID, tag.getVariantId());
        cv.put(KEY_TAGCOLLECTIONID, tag.getCollectionId());
        cv.put(KEY_TAGCHANNELID, tag.getChannelId());
        cv.put(KEY_TAGCOMMENT, tag.getComment());
        cv.put(KEY_TAGLOCATION, tag.getLocation());
        cv.put(KEY_TAGSHARING, Tools_Preferabli.getGson().toJson(tag.getSharing()));
        cv.put(KEY_TAGUSERID, tag.getUserId());
        cv.put(KEY_TAGVALUE, tag.getValue());
        cv.put(KEY_TAGDIRTY, tag.isDirty());
        cv.put(KEY_TAGBADGE, tag.getBadge());
        cv.put(KEY_TAGCHNAME, tag.getTagged_in_channel_name());
        cv.put(KEY_TAGFML, tag.getFormat_ml());
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

    public void deleteTag(long tagId) {
        getOurDatabase().delete(TABLE_TAGS, KEY_TAGID + " = ?", new String[]{Long.toString(tagId)});
    }

    public void deleteWine(Object_Product product) {
        getOurDatabase().delete(TABLE_WINES, KEY_WINEID + " = ?", new String[]{Long.toString(product.getId())});
    }

    public void clearTagTable() {
        getOurDatabase().delete(TABLE_TAGS, null, null);
    }

    public void clearUserCollectionTable() {
        getOurDatabase().delete(TABLE_USERCOLLECTIONS, null, null);
    }

    public void clearFeedMessageTable() {
        getOurDatabase().delete(TABLE_FEEDMESSAGES, null, null);
    }

    public void clearTagTable(long collectionId, boolean keepDirtyTags) {
        if (keepDirtyTags)
            getOurDatabase().delete(TABLE_TAGS, KEY_TAGCOLLECTIONID + " = ? AND " + KEY_TAGDIRTY + " = ?", new String[]{Long.toString(collectionId), "0"});
        else
            getOurDatabase().delete(TABLE_TAGS, KEY_TAGCOLLECTIONID + " = ?", new String[]{Long.toString(collectionId)});
    }

    public void clearSkippedTags() {
        getOurDatabase().delete(TABLE_TAGS, KEY_TAGTYPE + " = ?", new String[]{"skipped"});
    }

    public void updateUserCollectionTable(Object_UserCollection userCollection, boolean updateCollectionTable) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_UCOLLECTIONID, userCollection.getId());
        cv.put(KEY_UCOLLECTIONADMIN, userCollection.isIs_admin());
        cv.put(KEY_UCOLLECTIONAT, userCollection.getArchived_at());
        cv.put(KEY_UCOLLECTIONCA, userCollection.getCreated_at());
        cv.put(KEY_UCOLLECTIONCID, userCollection.getCollectionId());
        cv.put(KEY_UCOLLECTIONEDITOR, userCollection.isIs_editor());
        cv.put(KEY_UCOLLECTIONPINNED, userCollection.isIs_pinned());
        cv.put(KEY_UCOLLECTIONRT, userCollection.getRelationship_type());
        cv.put(KEY_UCOLLECTIONUA, userCollection.getUpdated_at());
        cv.put(KEY_UCOLLECTIONVIEWER, userCollection.isIs_viewer());

        if (updateCollectionTable && userCollection.getCollection() != null) {
            updateCollectionTable(userCollection.getCollection());
        }

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

    public void updateCollectionTable(Object_Collection collection) {
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

    public void updateVersionTable(Object_Collection collection, Object_Collection.Object_CollectionVersion version) {
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

    public void updateGroupTable(Object_Collection.Object_CollectionVersion version, Object_Collection.Object_CollectionGroup group) {
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

    public void updateTagTable(long customerId, Object_Collection.Object_CollectionOrder ordering, Object_Tag tag) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TAGCA, tag.getCreatedAt());
        cv.put(KEY_TAGGEDINCID, tag.getTaggedInCollectionId());
        cv.put(KEY_TAGGEDINCVID, tag.getTaggedInCollectionVersionId());
        cv.put(KEY_TAGID, tag.getId());
        cv.put(KEY_TAGWINEID, tag.getProductId());
        cv.put(KEY_TAGCUSTOMERID, customerId);
        cv.put(KEY_TAGTYPE, tag.getType());
        cv.put(KEY_TAGUA, tag.getUpdatedAt());
        cv.put(KEY_TAGVINTAGEID, tag.getVariantId());
        cv.put(KEY_TAGCOLLECTIONID, tag.getCollectionId());
        cv.put(KEY_TAGCOMMENT, tag.getComment());
        cv.put(KEY_TAGLOCATION, tag.getLocation());
        cv.put(KEY_TAGSHARING, Tools_Preferabli.getGson().toJson(tag.getSharing()));
        cv.put(KEY_TAGUSERID, tag.getUserId());
        cv.put(KEY_TAGVALUE, tag.getValue());
        cv.put(KEY_TAGDIRTY, tag.isDirty());
        cv.put(KEY_TAGBADGE, tag.getBadge());
        cv.put(KEY_TAGPARAMS, Tools_Preferabli.getGson().toJson(tag.getParameters()));
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

    public void updateOrderingTable(long groupId, Object_Collection.Object_CollectionOrder ordering) {
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

    public void clearCollectionTable() {
        getOurDatabase().delete(TABLE_COLLECTIONS, null, null);
    }

    public String argsArrayToString(ArrayList<String> args) {
        StringBuilder argsBuilder = new StringBuilder();
        argsBuilder.append("(");
        final int argsCount = args.size();
        for (int i = 0; i < argsCount; i++) {
            argsBuilder.append(args.get(i));
            if (i < argsCount - 1) {
                argsBuilder.append(",");
            }
        }
        argsBuilder.append(")");
        return argsBuilder.toString();
    }

    public void clearGroupOrderings(Object_Collection.Object_CollectionGroup group) {
        getOurDatabase().delete(TABLE_ORDERINGS, KEY_ORDERINGGID + " = ? AND " + KEY_ORDERINGDIRTY + " = ?", new String[]{Long.toString(group.getId()), "0"});
    }

    public void deleteCollection(Object_Collection collection) {
        getOurDatabase().delete(TABLE_COLLECTIONS, KEY_COLLECTIONID + " = ?", new String[]{Long.toString(collection.getId())});

        for (Object_Collection.Object_CollectionVersion version : collection.getVersions()) {
            getOurDatabase().delete(TABLE_VERSIONS, KEY_VERSIONID + " = ?", new String[]{Long.toString(version.getId())});
            getOurDatabase().delete(TABLE_GROUPS, KEY_GROUPVID + " = ?", new String[]{Long.toString(version.getId())});
        }
    }

    public void updateOrderingTable(ArrayList<Object_Collection.Object_CollectionOrder> orderings) {
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

    public void deleteUserCollection(Object_UserCollection userCollection) {
        getOurDatabase().delete(TABLE_USERCOLLECTIONS, KEY_UCOLLECTIONID + " = ?", new String[]{Long.toString(userCollection.getId())});
    }

    public void updateStyleTable(long customerId, Object_ProfileStyle style) {
        ContentValues cv = new ContentValues();
        if (style.getStyle() != null) {
            cv.put(KEY_STYLEID, style.getStyle().getId());
            cv.put(KEY_STYLEDESCRIPTION, style.getStyle().getDescription());
            cv.put(KEY_STYLENAME, style.getName());
            cv.put(KEY_STYLETYPE, style.getStyle().getType());
            cv.put(KEY_STYLEIMAGE, style.getStyle().getImage());
            cv.put(KEY_STYLECAT, style.getStyle().getProduct_category());
            cv.put(KEY_STYLELOC, Tools_Preferabli.getGson().toJson(style.getStyle().getLocations()));
        }

        cv.put(KEY_STYLECONFLICT, style.isConflict());
        cv.put(KEY_STYLEKEYWORDS, style.getKeywords());
        cv.put(KEY_STYLEOP, style.getOrderProfile());
        cv.put(KEY_STYLERATING, style.getRating());
        cv.put(KEY_STYLEOR, style.getOrderRecommend());
        cv.put(KEY_STYLEREFINE, style.isRefine());
        cv.put(KEY_STYLESTRENGTH, style.getStrength());
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

    public void updateStyleTable(Object_ProfileStyle style) {
        updateStyleTable(0, style);
    }

    public ArrayList<Object_Product> getJournalProducts() {
        Set<Object_Product> products = new HashSet<>();
        HashMap<Long, Object_Product> productsHash = new HashMap<>();
        HashMap<Long, Object_Variant> variantsHash = new HashMap<>();

        getProductsFromCollection(Tools_Preferabli.getKeyStore().getLong("ratings_id", 0), products, productsHash, variantsHash);
        getProductsFromCollection(Tools_Preferabli.getKeyStore().getLong("wishlist_id", 0), products, productsHash, variantsHash);

        ArrayList<Object_UserCollection> userCollections = getUserCollections("purchase");
        for (Object_UserCollection userCollection : userCollections) {
            getProductsFromCollection(userCollection.getCollectionId(), products, productsHash, variantsHash);
        }

        // add all our personal cellar tags in
        ArrayList<Object_Tag> personalCellarTags = getPersonalCellarTags();
        for (Object_Product product : products) {
            product.setRateSourceLocation("journal");
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

    public ArrayList<Object_Product> getPurchasedProducts(boolean lock_to_integration) {
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

    public void getProductsFromCollection(long collectionId, Set<Object_Product> products, HashMap<Long, Object_Product> productsHash, HashMap<Long, Object_Variant> variantsHash) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " INNER JOIN " + TABLE_VINTAGES + " ON " + KEY_TAGWINEID + " = " + KEY_VINTAGEWINEID + " INNER JOIN " + TABLE_WINES + " ON " + KEY_VINTAGEWINEID + " = " + KEY_WINEID + " WHERE " + KEY_TAGCOLLECTIONID + " = ?", new String[]{ Long.toString(collectionId)} );
        if(c instanceof SQLiteCursor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // fixed bug where cursor is not big enough for getting certain collections.
            ((SQLiteCursor) c).setWindow(new CursorWindow(null, 1024*1024*100));
        }

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Product product;
            if (productsHash.containsKey(c.getLong(c.getColumnIndex(KEY_WINEID)))) {
                product = productsHash.get(c.getLong(c.getColumnIndex(KEY_WINEID)));
            } else {
                product = getWineFromCursor(c);
                productsHash.put(product.getId(), product);
            }

            Object_Variant variant;
            if (variantsHash.containsKey(c.getLong(c.getColumnIndex(KEY_VINTAGEID)))) {
                variant = variantsHash.get(c.getLong(c.getColumnIndex(KEY_VINTAGEID)));
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

    public ArrayList<Object_Tag> getVariantTags(Object_Variant variant) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " WHERE " + KEY_TAGVINTAGEID + " = ?", new String[]{Long.toString(variant.getId())});

        ArrayList<Object_Tag> tags = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Tag tag = getTagFromCursor(c);
            tag.setVariant(variant);
            tags.add(tag);
        }
        c.close();

        return tags;
    }

    public ArrayList<Object_Tag> getWineTagsVariantIrrelevant(Object_Product product) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " WHERE " + KEY_TAGWINEID + " = ?", new String[]{Long.toString(product.getId())});

        ArrayList<Object_Tag> tags = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Tag tag = getTagFromCursor(c);
            tag.setVariant(product.getMostRecentVariant());
            tags.add(tag);
        }
        c.close();

        return tags;
    }

    public void deleteOrdering(long ordering_id) {
        getOurDatabase().delete(TABLE_ORDERINGS, KEY_ORDERINGID + " = ?", new String[]{Long.toString(ordering_id)});
    }

    public Object_Variant getVariant(long variant_id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_VINTAGES + " WHERE " + KEY_VINTAGEID + " = ?", new String[]{Long.toString(variant_id)});

        Object_Variant variant = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            variant = getVariantFromCursor(c);
        }
        c.close();

        return variant;
    }

    public ArrayList<Object_Product> getDirtyProducts() {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_WINES  + " INNER JOIN " + TABLE_VINTAGES + " ON " + KEY_WINEID + " = " + KEY_VINTAGEWINEID + " WHERE " + KEY_WINEDIRTY + " = ?", new String[]{"1"});

        HashMap<Long, Object_Product> wineHash = new HashMap<>();
        HashMap<Long, Object_Variant> variantsHash = new HashMap<>();

        ArrayList<Object_Product> products = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Product product;
            if (wineHash.containsKey(c.getLong(c.getColumnIndex(KEY_WINEID)))) {
                product = wineHash.get(c.getLong(c.getColumnIndex(KEY_WINEID)));
            } else {
                product = getWineFromCursor(c);
                wineHash.put(product.getId(), product);
            }

            Object_Variant variant;
            if (variantsHash.containsKey(c.getLong(c.getColumnIndex(KEY_VINTAGEID)))) {
                variant = variantsHash.get(c.getLong(c.getColumnIndex(KEY_VINTAGEID)));
            } else {
                variant = getVariantFromCursor(c);
                product.addVariant(variant);
                variantsHash.put(variant.getId(), variant);
            }
            variant.setProduct(product);

            products.add(product);
        }
        c.close();

        return products;
    }

    public ArrayList<Object_Product> getDirtyProductsWithTags() {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " INNER JOIN " + TABLE_VINTAGES + " ON " + KEY_TAGVINTAGEID + " = " + KEY_VINTAGEID + " INNER JOIN " + TABLE_WINES + " ON " + KEY_VINTAGEWINEID + " = " + KEY_WINEID + " WHERE " + KEY_TAGDIRTY + " = ?", new String[]{"1"});

        ArrayList<Object_Product> products = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Product product = getWineFromCursor(c);
            Object_Variant variant = getVariantFromCursor(c);
            Object_Tag tag = getTagFromCursor(c);
            tag.setVariant(variant);
            variant.setProduct(product);
            variant.addTag(tag);
            product.addVariant(variant);
            products.add(product);
        }
        c.close();

        return products;
    }

    public ArrayList<Object_Product> getCustomerTags(long customer_id, String tag_type) {
        Cursor c;
        if (tag_type == null) {
            c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " INNER JOIN " + TABLE_VINTAGES + " ON " + KEY_TAGVINTAGEID + " = " + KEY_VINTAGEID + " INNER JOIN " + TABLE_WINES + " ON " + KEY_VINTAGEWINEID + " = " + KEY_WINEID + " WHERE " + KEY_TAGCUSTOMERID + " = ?", new String[]{Long.toString(customer_id)});

        } else {
            c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " INNER JOIN " + TABLE_VINTAGES + " ON " + KEY_TAGVINTAGEID + " = " + KEY_VINTAGEID + " INNER JOIN " + TABLE_WINES + " ON " + KEY_VINTAGEWINEID + " = " + KEY_WINEID + " WHERE " + KEY_TAGCUSTOMERID + " = ? AND " + KEY_TAGTYPE + " = ?", new String[]{Long.toString(customer_id), tag_type});
        }

        ArrayList<Object_Product> products = new ArrayList<>();
        HashMap<Long, Object_Product> wineHash = new HashMap<>();
        HashMap<Long, Object_Variant> variantHash = new HashMap<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Product product;
            if (wineHash.containsKey(c.getLong(c.getColumnIndex(KEY_WINEID)))) {
                product = wineHash.get(c.getLong(c.getColumnIndex(KEY_WINEID)));
            } else {
                product = getWineFromCursor(c);
                wineHash.put(product.getId(), product);
            }

            Object_Variant variant;
            if (variantHash.containsKey(c.getLong(c.getColumnIndex(KEY_VINTAGEID)))) {
                variant = variantHash.get(c.getLong(c.getColumnIndex(KEY_VINTAGEID)));
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

    public void deleteGroup(Object_Collection.Object_CollectionGroup group) {
        getOurDatabase().delete(TABLE_GROUPS, KEY_GROUPID + " = ?", new String[]{Long.toString(group.getId())});
    }

    public ArrayList<Object_UserCollection> getUserCollections(String relationshipType) {
        // we need associated collections since this is in the journal
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_USERCOLLECTIONS + " LEFT OUTER JOIN " + TABLE_COLLECTIONS + " ON " + KEY_UCOLLECTIONCID + " = " + KEY_COLLECTIONID, null);

        ArrayList<Object_UserCollection> rts = new ArrayList<>();
        HashMap<Long, Object_Collection> collections = new HashMap<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_UserCollection userCollection = getUserCollectionFromCursor(c);
            if (userCollection.getRelationship_type().equalsIgnoreCase(relationshipType)) {
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
            collection.addUserCollection(userCollection);
        }

        c.close();

        return rts;
    }

    public ArrayList<Object_UserCollection> getUserCollections(long collectionId) {
        // we do not need the collection objects associated here since this is inside collection details
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_USERCOLLECTIONS + " WHERE " + KEY_UCOLLECTIONCID + " = ?", new String[]{Long.toString(collectionId)});

        ArrayList<Object_UserCollection> userCollections = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_UserCollection userCollection = getUserCollectionFromCursor(c);
            userCollections.add(userCollection);
        }
        c.close();

        return userCollections;
    }

    public int getPersonalCellarCount() {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_USERCOLLECTIONS + " WHERE " + KEY_UCOLLECTIONRT + " = ?", new String[]{"mycellar"});
        return c.getCount();
    }

    public ArrayList<Object_Collection> getChannelCollections(long channel_id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_COLLECTIONS + " WHERE " + KEY_COLLECTIONCID + " = ?", new String[]{Long.toString(channel_id)});

        ArrayList<Object_Collection> collections = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Collection collection = getCollectionFromCursor(c);
            collection.setUserCollections(getUserCollections(collection.getId()));
            collections.add(collection);
        }
        c.close();

        return collections;
    }

    public Object_Collection getCollection(long collectionId) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_COLLECTIONS + " LEFT OUTER JOIN " + TABLE_VERSIONS + " ON " + KEY_COLLECTIONID + " = " + KEY_VERSIONCID + " LEFT OUTER JOIN " + TABLE_GROUPS + " ON " + KEY_VERSIONID + " = " + KEY_GROUPVID + " LEFT OUTER JOIN " + TABLE_ORDERINGS + " ON " + KEY_GROUPID + " = " + KEY_ORDERINGGID + " LEFT OUTER JOIN " + TABLE_TAGS + " ON " + KEY_ORDERINGID + " = " + KEY_TAGORDERINGID + " LEFT OUTER JOIN " + TABLE_VINTAGES + " ON " + KEY_TAGWINEID + " = " + KEY_VINTAGEWINEID + " LEFT OUTER JOIN " + TABLE_WINES + " ON " + KEY_VINTAGEWINEID + " = " + KEY_WINEID + " WHERE " + KEY_COLLECTIONID + " = ?", new String[]{Long.toString(collectionId)});


        Object_Collection collection = null;
        Object_Collection.Object_CollectionVersion version = null;
        HashMap<Long, Object_Collection.Object_CollectionGroup> groupHash = new HashMap<>();
        HashMap<Long, Object_Collection.Object_CollectionOrder> orderingHash = new HashMap<>();
        HashMap<Long, Object_Product> wineHash = new HashMap<>();
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
            if (c.getLong(c.getColumnIndex(KEY_ORDERINGID)) == 0 || c.getLong(c.getColumnIndex(KEY_TAGID)) == 0 || c.getLong(c.getColumnIndex(KEY_VINTAGEID)) == 0 || c.getLong(c.getColumnIndex(KEY_WINEID)) == 0)
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
            if (wineHash.containsKey(c.getLong(c.getColumnIndex(KEY_WINEID)))) {
                product = wineHash.get(c.getLong(c.getColumnIndex(KEY_WINEID)));
            } else {
                product = getWineFromCursor(c);
                wineHash.put(product.getId(), product);
            }

            Object_Variant variant;
            if (variantHash.containsKey(c.getLong(c.getColumnIndex(KEY_VINTAGEID)))) {
                variant = variantHash.get(c.getLong(c.getColumnIndex(KEY_VINTAGEID)));
            } else {
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
                if (tag.getTagType() == Other_TagType.WISHLIST && tag.getProductId() == variant.getWineId()) {
                    variant.addTag(tag);
                    tag.setVariant(variant);
                } else if (tag.getType().equalsIgnoreCase("skipped") && tag.getProductId() == variant.getWineId()) {
                    variant.addTag(tag);
                    tag.setVariant(variant);
                } else if (variant.getId() == tag.getVariantId()) {
                    variant.addTag(tag);
                    tag.setVariant(variant);
                }
            }
        }

        if (collection != null) {
            collection.setUserCollections(getUserCollections(collection.getId()));

            Object_Collection.Object_CollectionGroup.sortGroups(version.getGroups());
            for (Object_Collection.Object_CollectionGroup group : version.getGroups()) {
                Object_Collection.Object_CollectionOrder.sortOrders(group.getOrderings());
            }
        }

        return collection;
    }

    public Object_Collection getCollectionBasic(long collectionId) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_COLLECTIONS + " LEFT OUTER JOIN " + TABLE_VERSIONS + " ON " + KEY_COLLECTIONID + " = " + KEY_VERSIONCID + " LEFT OUTER JOIN " + TABLE_GROUPS + " ON " + KEY_VERSIONID + " = " + KEY_GROUPVID + " WHERE " + KEY_COLLECTIONID + " = ?", new String[]{Long.toString(collectionId)});


        Object_Collection collection = null;
        Object_Collection.Object_CollectionVersion version = null;
        HashMap<Long, Object_Collection.Object_CollectionGroup> groupHash = new HashMap<>();

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
        }
        c.close();

        return collection;
    }

    public Object_Product getProductWithId(long wineId) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_WINES + " LEFT JOIN " + TABLE_VINTAGES + " ON " + KEY_WINEID + " = " + KEY_VINTAGEWINEID + " LEFT JOIN " + TABLE_TAGS + " ON " + KEY_VINTAGEID + " = " + KEY_TAGVINTAGEID + " WHERE " + KEY_WINEID + " = ?", new String[]{Long.toString(wineId)});

        Object_Product product = null;
        HashMap<Long, Object_Variant> variantHash = new HashMap<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (product == null) {
                product = getWineFromCursor(c);
            }

            Object_Variant variant;
            if (variantHash.containsKey(c.getLong(c.getColumnIndex(KEY_VINTAGEID)))) {
                variant = variantHash.get(c.getLong(c.getColumnIndex(KEY_VINTAGEID)));
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

    public Object_Collection.Object_CollectionGroup getGroup(long group_id) {
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

    public Object_Tag getTag(long tag_id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " WHERE " + KEY_TAGID + " = ?", new String[]{Long.toString(tag_id)});

        Object_Tag tag = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            tag = getTagFromCursor(c);

        }
        c.close();

        return tag;
    }

    public ArrayList<Object_Tag> getPersonalTags() {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS + " WHERE " + KEY_TAGCOLLECTIONID + " = ? OR " + KEY_TAGCOLLECTIONID + "= ? OR " + KEY_TAGCOLLECTIONID + "= ?", new String[]{Long.toString(Tools_Preferabli.getKeyStore().getLong("ratings_id", 0)), Long.toString(Tools_Preferabli.getKeyStore().getLong("wishlist_id", 0)), Long.toString(Tools_Preferabli.getKeyStore().getLong("skips_id", 0))});

        ArrayList<Object_Tag> tags = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Tag tag = getTagFromCursor(c);
            tags.add(tag);
        }
        c.close();

        return tags;
    }

    public ArrayList<Object_Tag> getPersonalCellarTags() {
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

    public boolean isCollectionSaved(long collection_id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_COLLECTIONS + " WHERE " + KEY_COLLECTIONID + " = ?", new String[]{Long.toString(collection_id)});

        boolean isCollectionSaved = false;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Collection collection = getCollectionFromCursor(c);
            isCollectionSaved = collection.isSaved();
        }
        c.close();

        return isCollectionSaved;
    }

    public Object_ProfileStyle getStyleById(long id) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_STYLES + " WHERE " + KEY_STYLEID + " = ?", new String[]{Long.toString(id)});

        Object_ProfileStyle style = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            style = getStyleFromCursor(c);
        }
        c.close();

        return style;
    }

    public ArrayList<Object_ProfileStyle> getStyles() {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_STYLES, null);

        ArrayList<Object_ProfileStyle> styles = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_ProfileStyle style = getStyleFromCursor(c);
            styles.add(style);
        }
        c.close();

        return styles;
    }

    public Object_Customer getCustomer(long customerId) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_CUSTOMERS + " WHERE " + KEY_CUSTOMERID + " = ?", new String[]{Long.toString(customerId)});

        Object_Customer customer = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            customer = getCustomerFromCursor(c);
        }
        c.close();

        return customer;
    }

    public Object_Customer getCustomerFromCursor(Cursor c) {
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

    public Object_ProfileStyle getStyleFromCursor(Cursor c) {
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
        int iStr = c.getColumnIndex(KEY_STYLESTRENGTH);
        int iCr = c.getColumnIndex(KEY_STYLECREATED);

        return new Object_ProfileStyle(c.getLong(iId), c.getInt(iRefine) == 1, c.getInt(iCon) == 1, c.getInt(iOR), c.getInt(iOP), c.getString(iKey), c.getString(iName), c.getString(iDes), c.getString(iType), c.getInt(iRating), c.getString(iFoods), c.getString(iImage), c.getString(iCat), c.getString(iLoc), c.getInt(iStr), c.getString(iCr));
    }

    public Object_Product getWineFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_WINEID);
        int iName = c.getColumnIndex(KEY_WINENAME);
        int iBrand = c.getColumnIndex(KEY_WINEBRAND);
        int iBrandLat = c.getColumnIndex(KEY_WINEBRANDLAT);
        int iBrandLon = c.getColumnIndex(KEY_WINEBRANDLON);
        int iCA = c.getColumnIndex(KEY_WINECA);
        int iGrape = c.getColumnIndex(KEY_WINEGRAPE);
        int iUA = c.getColumnIndex(KEY_WINEUA);
        int iNV = c.getColumnIndex(KEY_WINENV);
        int iDecant = c.getColumnIndex(KEY_WINEDECANT);
        int iRegion = c.getColumnIndex(KEY_WINEREGION);
        int iType = c.getColumnIndex(KEY_WINETYPE);
        int iImage = c.getColumnIndex(KEY_WINEIMAGE);
        int iBackImage = c.getColumnIndex(KEY_WINEBACKIMAGE);
        int iLocal = c.getColumnIndex(KEY_WINELOCALID);
        int iDirty = c.getColumnIndex(KEY_WINEDIRTY);
        int iCat = c.getColumnIndex(KEY_WINECAT);
        int iRSL = c.getColumnIndex(KEY_WINERSL);
        int iSC = c.getColumnIndex(KEY_WINESC);
        int iHASH = c.getColumnIndex(KEY_WINEHASH);

        return new Object_Product(c.getLong(iId), c.getString(iName), c.getString(iGrape), c.getInt(iDecant) == 1, c.getInt(iNV) == 1, c.getString(iType), c.getString(iImage), c.getString(iBackImage), c.getString(iBrand), c.getString(iRegion), c.getString(iCA), c.getString(iUA), c.getInt(iDirty) == 1, c.getDouble(iBrandLat), c.getDouble(iBrandLon), c.getString(iCat), c.getString(iRSL), c.getString(iSC), c.getString(iHASH));
    }

    public Object_Variant getVariantFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_VINTAGEID);
        int iCA = c.getColumnIndex(KEY_VINTAGECA);
        int iDS = c.getColumnIndex(KEY_VINTAGEDOLLARSIGNS);
        int iFresh = c.getColumnIndex(KEY_VINTAGEFRESH);
        int iImage = c.getColumnIndex(KEY_VINTAGEIMAGE);
        int iPrice = c.getColumnIndex(KEY_VINTAGEPRICE);
        int iUA = c.getColumnIndex(KEY_VINTAGEUA);
        int iRec = c.getColumnIndex(KEY_VINTAGERECOMMENDABLE);
        int iYear = c.getColumnIndex(KEY_VINTAGEYEAR);
        int iLocal = c.getColumnIndex(KEY_VINTAGELOCALID);
        int iVWID = c.getColumnIndex(KEY_VINTAGEWINEID);
        return new Object_Variant(c.getLong(iId), c.getInt(iYear), c.getInt(iFresh) == 1, c.getInt(iRec) == 1, c.getDouble(iPrice), c.getInt(iDS), c.getString(iImage), c.getString(iCA), c.getString(iUA), c.getLong(iVWID));
    }

    public Object_Tag getTagFromCursor(Cursor c) {
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
        int iVID = c.getColumnIndex(KEY_TAGVINTAGEID);
        int iDIRTY = c.getColumnIndex(KEY_TAGDIRTY);
        int iLocal = c.getColumnIndex(KEY_TAGLOCALID);
        int iBadge = c.getColumnIndex(KEY_TAGBADGE);
        int iParams = c.getColumnIndex(KEY_TAGPARAMS);
        int iWID = c.getColumnIndex(KEY_TAGWINEID);
        int iCHN = c.getColumnIndex(KEY_TAGCHNAME);
        int iTP = c.getColumnIndex(KEY_TAGPRICE);
        int iTQ = c.getColumnIndex(KEY_TAGQUANTITY);
        int iTFML = c.getColumnIndex(KEY_TAGFML);
        int iBin = c.getColumnIndex(KEY_TAGBIN);

        return new Object_Tag(c.getLong(iId), c.getLong(iUID), c.getLong(iTCHID), c.getLong(iVID), c.getLong(iWID), c.getLong(iTCID), c.getLong(iTICID), c.getLong(iTICVID), c.getString(iType), c.getString(iValue), c.getString(iComment), c.getString(iLoc), c.getString(iCA), c.getString(iUA), c.getString(iShare), c.getInt(iDIRTY) == 1, c.getString(iBadge), c.getString(iParams), c.getString(iCHN), c.getInt(iTFML), c.getDouble(iTP), c.getInt(iTQ), c.getString(iBin));
    }

    public Object_Collection getCollectionFromCursor(Cursor c) {
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

    public Object_UserCollection getUserCollectionFromCursor(Cursor c) {
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

    public Object_Collection.Object_CollectionVersion getVersionFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_VERSIONID);
        return new Object_Collection.Object_CollectionVersion(c.getLong(iId));
    }

    public Object_Collection.Object_CollectionGroup getGroupFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_GROUPID);
        int iName = c.getColumnIndex(KEY_GROUPNAME);
        int iOC = c.getColumnIndex(KEY_GROUPOC);
        int iOrder = c.getColumnIndex(KEY_GROUPORDER);
        int iVID = c.getColumnIndex(KEY_GROUPVID);
        return new Object_Collection.Object_CollectionGroup(c.getLong(iId), c.getString(iName), c.getInt(iOC), c.getInt(iOrder), c.getLong(iVID));
    }

    public Object_Collection.Object_CollectionOrder getOrderingFromCursor(Cursor c) {
        int iId = c.getColumnIndex(KEY_ORDERINGID);
        int iCVTGID = c.getColumnIndex(KEY_ORDERINGCVTID);
        int iOrder = c.getColumnIndex(KEY_ORDERINGORDER);
        int iOrderDirty = c.getColumnIndex(KEY_ORDERINGDIRTY);
        int iGroupId = c.getColumnIndex(KEY_ORDERINGGID);
        return new Object_Collection.Object_CollectionOrder(c.getLong(iId), c.getLong(iCVTGID), c.getInt(iOrder), c.getInt(iOrderDirty) == 1, c.getLong(iGroupId));
    }


    public ArrayList<Object_Search> getSearches() {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_SEARCHES, null);

        ArrayList<Object_Search> searches = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Object_Search search = getSearchFromCursor(c);
            searches.add(search);
        }
        c.close();

        return searches;
    }

    public Object_Search getSearch(String text) {
        Cursor c = getOurDatabase().rawQuery("SELECT * FROM " + TABLE_SEARCHES + " WHERE " + KEY_SEARCHTEXT + " = ?", new String[]{text});

        Object_Search search = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            search = getSearchFromCursor(c);
        }
        c.close();

        return search;
    }

    public Object_Search getSearchFromCursor(Cursor c) {
        int iSC = c.getColumnIndex(KEY_SEARCHCOUNT);
        int iSL = c.getColumnIndex(KEY_SEARCHLAST);
        int iST = c.getColumnIndex(KEY_SEARCHTEXT);

        return new Object_Search(c.getInt(iSC), c.getString(iSL), c.getString(iST));
    }
}