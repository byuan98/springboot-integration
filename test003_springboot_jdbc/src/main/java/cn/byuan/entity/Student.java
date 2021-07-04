package cn.byuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor// 生成一个无参的构造方法
@AllArgsConstructor// 生成一个包含所有参数的构造方法
@Accessors(chain = true)// 生成方法的链式调用
@Data// 生成get/set方法、重写toString方法等
public class Student implements Serializable {
    private Integer studentId;// 学生id
    private String studentName;// 学生姓名
    private String studentSex;// 学生性别
    private Double studentScore;// 学生分数
}
