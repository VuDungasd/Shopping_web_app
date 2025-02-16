package com.project.shopping_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;


@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

  // updatable = false: k hông cho phép cập nhật khi bản ghi đã được tạo
  // insertable = false: không cho phép giá trị được insert thông qua entity khi tạo bản ghi mới
  // columnDefinition = "..." nếu không có giá trị cung cấp thì db tự động lấy thời điểm hiện tại.
  @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Timestamp createdAt;

  @Column(name = "updated_at", updatable = true , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private Timestamp updatedAt;

}
