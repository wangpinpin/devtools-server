package com.wpp.devtools.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NotebookBo {


  private String id;

  @ApiModelProperty("标题")
  private String title;

  @ApiModelProperty("内容")
  private String content;


}
