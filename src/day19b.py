# Copyright (c) 2021 Mikołaj Kuranowski
# SPDX-License-Identifier: WTFPL
from typing import Callable, Iterable
from fileinput import FileInput

import core

RuleMatcher = Callable[[str, "RuleMatchers"], set[str]]
RuleMatchers = dict[str, "RuleMatcher"]


def parse_conjunction_rule(rule: str, name: str) -> RuleMatcher:
    rule_names = rule.split()

    def matcher(text: str, matchers: RuleMatchers) -> set[str]:
        inputs: set[str] = {text}
        outputs: set[str] = set()

        for rule_name in rule_names:

            for input in inputs:
                outputs.update(matchers[rule_name](input, matchers))

            # No more matches - no need to process anymore
            if not outputs:
                return outputs

            inputs = outputs
            outputs = set()

        return inputs

    matcher.__name__ = f"matcher-{name}"
    return matcher


def parse_disjunction_rule(rule: str, name: str) -> RuleMatcher:
    alternatives = [
        parse_conjunction_rule(i, f"{name}-alt{idx}")
        for idx, i in enumerate(rule.split(" | "))
    ]

    def matcher(text: str, matchers: RuleMatchers) -> set[str]:
        all_outputs: set[str] = set()

        for alternative in alternatives:
            all_outputs.update(alternative(text, matchers))

        return all_outputs

    matcher.__name__ = f"matcher-{name}"
    return matcher


def parse_char_rule(rule: str, name: str) -> RuleMatcher:
    assert rule[0] == rule[2] == '"'
    char = rule[1]

    def matcher(text: str, matchers: RuleMatchers) -> set[str]:
        return {text[1:]} if text[:1] == char else set()

    matcher.__name__ = f"matcher-{name}"
    return matcher


def parse_rule(rule: str, name: str) -> RuleMatcher:
    if '"' in rule:
        return parse_char_rule(rule, name)
    elif "|" in rule:
        return parse_disjunction_rule(rule, name)
    else:
        return parse_conjunction_rule(rule, name)


def parse_rules(lines: Iterable[str]) -> RuleMatchers:
    matchers: RuleMatchers = {}
    for line in lines:
        name, _, rule_text = line.partition(": ")
        matchers[name] = parse_rule(rule_text, name)
    return matchers


def matches_zero_fully(text: str, matchers: RuleMatchers) -> bool:
    result = matchers["0"](text, matchers)
    return "" in result


if __name__ == "__main__":
    input: "FileInput[str]" = FileInput()
    rules, lines = core.split_on((i.rstrip() for i in input), core.empty_str)
    matchers = parse_rules(rules)
    result = sum(1 for line in lines if matches_zero_fully(line, matchers))
    print(result)
