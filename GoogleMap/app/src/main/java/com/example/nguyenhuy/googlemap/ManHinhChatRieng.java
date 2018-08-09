package com.example.nguyenhuy.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class ManHinhChatRieng extends Fragment {
    TextView textView;
    View view;
    public ListView lv_chat;
    //public EditText edt_noidungchat;
    //public Button btn_gui;
    public static ArrayList<ThongTinTinNhan_ChatRieng> arr= new ArrayList<ThongTinTinNhan_ChatRieng>();
    public static ArrayList<ThongTinTinNhan_ChatRieng> arr2= new ArrayList<ThongTinTinNhan_ChatRieng>();
    public static ArrayList<ThongTinTinNhan_ChatRieng> arr3= new ArrayList<ThongTinTinNhan_ChatRieng>();


    public Adapter_Lv_Chat adapter;
    int loadThem;
    public ArrayList<TTTN_ChatRieng_NguoiDaChat> arr_nguoiDaChat = new ArrayList<TTTN_ChatRieng_NguoiDaChat>();

    public static  ArrayList<TTTN_ChatRieng_NguoiDaChat> arr_nguoiDaChat2 = new ArrayList<TTTN_ChatRieng_NguoiDaChat>();
     public static int idClick=-1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view=inflater.inflate(R.layout.man_hinh_chat_rieng,container,false);
        loadThem=0;
        lv_chat= (ListView) view.findViewById(R.id.lv_chatRieng);
        final ArrayAdapter adapter ;
        adapter= new Adapter_Lv_ChatRieng(getContext(),android.R.layout.simple_list_item_1,arr_nguoiDaChat2);


        final boolean[] flag = {false};
        MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child("ChatRieng").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //   if(flag[0] ==false){
                arr2.clear();
                arr.clear();
                arr_nguoiDaChat.clear();
                arr_nguoiDaChat2.clear();

                int s = Integer.parseInt(dataSnapshot.child("SoLuongTinNhan").getValue().toString());
                for (int i = 1; i <= s; i++) {
                    try {

                        String idNguoiGui = dataSnapshot.child(String.valueOf(i)).child("idNguoiGui").getValue().toString();
                        String idNguoiNhan = dataSnapshot.child(String.valueOf(i)).child("idNguoiNhan").getValue().toString();
                        String noiDung = dataSnapshot.child(String.valueOf(i)).child("noiDung").getValue().toString();
                        ThongTinTinNhan_ChatRieng tttn_cr = new ThongTinTinNhan_ChatRieng(Integer.parseInt(idNguoiGui), Integer.parseInt(idNguoiNhan), noiDung);
                   if (MapsActivity.sharedPreferences.getInt("id",0)==Integer.parseInt(idNguoiGui) || MapsActivity.sharedPreferences.getInt("id",0)==Integer.parseInt(idNguoiNhan)){
                        arr.add(0, tttn_cr);
                        arr2.add(tttn_cr);
                    }
                    }catch (Exception e){}

                }
               if(idClick!=0){
                   arr3.clear();
                for(int m=0;m<arr2.size();m++) {
                    Log.e("arr2", "" + arr2.get(m).noiDung);

                    if ((MapsActivity.sharedPreferences.getInt("id", 0) == arr2.get(m).idNguoiGui && idClick == arr2.get(m).idNguoiNhan) || (MapsActivity.sharedPreferences.getInt("id", 0) == arr2.get(m).idNguoiNhan && idClick == arr2.get(m).idNguoiGui)) {
                        arr3.add(arr2.get(m));
                    }

                }

                }
                Log.e("zo duoi", ".......................chat rieng" + arr.size());

                if(arr.size()>0) {
                for (int i = 0; i < arr.size(); i++) {
                    try {
                        if (MapsActivity.sharedPreferences.getInt("id", 0) != arr.get(i).idNguoiGui) {
                            arr_nguoiDaChat.add(new TTTN_ChatRieng_NguoiDaChat(arr.get(i).idNguoiGui, arr.get(i).noiDung));
                        } else {
                            arr_nguoiDaChat.add(new TTTN_ChatRieng_NguoiDaChat(arr.get(i).idNguoiNhan, arr.get(i).noiDung));

                        }
                    } catch (Exception e) {
                    }

                }

                for (int i = 0; i < arr_nguoiDaChat.size(); i++) {
                    Log.e("mang còn trùng", "..i=" + i + " id=" + arr_nguoiDaChat.get(i).id + " noidung=" + arr_nguoiDaChat.get(i).noiDungTinNhanCuoi);
                }

                    arr_nguoiDaChat2.add(arr_nguoiDaChat.get(0));
                    for (int i = 0; i < arr_nguoiDaChat.size(); i++) {
                        Log.e("remove", "loop i= " + i);

                        for (int j = 0; j < arr_nguoiDaChat2.size(); j++) {
                            Log.e("remove", "loop j= " + j);
                            if (arr_nguoiDaChat2.get(j).id == arr_nguoiDaChat.get(i).id) {
                                break;
                            } else {
                                if (j == arr_nguoiDaChat2.size() - 1) {
                                    arr_nguoiDaChat2.add(arr_nguoiDaChat.get(i));                   // khi xóa 1 cái đi thì stt của cái khác sẽ giảm;
                                    Log.e("remove", "remove " + i);
                                }
                            }
                        }

                    }
                    for (int i = 0; i < arr_nguoiDaChat2.size(); i++) {
                        Log.e("mangcuoicung", "..i=" + i + " id=" + arr_nguoiDaChat.get(i).id + " noidung=" + arr_nguoiDaChat.get(i).noiDungTinNhanCuoi);
                    }
                }     //    flag[0] =true;}
        //    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
      lv_chat.setAdapter(adapter);


      lv_chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              idClick=arr_nguoiDaChat2.get(i).id;
              arr3.clear();

              for(int m=0;m<arr2.size();m++){
                  Log.e("arr2",""+arr2.get(m).noiDung);
                  if((MapsActivity.sharedPreferences.getInt("id",0)==arr2.get(m).idNguoiGui && idClick==arr2.get(m).idNguoiNhan) ||(MapsActivity.sharedPreferences.getInt("id",0)==arr2.get(m).idNguoiNhan && idClick==arr2.get(m).idNguoiGui)) {
                arr3.add(arr2.get(m));

              }
              }
              Intent intent= new Intent(getContext(),MainActivity_ChatRieng_NoiDungChiTietTin.class);
              startActivity(intent);
              Activity_Chat.activity.finish();

          }
      });


        return view;
    }


}
