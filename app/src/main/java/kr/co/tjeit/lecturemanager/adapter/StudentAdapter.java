package kr.co.tjeit.lecturemanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.tjeit.lecturemanager.R;
import kr.co.tjeit.lecturemanager.data.User;
import kr.co.tjeit.lecturemanager.util.GlobalData;

/**
 * Created by tjoeun on 2017-08-31.
 */

public class StudentAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private List<User> mList;
    LayoutInflater inf;


    public StudentAdapter(Context context, List<User> list){
        super(context, R.layout.student_list_item, list);
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row == null){
            row = inf.inflate(R.layout.student_list_item,null);
        }
        User data = GlobalData.allUser.get(position);

        TextView nameTxt = (TextView)row.findViewById(R.id.nameTxt);
        nameTxt.setText(data.getName());

        return row;
    }
}
