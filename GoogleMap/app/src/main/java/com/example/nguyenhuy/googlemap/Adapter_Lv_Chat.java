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

public class Adapter_Lv_Chat extends ArrayAdapter<ThongTinTinNhan> {
    public Adapter_Lv_Chat(Context context, int resourse, List<ThongTinTinNhan> items) {
        super(context, resourse, items);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi=LayoutInflater.from(getContext());
            v= vi.inflate(R.layout.custom_listview_chat,null);
        }

        // Get item
        try {
            // Get item
            final ThongTinTinNhan tttn = getItem(position);
            if (tttn != null) {
                final TextView tv_noidungtinnhan = (TextView) v.findViewById(R.id.tv_noidungtinnhan);
                final TextView tv_ten = (TextView) v.findViewById(R.id.tv_ten);

                tv_noidungtinnhan.setText(tttn.noiDung);

                final ImageView img_iconKhac = (ImageView) v.findViewById(R.id.img_iconKhac);
                final ImageView img_iconMinh = (ImageView) v.findViewById(R.id.img_iconMinh);
                img_iconMinh.setVisibility(View.GONE);
                final boolean[] flag = {false};
                MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(tttn.idNguoiGui)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(flag[0] ==false) {
                            try {
                                Picasso.get().load(dataSnapshot.child("linkAnh").getValue().toString()).into(img_iconKhac);
                                Picasso.get().load(dataSnapshot.child("linkAnh").getValue().toString()).into(img_iconMinh);
                                tv_ten.setText(dataSnapshot.child("ten").getValue().toString());
                                if (MapsActivity.sharedPreferences.getInt("id", 0) == tttn.idNguoiGui) {  //la chinh minh
                                    img_iconKhac.setVisibility(View.GONE);
                                    img_iconMinh.setVisibility(View.VISIBLE);
                                    tv_noidungtinnhan.setGravity(Gravity.RIGHT);
                                    tv_noidungtinnhan.setTextColor(Color.BLUE);
                                    tv_ten.setVisibility(View.GONE);
                                } else {
                                    img_iconKhac.setVisibility(View.VISIBLE);
                                    img_iconMinh.setVisibility(View.GONE);
                                    tv_noidungtinnhan.setGravity(Gravity.LEFT);
                                    tv_noidungtinnhan.setTextColor(Color.BLACK);
                                    tv_ten.setVisibility(View.VISIBLE);


                                }
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
