package com.wpp.devtools.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "notebook")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Notebook {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String userId;
    private String title;
    private String content;
    private long index;
    private long indexTimestamp;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;

}
