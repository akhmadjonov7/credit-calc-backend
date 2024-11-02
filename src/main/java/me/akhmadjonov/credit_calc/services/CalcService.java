package me.akhmadjonov.credit_calc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import me.akhmadjonov.credit_calc.beans.CreditBean;
import me.akhmadjonov.credit_calc.beans.CreditResponseBean;
import me.akhmadjonov.credit_calc.beans.CurrencyBean;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CalcService {
    public List<CreditResponseBean> calc(CreditBean bean) {
        List<CreditResponseBean> list = new ArrayList<>();
        Double monthly_interest_rate = (bean.getAnnual_interest_rate() / 1200);
        Double debt = Double.valueOf(bean.getAmount());
        Double principal_debt = bean.getIs_annuity() ? 0.0 : debt / bean.getTerm();
        Double total_monthly_debt = bean.getIs_annuity() ? calcMonthlyAnnuityDebt(debt, monthly_interest_rate, bean.getTerm()) : 0.0;
        for (int i = 0; i < bean.getTerm(); i++) {
            Double percentage_debt = debt * monthly_interest_rate;
            if (bean.getIs_annuity()) {
                principal_debt = total_monthly_debt - percentage_debt;
            } else {
                total_monthly_debt = principal_debt + percentage_debt;
            }
            if (i == 0) {
                Double commission = bean.getCommission() != 0D ? debt * (bean.getCommission() / 100) : 0d;
                Double third_party_costs = getByCurrency((double) (bean.getInsurance() + bean.getNotary() + bean.getValuation_of_collateral()), bean.getCurrency_id());
                Double other = getByCurrency(Double.valueOf(bean.getOther()), bean.getCurrency_id());
                Double total = total_monthly_debt + commission + third_party_costs + other;
                list.add(CreditResponseBean.builder()
                        .month(i + 1)
                        .credit_balance(debt)
                        .principal_debt(principal_debt)
                        .percentage_debt(percentage_debt)
                        .commission(commission)
                        .third_party_costs(third_party_costs)
                        .other_costs(other)
                        .total_monthly_payment(total)
                        .build());
            } else {
                list.add(CreditResponseBean.builder()
                        .month(i + 1)
                        .credit_balance(debt)
                        .principal_debt(principal_debt)
                        .percentage_debt(percentage_debt)
                        .total_monthly_payment(total_monthly_debt)
                        .build());
            }
            debt -= principal_debt;
        }
        return list;
    }

    private Double calcMonthlyAnnuityDebt(Double debt, Double monthly_interest_rate, Integer term) {
        Double a = debt * monthly_interest_rate * Math.pow(1 + monthly_interest_rate, term);
        Double divisor = Math.pow(1 + monthly_interest_rate, term) - 1;
        return a / divisor;
    }


    public CurrencyBean getCurrencies(Integer currency_id) {
        try {
            Gson gson = new Gson();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://cbu.uz/uz/arkhiv-kursov-valyut/json/")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                return null;
            }
            Type typeList = new TypeToken<List<CurrencyBean>>() {
            }.getType();
            if (response.body() == null) return null;
            List<CurrencyBean> list = gson.fromJson(response.body().string(), typeList);
            response.close();
            Optional<CurrencyBean> currency = list.stream().filter(el -> Objects.equals(el.getId(), currency_id)).findFirst();
            return currency.orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getByCurrency(Double amount, Integer currency_id) {
        if (currency_id == 1) return amount;
        CurrencyBean currency =  getCurrencies(currency_id);
        return amount / currency.getRate();
    }
}
