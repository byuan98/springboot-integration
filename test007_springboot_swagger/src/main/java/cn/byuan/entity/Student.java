package cn.byuan.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor// 生成无参的构造方法
@AllArgsConstructor// 生成满参的构造方法
@Accessors(chain = true)// 使用链式调用
@Data// 自动生成get/set方法、重写toString方法等方法
public class Student implements Serializable {
    @ApiModelProperty(value = "学生id")// 对属性进行简要说明
    private Integer studentId;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学生分数")
    private Double studentScore;

}