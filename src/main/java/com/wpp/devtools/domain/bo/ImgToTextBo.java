package com.wpp.devtools.domain.bo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-07
 **/
@Data
public class ImgToTextBo {

    private MultipartFile file;

    private String languageType;
}
