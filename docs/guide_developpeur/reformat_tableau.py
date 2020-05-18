# coding: utf8

import os


tab_org = open("tab_org.rst","r")
tab_new = open("tab_new.rst","w")

line_new = ""

i = 0

for line in tab_org :
    #if i == 5: exit()

    line_new = line[37:-1]
    #print(new_line)

    tab_new.write(line_new + "\n")

    i+= 1

tab_new.close()
tab_org.close()

print("FIN")
