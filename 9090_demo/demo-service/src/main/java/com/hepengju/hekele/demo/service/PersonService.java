package com.hepengju.hekele.demo.service;

import com.hepengju.hekele.base.core.HeService;
import com.hepengju.hekele.demo.bo.Person;
import com.hepengju.hekele.demo.dao.PersonMapper;
import org.springframework.stereotype.Service;

@Service
public class PersonService extends HeService<PersonMapper, Person> {
}
