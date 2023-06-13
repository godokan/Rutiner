package com.godokan.rutiner.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.godokan.rutiner.adapter.ItemAdapter;
import com.godokan.rutiner.ListItem;
import com.godokan.rutiner.R;
import com.godokan.rutiner.helper.DateHelper;
import com.godokan.rutiner.helper.RutinDbHelper;
import com.godokan.rutiner.TableInfo;

import java.time.LocalDate;
import java.util.ArrayList;

public class DetailAccess extends Activity {

    EditText editName, editContext;
    View dialogView;
    ItemAdapter adapter;
    ListView listView;
    String sql = "";
    DateHelper dateHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailaccess);
        Intent intent = getIntent();
        final LocalDate today = (LocalDate)intent.getSerializableExtra("Date");

        listView = findViewById(R.id.listView);
        Button btnAdd = findViewById(R.id.btnAdd);
        RutinDbHelper helper = RutinDbHelper.getInstance(DetailAccess.this);
        dateHelper = DateHelper.getInstance();
        SQLiteDatabase db = helper.getWritableDatabase();
        updateList(db, today);

        btnAdd.setOnClickListener(view ->
                sqlInit(DetailAccess.this, db, today)
        );

        listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            ListItem listItem = (ListItem) adapterView.getItemAtPosition(position);
            PopupMenu popupMenu = new PopupMenu(DetailAccess.this, view, Gravity.END, 0, R.style.Windows98Theme_menuButton);

            getMenuInflater().inflate(R.menu.edit_list,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.editItem) {
                    sqlEdit(db, listItem.getId(), today);
                } else if (itemId == R.id.removeItem) {
                    sqlRemove(db, listItem.getId(), today);
                }
                return false;
            });
            popupMenu.show();
            return true;
        });

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            AlertDialog.Builder dlg = new AlertDialog.Builder(DetailAccess.this);

            ListItem listItem = (ListItem) adapterView.getItemAtPosition(position);
            dialogView = View.inflate(DetailAccess.this, R.layout.dialog_detail, null);
            TextView dp = dialogView.findViewById(R.id.type);
            TextView nm = dialogView.findViewById(R.id.name);
            TextView mm = dialogView.findViewById(R.id.context);
            TextView dt = dialogView.findViewById(R.id.date);
            TextView tm = dialogView.findViewById(R.id.flag);

            dp.setText(listItem.getType());
            nm.setText(listItem.getName());
            mm.setText(listItem.getContext());
            dt.setText(listItem.getDate());
            tm.setText(listItem.getFlag());

            dlg.setView(dialogView);
            dlg.setTitle(" ");
            if(tm.getText().toString().equals(getString(R.string.not_done))){
                dlg.setNegativeButton("상태변경", (dialog, which) -> {
                    try {
                        sql = "update "+TableInfo.TABLE_NAME+" set "+TableInfo.COLUMN_NAME_FLAG+" = ? where "+TableInfo.COLUMN_NAME_ID+" = "+listItem.getId();
                        db.execSQL(sql, new String[]{getString(R.string.done)});
                        System.out.println("변경 성공");
                        Toast.makeText(getApplicationContext(),"변경 완료", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {e.printStackTrace(); Toast.makeText(getApplicationContext(),"변경 실패", Toast.LENGTH_LONG).show();}
                    finally {updateList(db, today);}
                });
            } else {
                dlg.setNegativeButton("상태변경", (dialog, which) -> {
                    try {
                        sql = "update "+TableInfo.TABLE_NAME+" set "+TableInfo.COLUMN_NAME_FLAG+" = ? where "+TableInfo.COLUMN_NAME_ID+" = "+listItem.getId();
                        db.execSQL(sql, new String[]{getString(R.string.not_done)});
                        System.out.println("변경 성공");
                        Toast.makeText(getApplicationContext(),"변경 완료", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {e.printStackTrace(); Toast.makeText(getApplicationContext(),"변경 실패", Toast.LENGTH_LONG).show();}
                    finally {updateList(db, today);}
                });
            }
            dlg.setPositiveButton("닫기", null);
            dlg.show();
        });
    }

    ArrayList<String> getSqlList(SQLiteDatabase db, LocalDate day) {
        final ArrayList<String> list = new ArrayList<>();
        try {
            String sql = "select * from " + TableInfo.TABLE_NAME + " where " + TableInfo.COLUMN_NAME_DATE + " = ?";

            Cursor resultSet = db.rawQuery(sql, new String[]{dateHelper.parseDateString(day)});
            while (resultSet.moveToNext()) {
                String id = resultSet.getString(0);
                String type = resultSet.getString(1);
                String name = resultSet.getString(2);
                String context = resultSet.getString(3);
                String date = resultSet.getString(4);
                String flag = resultSet.getString(5);
                list.add(id + "☞" + type + "☞"+ name + "☞" + context + "☞" + date + "☞"+ flag);

                System.out.println();
            }
            resultSet.close();
        } catch (Exception e) {e.printStackTrace();}
        return list;
    }

    void updateList(SQLiteDatabase db, LocalDate day) {
        ArrayList<String> list = getSqlList(db, day);
        adapter = new ItemAdapter(DetailAccess.this);
        String[][] items = new String[list.size()][];
        for (int i = 0; i < list.size(); i++){
            items[i] = list.get(i).split("☞");
            adapter.addItem(new ListItem(items[i][0],items[i][1],items[i][2],items[i][3],items[i][4],items[i][5]));
        }
        listView.setAdapter(adapter);
    }

    void sqlInit(Context context, SQLiteDatabase db, LocalDate day) {
        dialogView = View.inflate(context, R.layout.dialog_init,null);
        RadioGroup drinkType = dialogView.findViewById(R.id.rutinType);
        editName = dialogView.findViewById(R.id.editName);
        editContext = dialogView.findViewById(R.id.editContext);
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle(" ");
        dlg.setView(dialogView);

        dlg.setNegativeButton("확인", (dialogInterface, i) -> {
            RadioButton checked = dialogView.findViewById(drinkType.getCheckedRadioButtonId());
            try {
                sql = "insert into "+ TableInfo.TABLE_NAME+
                        "("+TableInfo.COLUMN_NAME_TYPE+","+ TableInfo.COLUMN_NAME_NAME+","+TableInfo.COLUMN_NAME_CONTEXT+","+TableInfo.COLUMN_NAME_DATE+","+TableInfo.COLUMN_NAME_FLAG+
                        ") values  (?,?,?,?,?)";
                db.execSQL(sql, new String[]{checked.getText().toString(), editName.getText().toString(), editContext.getText().toString(), dateHelper.parseDateString(day), getString(R.string.not_done)});
                System.out.println("입력 성공");
                Toast.makeText(getApplicationContext(),"입력 완료", Toast.LENGTH_LONG).show();
            } catch (Exception e) {e.printStackTrace(); Toast.makeText(getApplicationContext(),"입력 실패", Toast.LENGTH_LONG).show();}
            finally {updateList(db, day);}
        });
        dlg.setPositiveButton("취소", null);
        dlg.show();
    }

    void sqlEdit(SQLiteDatabase db, String ID, LocalDate day) {
        dialogView = View.inflate(DetailAccess.this, R.layout.dialog_edit,null);
        RadioGroup drinkType = dialogView.findViewById(R.id.rutinType);
        editName = dialogView.findViewById(R.id.editName);
        editContext = dialogView.findViewById(R.id.editContext);
        AlertDialog.Builder dlg = new AlertDialog.Builder(DetailAccess.this);
        dlg.setTitle(" ");
        dlg.setView(dialogView);
        dlg.setNegativeButton("확인", (dialogInterface, i) -> {
            RadioButton checked = dialogView.findViewById(drinkType.getCheckedRadioButtonId());
            try {
                sql = "update "+TableInfo.TABLE_NAME+" set "+TableInfo.COLUMN_NAME_TYPE+" = ?,"+ TableInfo.COLUMN_NAME_NAME+" = ?,"+TableInfo.COLUMN_NAME_CONTEXT+"= ? where "+TableInfo.COLUMN_NAME_ID+" = "+ID;
                db.execSQL(sql, new String[]{checked.getText().toString(), editName.getText().toString(), editContext.getText().toString()});
                System.out.println("수정 성공");
                Toast.makeText(getApplicationContext(),"수정 완료", Toast.LENGTH_LONG).show();
            } catch (Exception e) {e.printStackTrace(); Toast.makeText(getApplicationContext(),"수정 실패", Toast.LENGTH_LONG).show();}
            finally {updateList(db, day);}
        });
        dlg.setPositiveButton("취소", null);
        dlg.show();
    }

    void sqlRemove(SQLiteDatabase db, String ID, LocalDate day) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(DetailAccess.this);
        AlertDialog alert = dlg.create();
        dlg.setTitle("기록삭제");
        dlg.setMessage("삭제하시겠습니까?");
        dlg.setNegativeButton("삭제", (dialogInterface, i) -> {
            try {
                sql = "delete from "+TableInfo.TABLE_NAME+" where id="+ID;
                db.execSQL(sql);
                Toast.makeText(DetailAccess.this, "삭제했습니다.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {e.printStackTrace();}
            updateList(db, day);
            alert.dismiss();
        });
        dlg.setPositiveButton("취소", null);
        dlg.show();
    }
}
