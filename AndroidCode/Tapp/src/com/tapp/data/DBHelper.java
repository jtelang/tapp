package com.tapp.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	// used to define db name
	private final static String DATABASE_NAME = "Tapp.db";
	// used to define db version
	private final static int DATABASE_VERSION = 1;
	// used to store current context
	private Context context;
	// define object of SQLiteDatabase
	private String DB_PATH = "";
	// SQLLite instance
	private SQLiteDatabase db = null;
	// score table name
	private static final String TABLE_CONTACTS = "tapp_contacts";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		this.context = context;

		try {

			DB_PATH = this.context.getDatabasePath(DATABASE_NAME).toString();

		} catch (Exception e) {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		try {

			db.execSQL("CREATE TABLE IF NOT EXISTS [tapp_contacts] ( [id] INTEGER PRIMARY KEY AUTOINCREMENT, [phone_no] TEXT, [name] TEXT, [contact_type_flag] INT(1) DEFAULT (0), [photo_url] TEXT, [raw_contact_id] INTEGER, [status] TEXT); CREATE INDEX [index_phone_no] ON [tapp_contacts] ([phone_no]); CREATE INDEX [index_contact_type_flag] ON [tapp_contacts] ([contact_type_flag]);");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	/**
	 * This function is used to check whether file is exist or not
	 */
	private boolean isDataBaseExist() {

		File dbFile = new File(DB_PATH);
		return dbFile.exists();
	}

	/**
	 * This function is use to copy database from one file(from assets) to
	 * another(application)
	 * 
	 * @throws IOException
	 */

	private void copyDataBase() throws IOException {

		try {

			// Open your local db as the input stream
			InputStream myInput = context.getAssets().open("database/Tapp.db");

			// Path to the just created empty dbos
			OutputStream myOutput = new FileOutputStream(DB_PATH);

			// transfer bytes from the input file to the output file
			byte[] buffer = new byte[2048];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in copy DB", e.toString());
		}
	}

	public void copyDataBaseToSDcard() throws IOException {

		try {
			// Open your local db as the input stream
			FileInputStream myInput = new FileInputStream(DB_PATH);

			// Path to the just created empty db

			String outFilePath = Environment.getExternalStorageDirectory().toString();

			File myNewFolder = new File(outFilePath);
			if (!myNewFolder.isDirectory()) {
				myNewFolder.mkdirs();
			}

			OutputStream myOutput = new FileOutputStream(outFilePath + "/" + DATABASE_NAME);

			// transfer bytes from the input file to the output file
			byte[] buffer = new byte[2048];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in copy DB", e.toString());
		}
	}

	public DBHelper open() throws Exception {
		try {

			// check that db is exist or not
			boolean isExist = isDataBaseExist();

			if (isExist == false) {
				db = getWritableDatabase();
				// copy db from assets
				copyDataBase();
				if (db.isOpen()) {
					db.close();
				}
			}
			db = getWritableDatabase();

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error in open db", "" + e.toString());
		}
		return this;
	}

	public boolean isOpen() {
		return db.isOpen();
	}

	public void beginTransaction() {

		try {
			db.beginTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in Transaction", e.toString());
		}
	}

	public void endTransaction() {

		try {
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in Transaction", e.toString());
		}
	}

	public boolean isTransactionRunning() {

		try {
			return db.inTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in isTransactionRunning", e.toString());
		}
		return false;
	}

	public void close() {

		if (db != null && db.isOpen()) {
			db.close();
			db.releaseReference();
			db = null;
		}
	}

	public void insertContact(int rawContactId, String phoneNo, String name, int contactTypeFlag, String photoUrl, String status) {

		try {

			ContentValues contentValues = new ContentValues();

			contentValues.put("phone_no", phoneNo);
			contentValues.put("name", name);
			contentValues.put("contact_type_flag", contactTypeFlag);
			contentValues.put("photo_url", photoUrl);
			contentValues.put("raw_contact_id", rawContactId);
			contentValues.put("status", status);

			db.insert(TABLE_CONTACTS, null, contentValues);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in insertBillData", e.toString());
		}
	}

	public void updateContactByRawId(int rawContactId, String name, String phoneNo, int contactTypeFlag, String photoUrl, String status) {

		try {

			ContentValues contentValues = new ContentValues();

			contentValues.put("phone_no", phoneNo);
			contentValues.put("name", name);
			contentValues.put("contact_type_flag", contactTypeFlag);
			contentValues.put("photo_url", photoUrl);
			contentValues.put("status", status);

			db.update(TABLE_CONTACTS, contentValues, "raw_contact_id = ?", new String[]{String.valueOf(rawContactId)});

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in updateContactByPhoneNo", e.toString());
		}
	}

	public void updateContactByPhoneNo(String phoneNo, int contactTypeFlag, String photoUrl, String status) {

		try {

			ContentValues contentValues = new ContentValues();

			contentValues.put("contact_type_flag", contactTypeFlag);
			contentValues.put("photo_url", photoUrl);
			contentValues.put("status", status);

			db.update(TABLE_CONTACTS, contentValues, "phone_no = ? OR phone_no LIKE ?", new String[]{phoneNo, "%" + phoneNo});

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in updateContactByPhoneNo", e.toString());
		}
	}

	public void updateContact(int rawContactId, String phoneNo, String name, int contactTypeFlag, String photoUrl, String status) {

		try {

			ContentValues contentValues = new ContentValues();

			contentValues.put("phone_no", phoneNo);
			contentValues.put("name", name);
			contentValues.put("contact_type_flag", contactTypeFlag);
			contentValues.put("photo_url", photoUrl);
			contentValues.put("status", status);

			db.update(TABLE_CONTACTS, contentValues, "raw_contact_id = ?", new String[]{String.valueOf(rawContactId)});

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in updateContact", e.toString());
		}
	}

	public ArrayList<ContactData> getContactList() {

		Cursor c = null;
		ArrayList<ContactData> list = new ArrayList<ContactData>();

		try {

			c = db.query(TABLE_CONTACTS, null, null, null, null, null, "name");

			if (c != null && c.getCount() > 0) {

				for (int i = 0; i < c.getCount(); i++) {

					c.moveToPosition(i);

					ContactData data = new ContactData();
					data.setId(c.getInt(0));
					data.setPhone(c.getString(1));
					data.setName(c.getString(2));
					data.setUserType(c.getInt(3));
					data.setPhoto(c.getString(4));
					data.setRawContatId(c.getInt(5));
					data.setBio(c.getString(6));

					list.add(data);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in getContactList", e.toString());
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return list;
	}

	public ContactData getContact(int rawContactId) {

		Cursor c = null;

		try {

			c = db.query(TABLE_CONTACTS, null, "raw_contact_id = ?", new String[]{String.valueOf(rawContactId)}, null, null, null);

			if (c != null && c.getCount() > 0) {

				c.moveToFirst();

				ContactData data = new ContactData();
				data.setId(c.getInt(0));
				data.setPhone(c.getString(1));
				data.setName(c.getString(2));
				data.setUserType(c.getInt(3));
				data.setPhoto(c.getString(4));
				data.setRawContatId(c.getInt(5));
				data.setBio(c.getString(6));

				return data;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in getContact", e.toString());
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return null;
	}

	public boolean isContactExistInDB(int rawContactId, String name, String phoneNo) {

		Cursor c = null;

		try {

			c = db.query(TABLE_CONTACTS, null, "raw_contact_id = ? AND name = ? AND (phone_no = ? OR phone_no LIKE ?)", new String[]{String.valueOf(rawContactId), name, phoneNo, "%" + phoneNo}, null, null, null);

			if (c != null && c.getCount() > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in isContactExistInDB", e.toString());
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return false;
	}

	public boolean isContactExistInDB() {

		Cursor c = null;

		try {

			c = db.query(TABLE_CONTACTS, null, null, null, null, null, null);

			if (c != null && c.getCount() > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in getContactList", e.toString());
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return false;
	}

	public int getDeletedContactRawId(String allRawContactIds) {

		Cursor c = null;

		try {

			c = db.query(TABLE_CONTACTS, new String[]{"raw_contact_id"}, "raw_contact_id NOT IN (" + allRawContactIds + ")", null, null, null, null);

			if (c != null && c.getCount() > 0) {

				c.moveToFirst();

				return c.getInt(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in getDeletedContactRawId", e.toString());
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return -1;
	}

	public void deleteContact(int rawContactId) {

		try {

			db.delete(TABLE_CONTACTS, "raw_contact_id = ?", new String[]{String.valueOf(rawContactId)});

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in deleteContact", e.toString());
		}
	}

	// public ArrayList<BillData> searchByBillNo(String billNo) {
	//
	// Cursor c = null;
	// ArrayList<BillData> list = new ArrayList<BillData>();
	//
	// try {
	//
	// c = db.query(TABLE_BILL_DETIALS, null,
	// "bill_no = ? AND is_completed = 0", new String[] { billNo }, null, null,
	// null);
	//
	// if (c != null && c.getCount() > 0) {
	//
	// for (int i = 0; i < c.getCount(); i++) {
	//
	// c.moveToPosition(i);
	//
	// BillData data = new BillData();
	// data.setId(c.getInt(0));
	// data.setBillNo(c.getInt(1));
	// data.setName(c.getString(2));
	//
	// Date date = new Date(c.getLong(3));
	// data.setBillDate(ConstantData.DATE_FORMAT.format(date));
	//
	// data.setTailorId(c.getInt(4));
	// data.setTailorName(c.getString(5));
	//
	// data.setWorkDone(isWorkDone(c.getInt(1)));
	//
	// list.add(data);
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in searchByBillNo", e.toString());
	// } finally {
	// if (c != null) {
	// c.close();
	// }
	// }
	//
	// return list;
	// }
	//
	// public void hideBill(int billNo) {
	//
	// try {
	//
	// ContentValues contentValues = new ContentValues();
	//
	// contentValues.put("is_completed", 1);
	//
	// db.update(TABLE_BILL_DETIALS, contentValues, "bill_no = ?", new String[]
	// { String.valueOf(billNo) });
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in hideBill", e.toString());
	// }
	// }
	//
	// public int deleteBill(int billNo) {
	//
	// Cursor c = null;
	//
	// try {
	//
	// db.delete(TABLE_SADI_DETAILS, "bill_no = ?", new String[] {
	// String.valueOf(billNo) });
	// return db.delete(TABLE_BILL_DETIALS, "bill_no = ?", new String[] {
	// String.valueOf(billNo) });
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in deleteBill", e.toString());
	// } finally {
	// if (c != null) {
	// c.close();
	// }
	// }
	//
	// return 0;
	// }
	//
	// public void insertTailorData(String name, String address, String
	// phone_no, int isDefault) {
	//
	// try {
	//
	// ContentValues contentValues = new ContentValues();
	//
	// contentValues.put("name", name);
	// contentValues.put("address", address);
	// contentValues.put("phone_no", phone_no);
	// contentValues.put("is_default", isDefault);
	//
	// db.insert(TABLE_TAILOR_DETAILS, null, contentValues);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in insertTailorData", e.toString());
	// }
	// }
	//
	// public void updateTailorData(int id, String name, String address, String
	// phone_no, int isDefault) {
	//
	// try {
	//
	// ContentValues contentValues = new ContentValues();
	//
	// contentValues.put("name", name);
	// contentValues.put("address", address);
	// contentValues.put("phone_no", phone_no);
	// contentValues.put("is_default", isDefault);
	//
	// db.update(TABLE_TAILOR_DETAILS, contentValues, "id = ?", new String[] {
	// String.valueOf(id) });
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in updateTailorData", e.toString());
	// }
	// }
	//
	// public ArrayList<TailorData> getTailorList() {
	//
	// Cursor c = null;
	// ArrayList<TailorData> list = new ArrayList<TailorData>();
	//
	// try {
	//
	// c = db.query(TABLE_TAILOR_DETAILS, null, null, null, null, null, null);
	//
	// if (c != null && c.getCount() > 0) {
	//
	// for (int i = 0; i < c.getCount(); i++) {
	//
	// c.moveToPosition(i);
	//
	// TailorData data = new TailorData();
	// data.setId(c.getInt(0));
	// data.setName(c.getString(1));
	// data.setAddress(c.getString(2));
	// data.setPhoneNo(c.getString(3));
	// data.setIsDefault(c.getInt(4));
	// list.add(data);
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in getTailorList", e.toString());
	// } finally {
	// if (c != null) {
	// c.close();
	// }
	// }
	//
	// return list;
	// }
	//
	// public int deleteTailor(int id) {
	//
	// Cursor c = null;
	//
	// try {
	//
	// return db.delete(TABLE_TAILOR_DETAILS, "id = ?", new String[] {
	// String.valueOf(id) });
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in deleteTailor", e.toString());
	// } finally {
	// if (c != null) {
	// c.close();
	// }
	// }
	//
	// return 0;
	// }
	//
	// public void updateDefaultTailor(int id) {
	//
	// try {
	//
	// ContentValues contentValues = new ContentValues();
	//
	// contentValues.put("is_default", 0);
	// db.update(TABLE_TAILOR_DETAILS, contentValues, null, null);
	//
	// contentValues.put("is_default", 1);
	// db.update(TABLE_TAILOR_DETAILS, contentValues, "id = ?", new String[] {
	// String.valueOf(id) });
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in updateDefaultTailor", e.toString());
	// }
	// }
	//
	// public ArrayList<SadiData> getSadiList(int billNo) {
	//
	// Cursor c = null;
	// ArrayList<SadiData> list = new ArrayList<SadiData>();
	//
	// try {
	//
	// c = db.query(TABLE_SADI_DETAILS, null, "bill_no = ?", new String[] {
	// String.valueOf(billNo) }, null, null, null);
	//
	// if (c != null && c.getCount() > 0) {
	//
	// for (int i = 0; i < c.getCount(); i++) {
	//
	// c.moveToPosition(i);
	//
	// SadiData data = new SadiData();
	// data.setId(c.getInt(0));
	// data.setBillNo(c.getInt(1));
	//
	// if (c.getString(2) != null && !c.getString(2).equals("")) {
	// SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss",
	// Locale.getDefault());
	// Date date = format.parse(c.getString(2));
	// data.setReturnDate(ConstantData.DATE_FORMAT.format(date));
	// }
	//
	// data.setDescription(c.getString(3));
	// String imageName = c.getString(4);
	// if (!imageName.equals("")) {
	// imageName = ConstantData.LOCAL_IMAGES_PATH + imageName;
	// }
	// data.setImagePath(imageName);
	// data.setRoll(c.getInt(5));
	// data.setSadi(c.getInt(6));
	// data.setBandhani(c.getInt(7));
	// data.setPalavNet(c.getInt(8));
	// data.setBorderPalav(c.getInt(9));
	// list.add(data);
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in getSadiList", e.toString());
	// } finally {
	// if (c != null) {
	// c.close();
	// }
	// }
	//
	// return list;
	// }
	//
	// public void insertSadiData(int billNo, String returnDate, String
	// description, String imagePath, int roll, int sadi, int bandhani, int
	// palavNet, int borderPalav) {
	//
	// try {
	//
	// if (!returnDate.equals("")) {
	// Date date = ConstantData.DATE_FORMAT.parse(returnDate);
	// SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss",
	// Locale.getDefault());
	// returnDate = formater.format(date);
	// }
	//
	// if (!imagePath.equals("")) {
	// imagePath = imagePath.substring(imagePath.lastIndexOf("/") + 1);
	// }
	//
	// ContentValues contentValues = new ContentValues();
	//
	// contentValues.put("bill_no", billNo);
	// contentValues.put("return_date", returnDate);
	// contentValues.put("description", description);
	// contentValues.put("image_path", imagePath);
	// contentValues.put("roll", roll);
	// contentValues.put("sadi", sadi);
	// contentValues.put("bandhani", bandhani);
	// contentValues.put("palav_net", palavNet);
	// contentValues.put("border_palav", borderPalav);
	//
	// db.insert(TABLE_SADI_DETAILS, null, contentValues);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in insertSadiData", e.toString());
	// }
	// }
	//
	// public void updateSadiData(int id, int billNo, String returnDate, String
	// description, String imagePath, int roll, int sadi, int bandhani, int
	// palavNet, int borderPalav) {
	//
	// try {
	//
	// if (!returnDate.equals("")) {
	// Date date = ConstantData.DATE_FORMAT.parse(returnDate);
	// SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss",
	// Locale.getDefault());
	// returnDate = formater.format(date);
	// }
	//
	// if (!imagePath.equals("")) {
	// imagePath = imagePath.substring(imagePath.lastIndexOf("/") + 1);
	// }
	//
	// ContentValues contentValues = new ContentValues();
	//
	// contentValues.put("bill_no", billNo);
	// contentValues.put("return_date", returnDate);
	// contentValues.put("description", description);
	// contentValues.put("image_path", imagePath);
	// contentValues.put("roll", roll);
	// contentValues.put("sadi", sadi);
	// contentValues.put("bandhani", bandhani);
	// contentValues.put("palav_net", palavNet);
	// contentValues.put("border_palav", borderPalav);
	//
	// db.update(TABLE_SADI_DETAILS, contentValues, "id = ?", new String[] {
	// String.valueOf(id) });
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in updateSadiData", e.toString());
	// }
	// }
	//
	// public void updateReturnDate(int id, String returnDate) {
	//
	// try {
	//
	// if (!returnDate.equals("")) {
	// Date date = ConstantData.DATE_FORMAT.parse(returnDate);
	// SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss",
	// Locale.getDefault());
	// returnDate = formater.format(date);
	// }
	//
	// ContentValues contentValues = new ContentValues();
	//
	// contentValues.put("return_date", returnDate);
	//
	// db.update(TABLE_SADI_DETAILS, contentValues, "id = ?", new String[] {
	// String.valueOf(id) });
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in updateSadiData", e.toString());
	// }
	// }
	//
	// public boolean isWorkDone(int billNo) {
	//
	// Cursor c = null;
	//
	// try {
	//
	// c = db.query(TABLE_SADI_DETAILS, null,
	// "bill_no = ? AND return_date <> ''", new String[] {
	// String.valueOf(billNo) }, null, null, null);
	//
	// if (c != null && c.getCount() > 0) {
	// return true;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in getSadiList", e.toString());
	// } finally {
	// if (c != null) {
	// c.close();
	// }
	// }
	//
	// return false;
	// }
	//
	// public int deleteSadi(int id) {
	//
	// Cursor c = null;
	//
	// try {
	//
	// return db.delete(TABLE_SADI_DETAILS, "id = ?", new String[] {
	// String.valueOf(id) });
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in deleteSadi", e.toString());
	// } finally {
	// if (c != null) {
	// c.close();
	// }
	// }
	//
	// return 0;
	// }
	//
	// public ArrayList<ReportData> getReportData(String sql) {
	//
	// Cursor c = null;
	// ArrayList<ReportData> list = new ArrayList<ReportData>();
	//
	// try {
	//
	// c = db.rawQuery(sql, null);
	//
	// if (c != null && c.getCount() > 0) {
	//
	// for (int i = 0; i < c.getCount(); i++) {
	//
	// c.moveToPosition(i);
	//
	// ReportData data = new ReportData();
	// data.setBillNo(c.getInt(1));
	//
	// if (c.getString(2) != null && !c.getString(2).equals("")) {
	// SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss",
	// Locale.getDefault());
	// Date date = format.parse(c.getString(2));
	// data.setReturnDate(ConstantData.DATE_FORMAT.format(date));
	// }
	//
	// data.setDescription(c.getString(3));
	// String imageName = c.getString(4);
	// if (!imageName.equals("")) {
	// imageName = ConstantData.LOCAL_IMAGES_PATH + imageName;
	// }
	// data.setImagePath(imageName);
	// data.setRoll(c.getInt(5));
	// data.setSadi(c.getInt(6));
	// data.setBandhani(c.getInt(7));
	// data.setPalavNet(c.getInt(8));
	// data.setBorderPalav(c.getInt(9));
	//
	// if (c.getString(10) != null && !c.getString(10).equals("")) {
	// Date date = new Date(c.getLong(10));
	// data.setBillDate(ConstantData.DATE_FORMAT.format(date));
	// }
	// list.add(data);
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("Error in getSadiList", e.toString());
	// } finally {
	// if (c != null) {
	// c.close();
	// }
	// }
	//
	// return list;
	// }
}