package com.hepengju.hekele.demo.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hepengju.hekele.base.core.JsonR;
import com.hepengju.hekele.base.util.PrintUtil;
import com.hepengju.hekele.data.client.DataClient;
import com.hepengju.hekele.data.util.GeneratorUtil;
import com.hepengju.hekele.demo.bo.Person;
import com.hepengju.hekele.demo.service.PersonService;
import com.hepengju.hekele.demo.vo.PersonVO;
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
    @Autowired private DataClient dataClient;

    //----------------------增删改查-------------------------------
    @ApiOperation("查询所有")
    @GetMapping("list")
    public JsonR<Person> list(Long pageNum, Long pageSize, PersonVO personVO) {
        // 用户账号和名称模糊查询，生日开始和结束日期限制查询； 如果再复杂一些，请自行编写SQL语句实现
        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<Person>()
                .ge(personVO.getBirthStart() != null, Person::getBirth, personVO.getBirthStart())
                .le(personVO.getBirthEnd() != null, Person::getBirth, personVO.getBirthEnd());
        wrapper.and(w -> w.like(StringUtils.isNotBlank(personVO.getUserCodeOrName()), Person::getUserCode, personVO.getUserCodeOrName())
                          .or()
                          .like(StringUtils.isNotBlank(personVO.getUserCodeOrName()), Person::getUserName, personVO.getUserCodeOrName()));
        return personService.listR(pageNum, pageSize, wrapper);
    }

    @ApiOperation("根据主键查询")
    @GetMapping("getById")
    public JsonR<Person> getById(@RequestParam String userId) { return personService.getRById(userId);}

    @ApiOperation("新增人员")
    @PostMapping("add")
    public JsonR add(@Valid Person person) {
        return personService.addR(person);
    }

    @ApiOperation("编辑人员")
    @PostMapping("edit")
    public JsonR edit(@Valid Person person) {
        return personService.editR(person);
    }

    @ApiOperation("删除人员")
    @PostMapping("delete")
    public JsonR delete(@RequestParam("userId") List<String> idList) {
        return personService.deleteR(idList);
    }

    //------------------------------------------------------------
    @ApiOperation("Feign远程调用DataClient的getGenList")
    @GetMapping("getGenList")
    public JsonR getGenList(){
        return dataClient.getGenList();
    }
    //------------------------------------------------------------
    @ApiOperation("获取测试数据")
    @GetMapping("getMockData")
    public JsonR getMockData(@RequestParam(defaultValue = "10") Integer count,
                     @RequestParam(defaultValue = "csv") String dataFormat){
        List<List<Object>> dataList = GeneratorUtil.getDataList(Person.class, count);
        String result = PrintUtil.printCSV(dataList);
        return JsonR.ok().addData(result);
    }

    @ApiOperation("下载测试数据")
    @GetMapping("downloadMockData")
    public void downloadMockData(@RequestParam(defaultValue = "10") Integer count,
                             @RequestParam(defaultValue = "csv") String dataFormat){
        GeneratorUtil.downloadDataList(Person.class, count, dataFormat);
    }
}
