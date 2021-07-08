package cn.byuan.service;

import cn.byuan.entity.Student;
import cn.byuan.vo.ResponseVo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
        private static Map<Integer, Student> studentMap=new HashMap<>();
        private static Integer studentId=10001;
        static {
            studentMap.put(studentId, new Student(studentId, "Godfery", 98.5));
            studentId++;
            studentMap.put(studentId, new Student(studentId, "Echo", 95.5));
            studentId++;
            studentMap.put(studentId, new Student(studentId, "Abi", 96.5));
            studentId++;
        }

//    插入一名学生返回影响行数
        public ResponseVo<Integer> addOneStudent(Student student){
            student.setStudentId(studentId);
            studentMap.put(studentId, student);
            studentId++;
            return new ResponseVo<>("插入一条数据成功", 200, 1);
        }

//    删除一位学生返回影响行数
        public  ResponseVo<Integer> deleteOneStudentByStudentId(Integer studentId){
            if(studentMap.containsKey(studentId) == false){
                return new ResponseVo<>("您输入的id不存在", 200, 0);
            }
            studentMap.remove(studentId);
            return new ResponseVo<>("删除成功", 200, 1);
        }

//    修改一位学生返回影响行数
        public ResponseVo<Integer> updateOneStudent(Student student){
            if(studentMap.containsKey(student.getStudentId()) == false){
                return new ResponseVo<>("根据学生id,您所修改的学生不存在", 200, 0);
            }
            studentMap.put(student.getStudentId(), student);
            return new ResponseVo<>("学生修改成功", 200, 1);
        }

//    输入studentId查询并返回对应的学生
        public ResponseVo<Student> getOneStudentByStudentId(Integer studentId){
            if(studentMap.containsKey(studentId) == false){
                return new ResponseVo<>("您所查询的学生不存在", 200, null);
            }
            return new ResponseVo<>("查询成功", 200, studentMap.get(studentId));
        }

//    获取所有学生
        public ResponseVo<Collection<Student>> getAllStudent(){
            return new ResponseVo<>("获取全部学生成功", 200, studentMap.values());
        }
}
