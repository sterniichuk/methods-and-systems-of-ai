package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.ActionDTO;
import com.example.expertsystemservice.domain.RuleDTO;
import com.example.expertsystemservice.domain.RuleType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleDTOValidator {

    public String isValid(RuleDTO r) {//returns empty string if valid or message why not
        return switch (r.decisionInfo().type()) {
            case BINARY -> checkActionsBinary(r);
            case FORMULA, BINARY_FORMULA, VALUE_FORMULA -> {
                String checkActions = checkActions(r);
                if (!checkActions.isEmpty()) {
                    yield checkActions;
                }
                String checkFormula = checkFormula(r);
                if (!checkFormula.isEmpty()) {
                    yield checkFormula;
                }
                if(r.decisionInfo().type() == RuleType.BINARY_FORMULA){
                    yield "";
                }
                yield checkActionFormulas(r);
            }
        };
    }

    private String checkActionFormulas(RuleDTO r) {
        if(hasActionEmptyFormula(r.thenAction())){
            return "provide formulas for then action";
        }
        return "";
    }

    private static boolean hasActionEmptyFormula(List<ActionDTO> actions) {
        return actions.stream()
                .anyMatch(x -> x.formula() == null || x.formula().isBlank());
    }

    private static String checkFormula(RuleDTO r) {
        String provideData = "provide all not blank variables and formula";
        if (r.decisionInfo().formula() == null || r.decisionInfo().variables() == null) {
            return provideData;
        } else if (r.decisionInfo().formula().isBlank() || r.decisionInfo().variables().isEmpty()) {
            return provideData;
        } else if (r.decisionInfo().variables().stream().anyMatch(x -> x == null || x.isBlank())) {
            return provideData;
        }
        return "";
    }

    private static String checkActionsBinary(RuleDTO r) {
        String notEmpty = "binary rule must contain then && else actions";
        if (r.thenAction() == null || r.elseAction() == null) {
            return notEmpty;
        } else if (r.thenAction().isEmpty() || r.elseAction().isEmpty()) {
            return notEmpty;
        }
        return "";
    }
    private static String checkActions(RuleDTO r) {
        String notEmpty = "Rule should contain any action";
        if (r.thenAction() == null && r.elseAction() == null) {
            return notEmpty;
        } else if ((r.thenAction() != null && r.thenAction().isEmpty())
               && (r.elseAction() != null && r.elseAction().isEmpty())) {
            return notEmpty;
        }
        return "";
    }
}
