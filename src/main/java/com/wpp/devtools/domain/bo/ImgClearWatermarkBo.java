package com.wpp.devtools.domain.bo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @program:
 * @description:
 * @author: wpp
 * @create: 2020-08-27
 **/
@Data
public class ImgClearWatermarkBo {

    @NotBlank(message = "图片不能为空")
    private MultipartFile file;

    @NotBlank(message = "水印颜色不能为空")
    private Integer watermarkColorR;

    @NotBlank(message = "水印颜色不能为空")
    private Integer watermarkColorG;

    @NotBlank(message = "水印颜色不能为空")
    private Integer watermarkColorB;

    @NotBlank(message = "背景颜色不能为空")
    private Integer backgroundColorR;

    @NotBlank(message = "背景颜色不能为空")
    private Integer backgroundColorG;

    @NotBlank(message = "背景颜色不能为空")
    private Integer backgroundColorB;

}
