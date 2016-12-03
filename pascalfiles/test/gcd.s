# Code file created by Pascal2016 compiler 2016-12-03 18:44:31
        .globl main                         
main:
        call    prog$gcd_1              # #Start program
        movl    $0,%eax                 # Set status 0 and
        ret                             # terminate the program
prog$gcd_1:
        enter   $36,$1                  # Start of gcd
        movl    0(%ebp),%edx            
        movl    %eax,-32(%edx)          
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        leave                           
        ret                             
