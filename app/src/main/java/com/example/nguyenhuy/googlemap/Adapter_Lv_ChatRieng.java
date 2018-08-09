package com.example.nguyenhuy.googlemap;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Lv_ChatRieng extends ArrayAdapter<TTTN_ChatRieng_NguoiDaChat> {
    public Adapter_Lv_ChatRieng(Context context, int resourse, List<TTTN_ChatRieng_NguoiDaChat> items) {
        super(context, resourse, items);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi=LayoutInflater.from(getContext());
            v= vi.inflate(R.layout.custom_listview_chat_rieng,null);
        }

        // Get item
        try {
            // Get item
            final TTTN_ChatRieng_NguoiDaChat tttn_cr = getItem(position);
            if (tttn_cr != null) {
                final TextView tv_noidungtinnhan = (TextView) v.findViewById(R.id.tv_chatRieng_noidungtinnhan);
                final TextView tv_ten = (TextView) v.findViewById(R.id.tv_chatRieng_ten);
                tv_noidungtinnhan.setText(tttn_cr.noiDungTinNhanCuoi);
                final ImageView img_icon = (ImageView) v.findViewById(R.id.img_chatRieng_anh);
                final boolean[] flag = {false};
                MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(flag[0] ==false) {
                            try {
//                                if(MapsActivity.sharedPreferences.getInt("id",0)==tttn_cr.id || MapsActivity.sharedPreferences.getInt("id",0)==tttn_cr.idNguoiNhan) {
//                                    if(MapsActivity.sharedPreferences.getInt("id",0) !=tttn_cr.idNguoiGui) { //minh la nguoi nhan
//                                        Picasso.get().load(dataSnapshot.child(String.valueOf(tttn_cr.idNguoiGui)).child("linkAnh").getValue().toString()).into(img_icon);
//                                        tv_ten.setText(dataSnapshot.child(String.valueOf(tttn_cr.idNguoiGui)).child("ten").getValue().toString());
//                                        tv_noidungtinnhan.setTextColor(Color.BLACK);
//                                    }
//                                    if(MapsActivity.sharedPreferences.getInt("id",0) !=tttn_cr.idNguoiNhan) {  // minh la nguoi gui
//                                        Picasso.get().load(dataSnapshot.child(String.valueOf(tttn_cr.idNguoiNhan)).child("linkAnh").getValue().toString()).into(img_icon);
//                                        tv_ten.setText(dataSnapshot.child(String.valueOf(tttn_cr.idNguoiNhan)).child("ten").getValue().toString());
//                                        tv_noidungtinnhan.setTextColor(Color.GRAY);
//
//                                    }
//                                }
                                Picasso.get().load(dataSnapshot.child(String.valueOf(tttn_cr.id)).child("linkAnh").getValue().toString()).into(img_icon);
                                tv_ten.setText(dataSnapshot.child(String.valueOf(tttn_cr.id)).child("ten").getValue().toString());
                                flag[0] = true;
                            }catch (Exception e){}
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //     Picasso.get().load(MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(tttn.idNguoiGui)).child("linkAnh")).into(img_iconKhac);
                //   }


            }
        }catch (Exception e){
            Log.e("exception","Loi : "+e);
        }

        return v;
    }
}
