package com.tht.psaux;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class SettingsAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> procNames;
    private LayoutInflater inflater;
    private static class ViewHolder{
        private ImageView imgPSmallPic, btnRemoveProc;
        private TextView txvProcName;
    }
    private void init(View view, ViewHolder v) {
        v.imgPSmallPic = (ImageView) view.findViewById(R.id.imgPSmallPic);
        v.txvProcName = (TextView) view.findViewById(R.id.txvProcName);
        v.btnRemoveProc = (ImageView) view.findViewById(R.id.btnRemoveProc);
    }

    public SettingsAdapter(Context context, ArrayList<String> procNames){
        this.context = context;
        this.procNames = procNames;
    }
    @Override
    public int getCount(){
        return procNames.size();
    }

    @Override
    public Object getItem(int position){
        return procNames.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }
    @Override
    public View getView(final int position, View conView, ViewGroup viewGroup){
        ViewHolder viewHolder;
        if(conView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conView = inflater.inflate(R.layout.settings_list, viewGroup, false);
            viewHolder = new ViewHolder();
            init(conView, viewHolder);
            conView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) conView.getTag();
        }
        viewHolder.txvProcName.setText(procNames.get(position));
        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(procNames.get(position));
            viewHolder.imgPSmallPic.setImageDrawable(icon);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return conView;
    }

}


