package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工信息
     * @Param EmployeeDTO 员工数据传输对象
     * @return Result通用结果对象
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO dto){
        return employeeService.save(dto);
    }


    /**
     * 分页查询员工信息
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询员工")
    public Result<PageResult<Employee>> getPage(EmployeePageQueryDTO employeePageQueryDTO){
        return employeeService.getPage(employeePageQueryDTO);
    }


    /**
     * 启用或禁用员工
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用/禁用员工")
    public Result enableOrDisable(@PathVariable Integer status, @RequestParam String id){
        employeeService.setStatus(status, id);
        return Result.success();
    }

    // 区分“路径变量”和“请求参数”
    // 请求参数不需要写("/{id}")，而且请求参数要写 @RequestParam
    // 路径变量要写这个，参数写 @PathVariable
    @GetMapping("/{id}")
    @ApiOperation("根据 id 查询员工")
    public Result getById(@PathVariable String id){
        // System.out.println("传入参数为："+id);
        // System.out.println("格式变换后为："+Long.parseLong(id));
        return employeeService.getById(Long.parseLong(id));
    }

    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        return employeeService.updateEmployee(employeeDTO);
    }


}
