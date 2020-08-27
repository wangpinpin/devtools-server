package com.wpp.devtools.service;

import cn.jiguang.common.utils.Base64;
import com.wpp.devtools.domain.bo.ImgClearWatermarkBo;
import com.wpp.devtools.util.CommonUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
public class DevtoolsService {

    /**
     * 图片转base64
     *
     * @param file
     * @return
     */
    public Object imgToBase64(MultipartFile file) {
        return "data:image/png;base64," + CommonUtils.toBaseImg64(file);
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
     * @param iw
     * @return
     */
    public Object imgClearWatermark(ImgClearWatermarkBo iw) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(iw.getFile().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //背景值
        Color wColor = new Color(iw.getBackgroundColorR(), iw.getBackgroundColorG(), iw.getBackgroundColorB());
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int color = bi.getRGB(i, j);
                Color oriColor = new Color(color);
                int red = oriColor.getRed();
                int greed = oriColor.getGreen();
                int blue = oriColor.getBlue();
                //替换颜色
                if ((red > iw.getWatermarkColorR() - 10 && red < iw.getWatermarkColorR() + 10) &&
                        (greed > iw.getWatermarkColorG() - 10 && greed < iw.getWatermarkColorG() + 10) &&
                        (blue > iw.getWatermarkColorB() - 10 && blue < iw.getWatermarkColorB() + 10)) {
                    bi.setRGB(i, j, wColor.getRGB());
                }
            }
        }
        String originalFilename = iw.getFile().getOriginalFilename();
        String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, type, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        char[] base64 = Base64.encode(stream.toByteArray());

        bi.flush();
        return base64;
    }

}
