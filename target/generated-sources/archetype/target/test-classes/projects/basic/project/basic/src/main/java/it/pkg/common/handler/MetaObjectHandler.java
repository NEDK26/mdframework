package it.pkg.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import it.pkg.common.constant.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MetaObjectHandler implements com.baomidou.mybatisplus.core.handlers.MetaObjectHandler {

    // INSERT 时触发：填充 createTime、updateTime、deleted
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", () -> now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", () -> now, LocalDateTime.class);
        // 新记录默认为"未删除"
        this.strictInsertFill(metaObject, "deleted", () -> SystemConstants.NOT_DELETED, Integer.class);
    }

    // UPDATE 时触发：只更新 updateTime
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
