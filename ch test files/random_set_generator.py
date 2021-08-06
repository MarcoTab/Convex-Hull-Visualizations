import random as rd

def main():
    n = int(input('Set size > '))
    fn = input('Filename > ')
    f = open(fn, 'w')

    for i in range(n):
        f.write('{:.4f}, {:.4f}\n'.format(rd.random() * n, rd.random() * n))

    f.close()



if __name__ == '__main__':
    main()