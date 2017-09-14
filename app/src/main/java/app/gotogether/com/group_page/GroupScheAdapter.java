package app.gotogether.com.group_page;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2017-09-10.
 */

public class GroupScheAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<ScheData> myDataList;
    private LayoutInflater layoutInflater;
    private String result = "";

    public GroupScheAdapter(Context context, int layout, ArrayList<ScheData> myDataList) {
        this.context = context;
        this.layout = layout;
        //원본 데이터를 가지고 있다(MyDataList)
        this.myDataList = myDataList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //원본 데이터의 개수를 반환
        return myDataList.size();
    }

    @Override
    public Object getItem(int position) {
        //어떠한 위치에 있는 원본 데이터의 항목 반환(어떠한 타입도 반환 가능-object이기 때문)
        return myDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        //어떠한 위치에 있는 원본 데이터의 항복의 식별자를 반환
        int r = Integer.parseInt(myDataList.get(position).get_id());
        return r;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final int pos = position;

        if (view == null) {
            view = layoutInflater.inflate(layout, viewGroup, false);

        }

        //view안에서 찾기 때문에 view.findviewbyid를 해주어야 한다.
        TextView textPlan = (TextView) view.findViewById(R.id.textViewPlan);
        TextView circle = (TextView) view.findViewById(R.id.circle);
        //숫자는 아이디를 찾기 때문에 문자열로 변환해주어야 한다.
        textPlan.setText(myDataList.get(position).getPlan());

        circle.setTextColor(Color.YELLOW);


        return view;
    }

    public String getResult() {
        return result;
    }


}