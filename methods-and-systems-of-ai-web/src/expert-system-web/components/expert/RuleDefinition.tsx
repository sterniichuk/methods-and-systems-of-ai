import React, {useEffect, useState} from 'react';
import {RuleType, ruleTypes} from "../../data/ActionType";
import {RuleDTO} from "../../data/ActionDTO";

interface Props {
    id: number
    scopeId: number
    ruleType: RuleType
    setRuleType: (a: RuleType) => void
    ruleObj: RuleDTO
    addNewRule: ()=>void
}

function RuleDefinition({id, scopeId, setRuleType, ruleType, ruleObj, addNewRule}: Props) {
    const [name, setName] = useState("");
    const [formula, setFormula] = useState("")
    const [question, setQuestion] = useState("");
    const [variables, setVariables] = useState<string[]>([])
    if (ruleObj.decisionInfo === undefined) {
        ruleObj.decisionInfo = {type: ruleType, formula: formula, variables: variables};
    }
    useEffect(() => {
        ruleObj.name = name;
    }, [name]);
    useEffect(() => {
        if (ruleObj.decisionInfo === undefined) {
            ruleObj.decisionInfo = {type: ruleType, formula: formula, variables: variables};
            return;
        }
        ruleObj.decisionInfo.formula = formula;
    }, [formula]);
    useEffect(() => {
        ruleObj.condition = question;
    }, [question]);

    function selectTypeStrategy(e: React.ChangeEvent<HTMLSelectElement>) {
        if (e.target == null || e.target.value == null) {
            return;
        }
        const filter = ruleTypes.filter(r => r.toString() == e.target.value);
        if (filter.length >= 1) {
            if (ruleObj.decisionInfo === undefined) {
                ruleObj.decisionInfo = {type: filter[0], formula: formula, variables: variables};
            } else {
                ruleObj.decisionInfo.type = filter[0];
            }
            setRuleType(filter[0]);
        }
    }

    function setStringHandler(e: React.FormEvent<any>,
                              callback: (value: (((prevState: string) => string) | string)) => void) {
        if (e.currentTarget == null || e.currentTarget.value == null) {
            return;
        }
        const newName = e.currentTarget.value;
        const updatedName = newName.replace(/\s+/g, ' ');
        callback(updatedName);
        e.currentTarget.value = updatedName;
    }

    function setVariablesStrategy(e: React.FormEvent<HTMLInputElement>) {
        if (e.currentTarget == null || e.currentTarget.value == null) {
            return;
        }
        const newVars = e.currentTarget.value;
        if (newVars.charAt(newVars.length - 1) == ',') {
            return;
        }
        const isValidVariable = (variableName: string) => {
            const validVariableRegex = /^[a-zA-Z_$][a-zA-Z0-9_$]*$/;
            return validVariableRegex.test(variableName);
        };
        const strings = newVars.split(',')
            .filter(x => x.length >= 1 && isValidVariable(x))
            .map(x => x.trim())
        if (ruleObj.decisionInfo === undefined) {
            ruleObj.decisionInfo = {type: ruleType, formula: formula, variables: strings};
        } else {
            ruleObj.decisionInfo.variables = strings;
        }
        setVariables(strings);
        e.currentTarget.value = strings.join(',');
    }

    return (
        <div key={`rule_definition_${id}_from_scope_${scopeId}`}
             className="section rule-definition-section">
            <div className="row block-header">
                <p className={"block-title"}>Rule definition #{id}</p>
                <button onClick={addNewRule} className={"plus-button"}>+</button>
            </div>
            <div className="input-holder">
                <div key={`long-input-wrapper_${id}_from_scope_${scopeId}`} className="long-input-wrapper">
                    <div className="input-title-pair long-input">
                        <p>Name</p>
                        <textarea onInput={event => setStringHandler(event, setName)}
                                  className={"rectangle-input"}/>
                    </div>
                    <div className="input-title-pair long-input">
                        <p>Question</p>
                        <textarea
                            onInput={event => setStringHandler(event, setQuestion)}
                            className={"rectangle-input"}/>
                    </div>
                </div>
                <div key={`short-input-wrapper_${id}_from_scope_${scopeId}`} className="short-input-wrapper">
                    <div key={`rule_type_${id}_from_scope_${scopeId}`} className="input-title-pair choice-input">
                        <p>Type</p>
                        <select onChange={(e) => selectTypeStrategy(e)} className={"rectangle-input"}>
                            {ruleTypes.map(r => {
                                return <option
                                    key={`type_input_${r}_from_rule_${r}_${id}_from_scope_${scopeId}`}
                                    value={r}>{r.toLowerCase().replace("_", " ")}</option>
                            })}
                        </select>
                    </div>
                    {ruleType == RuleType.BINARY ? null :
                        <div key={`rule_variables_${id}_from_scope_${scopeId}`} className="input-title-pair ">
                            <p>Variables</p>
                            <input
                                onInput={e => setVariablesStrategy(e)}
                                className={"rectangle-input"} type="text"/>
                        </div>}
                    {ruleType == RuleType.BINARY ? null : <div
                        key={`rule_formula_${id}_from_scope_${scopeId}`}

                        className="input-title-pair ">
                        <p>Formula</p>
                        <input
                            onInput={event => setStringHandler(event, setFormula)}
                            className={"rectangle-input"} type="text"/>
                    </div>}
                </div>
            </div>
        </div>
    );
}

export default RuleDefinition;