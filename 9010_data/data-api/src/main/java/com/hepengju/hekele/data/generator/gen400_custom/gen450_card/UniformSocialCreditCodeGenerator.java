package com.hepengju.hekele.data.generator.gen400_custom.gen450_card;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 统一社会信用代码
 *
 * <pre>
 *      1、统一社会信用代码设计为18位，使用阿拉伯数字或英文字母表示，由五个部分组成。
 *      2、第一部分（第1位），为登记管理部门代码。
 *      3、第二部分（第2位），为企业等纳税人类别代码。
 *      4、第三部分（第3-8位），为登记管理机关行政区划码。
 *      5、第四部分（第9-17位），为主体标识码袭。
 *      6、第五部分（第18位），为校验码，由系统自动生成。
 *
 *      参考: https://blog.csdn.net/daodfs111/article/details/105368576
 * </pre>
 */
@Data @ApiModel("统一社会信用代码")
public class UniformSocialCreditCodeGenerator {
}
