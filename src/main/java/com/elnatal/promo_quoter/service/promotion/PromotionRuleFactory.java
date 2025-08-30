package com.elnatal.promo_quoter.service.promotion;

import com.elnatal.promo_quoter.domain.PromotionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PromotionRuleFactory {

    private final Map<PromotionType, PromotionRule> rules;

    @Autowired
    public PromotionRuleFactory(PercentOffCategoryRule percentOffCategoryRule,
                                BuyXGetYRule buyXGetYRule) {
        this.rules = new HashMap<>();
        this.rules.put(PromotionType.PERCENT_OFF_CATEGORY, percentOffCategoryRule);
        this.rules.put(PromotionType.BUY_X_GET_Y, buyXGetYRule);
    }

    public PromotionRule getRule(PromotionType type) {
        PromotionRule rule = rules.get(type);
        if (rule == null) {
            throw new IllegalArgumentException("No rule found for promotion type: " + type);
        }
        return rule;
    }
}
