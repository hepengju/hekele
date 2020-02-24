//package com.hepengju.hekele.admin.web;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.io.IOUtils;
//import org.bytedeco.opencv.global.opencv_core;
//import org.bytedeco.opencv.global.opencv_imgcodecs;
//import org.bytedeco.opencv.global.opencv_imgproc;
//import org.bytedeco.opencv.opencv_core.Mat;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//@Api(tags = "图片处理")
//@Controller @Slf4j
//@RequestMapping("/admin/image")
//public class JavaCVController {
//
//    private String host = "http://ali.hepengju.com:8083/image/";
//    private String path = "/root/nginx/html/image/";
//
//    @ApiOperation("图片清晰度检测")
//    @PostMapping("laplacianStdDev")
//    public String laplacianStdDev(@RequestParam(value = "file", required = true) MultipartFile[] multipartFiles, Model model) throws IOException {
//
//        for (MultipartFile file : multipartFiles) {
//            String srcImageName = file.getOriginalFilename();
//            String filename = path + srcImageName;
//            try (FileOutputStream fos = new FileOutputStream(filename);) {
//                IOUtils.copy(file.getInputStream(), fos);
//            }
//
//            String srcName = FilenameUtils.getName(srcImageName);
//            String extName = FilenameUtils.getExtension(srcImageName);
//            String grayImageName = srcName + "-gray." + extName;
//            String laplImageName = srcName + "-lapl." + extName;
//
//            Mat srcImage = opencv_imgcodecs.imread(filename);
//            Mat grayImage = new Mat();
//
//            // 灰度图
//            opencv_imgproc.cvtColor(srcImage, grayImage, opencv_imgproc.COLOR_BGR2GRAY);
//            opencv_imgcodecs.imwrite(path + grayImageName, grayImage);
//
//            // 拉普拉斯
//            Mat laplImage = new Mat();
//            opencv_imgproc.Laplacian(grayImage, laplImage, opencv_core.CV_64F);
//            opencv_imgcodecs.imwrite(path + laplImageName, laplImage);
//
//            //平均值和标准差
//            Mat mean = new Mat();
//            Mat stdDev = new Mat();
//
//            //求矩阵的均值与标准差
//            opencv_core.meanStdDev(laplImage, mean, stdDev);
//            double stdDevDouble = stdDev.createIndexer().getDouble();
//
//            log.info("图片: {}, 标准差: {}", srcImageName, stdDevDouble);
//
//            model.addAttribute("stdDevDouble", stdDevDouble);
//            model.addAttribute("srcImage", host + srcImageName);
//            model.addAttribute("grayImage", host + grayImageName);
//            model.addAttribute("laplImage", host + laplImageName);
//        }
//
//        return "javacv";
//    }
//
//}
