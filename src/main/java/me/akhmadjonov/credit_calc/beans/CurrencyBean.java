package me.akhmadjonov.credit_calc.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyBean {
    private Integer id;
    private Double Rate;
}
