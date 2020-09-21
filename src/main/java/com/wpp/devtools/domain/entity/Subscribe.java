package com.wpp.devtools.domain.entity;


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
@Table(name = "subscribe")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Subscribe {

  @Id
  @GeneratedValue(generator = "jpa-uuid")
  private String id;
  private String userId;
  private String nickName;
  private String godNickName;
  private String email;
  private Integer hour;
  private String activityName;
  private boolean enabled;
  private boolean cancel;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;

}
