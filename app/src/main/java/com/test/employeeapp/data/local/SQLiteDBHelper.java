package com.test.employeeapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.employeeapp.data.model.EmployeeDetailsModel;
import com.test.employeeapp.data.model.EmployeeListModel;
import com.test.employeeapp.data.model.EmployeeListResponseModel;

import java.util.ArrayList;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "employee_database";

    public static final String EMPLOYEE_TABLE = "employee";
    public static final String ADDRESS_TABLE = "address";
    public static final String COMPANY_TABLE = "company";
    public static final String LOCATION_TABLE = "location";

    public static final String EMPLOYEE_COLUMN_ID = "id";
    public static final String EMPLOYEE_COLUMN_NAME = "name";
    public static final String EMPLOYEE_COLUMN_USER_NAME = "username";
    public static final String EMPLOYEE_COLUMN_EMAIL = "email";
    public static final String EMPLOYEE_COLUMN_PROFILE_IMAGE = "profileimage";
    public static final String EMPLOYEE_COLUMN_PHONE = "phone";
    public static final String EMPLOYEE_COLUMN_WEBSITE = "website";

    public static final String ADDRESS_COLUMN_STREET = "street";
    public static final String ADDRESS_COLUMN_SUITE = "suite";
    public static final String ADDRESS_COLUMN_CITY = "city";
    public static final String ADDRESS_COLUMN_ZIPCODE = "zipcode";

    public static final String COMPANY_COLUMN_NAME = "name";
    public static final String COMPANY_COLUMN_PHRASE = "catchPhrase";
    public static final String COMPANY_COLUMN_BS = "bs";

    public static final String LOCATION_COLUMN_LAT = "lat";
    public static final String LOCATION_COLUMN_LNG = "lng";

    private static final int DATABASE_VERSION = 1;

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + EMPLOYEE_TABLE + " (" +
                EMPLOYEE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                EMPLOYEE_COLUMN_NAME + " TEXT, " +
                EMPLOYEE_COLUMN_USER_NAME + " TEXT, " +
                EMPLOYEE_COLUMN_EMAIL + " TEXT, " +
                EMPLOYEE_COLUMN_PROFILE_IMAGE + " TEXT, " +
                EMPLOYEE_COLUMN_PHONE + " TEXT, " +
                EMPLOYEE_COLUMN_WEBSITE + " TEXT" + ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + ADDRESS_TABLE + " (" +
                EMPLOYEE_COLUMN_ID + " INTEGER , " +
                ADDRESS_COLUMN_STREET + " TEXT, " +
                ADDRESS_COLUMN_SUITE + " TEXT, " +
                ADDRESS_COLUMN_CITY + " TEXT, " +
                ADDRESS_COLUMN_ZIPCODE + " TEXT" + ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + COMPANY_TABLE + " (" +
                EMPLOYEE_COLUMN_ID + " INTEGER , " +
                COMPANY_COLUMN_NAME + " TEXT, " +
                COMPANY_COLUMN_PHRASE + " TEXT, " +
                COMPANY_COLUMN_BS + " TEXT" + ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + LOCATION_TABLE + " (" +
                EMPLOYEE_COLUMN_ID + " INTEGER , " +
                LOCATION_COLUMN_LAT + " TEXT, " +
                LOCATION_COLUMN_LNG + " TEXT" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EMPLOYEE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ADDRESS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + COMPANY_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void addEmployee(EmployeeListResponseModel employeeList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues employee = new ContentValues();
        employee.put(EMPLOYEE_COLUMN_ID, employeeList.getId());
        employee.put(EMPLOYEE_COLUMN_NAME, employeeList.getName());
        employee.put(EMPLOYEE_COLUMN_USER_NAME, employeeList.getUsername());
        employee.put(EMPLOYEE_COLUMN_EMAIL, employeeList.getEmail());
        employee.put(EMPLOYEE_COLUMN_PROFILE_IMAGE, employeeList.getProfileImage());
        employee.put(EMPLOYEE_COLUMN_PHONE, employeeList.getPhone());
        employee.put(EMPLOYEE_COLUMN_WEBSITE, employeeList.getWebsite());
        db.insert(EMPLOYEE_TABLE, null, employee);

        ContentValues address = new ContentValues();
        address.put(EMPLOYEE_COLUMN_ID, employeeList.getId());
        if (employeeList.getAddress() != null) {
            address.put(ADDRESS_COLUMN_STREET, employeeList.getAddress().getStreet());
            address.put(ADDRESS_COLUMN_SUITE, employeeList.getAddress().getSuite());
            address.put(ADDRESS_COLUMN_CITY, employeeList.getAddress().getCity());
            address.put(ADDRESS_COLUMN_ZIPCODE, employeeList.getAddress().getZipcode());
        } else {
            address.put(ADDRESS_COLUMN_STREET, "__");
            address.put(ADDRESS_COLUMN_SUITE, "__");
            address.put(ADDRESS_COLUMN_CITY, "__");
            address.put(ADDRESS_COLUMN_ZIPCODE, "__");
        }
        db.insert(ADDRESS_TABLE, null, address);

        ContentValues company = new ContentValues();
        company.put(EMPLOYEE_COLUMN_ID, employeeList.getId());
        if (employeeList.getCompany() != null) {
            company.put(COMPANY_COLUMN_NAME, employeeList.getCompany().getName());
            company.put(COMPANY_COLUMN_PHRASE, employeeList.getCompany().getCatchPhrase());
            company.put(COMPANY_COLUMN_BS, employeeList.getCompany().getBs());
        } else {
            company.put(COMPANY_COLUMN_NAME, "__");
            company.put(COMPANY_COLUMN_PHRASE, "__");
            company.put(COMPANY_COLUMN_BS, "__");
        }
        db.insert(COMPANY_TABLE, null, company);

        ContentValues location = new ContentValues();
        location.put(EMPLOYEE_COLUMN_ID, employeeList.getId());
        if (employeeList.getAddress().getGeo() != null) {
            location.put(LOCATION_COLUMN_LAT, employeeList.getAddress().getGeo().getLat());
            location.put(LOCATION_COLUMN_LNG, employeeList.getAddress().getGeo().getLng());
        } else {
            location.put(LOCATION_COLUMN_LAT, "__");
            location.put(LOCATION_COLUMN_LNG, "__");
        }
        db.insert(LOCATION_TABLE, null, location);
    }

    public ArrayList<EmployeeListModel> getAllEmployees() {
        ArrayList<EmployeeListModel> employeeArrayList = new ArrayList<EmployeeListModel>();

        String selectQuery = "SELECT  * FROM " + EMPLOYEE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                EmployeeListModel userModel = new EmployeeListModel();
                userModel.setId(cursor.getInt(cursor.getColumnIndex(EMPLOYEE_COLUMN_ID)));
                userModel.setName(cursor.getString(cursor.getColumnIndex(EMPLOYEE_COLUMN_NAME)));
                userModel.setProfileImage(cursor.getString(cursor.getColumnIndex(
                        EMPLOYEE_COLUMN_PROFILE_IMAGE)));

                String selectCompanyQuery = "SELECT  * FROM " + COMPANY_TABLE + " WHERE " +
                        EMPLOYEE_COLUMN_ID + " = " + userModel.getId();

                Cursor cursorCompany = db.rawQuery(selectCompanyQuery, null);

                if (cursorCompany.moveToFirst()) {
                    do {
                        userModel.setCompanyName(cursorCompany.getString(cursorCompany
                                .getColumnIndex(COMPANY_COLUMN_NAME)));
                    } while (cursorCompany.moveToNext());
                }

                employeeArrayList.add(userModel);
            } while (cursor.moveToNext());
        }
        return employeeArrayList;
    }

    public EmployeeDetailsModel getEmployeeDetails(int id) {
        EmployeeDetailsModel employeeDetailsModel = new EmployeeDetailsModel();

        String selectQuery = "SELECT  * FROM " + EMPLOYEE_TABLE + " WHERE " +
                EMPLOYEE_COLUMN_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                employeeDetailsModel.setId(cursor.getInt(cursor
                        .getColumnIndex(EMPLOYEE_COLUMN_ID)));
                employeeDetailsModel.setName(cursor.getString(cursor
                        .getColumnIndex(EMPLOYEE_COLUMN_NAME)));
                employeeDetailsModel.setUsername(cursor.getString(cursor
                        .getColumnIndex(EMPLOYEE_COLUMN_USER_NAME)));
                employeeDetailsModel.setEmail(cursor.getString(cursor
                        .getColumnIndex(EMPLOYEE_COLUMN_EMAIL)));
                employeeDetailsModel.setPhone(cursor.getString(cursor
                        .getColumnIndex(EMPLOYEE_COLUMN_PHONE)));
                employeeDetailsModel.setWebsite(cursor.getString(cursor
                        .getColumnIndex(EMPLOYEE_COLUMN_WEBSITE)));
                employeeDetailsModel.setProfileImage(cursor.getString(cursor
                        .getColumnIndex(EMPLOYEE_COLUMN_PROFILE_IMAGE)));


                String selectCompanyQuery = "SELECT  * FROM " + COMPANY_TABLE + " WHERE " +
                        EMPLOYEE_COLUMN_ID + " = " + id;

                Cursor cursorCompany = db.rawQuery(selectCompanyQuery, null);

                if (cursorCompany.moveToFirst()) {
                    do {
                        employeeDetailsModel.setCompanyName(cursorCompany.getString(cursorCompany
                                .getColumnIndex(COMPANY_COLUMN_NAME)));
                        employeeDetailsModel.setCatchPhrase(cursorCompany.getString(cursorCompany
                                .getColumnIndex(COMPANY_COLUMN_PHRASE)));
                        employeeDetailsModel.setBs(cursorCompany.getString(cursorCompany
                                .getColumnIndex(COMPANY_COLUMN_BS)));
                    } while (cursorCompany.moveToNext());
                }


                String selectAddressQuery = "SELECT  * FROM " + ADDRESS_TABLE + " WHERE " +
                        EMPLOYEE_COLUMN_ID + " = " + id;

                Cursor cursorAddress = db.rawQuery(selectAddressQuery, null);

                if (cursorAddress.moveToFirst()) {
                    do {
                        employeeDetailsModel.setStreet(cursorAddress.getString(cursorAddress
                                .getColumnIndex(ADDRESS_COLUMN_STREET)));
                        employeeDetailsModel.setSuite(cursorAddress.getString(cursorAddress
                                .getColumnIndex(ADDRESS_COLUMN_SUITE)));
                        employeeDetailsModel.setCity(cursorAddress.getString(cursorAddress
                                .getColumnIndex(ADDRESS_COLUMN_CITY)));
                        employeeDetailsModel.setZipcode(cursorAddress.getString(cursorAddress
                                .getColumnIndex(ADDRESS_COLUMN_ZIPCODE)));
                    } while (cursorAddress.moveToNext());
                }

                String selectLocationQuery = "SELECT  * FROM " + LOCATION_TABLE + " WHERE " +
                        EMPLOYEE_COLUMN_ID + " = " + id;

                Cursor cursorLocation = db.rawQuery(selectLocationQuery, null);

                if (cursorLocation.moveToFirst()) {
                    do {
                        employeeDetailsModel.setLat(cursorLocation.getString(cursorLocation
                                .getColumnIndex(LOCATION_COLUMN_LAT)));
                        employeeDetailsModel.setLng(cursorLocation.getString(cursorLocation
                                .getColumnIndex(LOCATION_COLUMN_LNG)));
                    } while (cursorAddress.moveToNext());
                }

            } while (cursor.moveToNext());
        }
        return employeeDetailsModel;
    }

    public ArrayList<EmployeeListModel> search(String name) {
        ArrayList<EmployeeListModel> employeeDetailsModel = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + EMPLOYEE_TABLE + " WHERE "
                + EMPLOYEE_COLUMN_NAME + " LIKE '%" + name + "%' OR " + EMPLOYEE_COLUMN_EMAIL
                + " LIKE '%" + name + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                EmployeeListModel employeeModel = new EmployeeListModel();
                employeeModel.setId(cursor.getInt(cursor.getColumnIndex(EMPLOYEE_COLUMN_ID)));
                employeeModel.setName(cursor.getString(cursor.getColumnIndex(EMPLOYEE_COLUMN_NAME)));
                employeeModel.setProfileImage(cursor.getString(cursor.getColumnIndex
                        (EMPLOYEE_COLUMN_PROFILE_IMAGE)));

                String selectCompanyQuery = "SELECT  * FROM " + COMPANY_TABLE + " WHERE "
                        + EMPLOYEE_COLUMN_ID + " = " + employeeModel.getId();

                Cursor cursorCompany = db.rawQuery(selectCompanyQuery, null);

                if (cursorCompany.moveToFirst()) {
                    do {
                        employeeModel.setCompanyName(cursorCompany.getString(cursorCompany
                                .getColumnIndex(COMPANY_COLUMN_NAME)));
                    } while (cursorCompany.moveToNext());
                }

                employeeDetailsModel.add(employeeModel);
            } while (cursor.moveToNext());
        }
        return employeeDetailsModel;
    }
}