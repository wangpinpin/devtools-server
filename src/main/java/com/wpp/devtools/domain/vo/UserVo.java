package com.wpp.devtools.domain.vo;


import lombok.Data;

@Data

public class UserVo {

  private Integer uid;
  private String nickName;
  private String email;
  private java.sql.Timestamp createTime;
  private String token;
}
