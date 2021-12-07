# Copyright (c) 2021 Mikołaj Kuranowski
# SPDX-License-Identifier: WTFPL
from typing import Optional
from dataclasses import dataclass


@dataclass
class Cup:
    value: int
    next: Optional["Cup"] = None


Cups = list[Cup]


def show_cups(first: Cup, sep: str = " ") -> str:
    text: str = str(first.value)
    assert first.next
    cup: Cup = first.next

    while cup is not first:
        text += sep + str(cup.value)
        assert cup.next
        cup = cup.next

    return text


def run(selected: Cup, cups: Cups, highest_value: int) -> Cup:
    # print(f"\nCups (first = selected): {show_cups(selected)}")
    # Drop 3 Cups
    dropped: list[Cup] = []

    for _ in range(3):
        assert selected.next
        dropped.append(selected.next)
        selected.next = selected.next.next

    dropped_values: set[int] = {cup.value for cup in dropped}

    # print(f"Picked up: {dropped[0].value} {dropped[1].value} {dropped[2].value}")

    # Find the destination cup
    target_value = selected.value - 1 if selected.value > 1 else highest_value
    while target_value in dropped_values:
        target_value = target_value - 1 if target_value > 1 else highest_value

    dest_cup = cups[target_value]
    # print(f"Destination: {dest_cup.value}")

    # Insert after destination
    dropped[-1].next = dest_cup.next
    dest_cup.next = dropped[0]

    assert selected.next
    return selected.next


def prepare_cups(numbers: list[int]) -> Cups:
    cups = [Cup(i) for i in numbers]

    # Link next cups
    for src, dest in zip(cups[:-1], cups[1:]):
        src.next = dest

    # Make cups into a circle
    cups[-1].next = cups[0]

    # Sort, so that cups[i].value == i.
    # We also need to add a fake element at i = 0
    cups.sort(key=lambda cup: cup.value)
    cups.insert(0, Cup(0))

    return cups


if __name__ == "__main__":
    # cup_values = [3, 8, 9, 1, 2, 5, 4, 6, 7]  # Test input
    cup_values = [2, 5, 3, 1, 4, 9, 8, 6, 7]  # My input
    # max_cup_val = max(cup_values)
    max_cup_val = 1_000_000

    cups = prepare_cups(cup_values + list(range(10, 1_000_001)))
    cup = cups[cup_values[0]]

    for _ in range(10_000_000):
        cup = run(cup, cups, max_cup_val)

    assert cups[1].next
    assert cups[1].next.next
    first = cups[1].next.value
    second = cups[1].next.next.value

    print(first, second, first * second)
