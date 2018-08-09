package com.example.nguyenhuy.googlemap;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static GoogleMap mMap;

    public static double latitude = 0;
    public static double longitude = 0;
    public static DatabaseReference mDatabase;
    public static SharedPreferences sharedPreferences;
    public static String thanhVien = "0";
    public static Intent intentSevice;
    public Button btnMenu;
    public Marker marker;
    ProgressDialog fdialog;
    static boolean chiDuong=false; // khi bat chi duong thi sẽ ko clear marker
    private int MY_PERMISSIONS_REQUEST_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedPreferences = getSharedPreferences("idThanhVien", MODE_PRIVATE);  //id luu stt thanh vien trong nhom,,,, tenNhom, maNhom
        btnMenu= (Button) findViewById(R.id.btnMenu);
        mDatabase = FirebaseDatabase.getInstance().getReference();

      //  ActivityCompat.requestPermissions(this,new String[]{com.example.nguyenhuy.googlemap.Manifest.permission}, 1);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        mDatabase.child("ReSetData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("<><><><><><>..",sharedPreferences.getString("tenNhom", "b")+" So Luong Thanh Vien Thay doi");

                mDatabase.child(sharedPreferences.getString("tenNhom", "b")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                        int s=Integer.parseInt(dataSnapshot.child("SoLuongThanhVien").getValue().toString());

                        }catch (Exception e){
                            if(chiDuong==false) {
                                try {
                                    mMap.clear();
                                }catch (Exception e2){}
                            }
                        }

                     if(sharedPreferences.getString("tenNhom", "b").equals("b")==false) {
                    String s = dataSnapshot.child("SoLuongThanhVien").getValue().toString();
                    int SoluongThanhVien = Integer.parseInt(s);
  //////                //  mMap.clear();

                         if(chiDuong==false) {
                          try {
                              mMap.clear();
                          }catch (Exception e){}
                         }
                    if(SoluongThanhVien>0){
                        for (int i = 1; i <= SoluongThanhVien; i++) {
                            try {
                                ThongTinThanhVien a = new ThongTinThanhVien();
                                //  Log.e("loi cc j the:..",sharedPreferences.getString("tenNhom", "b")+".."+String.valueOf(i)+"..");
                                String vido = dataSnapshot.child(String.valueOf(i)).child("viDo").getValue().toString();  /////loiiiiiiiiiiiiii
                                String kinhdo = dataSnapshot.child(String.valueOf(i)).child("kinhDo").getValue().toString();
                                String ten = dataSnapshot.child(String.valueOf(i)).child("stt").getValue().toString()+": "+dataSnapshot.child(String.valueOf(i)).child("ten").getValue().toString();
                                String linkAnh = dataSnapshot.child(String.valueOf(i)).child("linkAnh").getValue().toString();



                                if (i != sharedPreferences.getInt("id", 0)) {
                                    Bitmap bmp = Ion.with(MapsActivity.this)
                                            .load(linkAnh).asBitmap().get();
                                    Log.e(".....>>>>", "i=" + i + "--" + sharedPreferences.getInt("id", 0));
                                    marker=mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(vido),
                                            Double.parseDouble(kinhdo))).title(ten) .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bmp, 45, 45,false))));
                                    mMap.setOnMarkerClickListener(MapsActivity.this);
                                  //  Log.e("exex", " đã add marker");



                                }
                            }catch (Exception e){
                              //  Log.e("exex",""+e);
                            }
                    }
                    }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        if (sharedPreferences.getString("maNhom", "a").equals("a") == false) {
//        intentSevice = new Intent(this, MyService.class);
//        startService(intentSevice);
//        }

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MapsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_menu);

                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.TOP;

                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                final TextView tv_taoNhom= (TextView) dialog.findViewById(R.id.tv_taoNhom);
                final TextView tv_thamGiaNhom= (TextView) dialog.findViewById(R.id.tv_thamGiaNhom);
                final TextView tv_chatNhom= (TextView) dialog.findViewById(R.id.tv_chatNhom);
                final TextView tv_thoatNhom= (TextView) dialog.findViewById(R.id.tv_thoatNhom);
                final TextView tv_ngungGuiViTri= (TextView) dialog.findViewById(R.id.tv_ngungGuiViTri);
                final TextView tv_batGuiViTri= (TextView) dialog.findViewById(R.id.tv_batGuiViTri);

                final TextView tv_thongTinCaNhan= (TextView) dialog.findViewById(R.id.tv_thongTinCaNhan);

                tv_taoNhom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sharedPreferences.getString("maNhom", "a").equals("a") == true) {

                            intentSevice = new Intent(MapsActivity.this, MyService.class);
                            startService(intentSevice);

                            final String AB = "0123456789abcdefghijklmnopqrstuwxyz";//ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                            Random rnd = new Random();
                            StringBuilder sb = new StringBuilder(5);
                            for (int i = 0; i < 5; i++)
                                sb.append(AB.charAt(rnd.nextInt(AB.length())));
                            final String maNhom = sb.toString();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("maNhom", maNhom);
                            editor.commit();

                            Toast.makeText(MapsActivity.this, "Tạo nhóm thành công \nMã nhóm bạn là: "+maNhom, Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editorId = sharedPreferences.edit();
                            editorId.putInt("id", 1);
                            editorId.commit();
                            final boolean[] flagTaoNhom = {false};
                            mDatabase.child("SoLuongNhom").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (flagTaoNhom[0] == false) {
                                        String soluongnhom = dataSnapshot.getValue().toString();
                                        int soluongnhom2=Integer.parseInt(soluongnhom) + 1;

                                        SharedPreferences.Editor editorTenNhom = sharedPreferences.edit();
                                        editorTenNhom.putString("tenNhom", "Nhom"+String.valueOf(soluongnhom2));
                                        editorTenNhom.commit();
                                    //    Log.e("tao nhom:...",sharedPreferences.getString("tenNhom", "b"));

                                        mDatabase.child("SoLuongNhom").setValue(soluongnhom2);
                                        flagTaoNhom[0] = true;

                                        mDatabase.child("Nhom"+String.valueOf(soluongnhom2));
                                        mDatabase.child("Nhom"+String.valueOf(soluongnhom2)).child("SoLuongThanhVien").setValue(1);
                                        ThongTinThanhVien t = new ThongTinThanhVien(sharedPreferences.getString("ten","?"),latitude,longitude,1,sharedPreferences.getString("linkAnh","https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d"));
                                        mDatabase.child("Nhom"+String.valueOf(soluongnhom2)).child("1").setValue(t);
                                        mDatabase.child("Nhom"+String.valueOf(soluongnhom2)).child("MaNhom").setValue(maNhom);
                                        mDatabase.child("Nhom"+String.valueOf(soluongnhom2)).child("ChatRieng").child("SoLuongTinNhan").setValue(0);
                                        mDatabase.child("Nhom"+String.valueOf(soluongnhom2)).child("Chat").child("SoLuongTinNhan").setValue(0);


                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            dialog.dismiss();
                            final boolean[] flag = {false};
                            mDatabase.child("ReSetData").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(flag[0] ==false) {
                                        int m = Integer.parseInt(dataSnapshot.getValue().toString());
                                        mDatabase.child("ReSetData").setValue(m + 1);
                                        flag[0] =true;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else {
                            Toast.makeText(MapsActivity.this, "Bạn đang là thành viên của một nhóm \nMã nhóm bạn là: "+sharedPreferences.getString("maNhom", "a"), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }


                    }

                });

                tv_thamGiaNhom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sharedPreferences.getString("maNhom", "a").equals("a") == true) {



                            final Dialog dialog2 = new Dialog(MapsActivity.this);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog2.setContentView(R.layout.dialog_menu_thamgianhom);
                            final Button btn_kiemTra = (Button) dialog2.findViewById(R.id.btn_kiemTra);
                            final Button btn_huy = (Button) dialog2.findViewById(R.id.btn_huy);
                            final EditText edt_maNhom = (EditText) dialog2.findViewById(R.id.edt_maNhom);
                            btn_huy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog2.dismiss();
                                    dialog.dismiss();
                                }
                            });
                            btn_kiemTra.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String maDaNhap = edt_maNhom.getText().toString();
                                    final Boolean[] flag = {false};

                                        mDatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if (flag[0] == false) {
                                                        String t = dataSnapshot.child("SoLuongNhom").getValue().toString();
                                                        int t2 = Integer.parseInt(t);
                                                        for (int i = 1; i <= t2; i++) {
                                                            try {
                                                                String t3 = dataSnapshot.child("Nhom" + String.valueOf(i)).child("MaNhom").getValue().toString();
                                                                if (maDaNhap.equals(t3) == true) {

                                                                    intentSevice = new Intent(MapsActivity.this, MyService.class);
                                                                    startService(intentSevice);

                                                                    Toast.makeText(MapsActivity.this, "Bạn đã tham gia nhóm", Toast.LENGTH_SHORT).show();
                                                                    String t4 = dataSnapshot.child("Nhom" + String.valueOf(i)).child("SoLuongThanhVien").getValue().toString();
                                                                    int t5 = Integer.parseInt(t4) + 1;

                                                                    SharedPreferences.Editor editorTenNhom = sharedPreferences.edit();
                                                                    editorTenNhom.putString("tenNhom", "Nhom" + String.valueOf(i));
                                                                    editorTenNhom.commit();

                                                                    SharedPreferences.Editor editorId = sharedPreferences.edit();
                                                                    editorId.putInt("id", t5);
                                                                    editorId.commit();

                                                                    ThongTinThanhVien t6 = new ThongTinThanhVien(sharedPreferences.getString("ten", "?"), latitude, longitude, t5, sharedPreferences.getString("linkAnh", "https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d"));
                                                                    mDatabase.child("Nhom" + String.valueOf(i)).child(String.valueOf(t5)).setValue(t6);

                                                                    mDatabase.child("Nhom" + String.valueOf(i)).child("SoLuongThanhVien").setValue(t5);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putString("maNhom", maDaNhap);
                                                                    editor.commit();
                                                                 //   Log.e("idid khi tham gia", "" + sharedPreferences.getInt("id", 0));

                                                                    dialog2.dismiss();
                                                                    dialog.dismiss();
                                                                    final boolean[] flag = {false};
                                                                    mDatabase.child("ReSetData").addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            if (flag[0] == false) {
                                                                                int m = Integer.parseInt(dataSnapshot.getValue().toString());
                                                                                mDatabase.child("ReSetData").setValue(m + 1);
                                                                                flag[0] = true;
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                    break;
                                                                } else if (i == t2) {
                                                                    Toast.makeText(MapsActivity.this, "Không tìm thấy nhóm có mã trên", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }catch (Exception e){
                                                                if (i == t2)
                                                                Toast.makeText(MapsActivity.this, "Không tìm thấy nhóm có mã trên", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                        flag[0] = true;
                                                    }

                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }

                                        });

                                    //  dialog2.dismiss();
                                }
                            });

                            dialog2.show();
                        } else {
                            Toast.makeText(MapsActivity.this, "Bạn đang là thành viên của một nhóm\nMã nhóm bạn là: "+sharedPreferences.getString("maNhom", "a"), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }

                    }//
                });

                tv_thoatNhom.setOnClickListener(new View.OnClickListener() {    // xoa thanh vien,  -1 so luong thanh vien,   soluong thanh vien =0  thi xoa nhom,  -1 soluongnhom
                    @Override
                    public void onClick(View view) {
                        if (sharedPreferences.getString("maNhom", "a").equals("a") == false){



                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MapsActivity.this);
                            alertDialog.setTitle("          Xác Nhận...");
                            alertDialog.setMessage("Bạn có chắc chắn muốn thoát khỏi nhóm!");
                            alertDialog.setIcon(R.drawable.war);

                            alertDialog.setNeutralButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                    intentSevice = new Intent(MapsActivity.this, MyService.class);
                                    stopService(intentSevice);


                            Toast.makeText(MapsActivity.this, "Bạn đã thoát khỏi nhóm", Toast.LENGTH_SHORT).show();
                         mDatabase.child(sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(sharedPreferences.getInt("id", 0))).removeValue();
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            Boolean flag = false;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (flag == false) {
                                    String q1 = dataSnapshot.child(sharedPreferences.getString("tenNhom", "b")).child("SoLuongThanhVien").getValue().toString();
                                    int q2 = Integer.parseInt(q1);
                                    if (q2 == 1) {
                                        mDatabase.child(sharedPreferences.getString("tenNhom", "b")).removeValue();
                                    }
                                    SharedPreferences.Editor editor2 = sharedPreferences.edit();
                                    editor2.putString("tenNhom", "b");
                                    editor2.commit();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("maNhom", "a");
                                    editor.commit();
                                    SharedPreferences.Editor editor3 = sharedPreferences.edit();
                                    editor3.putInt("id", 0);
                                    editor3.commit();
                                    flag = true;
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                                    dialog.dismiss();

                            final boolean[] flag = {false};
                        mDatabase.child("ReSetData").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(flag[0] ==false) {
                                    int m = Integer.parseInt(dataSnapshot.getValue().toString());
                                    mDatabase.child("ReSetData").setValue(m + 1);
                                   flag[0] =true;


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                                    mMap.clear();
                            Intent intent=getIntent();
                            finish();
                            startActivity(intent);
                                    mMap.clear();
                                }
                            });
                            alertDialog.show();

                        }else {
                            Toast.makeText(MapsActivity.this, "Bạn chưa tham gia nhóm nào!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }



                   }

                });


                tv_chatNhom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(sharedPreferences.getString("maNhom","a").equals("a")==false) {
//                            Intent intent = new Intent(MapsActivity.this, Main2Activity_Chat.class);
//                            dialog.dismiss();
//                            startActivity(intent);

                            Intent intent = new Intent(MapsActivity.this, Activity_Chat.class);
                            dialog.dismiss();
                            startActivity(intent);
                        }else Toast.makeText(MapsActivity.this, "Bạn chưa tham gia nhóm nào!", Toast.LENGTH_SHORT).show();
                    }
                });



                tv_thongTinCaNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(MapsActivity.this, Main3Activity_ThongTinCaNhan.class);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                });

                tv_ngungGuiViTri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intentSevice = new Intent(MapsActivity.this, MyService.class);
                        stopService(intentSevice);
                        dialog.dismiss();
                        Toast.makeText(MapsActivity.this, "Vị trí của bạn sẽ được không cập nhật trong nhóm", Toast.LENGTH_SHORT).show();
                    }
                });
                tv_batGuiViTri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intentSevice = new Intent(MapsActivity.this, MyService.class);
                        startService(intentSevice);
                        dialog.dismiss();
                        Toast.makeText(MapsActivity.this, "Vị trí của bạn sẽ được cập nhật trong nhóm", Toast.LENGTH_SHORT).show();
                    }
                });


                dialog.show();
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13, 107.5), 6));

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                // TODO Auto-generated method stub
                latitude = arg0.getLatitude();
                longitude = arg0.getLongitude();
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });


    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

      //  Log.e("marker", ""+marker.getTitle());
        final Dialog dialog = new Dialog(MapsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_click_marker);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.RIGHT;

        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        TextView tv_chiDuong= (TextView) dialog.findViewById(R.id.tv_chiDuong);
        TextView tv_chatRieng= (TextView) dialog.findViewById(R.id.tv_chatRieng);
        if(chiDuong==true){
            //tv_chiDuong.setTextSize(13);
            tv_chiDuong.setText("Đóng chỉ đường");
        }else {
          //  tv_chiDuong.setTextSize(18);
            tv_chiDuong.setText("Chỉ đường");
        }

        tv_chiDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chiDuong==false){
                chiDuong=true;
                }else if(chiDuong==true){
                    chiDuong=false;
                }

               if(chiDuong==true) {
                   String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "&key=AIzaSyBeCkvBd6vggo4pGzNpn66mKCVaF0aqGns";
                   Log.d("onMapClick", url.toString());
                   FetchUrl FetchUrl = new FetchUrl();

                   // Start downloading json data from Google Directions API
                   FetchUrl.execute(url);

                   dialog.dismiss();
                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 8));

                   fdialog = new ProgressDialog(MapsActivity.this);
                   fdialog.setMessage("      Đang tải...");
                   fdialog.setCancelable(false);
                   fdialog.show();
               }else{
                    mMap.clear();
                    dialog.dismiss();
                    // de event cập nhật lại vị trí mấy cái marker  chạy
                   final boolean[] flag = {false};
                   mDatabase.child("ReSetData").addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if(flag[0] ==false) {
                               int m = Integer.parseInt(dataSnapshot.getValue().toString());
                               mDatabase.child("ReSetData").setValue(m + 1);
                               flag[0] =true;
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

               }
            }
        });

        tv_chatRieng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog3 = new Dialog(MapsActivity.this);
                dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog3.setContentView(R.layout.dialog_chat_rieng);
                 final EditText edt_nhapTin= (EditText) dialog3.findViewById(R.id.edt_nhapTin);
                 Button btn_dong =(Button) dialog3.findViewById(R.id.btn_dong);
                 Button btn_gui = (Button) dialog3.findViewById(R.id.btn_gui2);
                 btn_dong.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         dialog3.dismiss();
                     }
                 });
                 btn_gui.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         String laySttNguoiNhan= marker.getTitle().substring(0,1);
                         final ThongTinTinNhan_ChatRieng t = new ThongTinTinNhan_ChatRieng(sharedPreferences.getInt("id",0),Integer.parseInt(laySttNguoiNhan),edt_nhapTin.getText().toString());
                         final boolean[] flag = {false};
                         mDatabase.child(sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 if(flag[0] ==false) {
                                     int sl = Integer.parseInt(dataSnapshot.child("SoLuongTinNhan").getValue().toString());
                                     mDatabase.child(sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").child(String.valueOf(sl + 1)).setValue(t);
                                     mDatabase.child(sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").child("SoLuongTinNhan").setValue(sl+1);
                                     Toast.makeText(MapsActivity.this, "Đã gửi", Toast.LENGTH_SHORT).show();

                                     dialog3.dismiss();

                                 flag[0] =true;
                                 }
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });

                     }
                 });
                dialog3.show();

            }
        });


        dialog.show();
        return false;
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());          ///////////////////////////
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());               ////////////////////////////
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
            fdialog.dismiss();
        }
    }



}







