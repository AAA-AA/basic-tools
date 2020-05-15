package com.github.tools.image;

import com.github.tools.pub.Base64s;
import com.github.tools.pub.Checks;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/1 9:45 下午
 **/
@Slf4j
public final class Images {
    private Images() {
    }

    public static boolean isGif(byte[] bytes) {
        boolean flag = false;
        try {
            String type = getImageType(bytes);
            if ("gif".equals(type)) {
                flag = true;
            }
        } catch (Exception e) {
            log.error("判断gif文件异常", e);
        }
        return flag;
    }

    public static String getImageType(byte[] mapObj) throws IOException {
        String type = "";
        try (ByteArrayInputStream bais = new ByteArrayInputStream(mapObj);
             MemoryCacheImageInputStream mcis = new MemoryCacheImageInputStream(bais)) {
            Iterator<ImageReader> itr = ImageIO.getImageReaders(mcis);
            while (itr.hasNext()) {
                ImageReader reader = itr.next();
                String imageName = reader.getClass().getSimpleName();
                if (imageName != null) {
                    if ("GIFImageReader".equals(imageName)) {
                        type = "gif";
                    } else if ("JPEGImageReader".equals(imageName)) {
                        type = "jpg";
                    } else if ("PNGImageReader".equals(imageName)) {
                        type = "png";
                    } else if ("BMPImageReader".equals(imageName)) {
                        type = "bmp";
                    } else {
                        type = "noPic";
                    }
                }
            }
        } catch (Exception e) {
            type = "noPic";
        }
        return type;
    }

    /**
     * 判断黑白照片
     *
     * @param src  文件流
     * @param lv   级别
     * @param clip 是否裁剪
     * @return true／false
     */
    public static boolean isBlackWhiteImage(byte[] src, int lv, boolean clip) {
        try {

            BufferedImage srcImage = ImageIO.read(new ByteArrayInputStream(src));
            Raster raster = srcImage.getRaster();

            int no = raster.getNumBands();
            if (no < 3) {
                return true;
            }
            //Set level value.
            lv = 10 * lv;
            int x = 0;
            int y = 0;
            int width = raster.getWidth();
            int height = raster.getHeight();

            if (clip) {
                int clipSize = 300;
                if (width > clipSize && height > clipSize) {
                    x = (width - clipSize) / 2;
                    y = (height - clipSize) / 2;
                    width = clipSize;
                    height = clipSize;
                }
            }

            int wh = width * height;
            int threshold = wh / 8;

            int[] pixels = srcImage.getRGB(x, y, width, height, null, 0, width);

            int resRg = 0;
            int resGb = 0;
            int resBr = 0;

            for (int i = 0; i < pixels.length; i++) {
                int r = (pixels[i] & 0xff0000) >> 16;
                int g = (pixels[i] & 0xff00) >> 8;
                int b = (pixels[i] & 0xff);
                if (Math.abs(r - g) > lv) {
                    resRg++;
                }
                if (Math.abs(g - b) > lv) {
                    resGb++;
                }
                if (Math.abs(b - r) > lv) {
                    resBr++;
                }
            }

            int res = resRg + resGb + resBr;
            if (res < threshold) {
                return true;
            }

        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * 支持将本地及互联网图片转为base64
     * @param imageUri
     * @return
     */
    public static String getBase64ByImgUri(String imageUri) {
        if (Checks.isBlank(imageUri)) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            BufferedImage img = null;
            if (imageUri.contains("http")) {
                URL url = new URL(imageUri);
                img = ImageIO.read(url);
            } else {
                File file = new File(imageUri);
                if (!file.exists()) {
                    log.error(imageUri+ "图片路径不存在！");
                    return null;
                }
                img = ImageIO.read(file);
            }
            //统一转成jpg
            ImageIO.write(img, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();
            return Base64s.encodeToString(imageBytes);
        } catch (Exception e) {
            log.error("getBase64ByImgUrl error", e);
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] readImgUrlToByteArray(String imageUrl) {
        byte[] imageBytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            URL url = new URL(imageUrl);
            BufferedImage img = ImageIO.read(url);
            //统一转成jpg
            ImageIO.write(img, "jpg", bos);
            imageBytes = bos.toByteArray();
            return imageBytes;
        } catch (Exception e) {
            log.error("readImgUrlToByteArray error", e);
        }
        return imageBytes;
    }

}
