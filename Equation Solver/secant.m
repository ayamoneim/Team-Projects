syms x;
    flag = 1;
    func = input('Enter Function in terms of x: ');
    a = input('Ener Lower Limit: ');
    b = input('Ener Upper Limit: ');
   tolerance = input('Enter Maximum Error: ');

        f = inline(func);
        fplot(f,[-10,10]);
        hold on;
        c = b - (f(b) *((b - a) /(f(b)-f(a))));
        if(f(c)<= tolerance)
            return;
        end;
        disp('   Xn-1      f(Xn-1)      Xn      f(Xn)      Xn+1      f(Xn+1)');
        disp([a f(a) b f(b) c f(c)]);
        
        while abs(f(c)) > tolerance
            a = b;
            b = c;
            c = b - (f(b) *((b - a) /(f(b)-f(a))));
            disp([a f(a) b f(b) c f(c)]);
            
            plot(c,f(c),':r*');
            hold on;
            
            flag = flag + 1;
            
            if(flag == 100)
                break;
            end
        end
        
        display(['Root is x = ' num2str(c)]);