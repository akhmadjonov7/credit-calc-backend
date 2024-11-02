package me.akhmadjonov.credit_calc.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class CreditBean {
    private Integer types_id;
    private Long amount;
    private Integer term;
    private Double annual_interest_rate;
    private Boolean is_annuity;
    private Integer currency_id;
    private Long insurance = 0L;
    private Long notary = 0L;
    private Long valuation_of_collateral = 0L;
    private Long other = 0L;
    private Double commission = 0D;


    public String validate(){
        if (Objects.isNull(types_id)){
            return "Kredit turi tanlanmadi!";
        }
        if (Objects.isNull(amount)){
            return "Kredit miqdori tanlanmadi!";
        }
        if (Objects.isNull(term)){
            return "Kredit muddati tanlanmadi";
        }
        if (Objects.isNull(annual_interest_rate)){
            return "Yillik foiz stavkasi majburiy";
        }
        if (Objects.isNull(is_annuity)){
            return "Hisoblash turi tanlanmadi!";
        }
        if (Objects.isNull(currency_id)){
            return "Valyuta majburiy";
        }
        if (Objects.isNull(insurance)){
            this.insurance = 0L;
        }
        if (Objects.isNull(notary)){
            this.notary = 0L;
        }
        if (Objects.isNull(valuation_of_collateral)){
            this.valuation_of_collateral = 0L;
        }
        if (Objects.isNull(other)){
            this.other = 0L;
        }
        if (Objects.isNull(commission)){
            this.commission = 0D;
        }
        return null;
    }
}
