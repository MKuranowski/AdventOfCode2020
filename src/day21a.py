# Copyright (c) 2021 Mikołaj Kuranowski
# SPDX-License-Identifier: WTFPL
import re
from fileinput import FileInput
from itertools import chain
from typing import Counter, Iterable


def _to_bins(map: dict[str, set[str]]) -> tuple[dict[str, set[str]], ...]:
    bin0: dict[str, set[str]] = {}
    bin1: dict[str, set[str]] = {}
    bin2: dict[str, set[str]] = {}

    for k, v in map.items():
        if len(v) == 0:
            bin0[k] = v
        elif len(v) == 1:
            bin1[k] = v
        else:
            bin2[k] = v

    return bin0, bin1, bin2


def parse_line_posiblities(line: str) -> dict[str, set[str]]:
    m = re.match(r"(.*) \(contains (.*)\)", line)
    assert m
    foreign = set(m[1].split(" "))
    return {
        allergen: foreign.copy()
        for allergen in m[2].split(", ")
    }


def simplify_line_possibilities(parsed_lines: Iterable[dict[str, set[str]]]) \
        -> dict[str, set[str]]:
    simplified: dict[str, set[str]] = {}

    for parsed_line in parsed_lines:
        for allergen, foreign in parsed_line.items():

            if allergen not in simplified:
                simplified[allergen] = foreign
            else:
                simplified[allergen].intersection_update(foreign)

    return simplified


def solve_possibilities(simplified: dict[str, set[str]]) -> dict[str, str]:
    solved: dict[str, str] = {}

    while True:
        bin0, bin1, bin2 = _to_bins(simplified)

        if bin0:
            raise ValueError("lines have no solution")

        elif bin1:
            uniqs = set(chain.from_iterable(bin1.values()))
            for other_set in bin2.values():
                other_set.difference_update(uniqs)

            for k, v in bin1.items():
                solved[k] = v.pop()

            simplified = bin2

        elif bin2:
            raise ValueError("lines have no solution")

        else:
            break

    return solved


def load_translations(lines: Iterable[str]) -> dict[str, str]:
    return solve_possibilities(simplify_line_possibilities(map(parse_line_posiblities, lines)))


def load_all_foreign_words(lines: Iterable[str]) -> Counter[str]:
    return Counter(chain.from_iterable(
        line.partition(" (")[0].split(" ")
        for line in lines
    ))


if __name__ == "__main__":
    lines: list[str] = list(FileInput())
    all_foreign_words = load_all_foreign_words(lines)
    possiblilities = load_translations(lines)
    possible_foreign_words: set[str] = set().union(possiblilities.values())

    for k in possible_foreign_words:
        all_foreign_words.pop(k)

    print(sum(all_foreign_words.values()))
