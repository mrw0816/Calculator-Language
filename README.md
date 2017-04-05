# Calculator-Language
Kinds of tokens are:
  N (nonnegative floating point number (or integer))
  V (variable name)
  BIFN (built-in function name --- sqrt, sin, cos)
  STRING (a string, like "hello")
  NEWLINE (newline), MESSAGE (msg), SHOW (show), INPUT (input)
  various single-symbol tokens represent themselves:   + - * / = ( )  

---------------------
Context free grammar:  (E = <expression>) 

<statements> -> <statement>
<statements> -> <statement> <statements>

<statement> -> V = E
<statement> -> SHOW E           (show)
<statement> -> MESSAGE STRING   (keyword is "msg")
<statement> -> INPUT STRING V      (STRING is the token for a "whatever")
<statement> -> NEWLINE          (newline)

    (use E instead of <expression>, etc.)
    
    E = expression
    T = term
    F = function
    N = number
    V = variable
    BIFN = built in function( sqrt, cos, sin )

E -> T
E -> T + E | T - E

T -> F
T -> F * T | F / T

F -> N          (from lexical phase:  number)
F -> V          (from lexical phase:  variable)
F -> (E)
F -> - F
F -> BIFN ( E )

------------------

Examples of CalcLang Code

x=3
y=5
z=x+y*7
show z
angle = 3.14
w = sin( angle )
show x+y 
z = -z

---------------------
input "a= " a
input "b= " b
c = sqrt( a*a + b*b )
msg "triangle with sides "  show a
msg " and " show b
msg "has hyp = " show c
newline
msg "bye"
---------------------
input "enter angle: " angle
msg "cos is " show cos(angle)
