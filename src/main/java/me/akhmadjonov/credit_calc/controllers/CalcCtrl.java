package me.akhmadjonov.credit_calc.controllers;

import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import me.akhmadjonov.credit_calc.beans.CreditBean;
import me.akhmadjonov.credit_calc.beans.CreditResponseBean;
import me.akhmadjonov.credit_calc.beans.CreditTypesBean;
import me.akhmadjonov.credit_calc.services.CalcService;
import me.akhmadjonov.credit_calc.utils.ApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("api/credit-calc")
@PermitAll
public class CalcCtrl {
    private final CalcService service;

    @PostMapping
    @PermitAll
    public HttpEntity<?> calc(@RequestBody(required = false) CreditBean bean) {
        String validate = bean.validate();
        if (Objects.nonNull(validate)) {
            return new HttpEntity<>(new ApiResponse(validate, false, null));
        }
        List<CreditResponseBean> list = service.calc(bean);
        return new HttpEntity<>(new ApiResponse("", true, list));
    }
}
