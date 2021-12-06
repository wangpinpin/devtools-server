package com.wpp.devtools.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "game_record")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class GameRecord {

  @Id
  @GeneratedValue(generator = "jpa-uuid")
  private String id;
  private String userUUID;
  private String name;
  private String gameCode;
  private String gameLevel;
  private java.sql.Timestamp startTime;
  private java.sql.Timestamp endTime;
  private Long seconds;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;


}
