package me.akhmadjonov.credit_calc.beans;


import lombok.Data;

@Data
public class CreditTypesBean {
    private Integer id;
    private String name;
    private Long min_amount;
    private Long max_amount;
    private Long amount_step;
    private Integer min_term;
    private Integer max_term;
}
