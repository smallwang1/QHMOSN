package com.piesat.busiclogic.webGis.handle;

import org.piesat.decode.grib.bean.Grib_Struct_Data;
import org.piesat.decode.grib.helper.DataAttr;
import org.piesat.decode.grib.helper.GribConfig;
import org.piesat.decode.grib.helper.GribDecoderConfigureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.nc2.Variable;
import ucar.nc2.grib.grib1.*;
import ucar.nc2.grib.grib2.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

//grib解码
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;


public class GrbHelper
{
    //private final static Logger logger = LoggerFactory.getLogger(GribDecode.class);
    private static final Logger logger = LoggerFactory.getLogger(GrbHelper.class);
    //private static String SystemDate = GribConfig.getSysDate();

    public GrbHelper()
    {

    }

    public static void main(String[] args) throws Exception{
//		GribDecode gribDecode = new GribDecode();
//		Map<String,List<Grib_Struct_Data>> map  =gribDecode.read_grib2_data("C:\\Users\\admin\\Desktop\\安徽气象共享系统测试方案\\Z_NAFP_C_BABJ_20210706210610_P_HRCLDAS_RT_BEHF_0P01_HOR-TAIR-2021070621.GRB2","F_0044_0001_R001");
//		System.out.println("=======================>");

        File file = new File("C:\\Users\\admin\\Desktop\\安徽气象共享系统测试方案\\Z_NAFP_C_BABJ_20210706210610_P_HRCLDAS_RT_BEHF_0P01_HOR-TAIR-2021070621.GRB2");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");

    }


    //读取文件中的grib1场数据
    public Map<String,List<Grib_Struct_Data>> read_grib1_data(String filename,String d_data_id,Date rcv_time)
    {
        System.out.println("文件名：" + filename +",四级编码："+d_data_id);
        logger.info("文件名：" + filename +",四级编码："+d_data_id);
        //map:<"GLB",List1>,<"ANEA",List2>... 根据区域分List
        Map<String,List<Grib_Struct_Data>> map_grib_decode_result = new HashMap<String,List<Grib_Struct_Data>>();
        map_grib_decode_result.clear();
        ucar.unidata.io.RandomAccessFile randomAccessFile = null;
        try
        {
            randomAccessFile = new ucar.unidata.io.RandomAccessFile( filename, "r" );
            Grib1RecordScanner grib1RecordScanner = new Grib1RecordScanner( randomAccessFile );
            int grib1_count = 0; //grib1记录数
            while ( grib1RecordScanner.hasNext() )
            {
                //GridData gridData = new GridData();
                Grib_Struct_Data grib_struct_data = new Grib_Struct_Data();
                grib_struct_data.setgrib_version(1);

                try {
                    Grib1Record grib1_Record = grib1RecordScanner.next(); //grib1单场数据

                    grib1_count++; //grib1记录数
                    System.out.println("第" + grib1_count + "条grib1 record:");
                    logger.info("第" + grib1_count + "条grib1 record:");

                    Grib1SectionIndicator grib1SectionIndecator_0 =  grib1_Record.getIs();
                    Grib1SectionProductDefinition grib1SectionProductDefinition_1 = grib1_Record.getPDSsection(); //1段：产品定义段
                    Grib1SectionGridDefinition grib1SectionGridDefinition_2 = grib1_Record.getGDSsection(); //2段：网格描述段
                    Grib1SectionBinaryData grib1SectionBinaryData_4 = grib1_Record.getDataSection();

                    Long mesageLength = grib1SectionIndecator_0.getMessageLength();
                    logger.info("mesageLength:" + mesageLength);

                    //String tableName = getGrib1TableName( grib1SectionProductDefinition.getCenter(), grib1SectionProductDefinition.getSubCenter(), grib1SectionProductDefinition.getTableVersion() );
                    //Grib1ParamTableReader tableReader = new Grib1ParamTableReader( tableName );
                    //Grib1Parameter grib1Parameter = tableReader.getParameter( grib1SectionProductDefinition.getParameterNumber() );

                    //获取层次类型
                    int level_type = grib1SectionProductDefinition_1.getLevelType();
                    System.out.println("level_type:" + level_type);
                    logger.info("level_type:" + level_type);
                    grib_struct_data.setLevelType(level_type);

                    //获取要素名称：由参数指示符决定
                    int ParameterNumber = grib1SectionProductDefinition_1.getParameterNumber(); //参数指示符
                    System.out.println("ParameterNumber:" + ParameterNumber);
                    logger.info("ParameterNumber:" + ParameterNumber);
                    GribDecoderConfigureHelper decoderConfigureHelper = GribDecoderConfigureHelper.instance();
                    String d_data_table_id = d_data_id; //如果是EC高分：F.0010.0002.R001,F.0010.0003.R001,F.0010.0004.R001,用GRIB表格版本号作为入口
                    int TableVersion = grib1SectionProductDefinition_1.getTableVersion();
                    if(d_data_id.compareToIgnoreCase("F.0010.0002.R001")==0||d_data_id.compareToIgnoreCase("F.0010.0003.R001")==0||d_data_id.compareToIgnoreCase("F.0010.0004.R001")==0)
                    {
                        d_data_table_id = d_data_id.substring(0, 7) + "T" + TableVersion + ".R001"; //F.0010.T228.R001
                    }
                    String element = decoderConfigureHelper.getGrib1ElementShortName(d_data_table_id, ParameterNumber, level_type);
                    if(element==null)
                    {
                        element = ParameterNumber+"";
                        //记录此条为null的element
                        String log_data = "grib1:"+d_data_table_id+","+ParameterNumber+","+level_type+","+filename + "," + getSysTime();
                        logger.error("element not found," + log_data);
                        String element_log_file = GribConfig.getLogFilePath()+"element_null_"+GribConfig.getSysDate()+".log";
                        GribConfig.write_log_to_file(element_log_file, log_data);
                        System.out.println("element not found," + log_data);

                        continue;
                    }
                    grib_struct_data.setElement(element);
                    System.out.println("element:" + element);
                    logger.info("element:" + element);
                    String temp_file = GribConfig.getLogFilePath()+"temp_element.log";
                    GribConfig.write_log_to_file(temp_file, element);

                    //设置要素服务代码，如：TEM_100
                    grib_struct_data.setelement_serv(element+"_"+level_type);

                    //获取Datetime
                    String Datetime = grib1SectionProductDefinition_1.getReferenceDate().toString();
                    String year = Datetime.substring(0, 4);
                    String month = Datetime.substring(5, 7);
                    String day = Datetime.substring(8, 10);
                    String hour = Datetime.substring(11, 13);
                    String minute = Datetime.substring(14, 16);
                    String second = Datetime.substring(17, 19);
                    String datetime1 = year+month+day+hour+minute+second;
                    String datetime2 = Datetime.substring(0, 10) + " " + Datetime.substring(11, 19);
                    System.out.println("Datetime:" + Datetime);
                    System.out.println("datatime1:" + datetime1);
                    logger.info("datatime1:" + datetime1);
                    System.out.println("datatime2:" + datetime2);
                    grib_struct_data.setTime(datetime1);
                    grib_struct_data.setDATETIME(datetime2);


                    //获取层次高度（层次1、层次2）：某些层次类型，要把层次1和层次2合起来表示?
                    int level1 = grib1SectionProductDefinition_1.getLevelValue1(); //层次1：只有层次1
                    int level2 = grib1SectionProductDefinition_1.getLevelValue2(); //
                    System.out.println("level1:" + level1 + ",level2:" + level2);
                    logger.info("level1:" + level1 + ",level2:" + level2);

                    if(level_type==101||level_type==104||level_type==106   //层次类型为116时(kwbc资料)：level1=30,level=0
                            ||level_type==108||level_type==112||level_type==114 //有两层时，level1,level2的取值不变
                            ||level_type==116||level_type==120||level_type==121
                            ||level_type==128||level_type==141)
                    {
                        level1 = level1;
                        level2 = level2;
                    }
                    else //只有一层时，将level1,level2合起来表示一层
                    {
                        level1 = level1*256 + level2;
                        level2 = 999998;
                    }
                    grib_struct_data.setLevel1(level1);
                    grib_struct_data.setLevel2(level2);
                    grib_struct_data.setlevel1_orig(level1);
                    grib_struct_data.setlevel2_orig(level2);
                    System.out.println("level1:" + level1 + ",level2:" + level2);
                    logger.info("level1:" + level1 + ",level2:" + level2);

                    //获取预报时效：有疑问?
                    int timeUnit = grib1SectionProductDefinition_1.getTimeUnit(); //时效单位：0:分，1：时，2：日
                    int timevalue1 = grib1SectionProductDefinition_1.getTimeValue1(); //时效1
                    int timevalue2 = grib1SectionProductDefinition_1.getTimeValue2(); //时效2
                    int timeRangeIndicator = grib1SectionProductDefinition_1.getTimeRangeIndicator(); //时效范围指示符
                    //要根据时效范围指示符(Psec01（20）)来判断时效的取值：
                    //先根据Psec01（20）判断赋值，
                    //如果Psec01（20）=0、1，时效=P1；
                    //如果Psec01（20）=2，时效=P2；
                    //如果Psec01（20）=3、4、5、10，时效=P2。
                    int time_value = 0;
                    if(timeRangeIndicator==0||timeRangeIndicator==1)
                    {
                        time_value = timevalue1;
                    }
                    else if(timeRangeIndicator==2||timeRangeIndicator==3||timeRangeIndicator==4||timeRangeIndicator==5||timeRangeIndicator==10)
                    {
                        time_value = timevalue2;
                    }
                    //转换时效单位
                    if(timeUnit==0)
                    {
                        time_value = time_value/60;
                    }
                    else if(timeUnit==2)
                    {
                        time_value = time_value*24;
                    }

                    System.out.println("timeUnit:" + timeUnit + ",timevalue1:" + timevalue1 + ",timevalue2:" + timevalue2 + ",timeRangeIndicator:" + timeRangeIndicator);
                    System.out.println("time_value:" + time_value);
                    logger.info("time_value:" + time_value);
                    grib_struct_data.setValidTime(time_value + "");

                    grib_struct_data.setValueByteNum(4);
                    grib_struct_data.setValuePrecision(10);
                    grib_struct_data.setGridUnits(0);

                    //格点投影类型：Grib1:第2段的数据表示类型
                    int datapresentation = grib1SectionGridDefinition_2.getGridTemplate();
                    System.out.println("datapresentation:" + datapresentation);
                    grib_struct_data.setGridProject(datapresentation);

                    //获取经纬度
                    Grib1Gds.LatLon grib1Gds_LatLon = (Grib1Gds.LatLon)grib1SectionGridDefinition_2.getGDS(); //经纬度

                    float start_lo = (float)(Math.round(grib1Gds_LatLon.lo1*1000))/1000; //保留小数点后3位
                    float end_lo = (float)(Math.round(grib1Gds_LatLon.lo2*1000))/1000;
                    if(end_lo>360)  //为了保证end_lo>start_lo，nc库的读取会将end_lo在原始值上加360
                    {
                        end_lo = end_lo - 360;
                    }
                    float lo_space = (float)(Math.round(grib1Gds_LatLon.getDx()*1000))/1000;
                    int lo_number = grib1Gds_LatLon.getNx();
                    float start_la = (float)(Math.round(grib1Gds_LatLon.la1*1000))/1000;
                    float end_la = (float)(Math.round(grib1Gds_LatLon.la2*1000))/1000;
                    float la_space = (float)(Math.round(grib1Gds_LatLon.getDy()*1000))/1000;
                    int la_number = grib1Gds_LatLon.getNy();
                    System.out.println("start_lo:" + start_lo);
                    System.out.println("end_lo:" + end_lo);
                    System.out.println("lo_space:" + lo_space);
                    System.out.println("la_number:" + la_number);
                    System.out.println("start_la:" + start_la);
                    System.out.println("end_la:" + end_la);
                    System.out.println("la_space:" + la_space);
                    System.out.println("lo_number:" + lo_number);
                    logger.info("start_lo:" + start_lo);
                    logger.info("end_lo:" + end_lo);
                    logger.info("lo_space:" + lo_space);
                    logger.info("la_number:" + la_number);
                    logger.info("start_la:" + start_la);
                    logger.info("end_la:" + end_la);
                    logger.info("la_space:" + la_space);
                    logger.info("lo_number:" + lo_number);

                    grib_struct_data.setStartX(start_lo);
                    grib_struct_data.setStartY(start_la);
                    grib_struct_data.setEndX(end_lo);
                    grib_struct_data.setEndY(end_la);
                    grib_struct_data.setXStep(lo_space);
                    grib_struct_data.setYStep(la_space);
                    grib_struct_data.setXCount(lo_number);
                    grib_struct_data.setYCount(la_number);

                    grib_struct_data.setHeightCount(1);
                    float[] heights= { grib_struct_data.getLevel1() };
                    heights[0]=grib_struct_data.getLevel1();
                    if(heights[0]>0) {
                        heights[0]=grib_struct_data.getLevel1();
                    }
                    grib_struct_data.setHeights(heights);

                    //加工中心
                    int GeneratingCentrer = grib1SectionProductDefinition_1.getCenter();
                    System.out.println("GeneratingCentrer:" + GeneratingCentrer);
                    grib_struct_data.setProductCenter(GeneratingCentrer+"");

                    //制作方法，如无时用字符串0
                    grib_struct_data.setProductMethod("0");



                    //四级编码
                    grib_struct_data.setdata_id(d_data_id);

                    //event_time
                    grib_struct_data.setevent_time(rcv_time);

                    //区域
                    BigDecimal start_la_1 = new BigDecimal(String.valueOf(start_la));
                    logger.info("start_la_1:" + start_la_1);
                    BigDecimal end_la_1 = new BigDecimal(String.valueOf(end_la));
                    logger.info("end_la_1:" + end_la_1);
                    BigDecimal start_lo_1 = new BigDecimal(String.valueOf(start_lo));
                    logger.info("start_lo_1:" + start_lo_1);
                    BigDecimal end_lo_1 = new BigDecimal(String.valueOf(end_lo));
                    logger.info("end_lo_1:" + end_lo_1);
                    String area = decoderConfigureHelper.getGribArea(start_la_1, end_la_1, start_lo_1, end_lo_1);
                    if(area==null)
                    {
                        area = start_la + "_" + end_la + "_"+ start_lo + "_"+ end_lo;
                        //记录此条为null的area
                        String log_data = "grib1:"+d_data_id+","+start_la+","+end_la+","+start_lo+","+end_lo+","+filename+ "," + getSysTime();
                        logger.error("area not found," + log_data);
                        String area_log_file = GribConfig.getLogFilePath()+"area_null_"+GribConfig.getSysDate()+".log";
                        GribConfig.write_log_to_file(area_log_file, log_data);
                        System.out.println("area not found," + log_data);

                        continue;
                    }
                    grib_struct_data.setV_AREACODE(area);
                    logger.info("area:" + area);

                    //场文件收到时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String D_RYMDHM = dateFormat.format(rcv_time);
                    grib_struct_data.setD_RYMDHM(D_RYMDHM);

                    grib_struct_data.setV04001(Integer.parseInt(year)); //年
                    grib_struct_data.setV04002(Integer.parseInt(month)); //月
                    grib_struct_data.setV04003(Integer.parseInt(day)); //日
                    grib_struct_data.setV04004(Integer.parseInt(hour)); //时
                    grib_struct_data.setV04005(Integer.parseInt(minute)); //分
                    grib_struct_data.setV04006(Integer.parseInt(second)); //秒

                    //加工中心
                    grib_struct_data.setV_CCCC_N(GeneratingCentrer);

                    //子中心
                    int subCenter = grib1SectionProductDefinition_1.getSubCenter();
                    grib_struct_data.setV_CCCC_SN(subCenter);

                    //资料加工过程标识
                    int generatingProcess = grib1SectionProductDefinition_1.getGenProcess();
                    System.out.println("generatingProcess:" + generatingProcess);

                    //场类型
                    int type_field = 1;
                    if(timevalue1==0&&timevalue2==0&&timeRangeIndicator==0)
                    {
                        type_field = 0;
                    }
                    else
                    {
                        type_field = 1;
                    }
                    grib_struct_data.setV_FIELD_TYPE(type_field);

                    //D_FILE_ID:四级编码+要素+层次类型+资料时间+区域+场类型+加工过程类型（grib1都一致）
                    String d_file_id = d_data_id + "_" + element + "_" + level_type + "_" + datetime1 + "_" + area + "_" + type_field;
                    grib_struct_data.setD_FILE_ID(d_file_id);

                    logger.info("d_file_id="+d_file_id);

                    //区域+场类型+加工过程类型
                    grib_struct_data.setarea_fieldType_genProcessType(area + "_" + type_field);

                    //NAS文件存储路径：完整路径(包含NAS文件名)
                    //String data_dir = d_data_id.substring(0, 6); //F.0010
                    String day_dir = datetime1.substring(0, 8); //日期：20170802
                    String data_time = datetime1.substring(0,10); //时次：2017080216
                    DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
                    String data_dir = data_attributes.getdata_dir();
                    String nas_filename = data_attributes.getdescription() + "_" + element +"_" + level_type + "_" + data_time +"_" + area + "_" + type_field + ".grib1";
					/*
					if(data_attributes.getIsEnsemble()) //如果是集合预报
					{
						nas_filename = data_attributes.getdescription() + "ENS" + "_" + element + "_" + data_time +"_" + area + ".grib1";
					}
					*/
                    String storage_site = GribConfig.getPathNASFile() + "/" + data_dir + "/" + day_dir + "/" + nas_filename;
                    System.out.println("storage_site:" + storage_site);
                    grib_struct_data.setD_STORAGE_SITE(storage_site);

                    grib_struct_data.setV_FILE_NAME(nas_filename);

                    //产品描述?
                    String Description = data_attributes.getdescription()+" "+element;
                    grib_struct_data.setProductDescription(Description);
                    System.out.println("Description:" + Description);

                    //文件大小
                    grib_struct_data.setD_FILE_SIZE(0);

                    //是否归档：0表示未归档
                    grib_struct_data.setD_ARCHIVE_FLAG(0);

                    //文件格式
                    grib_struct_data.setV_FILE_FORMAT("grib1");

                    //存储状态：4：实时存储
                    grib_struct_data.setD_FILE_SAVE_HIERARCHY(4);

                    //时效
                    grib_struct_data.setV04320(time_value);



                    //通信系统发过来的原始文件名
                    grib_struct_data.setV_FILE_NAME_SOURCE(filename);

                    //拆分后的单场文件名

                    //保留字段1
                    grib_struct_data.setV_RETAIN1(999998);

                    //保留字段2
                    grib_struct_data.setV_RETAIN2(999998);

                    //保留字段3
                    grib_struct_data.setV_RETAIN3(999998);

                    //加工过程类型：999998
                    grib_struct_data.settypeOfGeneratingProcess(999998);


                    //网格定义段模板
                    int grid_template_num = grib1SectionGridDefinition_2.getGridTemplate();
                    grib_struct_data.setgrid_template_num(grid_template_num);

                    //产品定义模板
                    int prod_template_num = grib1SectionProductDefinition_1.getGridDefinition();
                    grib_struct_data.setprod_template_num(prod_template_num);

                    //数据表示模板
                    //grib1SectionBinaryData_4
                    grib_struct_data.setdatarep_template_num(999998);

                    //如果是集合预报，读取集合预报的成员变量
                    if(data_attributes.getIsEnsemble()) //如果是集合预报
                    {
                        String ens_member = read_ensmember(filename);
                        logger.info("ens_member:" + ens_member);
                        grib_struct_data.setmember(ens_member);
                    }

                    //格点场数据:data数组
                    float dataArray[] = grib1_Record.readData(randomAccessFile);
                    //System.out.println("data:" + dataArray.length);
                    //转化为二维数组
					/*
					float dataArray2[][] = new float[la_number][lo_number];
					int i=0,j=0;
					if(dataArray.length==la_number*lo_number)
					{
						for(int d=0;d<dataArray.length;d++)
						{
							//System.out.print(dataArray[d] + " ");
							//dataArray2[i][j] = dataArray[d];
							dataArray2[i][j] = (float)(Math.round(dataArray[d]*10))/10;
							System.out.print(dataArray2[i][j] + " ");
							j++;
							if(j%lo_number==0)
							{
								j=0;
								i++;
								System.out.println("");
							}
						}
					}
					*/

                    grib_struct_data.setdata(dataArray);
                    //List<Grib_Struct_Data> grib_record_list = new ArrayList<Grib_Struct_Data>();
                    //area_fieldType_genProcessType，按区域_场类型_加工过程类型，如：GLB_1_2
                    String area_fieldType_genProcessType = grib_struct_data.getarea_fieldType_genProcessType();
                    if(map_grib_decode_result.get(area_fieldType_genProcessType)==null)
                    {
                        List<Grib_Struct_Data> grib_record_list = new ArrayList<Grib_Struct_Data>();
                        grib_record_list.clear();
                        grib_record_list.add(grib_struct_data);
                        map_grib_decode_result.put(area_fieldType_genProcessType, grib_record_list);
                    }
                    else
                    {
                        List<Grib_Struct_Data> grib_record_list = map_grib_decode_result.get(area_fieldType_genProcessType);
                        grib_record_list.add(grib_struct_data);
                        map_grib_decode_result.put(area_fieldType_genProcessType, grib_record_list);
                    }


                }//end big try
                catch ( Exception e ) {
					/*
					StackTraceElement[] traceElements = e.getStackTrace();
					StringBuffer buffer = new StringBuffer();//用于拼接异常方法调用堆栈信息
					for ( int i = 0; i < traceElements.length; i++ )
					{
						StackTraceElement currentElement = traceElements[ i ];
						buffer.append( "\nClassName: " + currentElement.getClassName()
								+ " \tFileName: " + currentElement.getFileName()
								+ " \tLineNumber: " + currentElement.getLineNumber()
								+ " \tMethodName: " + currentElement.getMethodName() );
					}
					*/
                    //logger.error( "捕获解码单个网格数据异常并尝试继续解码: " + fileName + "  " + e.getClass().getName() + ": " + e.getMessage() + " 下面打印异常方法调用堆栈信息:" + buffer.toString() );

                    String log_data = "解码失败，filename=" + filename + ",第" + grib1_count + "个grib1场," + getSysTime() + ",Exception:" + e.getMessage();
                    logger.error(log_data);
                    String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
                    GribConfig.write_log_to_file(error_log_file, log_data);
                }
            }//end while

        }//end try
        catch ( IOException ioException )
        {
            //ioException.getStackTrace();
            String log_data = "读取文件失败，filename=" + filename + "," + getSysTime() + ",Exception:" + ioException.getMessage();
            logger.error(log_data);
            String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
            GribConfig.write_log_to_file(error_log_file, log_data);
        }
        finally
        {
            try
            {
                if(randomAccessFile!=null)
                {
                    randomAccessFile.flush();
                    randomAccessFile.close();//关闭Grib文件
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return map_grib_decode_result;
    }

    //读取文件中的grib2场数据
    public Map<String,List<Grib_Struct_Data>> read_grib2_data(String filename,String d_data_id)

    {	 Date rcv_time = new Date();
        System.out.println("文件名：" + filename +",四级编码："+d_data_id);
        logger.info("文件名：" + filename +",四级编码："+d_data_id);
        //map:<"GLB",List1>,<"ANEA",List2>... 根据区域分List
        Map<String,List<Grib_Struct_Data>> map_grib_decode_result = new HashMap<String,List<Grib_Struct_Data>>();

        ucar.unidata.io.RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new ucar.unidata.io.RandomAccessFile( filename, "r" );
            Grib2RecordScanner grib2RecordScanner = new Grib2RecordScanner( randomAccessFile );

            int grib2_count = 0; //grib2记录数
            while ( grib2RecordScanner.hasNext() )
            {
                Grib_Struct_Data grib_struct_data = new Grib_Struct_Data();
                grib_struct_data.setgrib_version(2);

                try {
                    Grib2Record grib2_Record = grib2RecordScanner.next();

                    grib2_count++; //grib2记录数
                    System.out.println("第" + grib2_count + "条grib2 record:");
                    logger.info("第" + grib2_count + "条grib2 record:");

                    Grib2SectionIndicator grib2SectionIndicator_0 = grib2_Record.getIs(); //0段：指示符段
                    Grib2SectionIdentification grib2SectionIdentification_1 = grib2_Record.getId(); //1段：产品标识段
                    Grib2SectionGridDefinition grib2SectionGridDefinition_3 = grib2_Record.getGDSsection(); //3段：网格定义段
                    Grib2Pds grib2Pds_4 = grib2_Record.getPDS(); //4段：产品定义段
                    Grib2SectionDataRepresentation grib2SectionDataRepresentation_5 = grib2_Record.getDataRepresentationSection(); //5段：数据表示段

                    //获取层次类型
                    int level_type1 = grib2Pds_4.getLevelType1(); //层次类型1
                    int level_type2 = grib2Pds_4.getLevelType2(); //层次类型2
                    System.out.println("level_type1:" + level_type1 + ",level_type2:" + level_type2);
                    logger.info("level_type1:" + level_type1 + ",level_type2:" + level_type2);
                    int levelType = level_type1;
                    grib_struct_data.setLevelType(levelType);

                    //获取要素名称：由学科、参数种类、参数编号决定
                    int discipline = grib2SectionIndicator_0.getDiscipline();
                    int parameterCategory = grib2Pds_4.getParameterCategory() ;
                    int parameterNumber = grib2Pds_4.getParameterNumber();
                    System.out.println("discipline:" + discipline +",parameterCategory:" + parameterCategory + ",parameterNumber:" + parameterNumber);
                    logger.info("discipline:" + discipline +",parameterCategory:" + parameterCategory + ",parameterNumber:" + parameterNumber);
                    GribDecoderConfigureHelper decoderConfigureHelper = GribDecoderConfigureHelper.instance();
                    String element = decoderConfigureHelper.getGrib2ElementShortName(d_data_id, discipline, parameterCategory, parameterNumber, levelType);
                    if(element==null)
                    {
                        element = discipline+"_"+parameterCategory+"_"+parameterNumber;
                        //记录此条为null的element
                        String log_data = "grib2:"+d_data_id+","+discipline+","+parameterCategory+","+parameterNumber+","+levelType+","+filename+ "," + getSysTime();
                        logger.error("element not found," + log_data);
                        String element_log_file = GribConfig.getLogFilePath()+"element_null_"+GribConfig.getSysDate()+".log";
                        GribConfig.write_log_to_file(element_log_file, log_data);
                        System.out.println("element not found," + log_data);

                        continue;
                    }
                    grib_struct_data.setElement(element);
                    System.out.println("element:" + element);
                    logger.info("element:" + element);
                    String temp_file = GribConfig.getLogFilePath()+"temp_element.log";
                    GribConfig.write_log_to_file(temp_file, element);

                    //设置要素服务代码，如：TEM_100
                    grib_struct_data.setelement_serv(element+"_"+levelType);

                    //获取Datetime
                    int year = grib2SectionIdentification_1.getYear();
                    int month = grib2SectionIdentification_1.getMonth();
                    int day = grib2SectionIdentification_1.getDay();
                    int hour = grib2SectionIdentification_1.getHour();
                    int minute = grib2SectionIdentification_1.getMinute();
                    int second = grib2SectionIdentification_1.getSecond();
                    String year_str = String.format("%02d",year);
                    String month_str = String.format("%02d",month);
                    String day_str = String.format("%02d",day);
                    String hour_str = String.format("%02d",hour);
                    String minute_str = String.format("%02d",minute);
                    String second_str = String.format("%02d",second);
                    String datetime1 = year_str+month_str+day_str
                            +hour_str+minute_str+second_str;
                    String datetime2 = year_str + "-" + month_str + "-" + day_str + " "
                            + hour_str +":"+minute_str + ":" + second_str;
                    System.out.println("datatime1:" + datetime1);
                    logger.info("datatime1:" + datetime1);
                    System.out.println("datatime2:" + datetime2);
                    grib_struct_data.setTime(datetime1);
                    grib_struct_data.setDATETIME(datetime2);


                    //获取层次高度（层次1、层次2）:
                    float level1 = (float)grib2Pds_4.getLevelValue1(); //层次1
                    float level1_orig = level1;
                    float level2 = (float)grib2Pds_4.getLevelValue2(); //层次2
                    float level2_orig = level2;
                    if(level_type1==100||level_type1==101||level_type1==108) //把原始数据的单位pa转化为hpa
                    {
                        level1 = level1/100;
                        level2 = level2/100;
                    }
                    else if(level_type1==106) //把单位进行转化，比如：0.28变为28
                    {
                        level1 = level1*100;
                        level2 = level2*100;
                    }
                    System.out.println("level1:" + level1 + ",level2:" + level2);
                    logger.info("level1:" + level1 + ",level2:" + level2);
                    if(Float.isInfinite(level1)) //如果这个值是Infinity，转换成999998。这种情况在之前CIMISS中都赋值为0
                    {
                        //level1 = 999998;
                        level1 = 0;
                        level1_orig = 0;
                    }
                    if(Float.isInfinite(level2)) //如果这个值是Infinity，转换成999998
                    {
                        level2 = 999998;
                        level2_orig = 999998;
                    }
                    System.out.println("level1:" + level1 + ",level2:" + level2);
                    logger.info("level1:" + level1 + ",level2:" + level2);
                    grib_struct_data.setLevel1((int)level1);
                    grib_struct_data.setLevel2((int)level2);
                    grib_struct_data.setlevel1_orig((int)level1_orig);
                    grib_struct_data.setlevel2_orig((int)level2_orig);

                    //获取预报时效
                    int timevalue = grib2Pds_4.getForecastTime();
                    int timeunit = grib2Pds_4.getTimeUnit(); //时效单位：0:分，1：时，2：日

                    //如果是第四段是模板4.8，其预报时效是第50-53个八位组（根据之前在CIMISS中的解码方法）
//					if(grib2Pds_4.getTemplateNumber()==8)
//					{
//						int timevalue_ret = read_timevalue_template48(filename);
//						System.out.println("timevalue_ret:" + timevalue_ret);
//						logger.info("timevalue_ret:" + timevalue_ret);
//						if(timevalue_ret!=-1)
//						{
//							timevalue = timevalue_ret;
//						}
//					}
//					else //只转换时效单位
//					{
                    if(timeunit==0)
                    {
                        timevalue = timevalue/60;
                    }
                    else if(timeunit==2)
                    {
                        timevalue = timevalue*24;
                    }
//					}
                    System.out.println("timevalue:" + timevalue);
                    logger.info("timevalue:" + timevalue);
                    System.out.println("timeunit:" + timeunit);
                    logger.info("timeunit:" + timeunit);
                    grib_struct_data.setValidTime(timevalue + "");

                    grib_struct_data.setValueByteNum(4);
                    grib_struct_data.setValuePrecision(10);
                    grib_struct_data.setGridUnits(0);

                    //格点投影类型：Grib2:第3段的模板号
                    int TemplateNumber = grib2SectionGridDefinition_3.getGDSTemplateNumber();
                    System.out.println("TemplateNumber:" + TemplateNumber);
                    grib_struct_data.setGridProject(TemplateNumber);

                    //获取经纬度
                    Grib2Gds.LatLon grib2Gds_LatLon = ( Grib2Gds.LatLon )grib2SectionGridDefinition_3.getGDS(); //经纬度
                    float start_lo = (float)(Math.round(grib2Gds_LatLon.lo1*1000))/1000;
                    float end_lo = (float)(Math.round(grib2Gds_LatLon.lo2*1000))/1000;
                    if(end_lo>360)  //为了保证end_lo>start_lo，nc库的读取会将end_lo在原始值上加360
                    {
                        end_lo = end_lo - 360;
                    }
                    float lo_space = (float)(Math.round(grib2Gds_LatLon.deltaLon*1000))/1000;
                    int lo_number = grib2Gds_LatLon.getNx();
                    float start_la = (float)(Math.round(grib2Gds_LatLon.la1*1000))/1000;
                    float end_la = (float)(Math.round(grib2Gds_LatLon.la2*1000))/1000;
                    float la_space = (float)(Math.round(grib2Gds_LatLon.deltaLat*1000))/1000;
                    int la_number = grib2Gds_LatLon.getNy();
                    System.out.println("start_lo:" + start_lo);
                    System.out.println("end_lo:" + end_lo);
                    System.out.println("lo_space:" + lo_space);
                    System.out.println("la_number:" + la_number);
                    System.out.println("start_la:" + start_la);
                    System.out.println("end_la:" + end_la);
                    System.out.println("la_space:" + la_space);
                    System.out.println("lo_number:" + lo_number);
                    logger.info("start_lo:" + start_lo);
                    logger.info("end_lo:" + end_lo);
                    logger.info("lo_space:" + lo_space);
                    logger.info("la_number:" + la_number);
                    logger.info("start_la:" + start_la);
                    logger.info("end_la:" + end_la);
                    logger.info("la_space:" + la_space);
                    logger.info("lo_number:" + lo_number);

                    grib_struct_data.setStartX(start_lo);
                    grib_struct_data.setStartY(start_la);
                    grib_struct_data.setEndX(end_lo);
                    grib_struct_data.setEndY(end_la);
                    grib_struct_data.setXStep(lo_space);
                    grib_struct_data.setYStep(la_space);
                    grib_struct_data.setXCount(lo_number);
                    grib_struct_data.setYCount(la_number);

                    grib_struct_data.setHeightCount(1);
                    float[] heights={ grib_struct_data.getLevel1() };
                    heights[0]=grib_struct_data.getLevel1();
                    if(heights[0]>0) {
                        heights[0]=grib_struct_data.getLevel1();
                    }
                    grib_struct_data.setHeights(heights);

                    //加工中心
                    int GeneratingCentrer = grib2SectionIdentification_1.getCenter_id();
                    System.out.println("GeneratingCentrer:" + GeneratingCentrer);
                    grib_struct_data.setProductCenter(GeneratingCentrer+"");

                    //制作方法，如无时用字符串0
                    grib_struct_data.setProductMethod("0");


                    //四级编码
                    grib_struct_data.setdata_id(d_data_id);

                    //event_time
                    grib_struct_data.setevent_time(rcv_time);

                    //区域
                    BigDecimal start_la_1 = new BigDecimal(String.valueOf(start_la));
                    BigDecimal end_la_1 = new BigDecimal(String.valueOf(end_la));
                    BigDecimal start_lo_1 = new BigDecimal(String.valueOf(start_lo));
                    BigDecimal end_lo_1 = new BigDecimal(String.valueOf(end_lo));
                    String area = decoderConfigureHelper.getGribArea(start_la_1, end_la_1, start_lo_1, end_lo_1);
                    if(area==null)
                    {
                        area = start_la + "_" + end_la + "_"+ start_lo + "_"+ end_lo;
                        //记录此条为null的area
                        String log_data = "grib2:"+d_data_id+","+start_la+","+end_la+","+start_lo+","+end_lo+","+filename+ "," + getSysTime();
                        logger.error("area not found," + log_data);
                        String area_log_file = GribConfig.getLogFilePath()+"area_null_"+GribConfig.getSysDate()+".log";
                        GribConfig.write_log_to_file(area_log_file, log_data);
                        System.out.println("area not found," + log_data);

                        //	continue;
                    }
                    grib_struct_data.setV_AREACODE(area);
                    System.out.println("area:" + area);
                    logger.info("area:" + area);

                    //第一个场文件收到时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String D_RYMDHM = dateFormat.format(rcv_time);
                    grib_struct_data.setD_RYMDHM(D_RYMDHM);

                    grib_struct_data.setV04001(year); //年
                    grib_struct_data.setV04002(month); //月
                    grib_struct_data.setV04003(day); //日
                    grib_struct_data.setV04004(hour); //时
                    grib_struct_data.setV04005(minute); //分
                    grib_struct_data.setV04006(second); //秒

                    //加工中心
                    grib_struct_data.setV_CCCC_N(GeneratingCentrer);

                    //子中心
                    int subCenter = grib2SectionIdentification_1.getSubcenter_id();
                    grib_struct_data.setV_CCCC_SN(subCenter);
                    System.out.println("subCenter:" + subCenter);

                    //资料加工过程标识：
                    int generatingProcess = grib2Pds_4.getGenProcessType();
                    System.out.println("generatingProcess:" + generatingProcess);

                    //场类型
                    int ifieldtype = grib2SectionIdentification_1.getTypeOfProcessedData();
                    grib_struct_data.setV_FIELD_TYPE(ifieldtype);

                    //D_FILE_ID:四级编码+要素+层次类型+资料时间+区域+场类型+加工过程类型
                    String d_file_id = d_data_id + "_" + element + "_" + levelType + "_" + datetime1 + "_" + area + "_" + ifieldtype + "_" + generatingProcess;
                    grib_struct_data.setD_FILE_ID(d_file_id);

                    //区域+场类型+加工过程类型
                    grib_struct_data.setarea_fieldType_genProcessType(area + "_" + ifieldtype + "_" + generatingProcess);


                    //NAS文件存储路径：完整路径(包含NAS文件名)
                    //String data_dir = d_data_id.substring(0, 6); //F.0010
                    String day_dir = datetime1.substring(0, 8); //日期：20170802
                    String data_time = datetime1.substring(0,10); //时次：2017080216
//					DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
//					String data_dir = data_attributes.getdata_dir();
//					String nas_filename = data_attributes.getdescription() + "_" + element + "_" + levelType + "_" + data_time +"_" + area + "_" + ifieldtype + "_" + generatingProcess + ".grib2";
//					/*
//					if(data_attributes.getIsEnsemble()) //如果是集合预报
//					{
//						nas_filename = data_attributes.getdescription() + "ENS" + "_" + element + "_" + data_time +"_" + area + ".grib2";
//					}
//					*/
//					String storage_site = GribConfig.getPathNASFile() + "/" + data_dir + "/" + day_dir + "/" + nas_filename;
//					System.out.println("storage_site:" + storage_site);
//					grib_struct_data.setD_STORAGE_SITE(storage_site);
//
//					grib_struct_data.setV_FILE_NAME(nas_filename);
//					System.out.println("nas_filename:" + nas_filename);

                    //产品描述?
//					String Description = data_attributes.getdescription()+" "+element;
//					grib_struct_data.setProductDescription(Description);
//					System.out.println("Description:" + Description);

                    //文件大小
                    grib_struct_data.setD_FILE_SIZE(0);

                    //是否归档：0表示未归档
                    grib_struct_data.setD_ARCHIVE_FLAG(0);

                    //文件格式
                    grib_struct_data.setV_FILE_FORMAT("grib2");

                    //存储状态：4：实时存储
                    grib_struct_data.setD_FILE_SAVE_HIERARCHY(4);

                    //时效
                    grib_struct_data.setV04320(timevalue);



                    //通信系统发过来的原始文件名
                    grib_struct_data.setV_FILE_NAME_SOURCE(filename);

                    //拆分后的单场文件名

                    //加工过程类型：006，补足0到3位
                    int typeOfGeneratingProcess = grib2Pds_4.getGenProcessType();
                    grib_struct_data.settypeOfGeneratingProcess(typeOfGeneratingProcess);

                    //保留字段1
                    grib_struct_data.setV_RETAIN1(typeOfGeneratingProcess);

                    //保留字段2
                    grib_struct_data.setV_RETAIN2(999998);

                    //保留字段3
                    grib_struct_data.setV_RETAIN3(999998);

                    //网格定义段模板号
                    int grid_template_num = grib2SectionGridDefinition_3.getGDSTemplateNumber();
                    grib_struct_data.setgrid_template_num(grid_template_num);

                    //产品定义模板号
                    int prod_template_num = grib2Pds_4.getTemplateNumber();
                    grib_struct_data.setprod_template_num(prod_template_num);

                    //数据表示段模板号
                    int datarep_template_num = grib2SectionDataRepresentation_5.getDataTemplate();
                    grib_struct_data.setdatarep_template_num(datarep_template_num);

//					//如果是集合预报，读取集合预报的成员变量
//					if(data_attributes.getIsEnsemble()) //如果是集合预报
//					{
//						String ens_member = read_ensmember(filename);
//						logger.info("ens_member:" + ens_member);
//						grib_struct_data.setmember(ens_member);
//					}

                    //格点场数据:data数组
                    float dataArray[] = grib2_Record.readData(randomAccessFile);
                    grib_struct_data.setdata(dataArray);
                    System.out.println("dataArray:" + dataArray.length);

                    String area_fieldType_genProcessType = grib_struct_data.getarea_fieldType_genProcessType();
                    if(map_grib_decode_result.get(area_fieldType_genProcessType)==null)
                    {
                        List<Grib_Struct_Data> grib_record_list = new ArrayList<Grib_Struct_Data>();
                        grib_record_list.clear();
                        grib_record_list.add(grib_struct_data);
                        map_grib_decode_result.put(area_fieldType_genProcessType, grib_record_list);
                    }
                    else
                    {
                        List<Grib_Struct_Data> grib_record_list = map_grib_decode_result.get(area_fieldType_genProcessType);
                        grib_record_list.add(grib_struct_data);
                        map_grib_decode_result.put(area_fieldType_genProcessType, grib_record_list);
                    }


                }//end big try
                catch ( Exception e ) {
					/*
					StackTraceElement[] traceElements = e.getStackTrace();
					StringBuffer buffer = new StringBuffer();//用于拼接异常方法调用堆栈信息
					for ( int i = 0; i < traceElements.length; i++ )
					{
						StackTraceElement currentElement = traceElements[ i ];
						buffer.append( "\nClassName: " + currentElement.getClassName()
								+ " \tFileName: " + currentElement.getFileName()
								+ " \tLineNumber: " + currentElement.getLineNumber()
								+ " \tMethodName: " + currentElement.getMethodName() );
					}
					*/
                    //logger.error( "捕获解码单个网格数据异常并尝试继续解码: " + fileName + "  " + e.getClass().getName() + ": " + e.getMessage() + " 下面打印异常方法调用堆栈信息:" + buffer.toString() );
                    e.printStackTrace();;
                    String log_data = "解码失败，filename=" + filename + ",第" + grib2_count + "个grib2场," + getSysTime() + ",Exception:" + e.getMessage();
                    logger.error(log_data);
                    String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
                    GribConfig.write_log_to_file(error_log_file, log_data);
                }
            }//end while

        }//end try
        catch ( IOException ioException )
        {
            //ioException.getStackTrace();
            String log_data = "读取文件失败，filename=" + filename + "," + getSysTime() + ",Exception:" + ioException.getMessage();
            logger.error(log_data);
            String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
            GribConfig.write_log_to_file(error_log_file, log_data);
        }
        finally
        {
            try
            {
                if(randomAccessFile!=null)
                {
                    randomAccessFile.flush();
                    randomAccessFile.close();//关闭Grib文件
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return map_grib_decode_result;
    }

    //读取grib2的模板4.8的时效值
    //返回：-1表示出错，大于0表示正常
    private int read_timevalue_template48(String filename)
    {
        DataInputStream dataInStream = null;  //读数据的输入流

        int time_value = -1; //时效值
        int time_unit = 1; //时效单位
        try
        {
            dataInStream = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)));

            //读取第0段：16个字节
            byte[] b0 = new byte[16];
            dataInStream.read(b0, 0, 16);

            //读取第1段
            int len1 = dataInStream.readInt();
            byte[] b1 = new byte[len1-4];
            dataInStream.read(b1, 0, len1-4);

            //读取第2段
            int len2 = dataInStream.readInt();
            int num_2 = dataInStream.read(); //第2段段号

            if(num_2==2) //如果第2段存在
            {
                byte[] b2 = new byte[len2-5];
                dataInStream.read(b2, 0, len2-5);

                //读取第3段
                int len3 = dataInStream.readInt();
                byte[] b3 = new byte[len3-4];
                dataInStream.read(b3, 0, len3-4);

                //读取第4段的时效：第49字节为时效单位，第50-53字节为时效
                byte[] b4 = new byte[48];
                dataInStream.read(b4, 0, 48); //读取前48个字节
                time_unit = dataInStream.read(); //时效单位：0:分，1：时，2：日
                time_value = dataInStream.readInt(); //时效
            }
            else //如果第2段不存在，直接读取第3段
            {
                byte[] b3 = new byte[len2-5];
                dataInStream.read(b3, 0, len2-5);

                //读取第4段的时效：第49字节为时效单位，第50-53字节为时效
                byte[] b4 = new byte[48];
                dataInStream.read(b4, 0, 48); //读取前48个字节
                time_unit = dataInStream.read(); //时效单位：0:分，1：时，2：日
                time_value = dataInStream.readInt(); //时效
            }

            //转换时效单位
            if(time_unit==0)
            {
                time_value = time_value/60;
            }
            else if(time_unit==2)
            {
                time_value = time_value*24;
            }


        }
        catch (IOException ex1)
        {
            //ex1.printStackTrace();
            String log_data = "读取文件失败（为了获取模板4.8的时效值），filename=" + filename + "," + getSysTime() + ",Exception:" + ex1.getMessage();
            logger.error(log_data);
            String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
            GribConfig.write_log_to_file(error_log_file, log_data);
        }
        finally
        {
            try
            {
                if(dataInStream!=null)
                {
                    dataInStream.close();
                }
            }
            catch (IOException ex2)
            {
                ex2.printStackTrace();
            }
        }

        return time_value;
    }

    //读取集合预报的成员变量值
    //@ 返回：集合成员变量值
    private String read_ensmember(String filename)
    {
        logger.info("读取成员变量，filename=" + filename);
        String ens0 = "";
        ucar.nc2.NetcdfFile ncFile = null;
        try
        {
            ncFile = ucar.nc2.NetcdfFile.open(filename);
            //ncFile = ucar.nc2.NetcdfFile.openInMemory(filename);
            //ncFile = ucar.nc2.dataset.NetcdfDataset.openFile(filename, null);

            List<Variable> vals=ncFile.getVariables();
            Variable ensval=null;
            for(Variable val: vals) {
                if("ens".equalsIgnoreCase(val.getShortName())){
                    ensval=val;
                    break;
                }
            }
            if (null != ensval) {
                int[] ens_array = (int[]) ensval.read().copyTo1DJavaArray();
                logger.info("ens length:" + ens_array.length);
                if (ens_array.length >= 1) {
                    ens0 = String.valueOf(ens_array[0]);
                }
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            String log_data = "读取文件集合成员变量失败，filename=" + filename + "," + getSysTime() + ",Exception:" + e.getMessage();
            logger.error(log_data);
            String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
            GribConfig.write_log_to_file(error_log_file, log_data);
        }
        finally
        {
            if(ncFile!=null)
            {
                try {
                    ncFile.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        //删除读取集合变量后生成的临时文件:*.gbx,*.ncx
        //delete_gbx_ncx_file(filename);

        return ens0;
    }

    //删除读取集合变量后生成的临时文件:*.gbx,*.ncx
    private void delete_gbx_ncx_file(String filename)
    {
        String cmd_delete = "rm -rf " + filename + ".gbx* " + filename + ".ncx*";
        logger.info("cmd_delete=" + cmd_delete);

        Process process = null;
        Runtime runtime = Runtime.getRuntime();
        int result_transfer_grid_simple = 1;
        try
        {
            process = runtime.exec(cmd_delete);

            // 采用字符流读取缓冲池内容，腾出空间
            steamout(process.getErrorStream(),filename,"error_delete_gbx_ncx_file");
            steamout(process.getInputStream(),filename,"output_delete_gbx_ncx_file");

            //result_transfer_grid_simple = process.waitFor();

        }
        catch (Exception e)
        {
            e.printStackTrace();

            String log_data = "delete_gbx_ncx_file函数异常,filename="+filename + "," + getSysTime()+",Exception:" + e.getMessage();
            logger.error(log_data);
            String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
            GribConfig.write_log_to_file(error_log_file, log_data);
        }
    }


    //输出流
    private void steamout(InputStream is,String absoluteFileName,String type)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                //System.out.println(line);
                logger.info(type + ":" + line);

                //如果转化为简单压缩出错
                if(type.compareToIgnoreCase("error_transfer_grid_simple")==0)
                {
                    logger.error(type + ":" + line);

                    String log_data = "使用grib_api转化文件压缩方式失败,filename="+absoluteFileName + ","+ getSysTime() + ",Exception:将复杂压缩转化为简单压缩失败";
                    logger.error(log_data);
                    String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
                    GribConfig.write_log_to_file(error_log_file, type + ":" + line);
                    GribConfig.write_log_to_file(error_log_file, log_data);
                }

            }

        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            String log_data = "steamout函数异常,filename="+absoluteFileName + "," + getSysTime()+",Exception:" + ioe.getMessage();
            logger.error(log_data);
            String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
            GribConfig.write_log_to_file(error_log_file, log_data);
        }

    }


    //byte数组转long
    public long bytesToLong(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    /**
     * 获得系统时间
     *
     * @param
     */
    private String getSysTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentSysTime = dateFormat.format(date);
        return currentSysTime;
    }

    /**
     * 获得系统时间
     *
     * @param
     */
    private String getSysTime2()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String currentSysTime = dateFormat.format(date);
        return currentSysTime;
    }

    /**
     * 获得系统日期
     *
     * @param
     */
    private String getSysDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String currentSysTime = dateFormat.format(date);
        return currentSysTime;
    }

}
