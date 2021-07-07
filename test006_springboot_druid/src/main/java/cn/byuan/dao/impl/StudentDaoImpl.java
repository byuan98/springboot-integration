package cn.byuan.dao.impl;

import cn.byuan.dao.StudentDao;
import cn.byuan.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentDaoImpl implements StudentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;// 类似于PreparedStatement

//    添加一个学生
    @Override
    public Integer addOneStudent(Student student) {
        String sql="insert into tab_student values(null, ?, ?, ?)";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {// 匿名内部类
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, student.getStudentName());
                preparedStatement.setString(2, student.getStudentSex());
                preparedStatement.setDouble(3, student.getStudentScore());
            }
        });
    }

//    根据主键studentId删除一个学生
    @Override
    public Integer deleteOneStudentByStudentId(Integer studentId) {
        String sql="delete from tab_student where pk_student_id=?";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, studentId);
            }
        });
    }

//    根据主键studentId修改一个学生
    @Override
    public Integer updateOneStudentByStudentId(Student student) {
        String sql="update tab_student set idx_student_name=?, idx_student_sex=?, idx_student_score=? where pk_student_id=?";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, student.getStudentName());
                preparedStatement.setString(2, student.getStudentSex());
                preparedStatement.setDouble(3, student.getStudentScore());
                preparedStatement.setInt(4, student.getStudentId());
            }
        });
    }

//    根据主键studentId查询一个学生
    @Override
    public Student getOneStudentByStudentId(Integer studentId) {
        String sql="select * from tab_student where pk_student_id=?";
        return jdbcTemplate.query(sql, getRowMapper(), studentId).get(0);
    }

//    获取全部学生
    @Override
    public List<Student> getAllStudent() {
        String sql="select * from tab_student";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    private RowMapper<Student> getRowMapper(){// 查询所使用的RowMapper对象，这里单独拿出来提高代码复用性
        return new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet resultSet, int i) throws SQLException {
                Student student=new Student();
                student.setStudentId(resultSet.getInt("pk_student_id"))
                        .setStudentName(resultSet.getString("idx_student_name"))
                        .setStudentSex(resultSet.getString("idx_student_sex"))
                        .setStudentScore(resultSet.getDouble("idx_student_score"));
                return student;
            }
        };
    }
}
