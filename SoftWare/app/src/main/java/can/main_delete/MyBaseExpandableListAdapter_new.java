package can.main_delete;

import can.aboutsqlite.Memo;
import can.memorycan.R;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class MyBaseExpandableListAdapter_new extends BaseExpandableListAdapter {

    private ArrayList<Group_new> gData;
    private ArrayList<ArrayList<Memo>> iData;
    private Context mContext;

    public MyBaseExpandableListAdapter_new(ArrayList<Group_new> gData,ArrayList<ArrayList<Memo>> iData, Context mContext) {
        this.gData = gData;
        this.iData = iData;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return iData.get(groupPosition).size();
    }

    @Override
    public Group_new getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Memo getChild(int groupPosition, int childPosition) {
        return iData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public void change_gData(ArrayList<Group_new> group)
    {
        gData = group;
        notifyDataSetChanged();
    }

    public void change_iData(ArrayList<ArrayList<Memo>> item)
    {
        iData = item;
        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_exlist_group_new, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name_new);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.tv_group_name.setText(gData.get(groupPosition).get_Group_name());
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final int index1 = groupPosition;
        final int index2 = childPosition;
        ViewHolderItem itemHolder;
        convertView = null;
        if(convertView == null){
            Log.e("groupposition",String.valueOf(convertView));
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_exlist_item_new, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.ckb_name = (CheckBox) convertView.findViewById(R.id.ckb_name_new);
            itemHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_new);
            if(groupPosition == 2 && gData.get(groupPosition).get_memo_done()==0 && iData.get(groupPosition).get(childPosition).getMemo_id() > 100) {
                Log.e("groupposition",String.valueOf(groupPosition));
                itemHolder.ckb_name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            convertView.setTag(itemHolder);
            itemHolder.ckb_name.setTag(childPosition);
//            notifyDataSetChanged();
        }else{
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
//        itemHolder.ckb_name.setOnCheckedChangeListener(null);
//        itemHolder.ckb_name.setChecked(iData.get(groupPosition).get(childPosition).getCheckStatus());
        itemHolder.ckb_name.setText(iData.get(index1).get(index2).getMemo_title());
        itemHolder.tv_name.setText(String.valueOf(iData.get(index1).get(index2).getmemo_dtimestring()));
        return convertView;
    }

//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
//        int index = (int)buttonView.getTag();
//        if(isChecked)
//            iData.get(index).get(index).setCheckStatus(true);
//        else
//            iData.get(index).get(index).setCheckStatus(false);
//    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


//    public void add(Item item)
//    {
//        if(iData == null)
//        {
//            iData=new ArrayList<>();
//        }
//        iData.add(item);
//    }

    private static class ViewHolderGroup{
        private TextView tv_group_name;
    }

    private static class ViewHolderItem{
        private TextView tv_name;
        private CheckBox ckb_name;
    }
}
