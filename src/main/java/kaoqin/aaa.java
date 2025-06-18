//package kaoqin;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class aaa {
//              if (outList.size()!=0) {
//        for (int outS = 0; outS < outList.size(); outS++) {
//            WorkOut workOut = outList.get(outS);
//            workOut.setBeginTime(changeDate(workOut.getBeginTime(), "begin"));
//            workOut.setEndTime(changeDate(workOut.getEndTime(), "end"));
//            DateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            SimpleDateFormat sdfTimes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String OutbeginTime = sdfTimes.format(fmt.parse(workOut.getBeginTime()));
//            String OutendTime = sdfTimes.format(fmt.parse(workOut.getEndTime()));
//            double dateStr = DateUtil.getHourSub(OutbeginTime, OutendTime) + 1;
//            double chuchai1 = DateUtil.getHourSub(OutbeginTime, workTime + " 08:30:00");
//            double chuchai2 = DateUtil.getHourSub(OutbeginTime, workTime + " 13:00:00");
//            //先判断是不是提了半天因公外出
//            if (DateUtil.getHourSub(workOut.getEndTime(), workOut.getEndTime()) > 12) {
//                // 解析时间
//                String beginTimeStr = changeDate(workOut.getBeginTime(), "begin");
//                String endTimeStr = changeDate(workOut.getEndTime(), "end");
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//                String outBegin = sdf.format(fmt.parse(beginTimeStr));
//                String outEnd = sdf.format(fmt.parse(endTimeStr));
//                // 定义参考时间段
//                String amStart = workTime + " 08:30:00";
//                String amEnd = workTime + " 12:30:00";
//                String pmStart = workTime + " 13:00:00";
//                String pmEnd = workTime + " 17:30:00";
//
//                // 上午判断
//                if (DateUtil.getHourSub(outBegin, amStart) <= 0 && DateUtil.getHourSub(outEnd, amEnd) >= 0) {
//                    timeRecord.put("vvar1", "");
//                    timeRecord.put("vvar2", "因公外出");
//                    totalHour += 4.5;
//                    oneDayHour1 += 4.5;
//                }
//                // 下午判断
//                if (DateUtil.getHourSub(outBegin, pmStart) <= 0 && DateUtil.getHourSub(outEnd, pmEnd) >= 0) {
//                    timeRecord.put("vvar3", "");
//                    timeRecord.put("vvar4", "因公外出");
//                    totalHour += 4.5;
//                    oneDayHour1 += 4.5;
//                }
//            } else { // 半天因公外出判断
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date morningMin = null;
//                Date morningMax = null;
//                Date afternoonMin = null;
//                Date afternoonMax = null;
//                try {
//                    Date morningStart = sdf.parse(workTime + " 00:00:00");
//                    Date morningEnd = sdf.parse(workTime + " 13:00:00");
//                    Date afternoonStart = sdf.parse(workTime + " 12:00:00");
//                    Date afternoonEnd = sdf.parse(workTime + " 23:59:59");
//                    for (WorkRecord workRecord : recordList) {
//                        String timeStr = workRecord.getCardTime();
//                        Date cardTime = sdf.parse(timeStr);
//                        // 上午段：00:00 ~ 13:00（不含13:00）
//                        if (!cardTime.before(morningStart) && cardTime.before(morningEnd)) {
//                            if (morningMin == null || cardTime.before(morningMin)) {
//                                morningMin = cardTime;
//                            }
//                            if (morningMax == null || cardTime.after(morningMax)) {
//                                morningMax = cardTime;
//                            }
//                        }
//                        // 下午段：12:00 ~ 24:00（即 23:59:59）
//                        if (!cardTime.before(afternoonStart) && !cardTime.after(afternoonEnd)) {
//                            if (afternoonMin == null || cardTime.before(afternoonMin)) {
//                                afternoonMin = cardTime;
//                            }
//                            if (afternoonMax == null || cardTime.after(afternoonMax)) {
//                                afternoonMax = cardTime;
//                            }
//                        }
//                    }
//                    if(isMorningOut(workOut.getBeginTime())){
//                        timeRecord.put("vvar1", "");
//                        timeRecord.put("vvar2", "因公外出");
//                        totalHour += 4.5;
//                        oneDayHour1 += 4.5;
//                    }
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
