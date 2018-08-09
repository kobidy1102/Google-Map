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

public class Adapter_ThongTinNhom extends ArrayAdapter<ThongTinThanhVien> {
    public Adapter_ThongTinNhom(Context context, int resourse, List<ThongTinThanhVien> items) {
        super(context, resourse, items);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi=LayoutInflater.from(getContext());
            v= vi.inflate(R.layout.custom_lv_thongtinnhom,null);
        }

        // Get item
        try {
            // Get item
            final ThongTinThanhVien tttn_cr = getItem(position);
            if (tttn_cr != null) {
                try {
                     TextView tv_ten = (TextView) v.findViewById(R.id.tv_thongTinNhom_ten);
                    tv_ten.setText(tttn_cr.ten);
                     ImageView img_icon = (ImageView) v.findViewById(R.id.img_avatar);
                    Picasso.get().load(tttn_cr.linkAnh).into(img_icon);
                  //  ManHinhThongTinNhom.lv_thanhVien.setSelectionAfterHeaderView();
                  //  ManHinhThongTinNhom.lv_thanhVien.setAdapter(ManHinhThongTinNhom.adapter);
                  //  ManHinhThongTinNhom.adapter.notifyDataSetChanged();
                }catch (Exception e){}
            }
        }catch (Exception e){
            Log.e("loi loi loi ","Loi : "+e);




        }

        return v;
    }
}
