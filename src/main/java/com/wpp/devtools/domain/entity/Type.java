package com.wpp.devtools.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wpp.devtools.domain.enums.TypeEnum;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "type")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Type {

  @Id
  @GeneratedValue(generator = "jpa-uuid")
  private String id;
  private String name;
  @Enumerated(EnumType.STRING)
  private TypeEnum type;
  private long sort;
  @JsonIgnore
  private java.sql.Timestamp createTime;
  @JsonIgnore
  private java.sql.Timestamp updateTime;


}
