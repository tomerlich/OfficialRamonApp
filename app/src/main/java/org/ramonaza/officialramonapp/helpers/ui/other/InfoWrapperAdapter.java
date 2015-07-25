package org.ramonaza.officialramonapp.helpers.ui.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.ramonaza.officialramonapp.R;
import org.ramonaza.officialramonapp.helpers.backend.InfoWrapper;

import java.util.ArrayList;

/**
 * Created by ilanscheinkman on 7/24/15.
 */
public class InfoWrapperAdapter extends ArrayAdapter<InfoWrapper>{

    //List of allowed view types
    public static final int NAME_ONLY=0;
    public static final int NAME_AND_DESC=1;

    private static class ViewHolder{
        public TextView name;
        public TextView desc;
    }

    private int displayType;

    public InfoWrapperAdapter(Context context, int displayType) {
        super(context,0,new ArrayList<InfoWrapper>());
    }

    public InfoWrapperAdapter(Context context, InfoWrapper[] objects, int displayType) {
        super(context, 0, objects);
        this.displayType=displayType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        switch (displayType) {
            case NAME_ONLY:
                if(convertView==null){
                    viewHolder=new ViewHolder();
                    convertView=LayoutInflater.from(getContext()).inflate(R.layout.infowrapper_name,parent,false);
                    viewHolder.name=(TextView) convertView.findViewById(R.id.infowrappername);
                    convertView.setTag(viewHolder);
                } else viewHolder=(ViewHolder) convertView.getTag();
                viewHolder.name.setText(getItem(position).getName());
                return convertView;
            default:
                return null;
        }
    }


}
