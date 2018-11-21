package can.aboutsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

/**
 * Created by dale on 2018/11/04.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private static final String TABLE1_NAME = "user";
    private static final String TABLE2_NAME = "memo";
    private static final String TABLE3_NAME = "picture";
    private static final String TABLE4_NAME = "audio";
   // private static final String TABLE5_NAME = "wallpaper";


    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }
    /*创建账户*/
    public void insert_User(int user_id,String user_password,String user_tel){
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("user_id",user_id);
            cv.put("user_password",user_password);
            cv.put("user_tel",user_tel);
            db.insert(TABLE1_NAME,null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }
    /*修改账户密码*/
    public void changeUser_password(int user_id,String user_password){
        db.execSQL("update user set user_password= ? where  user_id=?", new Object[]{ user_id,user_password });
    }

    /*用户获取密码*/
    public String getUser_password(int user_id){
        String sql="select user_password from user where user_id ="+user_id;

        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            String user_pw=c.getString(0);
            c.close();
            return user_pw;
        }else {
            c.close();
            return "none";//返回用户不存在
        }
    }

    //创建一个对象，赋属性值，memoid的值不需要填写，然后调用insertmemo接口把对象传进来，然后调用getmemoid函数获取memoid写入属性值
    /*创建备忘录*/
    public  void insert_Memo(Memo memo) {
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("memo_title",memo.getMemo_title());
            cv.put("memo_ctime",memo.getMemo_ctime());
            cv.put("memo_dtime",memo.getMemo_dtime());
            cv.put("memo_priority",memo.getMemo_priority());
            cv.put("memo_periodicity",memo.getMemo_periodicity());
            cv.put("memo_advanced",memo.getMemo_advanced());
            cv.put("memo_remind",memo.getMemo_remind());
            cv.put("memo_paper",memo.getMemo_paper());
            cv.put("user_id",memo.getUser_id());  // 主码自增
            cv.put("memo_done",memo.getMemo_done());
            cv.put("memo_content",memo.getMemo_content());
            db.insert(TABLE2_NAME,null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }

    //获取刚刚插入的memoid, 创建完一个备忘录后直接使用getmemoid获取memoid值
    public int getmemoid(){
        String sql= "select max(memo_id) from "+TABLE2_NAME;
        Cursor c=null;
        int memoid=0;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToFirst()) {
                memoid=c.getInt((c.getColumnIndex("memo_id")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memoid;
    }

    /*修改备忘录*/
    public  void update_Memo(Memo m) {
        db.execSQL("update memo set memo_title=? where memo_id=?", new Object[]{ m.getMemo_title(),m.getMemo_id() });
        db.execSQL("update memo set memo_dtime=? where memo_id=?", new Object[]{ m.getMemo_dtime(),m.getMemo_id() });
        db.execSQL("update memo set memo_periodicity=? where memo_id=?", new Object[]{ m.getMemo_periodicity(),m.getMemo_id() });
        db.execSQL("update memo set memo_priority=? where memo_id=?", new Object[]{ m.getMemo_priority(),m.getMemo_id() });
        db.execSQL("update memo set memo_advanced=? where memo_id=?", new Object[]{ m.getMemo_advanced(),m.getMemo_id() });
        db.execSQL("update memo set memo_remind=? where memo_id=?", new Object[]{ m.getMemo_remind(),m.getMemo_id() });
        db.execSQL("update memo set memo_paper=? where memo_id=?", new Object[]{ m.getMemo_paper(),m.getMemo_id() });
        db.execSQL("update memo set memo_content=? where memo_id=?", new Object[]{ m.getMemo_content(),m.getMemo_id() });
    }


    /*插入图片*/
    public  void insert_Picture(Picture picture) {
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("pic_filename",picture.getPic_filename());
            cv.put("memo_id",picture.getMemo_id());
            db.insert(TABLE3_NAME,null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }

    /*修改trip_on状态，1开0关，下同*/
    public void changeTrip_on(int user_id,int pri){
        db.execSQL("update user set trip_on=? where user_id=?", new Object[]{ pri,user_id });
    }
    /*修改trip_priority*///优先级规定：pri 0 低 1 中 2 高
    public void changeTrip_priority(int user_id,int pri){
        db.execSQL("update user set trip_priority=? where  user_id=?", new Object[]{ pri,user_id });
    }
    /*修改trip_paper状态*/
    public void changeTrip_paper(int user_id,int pri){
        db.execSQL("update user set trip_paper=? where  user_id=?", new Object[]{ pri,user_id });
    }


    /*修改weather_on状态*/
    public void changeWeather_on(int user_id,int pri){
        db.execSQL("update user set weather_on=? where  user_id=?", new Object[]{pri,user_id });
    }
    /*修改weather_priority为关*///优先级规定：pri 0 低 1 中 2 高
    public void changeWall_priority(int user_id,int pri){
        db.execSQL("update user set weather_priority=? where  user_id=?", new Object[]{pri,user_id });
    }
    /*修改weather_paper状态*/
    public void changeWallpaper_paper(int user_id,int pri){
        db.execSQL("update user set weather_paper=? where  user_id=?", new Object[]{ pri,user_id });
    }

    /*修改parcel_on状态*/
    public void changeParcel_on(int user_id,int pri){
        db.execSQL("update user set parcel_on= ? where  user_id=?", new Object[]{ pri,user_id });
    }
    /*修改parcel_priority为关*///优先级规定：pri 0 低 1 中 2 高
    public void changeParcel_priority(int user_id,int pri){
        db.execSQL("update user set parcel_priority=?  where  user_id=?", new Object[]{ pri,user_id });
    }
    /*修改parcel_paper状态*/
    public void changeParcel_paper(int user_id,int pri){
        db.execSQL("update user set parcel_paper= ? where  user_id=?", new Object[]{pri, user_id });
    }

    /*修改analysis_on状态*/
    public void changeAnalysis_on(int user_id,int pri){
        db.execSQL("update user set analysis_on= ? where  user_id=?", new Object[]{pri, user_id });
    }
    /*修改analysis_priority为关*/
    public void changeAnalysis_priority(int user_id,int pri){
        db.execSQL("update user set trip_priority=0  where  user_id=?", new Object[]{pri, user_id });
    }
    /*修改analysis_paper状态*/
    public void changeAnalysis_paper(int user_id,int pri){
        db.execSQL("update user set analysis_paper= 0 where  user_id=?", new Object[]{pri, user_id });
    }


    //改变备忘录的完成状态
    public void changestate(int memo_id){
        db.execSQL("update memo set memo_done = 1 where memo_id =?", new Object[]{ memo_id });
    }

    //删除已完成备忘录列表
    public void deletedone(int user_id){
        db.execSQL("delete from memo where memo_done==1 and user_id=?", new Object[]{ user_id });
    }

    //删除多条备忘录
    public void deletedone(List<Integer> memoidlist ){
        Iterator iter = memoidlist.iterator();
        while(iter.hasNext()){
            db.execSQL("delete from memo where memo_id=?", new Object[]{ iter.next() });
        }
    }
    //返回主界面的已完成列表(memo中任务id任务标题到期时间有效)
    public ArrayList<Memo> returnmemo1(int user_id){
        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id;
        ArrayList<Memo> memolist = new ArrayList<Memo>();
        Cursor c = null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                if(c.getInt((c.getColumnIndex("memo_done")))==0) continue;
                Memo m = new Memo();
                m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
                m.setMemo_dtime(c.getLong((c.getColumnIndex("memo_dtime"))));
                m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                memolist.add(m);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memolist;
    }


    public boolean judgedtime(String dtime){
        /****获取当前时间****/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
        String str =formatter.format(curDate);
        //到期时间小于当前时间，过期为false
        if(dtime.compareTo(str)<0){
            return false;
        }else {
            return true;
        }
    }

    //返回主界面的未完成列表(未到期)
    public ArrayList<Memo> returnmemo2(int user_id){
        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id;
        ArrayList<Memo> memolist = new ArrayList<Memo>();
        Cursor c = null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                if(c.getInt((c.getColumnIndex("memo_done")))==1) continue;

                long x=c.getLong((c.getColumnIndex("memo_dtime")));
                Date date = new Date(x);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dtime=format.format(date);

                if(judgedtime(dtime)){
                    Memo m = new Memo();
                    m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
                    m.setMemo_dtime(x);
                    m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                    memolist.add(m);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memolist;
    }

    //返回主界面的未完成列表(过期)
    public ArrayList<Memo> returnmemo3(int user_id){
        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id;
        ArrayList<Memo> memolist = new ArrayList<Memo>();
        Cursor c = null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                if(c.getInt((c.getColumnIndex("memo_done")))==1) continue;
                long x=c.getLong((c.getColumnIndex("memo_dtime")));
                Date date = new Date(x);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dtime=format.format(date);
                if(!judgedtime(dtime)){
                    Memo m = new Memo();
                    m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
                    m.setMemo_dtime(x);
                    m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                    memolist.add(m);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memolist;
    }

    /*获得memo的主界面列表按照优先级和memo_dtime升序查询*/
    public Memo[] returMemo_list(int user_id) {
        int a =0;
        String sql="select memo_title,memo_dtime from memo where user_id ="+user_id+" and order by mem0_priority DESC,memo_dtime ASC";
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.getCount()>10)
            a=10;
        else
            a=c.getCount();
        Memo[] memos = new Memo[a];
        if(c.moveToFirst()) {
            for (int i = 0; i < a; i++) {
                c.move(i);
                memos[i].setMemo_title(c.getString(0));
                memos[i].setMemo_dtime(c.getLong(1));

            }
        }
        return memos;

    }
    //返回一个备忘的详细内容（除图片音频）
    public Memo returnamemo(int memo_id) {
        String sql= "select * from "+TABLE2_NAME+" where memo_id = "+memo_id;
        Cursor c=null;
        Memo m=new Memo();
        try{

            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
                m.setMemo_advanced(c.getInt((c.getColumnIndex("memo_advanced"))));
                m.setMemo_content(c.getString((c.getColumnIndex("memo_content"))));
                m.setMemo_ctime(c.getLong((c.getColumnIndex("memo_ctime"))));
                m.setMemo_dtime(c.getLong((c.getColumnIndex("memo_dtime"))));
                m.setMemo_done(c.getInt((c.getColumnIndex("memo_done"))));
                m.setMemo_paper(c.getInt((c.getColumnIndex("memo_paper"))));
                m.setMemo_periodicity(c.getInt((c.getColumnIndex("memo_periodicity"))));
                m.setMemo_remind(c.getInt((c.getColumnIndex("memo_remind"))));
                m.setMemo_priority(c.getInt((c.getColumnIndex("memo_priority"))));
                m.setUser_id(c.getInt((c.getColumnIndex("user_id"))));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }

        return m;
    }


    //返回一张或多张图片路径
    public List<String> returnpicture(int memo_id){
        String sql= "select pic_filename from "+TABLE3_NAME+" where memo_id = "+memo_id;
        List<String> piclist = new ArrayList<String>();
        String s=null;
        Cursor c=null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                s=c.getString((c.getColumnIndex("pic_filename")));
                piclist.add(s);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return piclist;
    }

    //返回一张或多张音频路径
    public List<String> returnaudio(int memo_id){
        String sql= "select audio_filename from "+TABLE4_NAME+" where memo_id = "+memo_id;
        List<String> audiolist = new ArrayList<String>();
        String s=null;
        Cursor c=null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                s=c.getString((c.getColumnIndex("audio_filename")));
                audiolist.add(s);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return audiolist;
    }
    /*关闭数据库*/
    public void closeDB(){
        db.close();
    }
}
