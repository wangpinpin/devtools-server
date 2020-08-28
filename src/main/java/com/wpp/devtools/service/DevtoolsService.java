package com.wpp.devtools.service;

import com.wpp.devtools.util.CommonUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DevtoolsService {

    /**
     * 图片转base64
     *
     * @param file
     * @return
     */
    public Object imgToBase64(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        return "data:image/" + type + ";base64," + CommonUtils.toBaseImg64(file);
    }

    /**
     * 图片加水印
     *
     * @param file
     * @return
     */
    public Object imgAddWatermark(MultipartFile file) {

        return null;

    }


    /**
     * 图片去水印
     *
     * @param file
     * @param backgroundColor
     * @param backgroundColor
     * @return
     */
    public Object imgClearWatermark(MultipartFile file, String backgroundColor, String watermarkColor, int precision) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //背景值
        int[] bgC = Arrays.asList(backgroundColor.replace(" ", "").split(",")).stream().mapToInt(Integer::parseInt).toArray();
        int[] wmC = Arrays.asList(watermarkColor.replace(" ", "").split(",")).stream().mapToInt(Integer::parseInt).toArray();

        Color wColor = new Color(bgC[0], bgC[1], bgC[2]);
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int color = bi.getRGB(i, j);
                Color oriColor = new Color(color);
                int red = oriColor.getRed();
                int greed = oriColor.getGreen();
                int blue = oriColor.getBlue();
                //替换颜色
                if ((red > wmC[0] - precision && red < wmC[0] + precision) &&
                        (greed > wmC[1] - precision && greed < wmC[1] + precision) &&
                        (blue > wmC[2] - precision && blue < wmC[2] + precision)) {
                    bi.setRGB(i, j, wColor.getRGB());
                }
            }
        }
        String originalFilename = file.getOriginalFilename();
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, type, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        byte[] base64 = Base64.encode(stream.toByteArray());
        try {
            stream.close();
            stream.flush();
            bi.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
