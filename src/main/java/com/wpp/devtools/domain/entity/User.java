package com.wpp.devtools.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class User {

  @Id
  @GeneratedValue(generator = "jpa-uuid")
  private String id;
  private long subscribe;
  private String openid;
  private String nickname;
  private long sex;
  private String city;
  private String country;
  private String headimgurl;
  private java.sql.Timestamp subscribeTime;
  private String unionid;
  private String remark;
  private long groupid;
  private String tagidList;
  private String subscribeScene;
  private String qrScene;
  private String qrSceneStr;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;


}
