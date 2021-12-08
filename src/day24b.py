# Copyright (c) 2021 Mikołaj Kuranowski
# SPDX-License-Identifier: WTFPL
from fileinput import FileInput
from typing import Generator

from day24a import STEP_DELTAS, Point, find_black_tiles, parse_line, pt_add


def count_black_neighbors(pt: Point, black_tiles: set[Point]) -> int:
    return sum(1 for neighbor_delta in STEP_DELTAS.values()
               if pt_add(pt, neighbor_delta) in black_tiles)


def evolve_black_tiles(black_tiles: set[Point]) -> set[Point]:
    return {pt for pt in black_tiles if 0 < count_black_neighbors(pt, black_tiles) < 3}


def bbox_around(tiles: set[Point]) -> tuple[int, int, int, int]:
    return min(tile[0] for tile in tiles), \
        max(tile[0] for tile in tiles),    \
        min(tile[1] for tile in tiles),    \
        max(tile[1] for tile in tiles),


def all_points_in_bbox(bbox: tuple[int, int, int, int]) -> Generator[Point, None, None]:
    west, east, south, north = bbox

    for x in range(west - 2, east + 3):
        for y in range(south - 2, north + 3):
            if ((x & 1) ^ (y & 1)) == 0:
                yield x, y


def evolve_white_tiles(black_tiles: set[Point]) -> set[Point]:
    return {
        pt for pt in all_points_in_bbox(bbox_around(black_tiles))
        if pt not in black_tiles and count_black_neighbors(pt, black_tiles) == 2
    }


if __name__ == "__main__":
    input: "FileInput[str]" = FileInput()
    lines = (i.strip() for i in input)
    lines_as_points = map(parse_line, lines)
    black_tiles = find_black_tiles(lines_as_points)

    for day in range(100):
        black_tiles = evolve_black_tiles(black_tiles) | evolve_white_tiles(black_tiles)

    print(len(black_tiles))
