package com.pbp.tubes.uas.richhotel.Create;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.pbp.tubes.uas.richhotel.Api.ApiClient;
import com.pbp.tubes.uas.richhotel.Api.ApiInterface;
import com.pbp.tubes.uas.richhotel.R;
import com.pbp.tubes.uas.richhotel.Response.KamarResponseObject;
import com.pbp.tubes.uas.richhotel.Response.TransaksiResponseObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateReservasiUser extends AppCompatActivity {

    TextInputEditText twNamaPemesan, twIDPemesan, twAlamatPemesan, twNamaKamar;
    AutoCompleteTextView twTglCheckIn, twTglCheckOut;
    Button btnCreate, btnCancel;

    private String sNamaKamar;
    private ProgressDialog progressDialog;
    private String idKamar, idPemesan;

    private SharedPreferences sharedPreferences;
    public static final int mode = Activity.MODE_PRIVATE;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reservasi);
        sharedPreferences = getSharedPreferences("Login", mode);
        idPemesan = sharedPreferences.getString("id", "");

        idKamar = getIntent().getStringExtra("namaKamarId");

        twNamaPemesan = findViewById(R.id.etNamaPemesan);
        twAlamatPemesan = findViewById(R.id.etAlamatPemesan);
        twIDPemesan = findViewById(R.id.etIDPemesan);
        twIDPemesan.setText(idPemesan);
        twNamaKamar = findViewById(R.id.etPilihanKamar);
        twTglCheckIn = findViewById(R.id.etTglCheckIn);
        twTglCheckOut = findViewById(R.id.etTglCheckOut);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        btnCancel = findViewById(R.id.btnCancel);
        btnCreate = findViewById(R.id.btnCreate);

        twTglCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        twTglCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog2();
            }
        });


        btnCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(twNamaPemesan.getText().toString().isEmpty()){
                    twNamaPemesan.setError("Nama Pemesan Harus Diisi");
                    twNamaPemesan.requestFocus();
                } else if(twIDPemesan.getText().toString().isEmpty()){
                    twIDPemesan.setError("ID Pemesan Harus Diisi");
                    twIDPemesan.requestFocus();
                } else if(twAlamatPemesan.getText().toString().isEmpty()){
                    twAlamatPemesan.setError("Alamat Pemesan Harus Diisi");
                    twAlamatPemesan.requestFocus();
                } else if(twNamaKamar.getText().toString().isEmpty()) {
                    twNamaKamar.setError("Nama Kamar Harus Diisi");
                    twNamaKamar.requestFocus();
                } else if(twTglCheckIn.getText().toString().isEmpty()) {
                    twTglCheckIn.setError("Tanggal Check In Harus Diisi");
                    twTglCheckIn.requestFocus();
                } else if(twTglCheckOut.getText().toString().isEmpty()) {
                    twTglCheckOut.setError("Tanggal Check Out Harus Diisi");
                    twTglCheckOut.requestFocus();
                } else {
                    createReservasi();

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadKamarById(idKamar);
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                twTglCheckIn.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showDateDialog2(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                twTglCheckOut.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void loadKamarById(String id) {
        ApiInterface apiServiceKamarId = ApiClient.getClient().create(ApiInterface.class);
        Call<KamarResponseObject> getKamar = apiServiceKamarId.getKamarById(id, "data");

        getKamar.enqueue(new Callback<KamarResponseObject>() {
            @Override
            public void onResponse(Call<KamarResponseObject> call, Response<KamarResponseObject> response) {
                sNamaKamar = response.body().getKamar().getNama_kamar();
                twNamaKamar.setText(sNamaKamar);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<KamarResponseObject> call, Throwable t) {
                Toast.makeText(CreateReservasiUser.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void createReservasi() {
        ApiInterface apiServiceCreateReservasi = ApiClient.getClient().create(ApiInterface.class);
        Call<TransaksiResponseObject> addReservasi = apiServiceCreateReservasi.createTransaksi(twNamaPemesan.getText().toString(), twIDPemesan.getText().toString(),
                                                                                        twAlamatPemesan.getText().toString(), twNamaKamar.getText().toString(),
                                                                                        twTglCheckIn.getText().toString(), twTglCheckOut.getText().toString());

        addReservasi.enqueue(new Callback<TransaksiResponseObject>() {
            @Override
            public void onResponse(Call<TransaksiResponseObject> call, Response<TransaksiResponseObject> response) {
                try {
                    Toast.makeText(CreateReservasiUser.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }catch (Exception e){
                    Toast.makeText(CreateReservasiUser.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                    return;
                }

//                Log.i("createReservasi", "msg: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response));
//                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
//                startActivity(intent);
            }

            @Override
            public void onFailure(Call<TransaksiResponseObject> call, Throwable t) {
                Toast.makeText(CreateReservasiUser.this, t.getMessage(), Toast.LENGTH_LONG).show();

                Log.i("response", "msg: " +t.getMessage());
            }
        });
    }
}
