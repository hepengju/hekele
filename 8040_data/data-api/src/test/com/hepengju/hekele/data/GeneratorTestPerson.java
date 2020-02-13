package com.hepengju.hekele.data;


import com.hepengju.hekele.base.core.Now;
import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.generator.custom.address.ChinaAddressGenerator;
import com.hepengju.hekele.data.generator.custom.name.ChineseNameGenerator;
import com.hepengju.hekele.data.generator.custom.phone.MobileGenerator;
import com.hepengju.hekele.data.generator.date.DateGenerator;
import com.hepengju.hekele.data.generator.number.DoubleGenerator;
import com.hepengju.hekele.data.generator.number.IntegerGenerator;
import com.hepengju.hekele.data.generator.string.CodeGenerator;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.stream.IntStream;

public class GeneratorTestPerson {

	int COUNT = 100;
	
	@Test
	public void testPersonData() throws ParseException {
		
		//姓名
		Generator<String> nameGenerator = new ChineseNameGenerator();
		
		//性别
		Generator<String> genderGenerator = new CodeGenerator("M,F");
		
		//出生日期
		DateGenerator birthDateGenerator = new DateGenerator();
		birthDateGenerator.setMin("1970-01-01");
		birthDateGenerator.setMax(Now.yyyyMMdd());
		
		//家庭人口
		IntegerGenerator populationGenerator = new IntegerGenerator();
		populationGenerator.setMin(1);
		populationGenerator.setMax(10);
		
		//家庭地址
		ChinaAddressGenerator addressGenerator = new ChinaAddressGenerator();
		
		//手机号
		MobileGenerator mobileGenerator = new MobileGenerator();
		
		//年收入
		DecimalFormat df = new DecimalFormat("####.00");
		DoubleGenerator incomeGenerator = new DoubleGenerator();
		incomeGenerator.setMin(10000.0);
		incomeGenerator.setMax(99999.0);
		incomeGenerator.setScale(3);
		
		//经理,机构
		CodeGenerator managerGenerator = new CodeGenerator("M004,M005,M006,M007");
		
		//反射生成数据
		//List<Generator<?>> generatorList = new ArrayList<>();
		//generatorList.add(nameGenerator);
		//generatorList.add(genderGenerator);
		//generatorList.add(mobileGenerator);
		//generatorList.add(birthDateGenerator);
		//generatorList.add(populationGenerator);
		//generatorList.add(incomeGenerator);
		
		//手工生成数据
		StringBuilder sb = new StringBuilder();
		System.err.println("姓名\t性别\t手机号\t\t出生日期\t\t家庭人口\t年收入\t\t经理编号\t机构编号\t家庭地址");
		IntStream.range(0, COUNT).forEach(i -> {
			String managerId = managerGenerator.generate();
			String orgId = Arrays.asList("M004","M005").contains(managerId) ? "ORG0101":"ORG0102";
			sb.append(nameGenerator.generate()).append("\t")                     //姓名
			  .append(genderGenerator.generate()).append("\t")                   //性别
			  .append(mobileGenerator.generate()).append("\t")                   //手机号
			  .append(new SimpleDateFormat("yyyy-MM-dd").format(birthDateGenerator.generate())).append("\t")    //出生日期
			  .append(populationGenerator.generate()).append("\t")               //家庭人口
			  .append(df.format(incomeGenerator.generate())).append("\t")        //年收入
			  .append(managerId).append("\t")                                    //经理编号
			  .append(orgId).append("\t")                                        //机构编号
			  .append(addressGenerator.generate())                               //家庭地址
			  .append("\n");
		});
		
		System.out.println(sb);
	}
}
