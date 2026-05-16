package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.EmployService;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "认证管理", description = "用户登录、注册、密码修改")
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private EmployService employService;
    @Resource
    private UserService userService;

    @Operation(summary = "健康检查", description = "服务状态检查")
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    @Operation(summary = "用户登录", description = "支持管理员、企业、求职者三种角色登录，返回包含JWT token的用户信息")
    @PostMapping("/login")
    public Result login(@Valid @RequestBody Account account) {
        Account loginAccount = null;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            loginAccount = adminService.login(account);
        }
        if (RoleEnum.EMPLOY.name().equals(account.getRole())) {
            loginAccount = employService.login(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            loginAccount = userService.login(account);
        }
        return Result.success(loginAccount);
    }

    @Operation(summary = "用户注册", description = "支持企业(EMPLOY)和求职者(USER)注册")
    @PostMapping("/register")
    public Result register(@Valid @RequestBody Account account) {
        if (RoleEnum.EMPLOY.name().equals(account.getRole())) {
            employService.register(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.register(account);

        }
        return Result.success();
    }

    @Operation(summary = "修改密码", description = "修改指定角色的用户密码")
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        if (RoleEnum.EMPLOY.name().equals(account.getRole())) {
            employService.updatePassword(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

}
