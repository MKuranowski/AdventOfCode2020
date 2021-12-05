# Copyright (c) 2021 Mikołaj Kuranowski
# SPDX-License-Identifier: WTFPL
from typing import Callable, Iterable
from fileinput import FileInput

import core

RuleMatcher = Callable[[str, "RuleMatchers"], tuple[bool, str]]
RuleMatchers = dict[str, "RuleMatcher"]


def parse_conjunction_rule(rule: str, name: str) -> RuleMatcher:
    rule_names = rule.split()

    def matcher(text: str, matchers: RuleMatchers) -> tuple[bool, str]:
        ok = True

        for rule_name in rule_names:
            ok, text = matchers[rule_name](text, matchers)
            if not ok:
                break

        return ok, text

    matcher.__name__ = f"matcher-{name}"
    return matcher


def parse_disjunction_rule(rule: str, name: str) -> RuleMatcher:
    alternatives = [
        parse_conjunction_rule(i, f"{name}-alt{idx}")
        for idx, i in enumerate(rule.split(" | "))
    ]

    def matcher(text: str, matchers: RuleMatchers) -> tuple[bool, str]:
        ok = False

        for alternative in alternatives:
            ok, next_text = alternative(text, matchers)
            if ok:
                return ok, next_text

        return False, text

    matcher.__name__ = f"matcher-{name}"
    return matcher


def parse_char_rule(rule: str, name: str) -> RuleMatcher:
    assert rule[0] == rule[2] == '"'
    char = rule[1]

    def matcher(text: str, matchers: RuleMatchers) -> tuple[bool, str]:
        return (True, text[1:]) if text[:1] == char else (False, text)

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
    ok, rest = matchers["0"](text, matchers)
    return ok and not rest


if __name__ == "__main__":
    input: "FileInput[str]" = FileInput()
    rules, lines = core.split_on((i.rstrip() for i in input), core.empty_str)
    matchers = parse_rules(rules)
    result = sum(1 for line in lines if matches_zero_fully(line, matchers))
    print(result)
