package com.example.nguyenhuy.googlemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class Main3Activity_ThongTinCaNhan extends AppCompatActivity {

    Button btn_chonAnh, btn_luu,btn_khoiPhuc;
    EditText edt_ten;
    ImageView img_anhDaiDien;
    FirebaseStorage storage = FirebaseStorage.getInstance();
   static String nameImg;
    final StorageReference storageRef = storage.getReference();
    boolean luu=false;
   static String linkAnhDaChon;
   // linkAnhDaChon[0]= "a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3__thong_tin_ca_nhan);

        btn_chonAnh= (Button) findViewById(R.id.btn_chonAnh);
        btn_luu= (Button) findViewById(R.id.btn_luu);
        btn_khoiPhuc= (Button) findViewById(R.id.btn_khoiPhuc);


        edt_ten=(EditText) findViewById(R.id.edt_ten);
        img_anhDaiDien =(ImageView)findViewById(R.id.img_anhDaiDien);

        edt_ten.setText(MapsActivity.sharedPreferences.getString("ten","?"));
        Picasso.get().load(MapsActivity.sharedPreferences.getString("linkAnh","https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d")).into(img_anhDaiDien);
        nameImg="a";
        linkAnhDaChon="a";
        btn_chonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

                SharedPreferences.Editor editor3 = MapsActivity.sharedPreferences.edit();
                editor3.putInt("anhmoi", 1);
                editor3.commit();

            }


        });

        btn_luu.setOnClickListener(new View.OnClickListener() {   // up hinh lên fb,, nhưng chưa lấy dc link..
            @Override
            public void onClick(View view) {
                luu=true;
                final ProgressDialog dialog;
                dialog = new ProgressDialog(Main3Activity_ThongTinCaNhan.this);
                dialog.setMessage("      Đang lưu...");
                dialog.setCancelable(false);
                dialog.show();
                Calendar calendar = Calendar.getInstance();
                nameImg="img"+calendar.getTimeInMillis()+".png";

                StorageReference mountainsRef = storageRef.child(nameImg);
                img_anhDaiDien.setDrawingCacheEnabled(true);
                img_anhDaiDien.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) img_anhDaiDien.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(Main3Activity_ThongTinCaNhan.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Main3Activity_ThongTinCaNhan.this, "Đã lưu!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    }

                });

            }
        });

        btn_khoiPhuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor3 = MapsActivity.sharedPreferences.edit();
                editor3.putInt("anhmoi", 0);
                editor3.commit();
              try {
                  storageRef.child(edt_ten.getText().toString().substring(0,1).toUpperCase()+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          Picasso.get().load(uri).into(img_anhDaiDien);

                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception exception) {
                          Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d").into(img_anhDaiDien);

                      }
                  });
              }catch (Exception e){
                  Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d").into(img_anhDaiDien);
              }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = Main3Activity_ThongTinCaNhan.this.getContentResolver().openInputStream(data.getData());
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                img_anhDaiDien.setImageBitmap(bitmap);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
  //      Log.e("ondestroy", "" + MapsActivity.sharedPreferences.getInt("anhmoi", 0) + "..." + nameImg + "..." + edt_ten.getText().toString().substring(0, 1).toUpperCase() + ".png");
     if(luu==true){
        try {
            if (MapsActivity.sharedPreferences.getInt("anhmoi", 0) == 1) {  // anh tu thu vien
                storageRef.child(nameImg).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        SharedPreferences.Editor editor = MapsActivity.sharedPreferences.edit();
                        editor.putString("linkAnh", uri.toString());
                        editor.commit();
                        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(MapsActivity.sharedPreferences.getInt("id", 0))).child("linkAnh").setValue(MapsActivity.sharedPreferences.getString("linkAnh", "https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        linkAnhDaChon = "https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d";
                        SharedPreferences.Editor editor = MapsActivity.sharedPreferences.edit();
                        editor.putString("linkAnh", linkAnhDaChon);
                        editor.commit();
                        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(MapsActivity.sharedPreferences.getInt("id", 0))).child("linkAnh").setValue(MapsActivity.sharedPreferences.getString("linkAnh", "https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d"));

                    }
                });
            } else {
                Log.e("log", ".....zo else");// ảnh theo tên
                storageRef.child(edt_ten.getText().toString().substring(0, 1).toUpperCase() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {              // do lay cham,, bỏ phía sau code phía sau sẽ chạy trước..
                        SharedPreferences.Editor editor = MapsActivity.sharedPreferences.edit();
                        editor.putString("linkAnh", uri.toString());
                        editor.commit();
                        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(MapsActivity.sharedPreferences.getInt("id", 0))).child("linkAnh").setValue(MapsActivity.sharedPreferences.getString("linkAnh", "https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d"));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //    Log.e("log", "lấy thất bại");
                        linkAnhDaChon = "https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d";
                        SharedPreferences.Editor editor = MapsActivity.sharedPreferences.edit();
                        editor.putString("linkAnh", linkAnhDaChon);
                        editor.commit();
                        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(MapsActivity.sharedPreferences.getInt("id", 0))).child("linkAnh").setValue(MapsActivity.sharedPreferences.getString("linkAnh", "https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d"));


                    }
                });
            }
        } catch (Exception e) {         //do lỗi nhập tên ko có trong mấy ảnh có sẵn
            Log.e("loiloi", "" + e);
            linkAnhDaChon = "https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d";
            SharedPreferences.Editor editor = MapsActivity.sharedPreferences.edit();
            editor.putString("linkAnh", linkAnhDaChon);
            editor.commit();
            MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(MapsActivity.sharedPreferences.getInt("id", 0))).child("linkAnh").setValue(MapsActivity.sharedPreferences.getString("linkAnh", "https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d"));

        }
        // lay link xong
        Log.e("linkkkkkkkk...", "" + linkAnhDaChon);
        SharedPreferences.Editor editor3 = MapsActivity.sharedPreferences.edit();
        editor3.putString("ten", edt_ten.getText().toString());
        editor3.commit();
        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(MapsActivity.sharedPreferences.getInt("id", 0))).child("ten").setValue(MapsActivity.sharedPreferences.getString("ten", "?"));

    }
    }
}
