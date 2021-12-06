from fileinput import FileInput
from day21a import load_translations

if __name__ == "__main__":
    input: "FileInput[str]" = FileInput()
    translations = load_translations(input)
    keys_sorted = sorted(translations)
    vals_sorted_by_key = map(lambda key: translations[key], keys_sorted)

    print(",".join(vals_sorted_by_key))
