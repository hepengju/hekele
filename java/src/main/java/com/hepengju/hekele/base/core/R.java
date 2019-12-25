package com.hepengju.hekele.base.core;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hepengju.hekele.base.constant.HeConst;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * 页面响应的类
 *
 * <pre>
 * 目标: 规范Controller返回的JSON结构
 * 格式:
 * {
 *   errcode   : 0,            // 错误代码 [必须, 0表示成功, -1代码未知错误, 未来预定义的异常可从配置文件message.json中读取]
 *   errmsg    : "",           // 错误信息 [必选, 默认为空字符串]
 *   errdetail : "",           // 错误详细 [可选, 错误堆栈详细信息]
 *   data      : "Object",     // 数据 [可选, 内容为对象或数组, 依赖返回值的类型, 如果是分页会自动拆开处理]
 *   code      : "{}",         // 数据中的枚举值含义 [可选] {"status": {"0": "无效", "1": "有效"}, "sex": {"M":"男","F":"女"}}
 *   page: {                   // 分页信息 [可选], 20190826 --> 与pageHelper的一致
 *     pageNum: 1,                // 第几页
 *     pageSize: 10,              // 每页大小
 *     pages: 5,                  // 总页数
 *     total: 45                  // 总记录数
 *   },
 *   extra: "Object"           // 额外 [可选], 20191223 --> 在特殊情况下, 需要追加一些扩展信息, 可添加在此处
 * }
 * </pre>
 *
 * @see HeConst.MCode
 * @author he_pe 2019-12-21
 */
@Data @Accessors(chain = true)
public class R<T> {

    private int    errcode                       ;  // 错误代码
    private String errmsg                        ;  // 错误信息
    private Object data                          ;  // 数据

    @JsonInclude(JsonInclude.Include.NON_NULL) private String errdetail                     ;  // 错误详细
    @JsonInclude(JsonInclude.Include.NON_NULL) private Map<String, Map<String,String>> code ;  // 数据中的枚举值含义
    @JsonInclude(JsonInclude.Include.NON_NULL) private Page   page                          ;  // 分页
    @JsonInclude(JsonInclude.Include.NON_NULL) private Object extra                         ;  // 扩展信息: 其他需要临时添加的内容

    @Data @Accessors(chain = true)
    private class Page{
        private long pageNum  ; // 当前页
        private long pageSize ; // 每页大小
        private long pages    ; // 总页数
        private long total    ; // 总记录数
    }

    // 静态"返回成功"和"返回错误"方法, 服务内部调用判断是否成功的方法 (由于微服务的内部调用, 需要json的反序列化, 所以不能私有化构造函数)
    public static R ok()                { return new R().setErrcode(HeConst.MCode.SUCCESS).setErrmsg("")      ; }
    public static R err(String message) { return new R().setErrcode(HeConst.MCode.UNKNOWN_ERROR).setErrmsg(message) ; }
    public static boolean isOk(R result){ return null != result && "0".equals(result.getErrcode()); }

    // 手动编写, 保持泛型; 使用添加数据后, 再使用添加枚举值代码更加合理一些; 添加数据需要对各种类型的数据进行规范化处理
    public T    getData(){ return (T) data; }
    public R<T> setData(T data){this.data = data; return this;}
    public R<T> addCode(Map<String, Map<String,String>> code) {this.code = code; return this;}
    public R<T> addData(Object data){this.data = data; return this;}

    public R addData(R data) {
        return data;
    }
    public R addData(com.baomidou.mybatisplus.extension.plugins.pagination.Page page) {  // mybatis plus的分页信息
        return this.addData(page.getRecords())
                   .setPage(new Page().setPageNum(page.getCurrent())
                                      .setPageSize(page.getSize())
                                      .setPages(page.getPages())
                                      .setTotal(page.getTotal()));
    }
    public R addData(IPage page) {  // 20191112 hepengju 追加mybatis plus的IPage接口
        return this.addData(page.getRecords())
                   .setPage(new Page().setPageNum(page.getCurrent())
                                      .setPageSize(page.getSize())
                                      .setPages(page.getPages())
                                      .setTotal(page.getTotal()));
    }
    public R addData(com.github.pagehelper.Page page) { // pagehelper的分页信息1
        return this.addData(page.getResult())
                   .setPage(new Page().setPageNum(page.getPageNum())
                                      .setPageSize(page.getPageSize())
                                      .setPages(page.getPages())
                                      .setTotal(page.getTotal()));
    }
    public R addData(com.github.pagehelper.PageInfo page) { // pagehelper的分页信息2
        return this.addData(page.getList())
                   .setPage(new Page().setPageNum(page.getPageNum())
                                      .setPageSize(page.getPageSize())
                                      .setPages(page.getPages())
                                      .setTotal(page.getTotal()));
    }

    public R addData(JSONObject data) {
        JSONObject json = (JSONObject) data;

        // 分页
        if (containAllKeys(json, asList("current", "size", "pages", "total", "records"))) {                                       // com.baomidou.mybatisplus.extension.plugins.pagination.Page
            this.setPage(extractPage(json,"current","size", "pages", "total"))
                .addData(json.get("records"));
        } else if (containAllKeys(json, asList("pageNum", "pageSize", "pages", "total", "result"))){                              // com.github.pagehelper.Page
            this.setPage(extractPage(json,"pageNum","pageSize", "pages", "total"))
                .addData(json.get("result"));
        } else if (containAllKeys(json, asList("pageNum", "pageSize", "pages", "total", "list"))){                                // com.github.pagehelper.PageInfo
            this.setPage(extractPage(json,"pageNum","pageSize", "pages", "total"))
                .addData(json.get("list"));
         } else {
            this.addData(json.toString());
        }

        return this;
    }

    /**
     * 判断jsonObject中是否包含所有keys
     */
    private boolean containAllKeys(JSONObject json, List<String> keys){
        for (String key : keys) {
            if (!json.containsKey(key)) return false;
        }
        return true;
    }

    /**
     * 抽取page信息
     */
    private Page extractPage(JSONObject json, String pageNumKey, String pageSizeKey, String pagesKey, String totalKey){
        Page page = new Page();
        if(pageNumKey  != null) page.setPageNum(json.getLongValue(pageNumKey));
        if(pageSizeKey != null) page.setPageSize(json.getLongValue(pageSizeKey));
        if(pagesKey    != null) page.setPages(json.getLongValue(pagesKey));
        if(totalKey    != null) page.setTotal(json.getLongValue(totalKey));
        return page;
    }
}
