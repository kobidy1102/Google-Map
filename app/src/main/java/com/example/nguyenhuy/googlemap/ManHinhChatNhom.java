package com.example.nguyenhuy.googlemap;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManHinhChatNhom extends Fragment {
    TextView textView;
    View view;
    public ListView lv_chat;
    public EditText edt_noidungchat;
    public Button btn_gui;
    public ArrayList<ThongTinTinNhan> arr= new ArrayList<ThongTinTinNhan>();
    public Adapter_Lv_Chat adapter;
    int loadThem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


          view=inflater.inflate(R.layout.man_hinh_chat_nhom,container,false);
        loadThem=0;
        lv_chat= (ListView) view.findViewById(R.id.lv_chat);
        edt_noidungchat= (EditText) view.findViewById(R.id.edt_noidungchat);
        btn_gui= (Button) view.findViewById(R.id.btn_gui);
        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arr);
        adapter= new Adapter_Lv_Chat(getContext(),android.R.layout.simple_list_item_1,arr);
        lv_chat.setAdapter(adapter);
//        arr.clear();
//        final boolean[] flag = {false};
//        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("Chat").addChildEventListener(new ChildEventListener() {
//
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                if (flag[0] == false) {
//                    ThongTinTinNhan tttn = dataSnapshot.getValue(ThongTinTinNhan.class);
//                    //  arr.add(String.valueOf(tttn.idNguoiGui)+": "+tttn.noiDung);
//                    arr.add(new ThongTinTinNhan(tttn.idNguoiGui, tttn.noiDung));
//                    //   finalAdapter.notifyDataSetChanged();
//                    lv_chat.setSelection(arr.size() - 1);
//                flag[0] =true;
//                }
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arr.clear();
                int s = Integer.parseInt(dataSnapshot.child("SoLuongTinNhan").getValue().toString());

                for(int i=1; i<=s;i++){
                    try{
                       int id= Integer.parseInt(dataSnapshot.child(String.valueOf(i)).child("idNguoiGui").getValue().toString());
                     String noiDung=   dataSnapshot.child(String.valueOf(i)).child("noiDung").getValue().toString();
                    arr.add(new ThongTinTinNhan(id,noiDung));


                    }catch (Exception e){}
                }
                lv_chat.setSelection(arr.size() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lv_chat.setAdapter(adapter);

        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_noidungchat.getText().toString().equals("")==false) {

                   final ThongTinTinNhan t = new ThongTinTinNhan(MapsActivity.sharedPreferences.getInt("id",0),edt_noidungchat.getText().toString());
                    final boolean[] flag = {false};
                    MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("Chat").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if(flag[0] ==false) {
                                int sl = Integer.parseInt(dataSnapshot.child("SoLuongTinNhan").getValue().toString());
                                MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("Chat").child(String.valueOf(sl + 1)).setValue(t);
                                MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("Chat").child("SoLuongTinNhan").setValue(sl+1);
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

        lv_chat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.e("log", "fist= "+firstVisibleItem+"..last="+lv_chat.getLastVisiblePosition()+"..số iten hiện="+visibleItemCount+"...tổng iten dem="+totalItemCount);

                if(firstVisibleItem==0){
                    loadThem++;
                    if(loadThem==3) {
                  //      Toast.makeText(getContext(), "fist", Toast.LENGTH_SHORT).show();
                        //loadThem=0;
                    }
                }
            }
        });
        return view;
    }


}
