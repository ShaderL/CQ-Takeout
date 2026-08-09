package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 插入员工数据
     * @param employee
     * @return
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user,update_user)" +
            "values" +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    // #{name} ==> 通过反射调用 employee.getName();
    void insert(Employee employee);

    Page<Employee> selectPage(EmployeePageQueryDTO employeePageQueryDTO);

    @Select("select count(*) from employee")
    Integer CountAll();

    @Update("update employee set status = #{status} where id = #{id}")
    void setStatus(Integer status, String id);

    void Update(Employee employee);
}
