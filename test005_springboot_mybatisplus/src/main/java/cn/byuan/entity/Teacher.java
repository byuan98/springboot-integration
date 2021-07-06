package cn.byuan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor// 创建无参的构造方法
@AllArgsConstructor// 创建满参的构造方法
@Accessors(chain = true)// 使用链式方法
@Data// 重写toString方法等方法
@TableName("tab_teacher")// 对应表名
public class Teacher implements Serializable {
    @TableId(value = "pk_teacher_id", type = IdType.AUTO)// 主键必须有TableId注解
    private Integer teacherId;

    @TableField("teacher_name")
    private String teacherName;

    @TableField("teacher_sex")
    private String teacherSex;

    @TableField("teacher_salary")
    private Double teacherSalary;

    @TableField(value = "create_time", fill = FieldFill.INSERT)// 插入时自动填充
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)// 插入和修改时自动填充
    private Date updateTime;

    @TableField(value = "visibility", fill = FieldFill.INSERT)
    @TableLogic(value = "0", delval = "1")// 指定次字段为逻辑删除字段, 默认0是未删除, 1是已删除
    private Integer visibility;

}
