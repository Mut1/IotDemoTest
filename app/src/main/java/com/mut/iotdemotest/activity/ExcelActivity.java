package com.mut.iotdemotest.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.mut.iotdemotest.BaseActivity;
import com.mut.iotdemotest.MainActivity;
import com.mut.iotdemotest.R;
import com.mut.iotdemotest.entity.data2;
import com.mut.iotdemotest.fragment.HistoryFragment;
import com.mut.iotdemotest.utils.ExcelUtil;
import com.mut.iotdemotest.utils.ToastUtilsCs;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;

public class ExcelActivity extends BaseActivity implements View.OnClickListener {
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private int REQUEST_PERMISSION_CODE = 1000;


    private String filePath = "/sdcard/MingjieKeJiExcel";
    private List<data2> mdatalist;

    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private QMUIRoundButton exportButton;
    private QMUIRoundButton openButton;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecxel);

        requestPermission();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        exportButton = findViewById(R.id.export_button);
        exportButton.setOnClickListener(this);

        openButton = findViewById(R.id.open_button);
        openButton.setOnClickListener(this);
        ToastUtilsCs.showToast_info(ExcelActivity.this,"暂可一次导出150条数据");
    }

    private void exportExcel(Context context) {


        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }




        String[] title = {"时间", "车型","纬度","经度","拨禾轮","作业状态","幅宽","割台搅龙","输送槽主动轴","车速","竖割刀","纵轴脱粒滚筒","风机转速","驱动轮","振动筛驱动轴","籽粒水平绞龙","杂余水平绞龙","割茬高度","清选损失","夹带损失","鱼鳞筛开度","含杂率","破碎率","籽粒流量"};
        String sheetName = "传感器数据-150条";


        List<data2> demoBeanList = new ArrayList<>();
        IDataStorage dataStorage = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
        mdatalist=new ArrayList<>();
        mdatalist = dataStorage.loadAll(data2.class);
        if (mdatalist.size() == 0) {
            ToastUtilsCs.showToast_info(ExcelActivity.this,"数据库内无数据");
            //mStatusView.bind();

        }
        else {
            int size = mdatalist.size();
            if(size>150) {
                for (int i = size - 1; i >= 150; i--) {
                    demoBeanList.add(dataStorage.load(data2.class, String.valueOf(i)));
                }
            }else
            {
                for (int i = size - 1; i >= 0; i--) {
                    demoBeanList.add(dataStorage.load(data2.class, String.valueOf(i)));
                }
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            String excelFileName = "/"+format.format(date.getTime())+ ".xls";

            filePath = filePath + excelFileName;


            ExcelUtil.initExcel(filePath, sheetName, title);

            AlertDialog.Builder builder = new AlertDialog.Builder(ExcelActivity.this);
            builder.setTitle("导出完成")
                    .setMessage("是否删除导出的数据")
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDialog.dismiss();
                        }
                    })
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                                IDataStorage dataStorage = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
                                mdatalist = dataStorage.loadAll(data2.class);
                                int size = mdatalist.size();

                                if(size>150) {
                                for (int i = size - 1; i >= size-150 ; i--) {
                                    dataStorage.delete(data2.class,String.valueOf(i));
                                }
                            }else
                            {
                                for (int i = size - 1; i >= 0; i--) {
                                    dataStorage.delete(data2.class,String.valueOf(i));

                                }
                            }
                        }
                    });
            ExcelUtil.writeObjListToExcel(demoBeanList, filePath, context);
            ToastUtilsCs.showToast_success(ExcelActivity.this,"文件导出至:"+filePath);
            Log.d("111","导出至"+filePath);
            mDialog = builder.create();
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();

        }

    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT > 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permissions[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //授予权限
                Log.i("requestPermission:", "用户之前已经授予了权限！");
            } else {
                //未获得权限
                Log.i("requestPermission:", "未获得权限，现在申请！");
                requestPermissions(permissions
                        , REQUEST_PERMISSION_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("onPermissionsResult:", "权限" + permissions[0] + "申请成功");
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                Log.i("onPermissionsResult:", "用户拒绝了权限申请");
                AlertDialog.Builder builder = new AlertDialog.Builder(ExcelActivity.this);
                builder.setTitle("permission")
                        .setMessage("点击允许才可以使用我们的app哦")
                        .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (alertDialog != null && alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                }
                                ActivityCompat.requestPermissions(ExcelActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                        });
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }
    private void showDialogTipUserRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    private void openDir() {

        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setDataAndType(Uri.fromFile(file), "file/*");
        try {
            startActivity(intent);
        } catch (Exception e) {
            ToastUtilsCs.showToast_error(ExcelActivity.this,"请手动打开文件管理器！");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.export_button:
                exportExcel(this);

                break;
            case R.id.open_button:
                openDir();
            default:
                break;
        }
    }
}
