package kaoqin;

import java.io.File;

public class DeleteFile {
    public static void main(String[] args) {
        String path = "D:\\Java\\apache-tomcat-9.0.50\\bin\\upload\\c40e3e5d-58b3-4935-b0c2-1739f337301f_1_(4月)员工刷卡记录表.xls";
        deleteFileByPath(path);
    }
    public static boolean deleteFileByPath(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("文件删除成功：" + filePath);
            } else {
                System.out.println("文件删除失败：" + filePath);
            }
            return deleted;
        } else {
            System.out.println("文件不存在：" + filePath);
            return false;
        }
    }
}
