# Code file created by Pascal2016 compiler 2016-12-03 16:42:42
        .globl main                         
main:
        call    prog$tenstars_1         # #Start program
        movl    $0,%eax                 # Set status 0 and
        ret                             # terminate the program
prog$tenstars_1:
        enter   $36,$1                  # Start of tenstars
