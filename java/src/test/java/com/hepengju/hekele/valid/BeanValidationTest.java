package com.hepengju.hekele.valid;

import com.hepengju.hekele.admin.bo.Role;
import com.hepengju.hekele.base.util.ValidUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Bean验证
 */
public class BeanValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validatorTest() {
        Role role = new Role();
        role.setRoleCode("");
        role.setRoleName("");

        ValidUtil.validateBean(role);
        // 验证所有bean的所有约束
        //Set<ConstraintViolation<Role>> constraintViolations = validator.validate(role);
//        // 验证单个属性
//        Set<ConstraintViolation<Role>> constraintViolations2 = validator.validateProperty(role, "roleName");
//        // 检查给定类的单个属性是否可以成功验证
//        Set<ConstraintViolation<Role>> constraintViolations3 = validator.validateValue(Role.class, "roleCode", "sa!");
//
//        constraintViolations.forEach(constraintViolation -> System.out.println(constraintViolation.getMessage()));
//        constraintViolations2.forEach(constraintViolation -> System.out.println(constraintViolation.getMessage()));
//        constraintViolations3.forEach(constraintViolation -> System.out.println(constraintViolation.getMessage()));
        //System.out.println(constraintViolations);
    }
}
