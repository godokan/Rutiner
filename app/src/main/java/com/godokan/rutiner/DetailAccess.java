package com.godokan.rutiner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailAccess extends Activity {

    EditText editName, editMemo;
    View dialogView;
    ItemAdapter adapter;
    ListView listView;
    String sql = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailaccess);
        Intent intent = getIntent();
        final String today = intent.getStringExtra("Date");

        listView = findViewById(R.id.listView);
        Button btnAdd = findViewById(R.id.btnAdd);
        RutinDbHelper helper = RutinDbHelper.getInstance(DetailAccess.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        updateList(db, today);

        btnAdd.setOnClickListener(view ->
                sqlInit(DetailAccess.this, db, today)
        );

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                ListItem listItem = (ListItem) adapterView.getItemAtPosition(position);
                PopupMenu popupMenu = new PopupMenu(DetailAccess.this, view);
                getMenuInflater().inflate(R.menu.edit_list,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.editItem:
                                sqlEdit(db, listItem.getId(),today);
                                break;
                            case  R.id.removeItem:
                                sqlRemove(db, listItem.getId(), today);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(DetailAccess.this);

                ListItem listItem = (ListItem) adapterView.getItemAtPosition(position);
                dialogView = (View) View.inflate(DetailAccess.this, R.layout.dialog_detail, null);
                TextView dp = (TextView) dialogView.findViewById(R.id.type);
                TextView nm = (TextView) dialogView.findViewById(R.id.name);
                TextView mm = (TextView) dialogView.findViewById(R.id.context);
                TextView dt = (TextView) dialogView.findViewById(R.id.date);
                TextView tm = (TextView) dialogView.findViewById(R.id.flag);

                dp.setText(listItem.getType());
                nm.setText(listItem.getName());
                mm.setText(listItem.getContext());
                dt.setText(listItem.getDate());
                tm.setText(listItem.getFlag());

                dlg.setView(dialogView);
                dlg.setTitle(" ");
                dlg.setNegativeButton("닫기", null);
                dlg.show();
            }
        });
    }

    ArrayList<String> getSqlList(SQLiteDatabase db, String day) {
        final ArrayList<String> list = new ArrayList<>();
        try {
            String sql = "select "+TableInfo.COLUMN_NAME_TYPE+","+TableInfo.COLUMN_NAME_NAME+","+TableInfo.COLUMN_NAME_CONTEXT+","+TableInfo.COLUMN_NAME_DATE+","+TableInfo.COLUMN_NAME_FLAG+","+TableInfo.COLUMN_NAME_ID+" from " + TableInfo.TABLE_NAME + " where " + TableInfo.COLUMN_NAME_DATE + " = ?";
            Cursor resultSet = db.rawQuery(sql, new String[]{day});
            while (resultSet.moveToNext()) {
                String id = resultSet.getString(0);
                String type = resultSet.getString(1);
                String name = resultSet.getString(2);
                String context = resultSet.getString(3);
                String date = resultSet.getString(4);
                String flag = resultSet.getString(5);
                list.add(id + "☞" + type + "☞"+ name + "☞" + context + "☞" + date + "☞"+ flag);
            }
            resultSet.close();
        } catch (Exception e) {e.printStackTrace();}
        return list;
    }

    void updateList(SQLiteDatabase db, String day) {
        ArrayList<String> list = getSqlList(db, day);
        adapter = new ItemAdapter(DetailAccess.this);
        String[][] items = new String[list.size()][];
        for (int i = 0; i < list.size(); i++){
            items[i] = list.get(i).split("☞");
            adapter.addItem(new ListItem(items[i][0],items[i][1],items[i][2],items[i][3],items[i][4], items[i][5]));
        }
        listView.setAdapter(adapter);
    }

    void sqlInit(Context context, SQLiteDatabase db, String day) {
        dialogView = (View) View.inflate(context, R.layout.dialog_init,null);
        RadioGroup drinkType = (RadioGroup) dialogView.findViewById(R.id.drinkType);
        editName = dialogView.findViewById(R.id.editName);
        editMemo = dialogView.findViewById(R.id.editMemo);
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle("기록 추가");
        dlg.setView(dialogView);
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RadioButton checked = (RadioButton) dialogView.findViewById(drinkType.getCheckedRadioButtonId());
                try {
                    sql = "insert into "+ TableInfo.TABLE_NAME+
                            "("+TableInfo.COLUMN_NAME_DTYPE+","+ TableInfo.COLUMN_NAME_DNAME+","+TableInfo.COLUMN_NAME_MEMO+","+TableInfo.COLUMN_NAME_DATE+","+TableInfo.COLUMN_NAME_TIME+
                            ") values  (?,?,?,?,?)";
                    db.execSQL(sql, new String[]{checked.getText().toString(), editName.getText().toString(), editMemo.getText().toString(), dateHelper.getNowDate(), dateHelper.getNowTime()});
                    System.out.println("입력 성공");
                    Toast.makeText(getApplicationContext(),"입력 완료", Toast.LENGTH_LONG).show();
                } catch (Exception e) {e.printStackTrace(); Toast.makeText(getApplicationContext(),"입력 실패", Toast.LENGTH_LONG).show();}
                finally {updateList(db, day);}
            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();
    }

    void sqlEdit(SQLiteDatabase db, String ID, String day) {
        dialogView = (View) View.inflate(DetailAccess.this, R.layout.dialog_init,null);
        RadioGroup drinkType = (RadioGroup) dialogView.findViewById(R.id.drinkType);
        editName = dialogView.findViewById(R.id.editName);
        editMemo = dialogView.findViewById(R.id.editMemo);
        AlertDialog.Builder dlg = new AlertDialog.Builder(DetailAccess.this);
        AlertDialog alert = dlg.create();
        dlg.setTitle("기록수정");
        dlg.setView(dialogView);
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RadioButton checked = (RadioButton) dialogView.findViewById(drinkType.getCheckedRadioButtonId());
                try {
                    sql = "update "+TableInfo.TABLE_NAME+" set "+TableInfo.COLUMN_NAME_DTYPE+" = ?,"+ TableInfo.COLUMN_NAME_DNAME+" = ?,"+TableInfo.COLUMN_NAME_MEMO+"= ? where "+TableInfo.COLUMN_NAME_ID+" = "+ID;
                    db.execSQL(sql, new String[]{checked.getText().toString(), editName.getText().toString(), editMemo.getText().toString()});
                    System.out.println("수정 성공");
                    Toast.makeText(getApplicationContext(),"수정 완료", Toast.LENGTH_LONG).show();
                } catch (Exception e) {e.printStackTrace(); Toast.makeText(getApplicationContext(),"수정 실패", Toast.LENGTH_LONG).show();}
                finally {updateList(db, day);}
            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();
    }

    void sqlRemove(SQLiteDatabase db, String ID, String day) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(DetailAccess.this);
        AlertDialog alert = dlg.create();
        dlg.setTitle("기록삭제");
        dlg.setMessage("삭제하시겠습니까?");
        dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    sql = "delete from "+TableInfo.TABLE_NAME+" where id="+ID;
                    db.execSQL(sql);
                    Toast.makeText(DetailAccess.this, "삭제했습니다.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {e.printStackTrace();}
                updateList(db, day);
                alert.dismiss();
            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();
    }
}
