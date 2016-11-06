# This example file shows using Java functions from a Python script

import sys
from java.lang import System


def test(value=None):
    """
    Prints out a simple message for testing purposes.
    """
    if value is not None:
        System.out.println('This is a Jython script executing from ' + value.lower())
    else:
        System.out.println('This is a Jython test.')


if __name__ == '__main__':
    if len(sys.argv) > 1:
        test(sys.argv[1])
    else:
        test()