package com.hepengju.hekele.base.core;

import java.util.Map;

public interface CodeService {
    Map<String, Map<String, String>> getMapByTypeCodes(String... typeCodes);
}
