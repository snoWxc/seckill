package com.xue.secondkill.vo;

import com.xue.secondkill.utils.ValidatorUtil;
import com.xue.secondkill.validator.IsMobile;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {


    private boolean required=false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        //是否需要是必填的
        required=constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            //如果是必填
            return ValidatorUtil.isMobile(value);
        }else{
            //如果是非必填,且没有填写，直接返回true
            if(StringUtils.isEmpty(value)){
                return true;
            }else{
                return ValidatorUtil.isMobile(value);
            }
        }
    }

}
