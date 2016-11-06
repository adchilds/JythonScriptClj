import sys


def multiply(a, b):
    """
    Multiplies two values, 'a' and 'b', together.

    :param a: the first parameter to multiply
    :param b: the second parameter to multiply
    :return: the product of [a * b]
    """
    return a * b


if __name__ == '__main__':
    # Set the defaults
    a = 5
    b = 5

    # If arguments were passed to this script, use those
    try:
        sysA = sys.argv[1]
        sysB = sys.argv[2]

        if sysA is not None:
            a = sysA

        if b is not None:
            b = sysB
    except Exception:
        pass

    # Multiple either (5 * 5) or (a * b [arguments passed in from Java])
    result = multiply(a, b)