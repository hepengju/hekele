package com.hepengju.hekele.data.web;

import lombok.Data;

import java.util.List;

@Data
public class GeneratorDTO {
    private String className;
    private String desc;
    private String type;

    private List<Object> sampleData;
}
