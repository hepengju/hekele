package com.hepengju.hekele.data.generator;

import com.hepengju.hekele.data.meta.GeneratorType;
import com.hepengju.hekele.data.meta.MetaGenerator;
import io.swagger.annotations.ApiModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	default MetaGenerator toMetaGenerator(){
		MetaGenerator meta = new MetaGenerator();

		meta.setClassName(this.getClass().getName());

		ApiModel apiModel = this.getClass().getAnnotation(ApiModel.class);
		meta.setDesc(apiModel == null ? "未知" : apiModel.value().replace("生成器",""));

		String packageName = this.getClass().getPackage().getName();
		if        (packageName.endsWith(GeneratorType.DATE.name().toLowerCase()))   { meta.setType(GeneratorType.DATE.name().toLowerCase());
		} else if (packageName.endsWith(GeneratorType.NUMBER.name().toLowerCase())) { meta.setType(GeneratorType.NUMBER.name().toLowerCase());
		} else if (packageName.endsWith(GeneratorType.STRING.name().toLowerCase())) { meta.setType(GeneratorType.STRING.name().toLowerCase());
		} else {                                                        meta.setType(GeneratorType.CUSTOM.name().toLowerCase());
		}

		meta.setSampleData((List<Object>) generateList(10));

		try {meta.setMin(BeanUtils.getSimpleProperty(this, "min"));}catch (Exception e){};
		try {meta.setMax(BeanUtils.getSimpleProperty(this, "max"));}catch (Exception e){};
		try {meta.setScale(Integer.parseInt(BeanUtils.getSimpleProperty(this, "scale")));}catch (Exception e){};
		try {meta.setCode(BeanUtils.getSimpleProperty(this, "code"));}catch (Exception e){};
		try {meta.setCodeMulti(Boolean.parseBoolean(BeanUtils.getSimpleProperty(this, "codeMulti")));}catch (Exception e){};

		return meta;
	}

	/**
	 * 生成数据列表
	 */
	default List<T> generateList(int size){
		List<T> sampleData = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			sampleData.add(this.generate());
		}
		return sampleData;
	}

	/**
	 * 生成数据列表-不重复
	 */
	default Set<T> generateSet(int size){
		Set<T> sampleData = new HashSet<>();
		for (int i = 0; i < size; i++) {
			sampleData.add(this.generate());
		}

		while (sampleData.size() < size) {
			sampleData.add(this.generate());
		}
		return sampleData;
	}

}
