package com.example.nguyenhuy.googlemap;

import android.app.Activity;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManHinhThongTinNhom extends Fragment {
    View view;
    TextView tv_maNhom;
   static  ListView lv_thanhVien;
    static ArrayList<ThongTinThanhVien> arr= new ArrayList<ThongTinThanhVien>();
    static public Adapter_ThongTinNhom adapter;
      boolean[] flag2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.man_hinh_thong_tin_nhom,container,false);

        tv_maNhom = (TextView) view.findViewById(R.id.tv_maNhom);
       // tv_soLuongThanhVien = (TextView) view.findViewById(R.id.tv_soLuongThanhVien);
        lv_thanhVien =(ListView) view.findViewById(R.id.lv_thanhVien);
        adapter= new Adapter_ThongTinNhom(getContext(),android.R.layout.simple_list_item_1,arr);
        lv_thanhVien.setAdapter(adapter);
        tv_maNhom.setText(MapsActivity.sharedPreferences.getString("maNhom","a"));
       flag2 = new boolean[]{false};

        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom","a")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arr.clear();
                if(flag2[0] ==false) {
                  //  tv_soLuongThanhVien.setText(dataSnapshot.child("SoLuongThanhVien").getValue().toString());

                    int sltv = Integer.parseInt(dataSnapshot.child("SoLuongThanhVien").getValue().toString());
                    for (int i = 1; i <= sltv; i++) {
                        try {
                            String ten = dataSnapshot.child(String.valueOf(i)).child("ten").getValue().toString();
                            String linkAnh = dataSnapshot.child(String.valueOf(i)).child("linkAnh").getValue().toString();
                            String stt = dataSnapshot.child(String.valueOf(i)).child("stt").getValue().toString();

                            arr.add(new ThongTinThanhVien(ten, 0, 0, Integer.parseInt(stt), linkAnh));
                            Log.e("xxxxxx",""+ten);

                          //  lv_thanhVien.setSelectionAfterHeaderView();
                          //  lv_thanhVien.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            lv_thanhVien.requestLayout();
                        } catch (Exception e) {
                        }

                    }
                    flag2[0] =true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
      //  lv_thanhVien.setAdapter(adapter);

        lv_thanhVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {




                final Dialog dialog3 = new Dialog(getContext());
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






                        final ThongTinTinNhan_ChatRieng t = new ThongTinTinNhan_ChatRieng(MapsActivity.sharedPreferences.getInt("id",0),arr.get(i).stt,edt_nhapTin.getText().toString());
                        final boolean[] flag = {false};
                        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(flag[0] ==false) {
                                    int sl = Integer.parseInt(dataSnapshot.child("SoLuongTinNhan").getValue().toString());
                                    MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").child(String.valueOf(sl + 1)).setValue(t);
                                    MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").child("SoLuongTinNhan").setValue(sl+1);
                                    Toast.makeText(getContext(), "Đã gửi", Toast.LENGTH_SHORT).show();
                           //         Activity_Chat.chat.finish();
                                   dialog3.dismiss();

                                   flag[0] =true;
                                   Intent intent= new Intent(getContext(),Activity_Chat.class);
                                   Activity_Chat.startFragment=2;          //setFlagmant 2.. la cao hien tai
                                   startActivity(intent);
                                    Activity_Chat.activity.finish();


                               }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
                if(arr.get(i).stt != MapsActivity.sharedPreferences.getInt("id",0))
                dialog3.show();
            }
        });

        return  view;
    }
}
