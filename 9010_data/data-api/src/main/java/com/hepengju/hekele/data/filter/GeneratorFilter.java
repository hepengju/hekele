package com.hepengju.hekele.data.filter;

import com.hepengju.hekele.data.generator.Generator;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * Generator过滤器
 */
public class GeneratorFilter implements TypeFilter {

    private TypeFilter filter01 = new AssignableTypeFilter(Generator .class);
    private TypeFilter filter02 = new AnnotationTypeFilter(Order.class);

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return filter01.match(metadataReader, metadataReaderFactory)
                && filter02.match(metadataReader, metadataReaderFactory);
    }
}
