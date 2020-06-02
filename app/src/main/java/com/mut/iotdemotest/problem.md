不同fragment获取对应车型数据
解决方法：创建fragment时，传入车型


evenbus

isLoading
isIniting


时间加8小时

### 
### 1.数据导出到Excel
1.两种框架
2.动态权限获取
3.文件io流
4.数据类型强制转换 判断为空情况 （数据库有大量数据的时候）

### 
03版本的最大行数是65536行；
07版本的最大行数是1048576行；

xlsx 格式 office打开出错



//                String messageContent="{\"GPSerr\":\"0\",\"CANerr\":\"0\",\"Chesu\":\"72\",\"HanZL\":\"4\",\"E\":\"1920.658\",\"Getai\":\"86\",\"Mark\":\"A100101\",\"Time\":\"12:32:5\",\"N\":\"230.125\",\"Fukuan\":\"338\",\"QieLTL\":\"17\",\"LiZSP\":\"690\",\"ZongZTL\":\"680\",\"Shusongzhou\":\"192\",\"PoSL\":\"0\",\"QuDL\":\"14\",\"Zuoye\":\"0\",\"ZaYSP\":\"14\",\"QinXSS\":\"9\",\"ZhengDS\":\"332\",\"Bohelun\":\"27\",\"GeCGD\":\"44\",\"JiaDSS\":\"583\",\"FongJZS\":\"1495\",\"YuLSD\":\"9\",\"LiZLL\":\"3\"}";
//                Gson gson = new Gson();
//                data2 data= gson.fromJson(messageContent,data2.class);
//                IDataStorage dataStorage1 = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
//                for (int i = 0; i < 60000; i++) {
//                    //999912
//                    dataStorage1.storeOrUpdate(data,String.valueOf(i));
//
//                }
//                ToastUtilsCs.showToast_success(POIExcelActivity.this,dataStorage1.loadAll(data2.class).size()+"");
//
//                List<data2> list = new ArrayList<>();
//                Log.e("22222",dataStorage1.loadAll(data2.class).size()+"");