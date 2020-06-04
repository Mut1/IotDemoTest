package com.mut.iotdemotest.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.mut.iotdemotest.BaseActivity;
import com.mut.iotdemotest.R;
import com.mut.iotdemotest.entity.data2;
import com.mut.iotdemotest.utils.ToastUtilsCs;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
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

public class POIExcelActivity extends BaseActivity implements View.OnClickListener {
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_PERMISSION_CODE = 1000;
    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private List<data2> mdatalist;

    private QMUIRoundButton export_button;
    private QMUIRoundButton open_button;
    private String filePath = "/sdcard/明杰科技APP导出数据";
    private QMUILoadingView loading_view;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecxel);
        requestPermission();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        initView();
    }

    private void exportExcel() {

        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        List<data2> listContent = new ArrayList<>();
        List<String> listId = new ArrayList<>();
        // String sheetName=String.valueOf(df.format(date.getTime()));
        String sheetName = "表一";
        String[] sheetHeads = {"时间", "车型", "纬度", "经度", "拨禾轮", "作业状态", "幅宽", "割台搅龙", "输送槽主动轴", "车速", "竖割刀", "纵轴脱粒滚筒", "风机转速", "驱动轮", "振动筛驱动轴", "籽粒水平绞龙", "杂余水平绞龙", "割茬高度", "清选损失", "夹带损失", "鱼鳞筛开度", "含杂率", "破碎率", "籽粒流量"};
//    String[] sheetHeads = {"时间", "车型"};
        IDataStorage dataStorage = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
        mdatalist = new ArrayList<>();
        mdatalist = dataStorage.loadAll(data2.class);
        if (mdatalist.size() == 0) {
            ToastUtilsCs.showToast_info(POIExcelActivity.this, "数据库内无数据");
        } else {
            int size = mdatalist.size();
            if (size >= 60000)
                for (int i = size - 1; i >= size - 60000; i--) {
                    listContent.add(dataStorage.load(data2.class, String.valueOf(i)));
                    listId.add(String.valueOf(i));

                }
            if (size < 60000) {
                for (int i = size - 1; i >= 0; i--) {
                    listContent.add(dataStorage.load(data2.class, String.valueOf(i)));
                    listId.add(String.valueOf(i));

                }
            }
            String excelFileName = "/" + df.format(date.getTime()) + ".xls";
          //  filePath = filePath + excelFileName;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(filePath + excelFileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            writeExcel(sheetName, sheetHeads, listContent, fos);
     loading_view.stop();
     loading_view.setVisibility(View.INVISIBLE);
            if (mdatalist.size() > 60000) {
                pop_delete(listId);
            }
            ToastUtilsCs.showToast_success(POIExcelActivity.this, "导出成功！");
        }
    }

    private void initView() {
        export_button = (QMUIRoundButton) findViewById(R.id.export_button);
        open_button = (QMUIRoundButton) findViewById(R.id.open_button);
        export_button.setOnClickListener(this);
        open_button.setOnClickListener(this);
        loading_view = (QMUILoadingView) findViewById(R.id.loading_view);
        loading_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.export_button:
                loading_view.setVisibility(View.VISIBLE);
                loading_view.start();
                exportExcel();
                break;
            case R.id.open_button:
                //TODO: 打开文件夹出错
                // openDir();
                pop();
                break;
        }
    }

    /**
     * 导出Excel
     *
     * @param sheetName   表名
     * @param sheetHeads  表头
     * @param listContent 数据集合
     * @param os          输出流，可输出到文件、网络
     */
    public void writeExcel(String sheetName, String[] sheetHeads,
                           List<data2> listContent, OutputStream os) {
        try {
            /**
             * 2003-2007 版本的 工作簿，文件的后缀名为xls的文件可以单独使用 HSSFWorkbook workbook = new HSSFWorkbook()
             * 2007 以上版本的 工作簿，文件的后缀名为Xlsx的文件可以单独使用  XSSFWorkbook workbook=new XSSFWorkbook();
             * 下面对应的类加上对应的前缀HSSF或XSSF，而没有前缀的可以通用xls和xlsx的文件， 需要加前缀的类如：HSSFWorkbook、HSSFSheet、HSSFRow、HSSFCell、HSSFFont、HSSFCellStyle、HSSFDataFormat、
             * 读写流程：Workbook(工作簿)——>Sheet(工作表)——>Row(行)——>Cell(单元格)
             */
            Workbook workbook = new HSSFWorkbook();// 创建一个2007以上版本的工作簿，创建需要指定具体的版本，读取可以不用
            Sheet sheet = workbook.createSheet(sheetName);// 创建一个工作表

            sheet.setDefaultRowHeight((short) (1 * 256));// 设置默认行高，表示1个字符的高度

            Font font = workbook.createFont();// 创建一个字体对象，
            // font.setBold(true);// 是否加粗
            font.setFontName("宋体");// 设置字体
            font.setFontHeightInPoints((short) 16);// 设置字体大小

            CellStyle cellStyle = workbook.createCellStyle();// 创建一个单元格的样式
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 居中对齐
            cellStyle.setWrapText(true);// 设置自动换行
            cellStyle.setFont(font);// 将字体设置到样式中
            cellStyle.setFillForegroundColor(HSSFColor.RED.index);// 设置背景色

            int rowCount = 0;//用于计数已有多少行的数据，其真实有的数据为rowCount条，访问时下标需要减1

            // 填充表头数据
            Cell cell = null;
            String head;
            Row headRow = sheet.createRow(rowCount++);// 创建第一行，下标从0 开始
            for (int i = 0; i < sheetHeads.length; i++) {
                head = sheetHeads[i];
                sheet.setColumnWidth(i, (head.length() + 20) * 256);// 设置第i列的列宽，第一个参数代表列下标(从0开始),第2个参数代表宽度值需要乘256表示多少个字符宽度
                cell = headRow.createCell(i);// 创建一个单元格
                cell.setCellValue(head);// 为单元格设置一个值
                cell.setCellStyle(cellStyle);// 设置单元格的样式
            }

            // 填充内容数据
            cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);// 设置背景色
            data2 mdatabean = null;
            Row row = null;
            // 为日期单独设置单元格的格式
//            DataFormat dataFormat = workbook.createDataFormat();
//            CellStyle cellStyleData = workbook.createCellStyle();
//            cellStyleData.setDataFormat(dataFormat.getFormat("yyyy年MM月dd日 hh:mm"));

            for (int i = 0; i < listContent.size(); i++) {
                DecimalFormat df = new DecimalFormat("#.0000000");
                ;

                mdatabean = listContent.get(i);
                row = sheet.createRow(rowCount++);// 创建第i+1行d
                cell = row.createCell(0);// 创建第i+1行是第1个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(ToastUtilsCs.addDateMinut2(mdatabean.getTime(), 8));

                cell = row.createCell(1);// 创建第i+1行是第2个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getMark());

                cell = row.createCell(2);// 创建第i+1行是第3个单元格
                cell.setCellStyle(cellStyle);
                String n = mdatabean.getN();
                if (n.equals("")) {
                    n = "error";
                    cell.setCellValue(n);

                } else if (!n.equals("")) {
                    n = String.valueOf(df.format((Double.valueOf(mdatabean.getN())) / 100));
                    cell.setCellValue(n);

                }
                // Log.e("1111",e);

                cell = row.createCell(3);// 创建第i+1行是第4个单元格
                cell.setCellStyle(cellStyle);
                String e = mdatabean.getE();
                if (e.equals("")) {
                    e = "error";
                    cell.setCellValue(e);

                } else if (!e.equals("")) {
                    e = String.valueOf(df.format((Double.valueOf(mdatabean.getE())) / 100));
                    cell.setCellValue(e);

                }


                cell = row.createCell(4);// 创建第i+1行是第5个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getBohelun());

                cell = row.createCell(5);// 创建第i+1行是第6个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getZuoye());

                cell = row.createCell(6);// 创建第i+1行是第7个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getFukuan());

                cell = row.createCell(7);// 创建第i+1行是第8个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getGetai());

                cell = row.createCell(8);// 创建第i+1行是第9个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getShusongzhou());

                cell = row.createCell(9);// 创建第i+1行是第10个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getChesu());

                cell = row.createCell(10);// 创建第i+1行是第11个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getQieLTL());

                cell = row.createCell(11);// 创建第i+1行是第12个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getZongZTL());

                cell = row.createCell(12);// 创建第i+1行是第13个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getFongJZS());

                cell = row.createCell(13);// 创建第i+1行是第14个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getQuDL());

                cell = row.createCell(14);// 创建第i+1行是第15个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getZhengDS());

                cell = row.createCell(15);// 创建第i+1行是第16个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getLiZSP());

                cell = row.createCell(16);// 创建第i+1行是第17个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getZaYSP());

                cell = row.createCell(17);// 创建第i+1行是第18个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getGeCGD());

                cell = row.createCell(18);// 创建第i+1行是第19个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getQinXSS());

                cell = row.createCell(19);// 创建第i+1行是第20个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getJiaDSS());

                cell = row.createCell(20);// 创建第i+1行是第21个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getYuLSD());

                cell = row.createCell(21);// 创建第i+1行是第22个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getHanZL());

                cell = row.createCell(22);// 创建第i+1行是第23个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getPoSL());

                cell = row.createCell(23);// 创建第i+1行是第24个单元格
                cell.setCellStyle(cellStyle);
                cell.setCellValue(mdatabean.getLiZLL());

//                cell = row.createCell(2);// 创建第i+1行是第3个单元格
//                cell.setCellStyle(cellStyle);
                // cell.setCellValue(student.isSex());

//                cell = row.createCell(3);// 创建第i+1行是第4个单元格
//                cell.setCellStyle(cellStyle);
                // cell.setCellValue(student.getAge());

//                cell = row.createCell(4);// 创建第i+1行是第5个单元格
//                cell.setCellStyle(cellStyle);
                //cell.setCellValue(student.getScore());

//                cell = row.createCell(5);// 创建第i+1行是第6个单元格
//                //cell.setCellValue(student.getDate());
//                cell.setCellStyle(cellStyle);
            }
            //为单个的单元格增加边框
//            row = sheet.createRow(rowCount++);
//            cell=row.createCell(0);
//            cell.setCellValue("为该单元格加上边框");
//            CellStyle borderCellStyle = workbook.createCellStyle();
//            borderCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
//            borderCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
//            borderCellStyle.setBorderTop(CellStyle.BORDER_THIN);
//            borderCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
//            borderCellStyle.setBorderRight(CellStyle.BORDER_THIN);
//            cell.setCellStyle(borderCellStyle);


//            //增加最后一行数据
//            row = sheet.createRow(rowCount++);
//            row.createCell(0).setCellValue("@版权归LJP所有，该文件仅供学习参考使用");
//            // 合并单元格
//            CellRangeAddress rangeAddress =new CellRangeAddress(rowCount-1, rowCount-1, 0, 3); // 起始行, 终止行, 起始列, 终止列
//            sheet.addMergedRegion(rangeAddress);
//            // 使用RegionUtil类为合并后的单元格添加边框
//            RegionUtil.setBorderBottom(1, rangeAddress, sheet, workbook); // 下边框
//            RegionUtil.setBorderLeft(1, rangeAddress, sheet, workbook); // 左边框
//            RegionUtil.setBorderRight(1, rangeAddress, sheet, workbook); // 有边框
//            RegionUtil.setBorderTop(1, rangeAddress, sheet, workbook); // 上边框


            workbook.write(os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            ToastUtilsCs.showToast_error(POIExcelActivity.this, "请手动打开文件管理器！");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(POIExcelActivity.this);
                builder.setTitle("permission")
                        .setMessage("点击允许才可以使用我们的app哦")
                        .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (alertDialog != null && alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                }
                                ActivityCompat.requestPermissions(POIExcelActivity.this,
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

    private void pop() {
        new XPopup.Builder(POIExcelActivity.this)
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated() {
                        Log.e("tag", "弹窗创建了");
                    }

                    @Override
                    public void onShow() {
                        Log.e("tag", "onShow");
                    }

                    @Override
                    public void onDismiss() {
                        Log.e("tag", "onDismiss");
                    }

                    //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                    @Override
                    public boolean onBackPressed() {
                        ToastUtils.showShort("我拦截的返回按键，按返回键XPopup不会关闭了");
                        return true;
                    }
                }).asConfirm("很抱歉！", "打开文件管理器失败，请自行进入“明杰科技APP导出数据”文件夹中查看",
                "取消", "确定",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {

                        //  toast("click confirm");
                    }
                }, null, false)
                .show();
    }

    private void pop_delete(final List<String> data) {
        new XPopup.Builder(POIExcelActivity.this)
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated() {
                        Log.e("tag", "弹窗创建了");
                    }

                    @Override
                    public void onShow() {
                        Log.e("tag", "onShow");
                    }

                    @Override
                    public void onDismiss() {
                        Log.e("tag", "onDismiss");
                    }

                    //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                    @Override
                    public boolean onBackPressed() {
                        ToastUtils.showShort("我拦截的返回按键，按返回键XPopup不会关闭了");
                        return true;
                    }
                }).asConfirm("数据已导出", "是否删除已导出数据！",
                "取消", "确定",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        IDataStorage dataStorage = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
                        dataStorage.delete(data2.class, data);
                        //  toast("click confirm");
                    }
                }, null, false)
                .show();
    }

}
