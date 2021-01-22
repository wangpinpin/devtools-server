package com.wpp.devtools.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
  private Integer uid;
  private String nickName;
  private Integer gender;
  private String email;
  private String headImg;
  private String password;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;

}
