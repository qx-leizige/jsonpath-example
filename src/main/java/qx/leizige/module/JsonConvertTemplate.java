package qx.leizige.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * json转换模版
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonConvertTemplate implements Serializable {


    /**
     * 源json路径
     */
    private String oldPath;

    /**
     * 目标json路径
     */
    private String newPath;

    /**
     * 转换后字段类型
     */
    private String targetType;

    /**
     * value 转换表达式 （groovy脚本）
     */
    private String valueExpression;

}
