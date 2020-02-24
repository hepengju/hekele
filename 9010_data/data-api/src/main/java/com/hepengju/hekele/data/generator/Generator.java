package com.hepengju.hekele.data.generator;

import com.hepengju.hekele.base.util.BeanUtil;
import com.hepengju.hekele.data.meta.GeneratorMeta;
import com.hepengju.hekele.data.meta.GeneratorType;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据生成器
 * 
 * <br> 具有生成测试数据的功能
 *
 * @author hepengju
 *
 */
@Component
@FunctionalInterface
public interface Generator<T>{

	/**
	 * 生成数据
	 */
	T generate();

	default String generateString() {
		T value = generate();
		if (value == null) return "";

		String result = value.toString();

		// 格式化: 日期, 数字
		String format = BeanUtil.getProperty(this, "format");
		if(StringUtils.isNotBlank(format)){
			if (value instanceof Date) {
				result = new SimpleDateFormat(format).format(value);
			} else if (value instanceof TemporalAccessor) {
				result = DateTimeFormatter.ofPattern(format).format((TemporalAccessor) value);
			} else if (value instanceof Double) {
				result = new DecimalFormat(format).format(value);
			}
		}

		String prefix = BeanUtil.getProperty(this, "prefix");
		String suffix = BeanUtil.getProperty(this, "suffix");

		return ( prefix == null ? "" : prefix)
		  	  +  result
			  +( suffix == null ? "" : suffix);
	}

	/**
	 * 生成数据列表
	 */
	default List<T> generateList(int size){
		List<T> sampleData = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			sampleData.add(this.generate());
		}
		return sampleData;
	}

	/**
	 * 生成数据字符串列表
	 */
	default List<String> generateStringList(int size){
		List<String> sampleData = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			sampleData.add(this.generateString());
		}
		return sampleData;
	}

	/**
	 * 转换为GeneratorMeta
	 * @return
	 */
	default GeneratorMeta toGeneratorMeta(){
		GeneratorMeta meta = new GeneratorMeta();
		meta.setClassName(this.getClass().getName());
		meta.setName(StringUtils.uncapitalize(this.getClass().getSimpleName().replace("Generator","")));

		ApiModel apiModel = this.getClass().getAnnotation(ApiModel.class);
		meta.setColumnTitle(apiModel == null ? "未知" : apiModel.value().replace("生成器",""));
		meta.setColumnName(meta.getName());

		String packageName = this.getClass().getPackage().getName();
		if (packageName.endsWith(GeneratorType.DATE.name().toLowerCase())) {
			meta.setType(GeneratorType.DATE_NUMBER.name().toLowerCase());
		} else if (packageName.endsWith(GeneratorType.NUMBER.name().toLowerCase())) {
			meta.setType(GeneratorType.DATE_NUMBER.name().toLowerCase());
		} else if (packageName.endsWith(GeneratorType.STRING.name().toLowerCase())) {
			meta.setType(GeneratorType.STRING.name().toLowerCase());
		} else {
			meta.setType(GeneratorType.CUSTOM.name().toLowerCase());
		}

		meta.setSampleData(generateStringList(GeneratorMeta.SAMPLE_SIZE));
		BeanUtil.copyNotBlankProperties(this, meta);
		return meta;
	}

//	/**
//	 * 生成数据列表-不重复
//	 */
//	default Set<T> generateSet(int size){
//		Set<T> sampleData = new HashSet<>();
//		for (int i = 0; i < size; i++) {
//			sampleData.add(this.generate());
//		}
//
//		while (sampleData.size() < size) {
//			sampleData.add(this.generate());
//		}
//		return sampleData;
//	}
}
