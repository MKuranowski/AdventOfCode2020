# Copyright (c) 2021 Mikołaj Kuranowski
# SPDX-License-Identifier: WTFPL
from typing import Optional
from dataclasses import dataclass


@dataclass
class Cup:
    value: int
    next: Optional["Cup"] = None


def show_cups(first: Cup, sep: str = " ") -> str:
    text: str = str(first.value)
    assert first.next
    cup: Cup = first.next

    while cup is not first:
        text += sep + str(cup.value)
        assert cup.next
        cup = cup.next

    return text


def run(selected: Cup, highest_value: int) -> Cup:
    # Drop 3 Cups
    dropped: list[Cup] = []

    for _ in range(3):
        assert selected.next
        dropped.append(selected.next)
        selected.next = selected.next.next

    # print(f"\nCups (first = selected): {show_cups(selected)}")
    # print(f"Picked up: {dropped[0].value} {dropped[1].value} {dropped[2].value}")

    # Find the destination cup
    target_value = selected.value - 1
    assert selected.next
    dest_cup: Cup = selected.next

    while dest_cup.value != target_value:
        if dest_cup is selected:
            target_value = highest_value if target_value == 0 else target_value - 1

        assert dest_cup.next
        dest_cup = dest_cup.next

    # print(f"Destination: {dest_cup.value}")

    # Insert after destination
    dropped[-1].next = dest_cup.next
    dest_cup.next = dropped[0]

    return selected.next


def prepare_cups(numbers: list[int]) -> Cup:
    cups = [Cup(i) for i in numbers]

    # Link next cups
    for src, dest in zip(cups[:-1], cups[1:]):
        src.next = dest

    # Make cups into a circle
    cups[-1].next = cups[0]

    return cups[0]


if __name__ == "__main__":
    # cup_values = [3, 8, 9, 1, 2, 5, 4, 6, 7]  # Test input
    cup_values = [2, 5, 3, 1, 4, 9, 8, 6, 7]  # My input
    max_cup_val = max(cup_values)
    c = prepare_cups(cup_values)
    for _ in range(100):
        c = run(c, max_cup_val)
    print(show_cups(c))
