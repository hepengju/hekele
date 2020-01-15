//package com.hepengju.hekele.javacv;
//
//import org.bytedeco.opencv.global.opencv_core;
//import org.bytedeco.opencv.global.opencv_imgcodecs;
//import org.bytedeco.opencv.global.opencv_imgproc;
//import org.bytedeco.opencv.opencv_core.Mat;
//import org.junit.jupiter.api.Test;
//
//
///**
// *
// *
// * @see <a href="https://blog.csdn.net/qq_34997906/article/details/87970817">基于OpenCV对图片清晰度、色偏和亮度的检测</a>
// * @see <a href="https://www.cnblogs.com/mtcnn/p/9411899.html">OpenCV 图像清晰度评价（相机自动对焦）</a>
// *      - Tenengrad梯度方法
// *      - Laplacian梯度方法
// *      - 方差方法
// * @see <a href="https://www.jianshu.com/p/b0fa7a8eba78">3行代码Python搞定图片清晰度识别</a>
// * @see <a href="https://blog.csdn.net/lx83350475/article/details/84228922">https://blog.csdn.net/lx83350475/article/details/84228922</a>
// * @see <a href="https://baike.baidu.com/item/%E6%A0%87%E5%87%86%E5%B7%AE/">标准差</a>
// *      - 标准差也成为"均方差", 反映一个数据集的离散程度.
// *          一个较大的标准差，代表大部分数值和其平均值之间差异较大(清晰)；
// *          一个较小的标准差，代表这些数值较接近平均值(模糊)。
// * @see <a href="https://www.codeleading.com/article/6392184266/">图片清晰度评价: Java实现</a>
// */
//public class JavaCVTest {
//
//    /**
//     * 判断图片是否模糊
//     *
//     * 原理: 期望的是一个单一的浮点数就可以表示图片的清晰度。
//     *
//     *     Pech-Pacheco 在 2000 年模式识别国际会议提出将图片中某一通道（一般用灰度值）通过拉普拉斯掩模做卷积运算，然后计算标准差，出来的值就可以代表图片清晰度。
//     * 这种方法凑效的原因就在于拉普拉斯算子定义本身。
//     * 它被用来测量图片的二阶导数，突出图片中强度快速变化的区域，和 Sobel 以及 Scharr 算子十分相似。
//     * 并且，和以上算子一样，拉普拉斯算子也经常用于边缘检测。
//     * 此外，此算法基于以下假设：如果图片具有较高方差，那么它就有较广的频响范围，代表着正常，聚焦准确的图片。
//     * 但是如果图片具有有较小方差，那么它就有较窄的频响范围，意味着图片中的边缘数量很少。正如我们所知道的，图片越模糊，其边缘就越少。
//     * 有了代表清晰度的值，剩下的工作就是设定相应的阀值，如果某图片方差低于预先定义的阈值，那么该图片就可以被认为是模糊的，高于阈值，就不是模糊的。
//     *
//     * 步骤:
//     * 1. 图片转换为灰度图
//     * 2. 对灰色图用拉普拉斯算子做卷积并计算出方差值, 用于判断图片的清晰度
//     */
//
//    //private String filename = "d:/F1.jpg"; // 17.531
//    //private String filename = "d:/F2.webp"; // 62.935
//    //private String filename = "d:/F3.jpg"; // 27.88
//    //private String filename = "d:/F4.jpg"; // 12.12
//    //private String filename = "d:/F5.jpg"; // 10.21
//
//    //private String[] files = {"d:/M1.jpg","d:/M2.jpg","d:/M3.jpg"}; // 54, 30, 21
//    private String[] files = {"d:/F6.webp","d:/F7.webp"}; // 23, 25
//
//    @Test
//    public void testIsBlur() {
//        for (String filename : files) {
//            int dot = filename.lastIndexOf(".");
//            String pre = filename.substring(0, dot);
//            String suf = filename.substring(dot + 1);
//
//            Mat srcImage = opencv_imgcodecs.imread(filename);
//            Mat grayImage = new Mat();
//
//            // 灰度图
//            opencv_imgproc.cvtColor(srcImage, grayImage, opencv_imgproc.COLOR_BGR2GRAY);
//            opencv_imgcodecs.imwrite(pre + "-gray." + suf, grayImage);
//
//            // 拉普拉斯
//            Mat laplImage = new Mat();
//            opencv_imgproc.Laplacian(grayImage, laplImage, opencv_core.CV_64F);
//            opencv_imgcodecs.imwrite(pre + "-lapl." + suf, laplImage);
//
//            //平均值和标准差
//            Mat mean = new Mat();
//            Mat stdDev = new Mat();
//
//            //求矩阵的均值与标准差
//            opencv_core.meanStdDev(laplImage, mean, stdDev);
//            System.out.println("平均值: " + mean.createIndexer().getDouble() + ", 标准差: " + stdDev.createIndexer().getDouble());
//        }
//
//    }
//
//}
