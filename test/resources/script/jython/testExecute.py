import sys
from java.lang import System


def test(value=None):
    """
    Prints out a simple message for testin purposes.
    """
    if value is not None:
        System.out.println('This is a Jython test with a value [' + value + ']')
    else:
        System.out.println('This is a Jython test.')
    print 'This is a Python test.'


if __name__ == '__main__':
    if len(sys.argv) > 1:
        test(sys.argv[1])
    else:
        test()