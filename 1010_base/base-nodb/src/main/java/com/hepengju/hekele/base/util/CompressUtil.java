package com.hepengju.hekele.base.util;

import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;
import com.hepengju.hekele.base.core.exception.HeException;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩/解压工具类
 * 
 * <pre>
 *  1. 格式: zip/rar/7z/tar.gz
 *  2. 方法: zip/unzip, rar/unrar
 *  
 *  commons-compress: 
 *    The Apache Commons Compress library defines an API for working with 
 *    ar, cpio, Unix dump, tar, zip, gzip, XZ, Pack200, bzip2, 7z, arj, 
 *    lzma, snappy, DEFLATE, lz4, Brotli, Zstandard, DEFLATE64 and Z files. 
 * </pre>
 * 
 * @see <a href="http://commons.apache.org/proper/commons-compress/index.html"> Apache Commons Compress </a>
 * @see <a href="https://www.cnblogs.com/chuckjam/p/9455498.html"> Java压缩技术之解压篇 </a>
 * 
 * @author hepengju
 *
 */
public class CompressUtil {

    /* ******************************解压****************************** */
    /**
     * 通用解压方法
     */
    public static void decompress(String compressFile) {
        String extName = compressFile.substring(compressFile.lastIndexOf("."), compressFile.length());
        switch (extName.toLowerCase()) {
        case "rar": unrar(compressFile); break;
        case "zip": unzip(compressFile); break;
        case "7z" : un7z (compressFile); break;
        default:
            throw new HeException("util.compress.format.notSupport", extName);
        }
    }
    
    /**
     * 默认解压路径
     */
    public static String getDefaultPath(String compressFile) {
        File srcFile = new File(compressFile);
        return srcFile.getParent();
    }
    
    /**
     * 解压到XXX(文件名)路径
     */
    public static String getWithFolderPath(String compressFile) {
        File srcFile = new File(compressFile);
        String absPath = srcFile.getAbsolutePath();
        String path = absPath.substring(0, absPath.lastIndexOf("."));
        return path;
    }
    
    /**
     * 7z
     */
    public static void un7z(String compressFile) { un7z(compressFile, getDefaultPath(compressFile));}
    public static void un7z(String compressFile, String destPath) {
        //String destPath = getDefaultPath(compressFile);
        try (SevenZFile sevenZFile = new SevenZFile(new File(compressFile))) {
            SevenZArchiveEntry entry = null;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                File tarFile = new File(destPath, entry.getName());
                if (entry.isDirectory()) {
                    tarFile.mkdirs();
                } else {
                    tarFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(tarFile)) {
                        int len = 0;
                        byte[] buf = new byte[2048];
                        while ((len = sevenZFile.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                    }
                }
            }
        } catch (IOException e) {
            throw new HeException(e);
        }
    }
    
    
    /**
     * zip
     */
    public static void unzip(String compressFile) { unzip(compressFile, getDefaultPath(compressFile), "UTF-8");}
    public static void unzip(String compressFile,String destPath) { unzip(compressFile, destPath, "UTF-8");}
    public static void unzip(String compressFile,String destPath, String encoding) {
        try (ZipFile zipFile = new ZipFile(new File(compressFile), encoding)) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry zae = entries.nextElement();
                File tarFile = new File(destPath, zae.getName());
                if (zae.isDirectory()) {
                    tarFile.mkdirs();
                } else {
                    tarFile.getParentFile().mkdirs();
                    Files.copy(zipFile.getInputStream(zae), tarFile.toPath());
                }
            }
        } catch (IOException e) {
            throw new HeException(e);
        }
    }
    
    /**
     * rar
     */
    public static void unrar(String compressFile) { unrar(compressFile, getDefaultPath(compressFile));}
    public static void unrar(String compressFile,String destPath) {
        File destp = new File(destPath);
        if(!destp.exists()) destp.mkdirs();
        try {
            Junrar.extract(compressFile, destPath);
        } catch (IOException | RarException e) {
            throw new HeException(e);
        }
    }

    /* ******************************压缩****************************** */
    /**
     * 压缩(jdk版本的压缩)
     */
    public static void zip(File destFile, File... srcFiles) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destFile))) {
            for (File srcFile : srcFiles) {
                recursionZipFile(zos, srcFile, "");
            }
        } catch(IOException e) {
            throw new HeException(e);
        }
    }
    
    /**
     * 递归压缩
     */
    private static void recursionZipFile(ZipOutputStream zos, File srcFile, String baseDir) throws IOException {
        String zeName = "".equals(baseDir) ?  srcFile.getName() : baseDir + "/" + srcFile.getName();
        if (srcFile.isFile()) {
            ZipEntry zipEntry = new ZipEntry(zeName);
            //System.out.println(zipEntry);
            zos.putNextEntry(zipEntry);
            Files.copy(srcFile.toPath(), zos);
            zos.closeEntry();
        } else {
            File[] listFiles = srcFile.listFiles();
            
            //保留空的文件夹
            if(listFiles == null || listFiles.length == 0) {
                ZipEntry zipEntry = new ZipEntry(zeName + "/");
                //System.out.println(zipEntry);
                zos.putNextEntry(zipEntry);
                zos.closeEntry();
            }else {
                for (File file : listFiles) {
                    recursionZipFile(zos, file, zeName);
                }
            }
        }
    }
    
//  /**
//  * jdk版本的zip解压缩: 存在字符编码的问题
//  * 
//  * <br> 实测: windows下压缩后, 解压缩报错: java.lang.IllegalArgumentException: MALFORMED
//  */
// public static void unzip(String compressFile) {
//     String destPath = getDefaultPath(compressFile);
//     try (ZipFile zipFile = new ZipFile(compressFile)) {
//         Enumeration<? extends ZipEntry> entries = zipFile.entries();
//         while (entries.hasMoreElements()) {
//             ZipEntry entry = entries.nextElement();
//             if (entry.isDirectory()) {
//                 new File(destPath, entry.getName()).mkdirs();
//             } else {
//                 File tarFile = new File(destPath, entry.getName());
//                 if (!tarFile.getParentFile().exists()) {
//                     tarFile.getParentFile().mkdirs();
//                 }
//                 tarFile.createNewFile();
//                 Files.copy(zipFile.getInputStream(entry), tarFile.toPath());
//             }
//         }
//     } catch (IOException e) {
//         throw new HeException(e);
//     }
// }
}
