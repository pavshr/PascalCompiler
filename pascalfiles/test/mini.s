# Code file created by Pascal2016 compiler 2016-12-03 23:45:33
        .globl main                         
main:
        call    prog$mini_1             # #Start program
        movl    $0,%eax                 # Set status 0 and
        ret                             # terminate the program
prog$mini_1:
        enter   $32,$1                  # Start of mini
        movl    $120,%eax               # 'x'
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        leave                           # End of mini
        ret                             
