package com.example.service;

import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private AdminService adminService;

    @Test
    void addUsesDefaultsAndAdminRoleWhenPasswordAndNameAreMissing() {
        Admin admin = new Admin();
        admin.setUsername("root");
        when(adminMapper.selectByUsername("root")).thenReturn(null);

        adminService.add(admin);

        ArgumentCaptor<Admin> captor = ArgumentCaptor.forClass(Admin.class);
        verify(adminMapper).insert(captor.capture());
        Admin inserted = captor.getValue();
        assertThat(inserted.getPassword()).isEqualTo(Constants.USER_DEFAULT_PASSWORD);
        assertThat(inserted.getName()).isEqualTo("root");
        assertThat(inserted.getRole()).isEqualTo(RoleEnum.ADMIN.name());
    }

    @Test
    void addRejectsDuplicateUsername() {
        Admin admin = new Admin();
        admin.setUsername("root");
        when(adminMapper.selectByUsername("root")).thenReturn(new Admin());

        assertThatThrownBy(() -> adminService.add(admin))
                .isInstanceOf(CustomException.class)
                .extracting("code", "msg")
                .containsExactly(ResultCodeEnum.USER_EXIST_ERROR.code, ResultCodeEnum.USER_EXIST_ERROR.msg);
        verify(adminMapper, never()).insert(admin);
    }

    @Test
    void deleteBatchDeletesEveryRequestedAdminId() {
        adminService.deleteBatch(List.of(1, 2, 3));

        verify(adminMapper).deleteById(1);
        verify(adminMapper).deleteById(2);
        verify(adminMapper).deleteById(3);
    }
}
