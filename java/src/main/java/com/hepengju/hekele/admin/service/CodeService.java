package com.hepengju.hekele.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hepengju.hekele.admin.bo.Code;
import com.hepengju.hekele.admin.dao.CodeMapper;
import com.hepengju.hekele.base.core.HeService;
import com.hepengju.hekele.base.core.Now;
import com.hepengju.hekele.base.core.R;
import com.hepengju.hekele.base.core.exception.HeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * 代码Service
 *
 * <pre>
 * 日期: 2019-03-10
 * 说明: 由于自贸区实际测试过程中,王正英反馈有些代码的量很大(1489个),第一次请求时导致缓慢,第二次走二级缓存快一些.
 *      希望提升第一次访问时的效率, 经考虑与讨论, 将所有的代码启动后(延迟1分钟)缓存在内存中, 提升效率
 *
 * 日期: 2019-03-12
 * 说明: 为了解决依赖项目中的事务方法内调用findXXX方法的数据源未切换问题,改为启动即立刻加载,后续在内存中查(不再调用数据库)
 *
 * 日期: 2019-12-28
 * 说明: 喝可乐项目中, 暂不考虑多数据源, 为了避免新增/编辑/删除/禁用/启用等都调用cacheCode方法, 还是先使用MyBatis的二级缓存处理
 *       --> 想想还是直接缓存比较好
 * </pre>
 *
 * @author hepengju
 */
@Service
public class CodeService extends HeService<CodeMapper, Code> {

    @Autowired private CodeMapper codeMapper;
    private List<Code> cachedCodeList;

    @PostConstruct
    public void refresh() {
        cachedCodeList = this.list(new LambdaQueryWrapper<Code>()
                .eq(Code::getEnableFlag, "1")
                .orderByAsc(Code::getSysCode, Code::getTypeCode, Code::getItemSort));
    }

    public List<Code> getByTypeCodes(String... typeCodes) {
        List<String> typeCodeList = Arrays.asList(typeCodes);
        return cachedCodeList.stream()
                             .filter(code -> typeCodeList.contains(code.getTypeCode()))
                             .collect(toList());
    }


    public Map<String, Map<String, String>> getMapByTypeCodes(String... typeCodes) {
        List<Code> codeList = getByTypeCodes(typeCodes);
        return codeList.stream().collect(
                groupingBy(Code::getTypeCode, LinkedHashMap::new,
                        toMap(Code::getItemCode, Code::getItemName, (e1, e2) -> e1, LinkedHashMap::new)));
    }

    /**
     * 清空并重新插入所有代码-开发环境使用
     */
    public R truncateThenInsertAll(List<Code> codeList) {
        // 清空表
        codeMapper.truncate();

        Date now = new Date();
        String userCode = Now.userCode();
        String userName = Now.userName();

        // 关键字段非空验证
        for (Code code : codeList) {
            if (StringUtils.isAnyBlank(code.getSysCode(), code.getTypeCode(), code.getTypeName(), code.getItemCode(), code.getItemName()))
                throw new HeException("code.typeAndItem.noneNull");
            code.setCreateTime(now);
            code.setCreateUserCode(userCode);
            code.setCreateUserName(userName);
        }

        // typeCode, itemCode联合唯一验证
        List<String> typeCodeItemCodeList = new ArrayList<>();
        for (Code code : codeList) {
           String typeCodeItemCode = code.getTypeCode() + "-" + code.getItemCode();
           if(typeCodeItemCodeList.contains(typeCodeItemCode)) throw new HeException("code.typeCodeItemCode.repeat", code.getTypeCode(), code.getItemCode());
           typeCodeItemCodeList.add(typeCodeItemCode);
        }
        this.saveBatch(codeList);
        this.refresh();
        return R.ok();
    }
}
