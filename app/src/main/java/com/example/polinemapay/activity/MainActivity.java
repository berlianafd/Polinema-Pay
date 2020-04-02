package com.example.polinemapay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.polinemapay.R;
import com.example.polinemapay.app.AppConfig;
import com.example.polinemapay.app.AppController;
import com.example.polinemapay.helper.SQLiteHandler;
import com.example.polinemapay.helper.SessionManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
//	private ProgressDialog pDialog;

	private Toolbar toolbar;
	private NavigationView navigationView;
	private DrawerLayout drawerLayout;
	private CardView jemput, tukarSampah, tukarPoinUser, generate, tukarPoinMerchant, pesanan, tugas, kertas, plastik;

	private TextView txtName, ttlPoin, ttlBeratSampah, ttlSampahKertas, ttlSampahPlastik, label, labelttl, labelkg, hargaKrts, hargaPlstk;
	public String idUser, HargaKertas, HargaPlastik;
	private SQLiteHandler db;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtName = (TextView) findViewById(R.id.namaHome);
		ttlPoin = (TextView) findViewById(R.id.totalPoin);
		ttlBeratSampah = (TextView) findViewById(R.id.totalBeratSampah);
		ttlSampahKertas = (TextView) findViewById(R.id.totalBeratKertas);
		ttlSampahPlastik = (TextView) findViewById(R.id.totalBeratPlastik);
		label = (TextView) findViewById(R.id.labelSampah);
		labelttl = (TextView) findViewById(R.id.labelTotalSampah);
		labelkg = (TextView) findViewById(R.id.labelKilogram);
		hargaKrts = (TextView) findViewById(R.id.hargaKertas);
		hargaPlstk = (TextView) findViewById(R.id.hargaPlastik);

		jemput = (CardView) findViewById(R.id.jemputButton);
		tukarSampah = (CardView) findViewById(R.id.tukarSampahButton);
		tukarPoinUser = (CardView) findViewById(R.id.tukarPoinUserButton);
		generate = (CardView) findViewById(R.id.generateButton);
		tukarPoinMerchant = (CardView) findViewById(R.id.tukarPoinMerchantButton);
		pesanan = (CardView) findViewById(R.id.pesananButton);
		tugas = (CardView) findViewById(R.id.tugasButton);
		kertas = (CardView) findViewById(R.id.cardViewKertas);
		plastik = (CardView) findViewById(R.id.cardViewPlastik);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		// Fetching user details from SQLite
		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("name");
		String level = user.get("level");
		String nohp = user.get("nohp");


		// Displaying the user details on the screen
		txtName.setText("Hai! "+ name);

		checkUserId(nohp, name);
		checkHargaKertas("1");
		checkHargaPlastik("2");

		Toast.makeText(getApplicationContext(), idUser, Toast.LENGTH_LONG).show();

		if(level.equals("User")){
			jemput.setVisibility(View.VISIBLE);
			tukarSampah.setVisibility(View.VISIBLE);
			tukarPoinUser.setVisibility(View.VISIBLE);
			generate.setVisibility(View.GONE);
			tukarPoinMerchant.setVisibility(View.GONE);
			pesanan.setVisibility(View.GONE);
			tugas.setVisibility(View.GONE);
		} else if (level.equals("Sukarelawan")){
			jemput.setVisibility(View.GONE);
			tukarSampah.setVisibility(View.GONE);
			tukarPoinUser.setVisibility(View.GONE);
			generate.setVisibility(View.GONE);
			tukarPoinMerchant.setVisibility(View.GONE);
			pesanan.setVisibility(View.VISIBLE);
			tugas.setVisibility(View.VISIBLE);
		} else {
			jemput.setVisibility(View.GONE);
			tukarSampah.setVisibility(View.GONE);
			tukarPoinUser.setVisibility(View.GONE);
			generate.setVisibility(View.VISIBLE);
			tukarPoinMerchant.setVisibility(View.VISIBLE);
			pesanan.setVisibility(View.GONE);
			tugas.setVisibility(View.GONE);
			label.setText("Statistik Penghasilanmu!");
			labelttl.setText("Total Poin hari ini:");
			labelkg.setText("Poin");
			kertas.setVisibility(View.GONE);
			plastik.setVisibility(View.GONE);
		}

		// Menginisiasi Toolbar dan mensetting sebagai actionbar
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		// Menginisiasi  NavigationView
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		//Mengatur Navigasi View Item yang akan dipanggil untuk menangani item klik menu navigasi
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			// This method will trigger on item Click of navigation menu
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				//Memeriksa apakah item tersebut dalam keadaan dicek  atau tidak,
				if(menuItem.isChecked()) menuItem.setChecked(false);
				else menuItem.setChecked(true);
				//Menutup  drawer item klik
				drawerLayout.closeDrawers();
				//Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
				switch (menuItem.getItemId()){
					// pilihan menu item navigasi akan menampilkan pesan toast klik kalian bisa menggantinya
					//dengan intent activity
					case R.id.navigation1:
						Intent intent = new Intent(MainActivity.this, MainActivity.class);
						startActivity(intent);
						return true;
					case R.id.navigation2:
						Intent intent1 = new Intent(MainActivity.this, RiwayatActivity.class);
						intent1.putExtra("idUser", idUser);
						startActivity(intent1);
						return true;
					case R.id.navigation3:
						Intent intent2 = new Intent(MainActivity.this, ProfilActivity.class);
						intent2.putExtra("idUser", idUser);
						startActivity(intent2);
						return true;
					case R.id.navigation4:
						logoutUser();
						return true;
					default:
						Toast.makeText(getApplicationContext(),"Kesalahan Terjadi ", Toast.LENGTH_SHORT).show();
						return true;
				}
			}
		});
		// Menginisasi Drawer Layout dan ActionBarToggle
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){
			@Override
			public void onDrawerClosed(View drawerView) {
				// Kode di sini akan merespons setelah drawer menutup disini kita biarkan kosong
				super.onDrawerClosed(drawerView);
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				//  Kode di sini akan merespons setelah drawer terbuka disini kita biarkan kosong
				super.onDrawerOpened(drawerView);
			}
		};
		//Mensetting actionbarToggle untuk drawer layout
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		//memanggil synstate
		actionBarDrawerToggle.syncState();

	}

	/**
	 * function to check points profile details in mysql db
	 * */
	private void checkPoints(final String idUser, final String nohp) {
		// Tag used to cancel the request
		String tag_string_req = "req_checkPoints";

//		pDialog.setMessage("Checkin in ...");
//		showDialog();

		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_CEKPOINUSER, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, "Check Points Response: " + response.toString());
//				hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {
						JSONObject user = jObj.getJSONObject("user");
						String totalBeratSampah = user.getString("totalBeratSampah");
						String totalBeratKertas = user.getString("totalBeratKertas");
						String totalBeratPlastik = user.getString("totalBeratPlastik");
						String totalPoin = user.getString("totalPoin");

						ttlBeratSampah.setText(totalBeratSampah);
						ttlSampahKertas.setText(totalBeratKertas);
						ttlSampahPlastik.setText(totalBeratPlastik);
						ttlPoin.setText(totalPoin);
					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(),
								errorMsg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Get Data Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_LONG).show();
//				hideDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("idUser", idUser);
				params.put("nohp", nohp);

				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	/**
	 * function to check id profile details in mysql db
	 * */
	private void checkUserId(final String nohp, final String name) {
		// Tag used to cancel the request
		String tag_string_req = "req_checkIdUser";

//		pDialog.setMessage("Checkin in ...");
//		showDialog();

		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_CEKIDUSER, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, "Check Id Response: " + response.toString());
//				hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {
						JSONObject user = jObj.getJSONObject("user");
						idUser = user.getString("id");
						Log.e(TAG, "Get Id : " + idUser);

						checkPoints(idUser, nohp);

					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(),
								errorMsg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Get Id Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_LONG).show();
//				hideDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("nohp", nohp);
				params.put("name", name);

				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	private void checkHargaKertas(final String id) {
		// Tag used to cancel the request
		String tag_string_req = "req_checkHarga";

		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_CEKHARGAKERTAS, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, "Check Harga Response: " + response.toString());
//				hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {
						JSONObject user = jObj.getJSONObject("user");
						HargaKertas = user.getString("harga");
						String hargakg =  Double.toString(Double.parseDouble(HargaKertas) * 1000);
						hargaKrts.setText(hargakg+"/Kg");
					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(),
								errorMsg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Get Data Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_LONG).show();
//				hideDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", id);
				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	private void checkHargaPlastik(final String id) {
		// Tag used to cancel the request
		String tag_string_req = "req_checkHarga";

		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_CEKHARGAPLASTIK, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, "Check Harga Response: " + response.toString());
//				hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");

					// Check for error node in json
					if (!error) {
						JSONObject user = jObj.getJSONObject("user");
						HargaPlastik = user.getString("harga");
						String hargakg =  Double.toString(Double.parseDouble(HargaPlastik) * 1000);
						hargaPlstk.setText(hargakg+"/Kg");

					} else {
						// Error in login. Get the error message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(),
								errorMsg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Get Data Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_LONG).show();
//				hideDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", id);
				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	public void scanUser(View view) {
		Intent intent = new Intent(MainActivity.this, ScannActivity.class);
		intent.putExtra("idUser", idUser);
		intent.putExtra("hargakertas", HargaKertas);
		intent.putExtra("hargaplastik", HargaPlastik);
		startActivity(intent);
	}

	public void tukarPoinUser(View view) {
		Intent intent = new Intent(MainActivity.this, TukarpoinActivity.class);
		intent.putExtra("idUser", idUser);
		startActivity(intent);
	}

	public void generateQrCode(View view) {
		Intent intent = new Intent(MainActivity.this, GenerateqrActivity.class);
		intent.putExtra("idUser", idUser);
		startActivity(intent);
	}

	public void jemputSampahUser(View view) {
		Intent intent = new Intent(MainActivity.this, JemputActivity.class);
		intent.putExtra("idUser", idUser);
		startActivity(intent);
	}

	public void pesananJemputSampah(View view) {
		Intent intent = new Intent(MainActivity.this, PesananActivity.class);
		intent.putExtra("idUser", idUser);
		startActivity(intent);
	}

	public void tugasJemputSampah(View view) {
		Intent intent = new Intent(MainActivity.this, TugasActivity.class);
		intent.putExtra("idUser", idUser);
		startActivity(intent);
	}
}
