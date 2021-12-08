# Copyright (c) 2021 Mikołaj Kuranowski
# SPDX-License-Identifier: WTFPL
from fileinput import FileInput
from typing import Iterable


Point = tuple[int, int]

STEP_DELTAS: dict[str, Point] = {
    "e":  (2, 0),
    "se": (1, -1),
    "sw": (-1, -1),
    "w":  (-2, 0),
    "ne": (1, 1),
    "nw": (-1, 1),
}


def pt_add(p1: Point, p2: Point) -> Point:
    return p1[0] + p2[0], p1[1] + p2[1]


def assert_valid_tile(pt: Point) -> None:
    # Both numbers should be odd or even
    if (pt[0] & 1) ^ (pt[1] & 1):
        raise ValueError(f"invalid hexagon tile: {pt}")


def parse_line(steps: str) -> Point:
    point: Point = 0, 0
    direction: str = ""

    while steps:
        # Get the direction
        if steps[0] == "e" or steps[0] == "w":
            direction, steps = steps[:1], steps[1:]
        else:
            direction, steps = steps[:2], steps[2:]

        delta = STEP_DELTAS[direction]
        point = pt_add(point, delta)
        assert_valid_tile(point)

    return point


def find_black_tiles(tiles: Iterable[Point]) -> set[Point]:
    black_tiles: set[Point] = set()

    for tile in tiles:
        if tile in black_tiles:
            black_tiles.remove(tile)
        else:
            black_tiles.add(tile)

    return black_tiles


if __name__ == "__main__":
    input: "FileInput[str]" = FileInput()
    lines = (i.strip() for i in input)
    lines_as_points = map(parse_line, lines)
    black_tiles = find_black_tiles(lines_as_points)

    print(len(black_tiles))
