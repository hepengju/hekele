package com.hepengju.hekele.demo.client;

import com.hepengju.hekele.base.core.JsonR;
import com.hepengju.hekele.demo.bo.Person;
import com.hepengju.hekele.demo.dto.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "demo-service", path = "/demo", fallbackFactory = PersonClientFallbackFactory.class)
public interface PersonClient {
    @GetMapping("/peson/list")
    JsonR<Person> list(Long pageNum, Long pageSize, PersonDTO personDTO);

    @GetMapping("/person/getById")
    JsonR<Person> getById(@RequestParam String userId);
}
