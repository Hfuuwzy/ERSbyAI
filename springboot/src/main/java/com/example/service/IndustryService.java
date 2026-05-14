package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Industry;
import com.example.mapper.IndustryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行业层方法
 */
@Service
public class IndustryService {

    @Resource
    private IndustryMapper IndustryMapper;

    public void add(Industry industry) {
        IndustryMapper.insert(industry);
    }

    public void updateById(Industry industry) {
        IndustryMapper.updateById(industry);
    }

    public void deleteById(Integer id) {
        IndustryMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            IndustryMapper.deleteById(id);
        }
    }

    public Industry selectById(Integer id) {
        return IndustryMapper.selectById(id);
    }

    public List<Industry> selectAll(Industry industry) {
        return IndustryMapper.selectAll(industry);
    }

    public PageInfo<Industry> selectPage(Industry industry, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Industry> list = IndustryMapper.selectAll(industry);
        return PageInfo.of(list);
    }

}
