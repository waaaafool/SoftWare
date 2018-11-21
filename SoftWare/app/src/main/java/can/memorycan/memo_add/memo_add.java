package can.memorycan.memo_add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import can.aboutsqlite.*;
import can.main_delete.MainActivity;
import can.memorycan.memo_add.clock.widget.CustomDatePicker;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import can.memorycan.R;

import java.util.ArrayList;

import can.memorycan.R;
import can.memorycan.memo_add.list_View.Group;
import can.memorycan.memo_add.list_View.Item;
import can.memorycan.memo_add.list_View.MyBaseExpandableListAdapter;
import can.memorycan.memo_add.clock.Clock;
public class memo_add extends AppCompatActivity{
    /*定义控件变量*/
    private RelativeLayout selectDate, selectTime;
    private ImageView memo_add_back;
    private EditText editMemoTitle;
    private Spinner spinMoreSetting,listItemSpinner;
    private TextView currentDate, currentTime;
    private CustomDatePicker customDatePicker1, customDatePicker2;
    private ArrayList<Group> gData = null;
    private ArrayList<ArrayList<Item>> iData = null;
    private ArrayList<Item> lData = null;
    private Context mContext;
    private Button memo_add_save;
    private ExpandableListView exlist_lol;
    private MyBaseExpandableListAdapter myAdapter = null;
    private DBManager mgr;
    public int x=-1,y=-1;

    /*创建Memo所需的变量*/
    int memo_id;
    String memo_title;
    String memo_ctime="now";
    String memo_dtime;
    int memo_priority;
    int memo_periodicity;
    int memo_advanced;
    int memo_remind;
    int memo_paper;
    int user_id;
    int memo_done;
    String memo_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add);
        mContext = memo_add.this;
        /*创建DATABASE*/
        mgr = new DBManager(this);

        /*判断是更新memo还是创建新的memo*/
        Intent it=getIntent();
        Bundle bd=it.getExtras();
        int n=bd.getInt("Flag");

        Memo temp_memo=mgr.returnamemo(n);

        currentDate = (TextView) findViewById(R.id.currentDate);
        currentTime = (TextView) findViewById(R.id.currentTime);

        exlist_lol =  findViewById(R.id.exlist_lol);
        /*建立监听事件*/
        selectTime = (RelativeLayout) findViewById(R.id.selectTime);
        selectTime.setOnClickListener(new class_selectTime());

        /*返回按钮*/
        memo_add_back=(ImageButton)findViewById(R.id.memo_add_back);
        memo_add_back.setOnClickListener(new back_to_main());

        /*保存按钮*/
        memo_add_save=(Button)findViewById(R.id.memo_add_save);
        memo_add_save.setOnClickListener(new to_main());

        editMemoTitle=findViewById(R.id.memo_add_title);
        editMemoTitle.setOnClickListener(new class_addTitle());

        spinMoreSetting=findViewById(R.id.memo_add_priority);

        /*n==-1,就是创建新的备忘录*/

        /*从数据库获得优先级，并写进去*/
        String temp_priority="";
        if(temp_memo.getMemo_priority()==0) temp_priority="无";
        else if(temp_memo.getMemo_priority()==3) temp_priority="高";
        else if(temp_memo.getMemo_priority()==2) temp_priority="中";
        else if(temp_memo.getMemo_priority()==1) temp_priority="低";
        if(n!=-1) {
            SpinnerAdapter apsAdapter= spinMoreSetting.getAdapter();
            int k= apsAdapter.getCount();
            for(int i=0;i< k;i++){
                if(temp_priority.equals(apsAdapter.getItem(i).toString())){
                    spinMoreSetting.setSelection(i,true);
                    break;
                }
            }

        }
        spinMoreSetting.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                if(arg2==0){memo_priority=0;}
                else if(arg2==1){memo_priority=3;}
                else if(arg2==2){memo_priority=2;}
                else if(arg2==3){memo_priority=1;}
               // memo_priority=arg2;Toast.makeText(mContext, "你点击了："+arg2 , Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


       /* exlist_lol.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(mContext, "你点击了：" + iData.get(groupPosition).get(childPosition).getiName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

       /* exlist_lol.setOnChildClickListener((ExpandableListView.OnChildClickListener) this);*/

        initDatePicker();

        /*为了可折叠下拉列表准备spinner类型*/
        String[] time = getResources().getStringArray(R.array.time);
        ArrayAdapter<String> _Adapter0=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, time);
        String[] advancedTime = getResources().getStringArray(R.array.advanced_time);
        ArrayAdapter<String> _Adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, advancedTime);
        String[] remindType = getResources().getStringArray(R.array.remind_type);
        ArrayAdapter<String> _Adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, remindType);
        String[] switch1 = getResources().getStringArray(R.array.switch1);
        ArrayAdapter<String> _Adapter3=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, switch1);


        //数据准备
        gData = new ArrayList<Group>();
        iData = new ArrayList<ArrayList<Item>>();
        gData.add(new Group("更多设置"));
        lData = new ArrayList<Item>();

        //更多设置组
        lData.add(new Item(R.drawable.icon_generate,"任务周期性",_Adapter0));
        lData.add(new Item(R.drawable.icon_bell,"提前定时提醒",_Adapter1));
        lData.add(new Item(R.drawable.icon_tel,"到期提醒方式",_Adapter2));
        lData.add(new Item(R.drawable.icon_small_picture,"是否显示在锁屏",_Adapter3));
        iData.add(lData);


        //将Adapter加入方法创建myAdapter类
        myAdapter = new MyBaseExpandableListAdapter(gData,iData,mContext);
        exlist_lol.setAdapter(myAdapter);

        //默认的spinner的值
        String temp_str[]={"无","不提醒","不提醒","否"};


        //已有储存的
        String temp_str1[]=temp_str;
        if(temp_memo.getMemo_periodicity()==0) temp_str1[0]="不提醒";
        else if(temp_memo.getMemo_periodicity()==10) temp_str1[0]="提前十分钟";
        else if(temp_memo.getMemo_periodicity()==30) temp_str1[0]="提前30分钟";
        else if(temp_memo.getMemo_periodicity()==60) temp_str1[0]="提前1小时";

        if(temp_memo.getMemo_advanced()==0) temp_str1[1]="不提醒";
        else if(temp_memo.getMemo_advanced()==10) temp_str1[1]="提前十分钟";
        else if(temp_memo.getMemo_advanced()==30) temp_str1[1]="提前30分钟";
        else if(temp_memo.getMemo_advanced()==60) temp_str1[1]="提前1小时";

        if(temp_memo.getMemo_remind()==0) temp_str1[1]="不提醒";
        else if(temp_memo.getMemo_remind()==1) temp_str1[1]="闹铃";
        else if(temp_memo.getMemo_remind()==2) temp_str1[1]="震动";
        else if(temp_memo.getMemo_remind()==3) temp_str1[1]="闹铃加震动";


        if(temp_memo.getMemo_paper()==0) temp_str1[4]="否";
        else if(temp_memo.getMemo_paper()==0) temp_str1[4]="是";
        //myAdapter.setFlag(1);
        if(n==-1) {
            myAdapter.setSet_spinner(temp_str);
        }

        String s[]=myAdapter.sp_display();
        if(s[0]=="不提醒") memo_periodicity=0;
        else if(s[0]=="提前十分钟") memo_periodicity=10;
        else if(s[0]=="提前30分钟") memo_periodicity=30;
        else if(s[0]=="提前1小时")  memo_periodicity=60;

        if(s[1]=="不提醒") memo_advanced=0;
        else if(s[1]=="提前十分钟") memo_advanced=10;
        else if(s[1]=="提前30分钟") memo_advanced=30;
        else if(s[1]=="提前1小时")  memo_advanced=60;

        if(s[2]=="不提醒") memo_remind=0;
        else if(s[2]=="闹钟") memo_remind=10;
        else if(s[2]=="震动") memo_remind=30;
        else if(s[2]=="闹钟加震动")  memo_remind=60;

        if(s[3]=="否") memo_paper=0;
        else if(s[3]=="是") memo_paper=1;

        /*如果不是更新,设置标题*/
        if(n!=-1)  editMemoTitle.setText(temp_memo.getMemo_title());

        //构建memo
        user_id=123;
        memo_done=1;
        memo_content="15315";
        Memo final_memo=new Memo(memo_title, memo_dtime,memo_priority,memo_periodicity,memo_advanced,memo_remind,memo_paper,user_id,memo_done,memo_content);
        mgr.update_Memo(final_memo);

    }

    /*为每个监听创建类*/
    private class class_addTitle implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "你点击了：memo_add_title" , Toast.LENGTH_SHORT).show();
            memo_title=editMemoTitle.getText().toString();
        }
    }

    /*返回的监听类*/
    private class back_to_main implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(memo_add.this,MainActivity.class);
            startActivity(intent);
        }
    }

    /*保存的监听相应类*/
    private class to_main implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(memo_add.this,MainActivity.class);
            startActivity(intent);
        }
    }
    private  class class_selectTime implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            customDatePicker2.show(currentTime.getText().toString());
            memo_dtime=currentTime.getText().toString()+":00";
            Toast.makeText(mContext, "你点击了：" +currentTime.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
    /*进行时间选择的初始化*/
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        currentDate.setText(now.split(" ")[0]);
        currentTime.setText(now);

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentDate.setText(time.split(" ")[0]);
            }
        }, "2018-01-01 00:00", "2035-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentTime.setText(time);
            }
        }, "2018-01-01 00:00","2035-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }
    /*销毁活动时关闭DB*/
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

}
