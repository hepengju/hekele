package com.hepengju.hekele.admin.service;

import com.hepengju.hekele.admin.bo.Code;
import com.hepengju.hekele.admin.dao.CodeMapper;
import com.hepengju.hekele.base.core.HeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
 * </pre>
 *
 * @author hepengju
 */
@Service
public class CodeService extends HeService<CodeMapper, Code> {

    private List<Code> cachedStatusCodeList = new ArrayList<>(); //延迟启动缓存的codeList(有效未删除)


}
