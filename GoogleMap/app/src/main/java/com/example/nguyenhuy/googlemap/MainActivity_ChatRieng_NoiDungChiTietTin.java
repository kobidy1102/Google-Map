package com.example.nguyenhuy.googlemap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity_ChatRieng_NoiDungChiTietTin extends AppCompatActivity {
    TextView textView;
    View view;
    public static ListView lv_chat;
    public EditText edt_noidungchat;
    public Button btn_gui;
    public static Adapter_Lv_Chat adapter;
    int loadThem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__chat_rieng__noi_dung_chi_tiet_tin);
        loadThem=0;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv_chat= (ListView) findViewById(R.id.lv_chatRieng_chiTiet_chat);
        edt_noidungchat= (EditText) findViewById(R.id.edt_chatRieng_chiTiet_noidungchat);
        btn_gui= (Button) findViewById(R.id.btn_chatRieng_chiTiet_gui);
        final ArrayAdapter adapter;
        adapter= new Adapter_Lv_ChatRieng_ChiTietTin(MainActivity_ChatRieng_NoiDungChiTietTin.this,android.R.layout.simple_list_item_1,ManHinhChatRieng.arr3);
        lv_chat.setAdapter(adapter);


        lv_chat.setAdapter(adapter);

        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_noidungchat.getText().toString().equals("")==false) {

                    final ThongTinTinNhan_ChatRieng t = new ThongTinTinNhan_ChatRieng(MapsActivity.sharedPreferences.getInt("id",0),ManHinhChatRieng.idClick,edt_noidungchat.getText().toString());
                    final boolean[] flag = {false};
                    MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if(flag[0] ==false) {
                                int sl = Integer.parseInt(dataSnapshot.child("SoLuongTinNhan").getValue().toString());
                                MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").child(String.valueOf(sl + 1)).setValue(t);
                                MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").child("SoLuongTinNhan").setValue(sl+1);
                           //     Toast.makeText(MainActivity_ChatRieng_NoiDungChiTietTin.this, "Đã gửi", Toast.LENGTH_SHORT).show();
                                  edt_noidungchat.setText("");
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


        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
                lv_chat.setSelection(ManHinhChatRieng.arr3.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home==item.getItemId()){
            Intent intent= new Intent(MainActivity_ChatRieng_NoiDungChiTietTin.this,Activity_Chat.class);
            Activity_Chat.startFragment=1;          //setFlagmant 2.. la cao hien tai
            startActivity(intent);
            finish();
            Log.e("zoo","zoooooooooooooo");
        }
        return super.onOptionsItemSelected(item);
    }
}
