package com.hepengju.hekele.data;

import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.generator.gen100_date.DateGenerator;
import com.hepengju.hekele.data.generator.gen100_date.DateTimeGenerator;
import com.hepengju.hekele.data.generator.gen100_date.LocalDateGenerator;
import com.hepengju.hekele.data.generator.gen100_date.LocalDateTimeGenerator;
import com.hepengju.hekele.data.generator.gen200_number.AutoIncrementGenerator;
import com.hepengju.hekele.data.generator.gen200_number.DoubleGenerator;
import com.hepengju.hekele.data.generator.gen200_number.IntegerGenerator;
import com.hepengju.hekele.data.generator.gen300_string.*;
import com.hepengju.hekele.data.generator.gen400_custom.gen410_name.*;
import com.hepengju.hekele.data.generator.gen400_custom.gen420_password.Md5Generator;
import com.hepengju.hekele.data.generator.gen400_custom.gen420_password.PasswordGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen420_password.Sha256Generator;
import com.hepengju.hekele.data.generator.gen400_custom.gen430_phone.MobileGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen430_phone.TelephoneGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen440_computer.EmailGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen440_computer.IPv4Generator;
import com.hepengju.hekele.data.generator.gen400_custom.gen440_computer.UUIDGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen450_card.IdCardGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen460_address.ChinaAddressGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen460_address.ChinaCityGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen460_address.ChinaProvinceGenerator;
import com.hepengju.hekele.data.meta.GeneratorMeta;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.List;


/**
 * 测试生成器
 *
 * @author hepengju
 *
 */
public class GeneratorTest {

    int COUNT = 5;

    @Test
    public void testFormatPrefixSuffix(){
        DateTimeGenerator dt = new DateTimeGenerator();
        dt.setFormat("yyyyMMddHHmmss");
        dt.setPrefix("ORDER-");
        dt.setSuffix("-001");

        // 基本设置
        System.out.println(dt.generate());
        System.out.println(dt.generateString());
        System.out.println();
        printList(dt.generateList(COUNT));
        printList(dt.generateStringList(COUNT));

        // 相互转换
        GeneratorMeta generatorMeta = dt.toGeneratorMeta();
        System.out.println(generatorMeta);

        Generator generator = generatorMeta.toGenerator();
        System.out.println(generator);
    }



    @Test
    public void testGen100_date(){
        testGenerator(new DateGenerator());
        testGenerator(new DateTimeGenerator());
        testGenerator(new LocalDateGenerator());
        testGenerator(new LocalDateTimeGenerator());
    }

    @Test
    public void testGen200_number(){
        testGenerator(new IntegerGenerator());
        testGenerator(new DoubleGenerator());
        testGenerator(new AutoIncrementGenerator());
    }

    @Test
    public void testGen300_string(){
        testGenerator(new CodeGenerator());
        testGenerator(new NullGenerator());
        testGenerator(new RandomChineseGenerator());
        testGenerator(new RandomEmailGenerator());
        testGenerator(new RandomMobileGenerator());
        testGenerator(new RandomStringGenerator());
        testGenerator(new RandomNumberGenerator());
    }

    @Test
    public void testGen410_name(){
        testGenerator(new NameGenerator());
        testGenerator(new ChineseNameGenerator());
        testGenerator(new ChineseNamePinyinGenerator());
        testGenerator(new EnglishNameGenerator());
        testGenerator(new CompanyNameGenerator());
    }

    @Test
    public void testGen420_password(){
        testGenerator(new PasswordGenerator());
        testGenerator(new Md5Generator());
        testGenerator(new Sha256Generator());
    }

    @Test
    public void testGen430_phone(){
        testGenerator(new MobileGenerator());
        testGenerator(new TelephoneGenerator());
    }

    @Test
    public void testGen440_computer(){
        testGenerator(new EmailGenerator());
        testGenerator(new IPv4Generator());
        testGenerator(new UUIDGenerator());
    }

    @Test
    public void testGen450_card(){
        testGenerator(new IdCardGenerator());
    }

    @Test
    public void testGen460_address(){
        testGenerator(new ChinaAddressGenerator());
        testGenerator(new ChinaCityGenerator());
        testGenerator(new ChinaProvinceGenerator());
    }

    private void testGenerator(Generator gen) {
        System.err.println(gen.getClass());
        System.out.println(gen.generate());
        System.out.println(gen.generateString());
        System.out.println();
        System.out.println(gen.generateList(COUNT));
        System.out.println(gen.generateStringList(COUNT));


        GeneratorMeta generatorMeta = gen.toGeneratorMeta();
        System.out.println(generatorMeta);
        Generator newGen = generatorMeta.toGenerator();
        System.out.println(newGen);
        System.out.println();

        System.err.println(StringUtils.repeat("*", 100));
    }

    private void printList(List<? extends Object> objList) {
        objList.forEach(obj -> System.out.println(obj));
        System.out.println();
    }
}
