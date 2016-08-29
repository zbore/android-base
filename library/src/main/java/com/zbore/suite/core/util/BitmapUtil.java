/**
 * com.qfang.util.BitmapHelper2
 * description:
 * version：
 * author：Administrator
 * create at 2015-3-30 下午3:49:54： 
 */
package com.zbore.suite.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

/** 
* @ClassName: BitmapHelper2 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Administrator
* @date 2015-3-30 下午3:49:54 
*  
*/
public class BitmapUtil {
    
    // 默认最大的剪切文件大小为300K
    public static int MAX_IMG_LEN = 300;
    public static float RADIO_WIDTH = 480f; // 等比压缩默认宽度
    public static float RADIO_HEIGHT = 800f; // 等比压缩默认高度

    /** 
     * 尽量不要使用setImageBitmap或setImageResource 
     * 或BitmapFactory.decodeResource来设置一张大图，  
     * 因为这些函数在完成decode后，最终都是通过java层的createBitmap来完成的， 
     * 需要消耗更多内存。 
     * 因此，改用先通过BitmapFactory.decodeStream方法， 
     * 创建出一个bitmap，再将其设为ImageView的 source， 
     * decodeStream最大的秘密在于其直接调用 JNI >> nativeDecodeAsset() 来完成decode， 
     * 无需再使用java层的createBitmap，从而节省了java层的空间。 
     *  
     * 如果在读取时加上图片的Config参数，可以更有效减少加载的内存， 
     * 从而有效阻止抛出out of Memory异常 
     * 另外，decodeStream直接拿的图片来读取字节码了， 不会根据机器的各种分辨率来自动适应， 
     * 使用了decodeStream之后，需要在hdpi和mdpi，ldpi中配置相应的图片资源， 
     * 否则在不同分辨率机器上都是同样大小（像素点数量），显示出来的大小就不对了。 
     *  
     * BitmapFactory.Options.inPreferredConfig 
     *  
     * ALPHA_8:数字为8，图形参数应该由一个字节来表示,应该是一种8位的位图 
     * ARGB_4444:4+4+4+4=16，图形的参数应该由两个字节来表示,应该是一种16位的位图. 
     * ARGB_8888:8+8+8+8=32，图形的参数应该由四个字节来表示,应该是一种32位的位图. 
     * RGB_565:5+6+5=16，图形的参数应该由两个字节来表示,应该是一种16位的位图. 
     *  
     * ALPHA_8，ARGB_4444，ARGB_8888都是透明的位图，也就是所字母A代表透明。 
     * ARGB_4444:意味着有四个参数,即A,R,G,B,每一个参数由4bit表示. 
     * ARGB_8888:意味着有四个参数,即A,R,G,B,每一个参数由8bit来表示. 
     * RGB_565:意味着有三个参数,R,G,B,三个参数分别占5bit,6bit,5bit. 
     *  
     *  
     * BitmapFactory.Options.inPurgeable; 
     *  
     * 如果 inPurgeable 设为True的话表示使用BitmapFactory创建的Bitmap 
     * 用于存储Pixel的内存空间在系统内存不足时可以被回收， 
     * 在应用需要再次访问Bitmap的Pixel时（如绘制Bitmap或是调用getPixel）， 
     * 系统会再次调用BitmapFactory decoder重新生成Bitmap的Pixel数组。  
     * 为了能够重新解码图像，bitmap要能够访问存储Bitmap的原始数据。 
     *  
     * 在inPurgeable为false时表示创建的Bitmap的Pixel内存空间不能被回收， 
     * 这样BitmapFactory在不停decodeByteArray创建新的Bitmap对象， 
     * 不同设备的内存不同，因此能够同时创建的Bitmap个数可能有所不同， 
     * 200个bitmap足以使大部分的设备重新OutOfMemory错误。 
     * 当isPurgable设为true时，系统中内存不足时， 
     * 可以回收部分Bitmap占据的内存空间，这时一般不会出现OutOfMemory 错误。 
     *  
     *  
     * inInputShareable 是否深拷贝  
     * This field works in conjuction with inPurgeable. 
     * If inPurgeable is false, then this field is ignored. If inPurgeable is 
     * true, then this field determines whether the bitmap can share a reference 
     * to the input data (inputstream, array, etc.) or if it must make a deep copy. 
     *  
     * @param imgPath 
     * @return Bitmap 
     */
    public static Bitmap getBitmap(String imgPath) {
        // Get bitmap through image path  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;

        //  This field was deprecated in API level 21
        /*
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        */

        // Do not compress  
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    /** 
     * Store bitmap into specified image path 
     *  
     * @param bitmap 
     * @param outPath 
     * @throws FileNotFoundException  
     */
    public static void storeImage(Bitmap bitmap, String outPath) throws FileNotFoundException {
        FileOutputStream os = new FileOutputStream(outPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
    }

    /** 
     * Compress image by pixel, this will modify image width/height.  
     * Used to get thumbnail 
     *  
     * @param imgPath image path 
     * @param pixelW target pixel of width 
     * @param pixelH target pixel of height 
     * @return 
     */
    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容  
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now    
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸  
        float hh = pixelH;// 设置高度为800f时，可以明显看到图片缩小了  
        float ww = pixelW;// 设置宽度为480f，可以明显看到图片缩小了  
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可    
        int be = 1;//be=1表示不缩放    
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放    
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放    
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;

        newOpts.inSampleSize = be;//设置缩放比例  
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩  
        //        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除  
        return bitmap;
    }

    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出      
            os.reset();//重置baos即清空baos    
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中    
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了    
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为800f时，可以明显看到图片缩小了  
        float ww = pixelW;// 设置宽度为480f，可以明显看到图片缩小了  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可    
        int be = 1;//be=1表示不缩放    
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放    
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放    
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;

        newOpts.inSampleSize = be;//设置缩放比例    
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了    
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩  
        //      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除  
        return bitmap;
    }

    /** 
     * Compress by quality,  and generate image to the path specified 
     *  
     * @param image 
     * @param outPath 
     * @param maxSize target will be compressed to be smaller than this size.(kb) 
     * @throws IOException  
     */
    public static void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale  
        int options = 100;
        // Store the bitmap into output stream(no compress)  
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop  
        while (os.toByteArray().length / 1024 > maxSize) {
            // interval 10 
            options -= 15;
            if (options <= 0) {
                // 修正 IllegalArgumentException("quality must be 0..100")
                break;
            }
            // Clean up os  
            os.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }

        // Generate compressed image file  
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }

    /** 
     * Compress by quality,  and generate image to the path specified 
     *  
     * @param imgPath 
     * @param outPath 
     * @param maxSize target will be compressed to be smaller than this size.(kb) 
     * @param needsDelete Whether delete original file after compress 
     * @throws IOException  
     */
    public static void compressAndGenImage(String imgPath, String outPath, int maxSize) throws IOException {
        compressAndGenImage(getBitmap(imgPath), outPath, maxSize);
    }

    /** 
     * Ratio and generate thumb to the path specified 
     *  
     * @param image 
     * @param outPath 
     * @param pixelW target pixel of width 
     * @param pixelH target pixel of height 
     * @throws FileNotFoundException 
     */
    public void ratioAndGenThumb(Bitmap image, String outPath, float pixelW, float pixelH) throws FileNotFoundException {
        Bitmap bitmap = ratio(image, pixelW, pixelH);
        storeImage(bitmap, outPath);
    }

    /** 
     * Ratio and generate thumb to the path specified 
     *  
     * @param image 
     * @param outPath 
     * @param pixelW target pixel of width 
     * @param pixelH target pixel of height 
     * @throws FileNotFoundException 
     */
    public static void ratioAndGenThumb(String imgPath, String outPath, float pixelW, float pixelH) throws FileNotFoundException {
        Bitmap bitmap = ratio(imgPath, pixelW, pixelH);
        storeImage(bitmap, outPath);
    }
}
