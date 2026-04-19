#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseEntity implements Serializable {

    // 主键，数据库自增
    @TableId(type = IdType.AUTO)
    private Long id;

    // 创建时间，由 MetaObjectHandler 在 INSERT 时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间，由 MetaObjectHandler 在 INSERT 和 UPDATE 时自动填充
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 逻辑删除标志：0=未删除，1=已删除
    // 加了 @TableLogic 后，MP 的所有查询会自动拼上 WHERE deleted = 0
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
