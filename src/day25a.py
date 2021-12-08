

def transform(subject: int, loop: int) -> int:
    value = 1
    for _ in range(loop):
        value = (value * subject) % 20201227
    return value


def find_loop_size(subject: int, transformed: int) -> int:
    loop_size = 0
    value = 1

    while True:
        loop_size += 1
        value = (value * subject) % 20201227

        if value == transformed:
            return loop_size


def find_encryption_key(door_public_key: int, card_public_key: int) -> int:
    card_loop_size = find_loop_size(7, card_public_key)
    return transform(door_public_key, card_loop_size)


if __name__ == "__main__":
    # door_pk = 17807724  # Test input
    # card_pk = 5764801
    door_pk = 11349501  # My input
    card_pk = 5107328

    print(find_encryption_key(door_pk, card_pk))
