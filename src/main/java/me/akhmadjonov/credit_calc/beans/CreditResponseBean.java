package me.akhmadjonov.credit_calc.beans;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreditResponseBean {
    private Integer month;
    private Double credit_balance;
    private Double principal_debt;
    private Double commission;
    private Double third_party_costs;
    private Double other_costs;
    private Double percentage_debt;
    private Double total_monthly_payment;
}
