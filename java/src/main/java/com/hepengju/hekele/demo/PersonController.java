package com.hepengju.hekele.demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hepengju.hekele.base.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 本项目的示例规范CRUD
 *
 * @author he_pe 2019-12-25
 */
@Api(tags = "示例人员")
@RestController
@RequestMapping("/demo/person/")
public class PersonController {

    @Autowired private PersonService personService;

    //----------------------增删改查-------------------------------
    @ApiOperation("查询所有")
    @GetMapping("list")
    public R<Person> list(Long pageNum, Long pageSize, Person person) {
        // 姓名模糊查询
        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<Person>()
            .like(StringUtils.isNotBlank(person.getUserName()), Person::getUserName, person.getUserName());
        return personService.listR(pageNum, pageSize, wrapper);
    }

    @ApiOperation("根据主键查询")
    @GetMapping("getById")
    public R<Person> getById(@RequestParam String userId) { return personService.getRById(userId);}

    @ApiOperation("新增人员")
    @PostMapping("add")
    public R add(@Valid Person person) {
        return personService.addR(person);
    }

    @ApiOperation("编辑人员")
    @PostMapping("edit")
    public R edit(@Valid Person person) {
        return personService.editR(person);
    }

    @ApiOperation("删除人员")
    @PostMapping("delete")
    public R delete(@RequestParam("userId") List<String> idList) {
        return personService.deleteR(idList);
    }

}
