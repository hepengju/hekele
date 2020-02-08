package com.hepengju.hekele.data.generator.number;

import com.hepengju.hekele.data.generator.NumberGenerator;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

@ApiModel("整数生成器")
@Data @NoArgsConstructor @AllArgsConstructor
public class IntegerGenerator implements NumberGenerator {

	private Integer min = 0;
	private Integer max = 99999;
	
	@Override
	public Integer generate() {
		return RandomUtils.nextInt(min, max);
	}
}
