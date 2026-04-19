#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)   // 让 equals/hashCode 包含父类字段
@TableName("user")
public class User extends BaseEntity {

    private String username;

    private String password;

    private String phone;

    // 头像 URL
    private String avatar;
}
