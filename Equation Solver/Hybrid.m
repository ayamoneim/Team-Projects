function [ret,iter,bisection,secant] = Hybrid(f,xl,xu,epsilon, maxIter)
	iter=0;
    secant=0;
    bisection= 0;
	
	flagSecant= 0;
	a=xl; b=xu; c=a;
	fa=f(a); fb=f(b); fc=fa;
	
	for i= 1:maxIter
        iter = iter + 1;
		if (abs(fc) < abs(fb))
			t=c; c=b; b=t;
			t=fc; fc=fb; fb=t;
			a=c; fa=fc;
		end
		
		if(abs(b-c) <= epsilon)
            ret = NaN;
			return;
		end
		
		dm = (c-b)/2;
		df=(fa-fb);
		
		if (df==0)
			ds=dm;
		else
			ds=-fb*(a-b)/df;
		end
		
		if(ds*dm<0 || abs(ds) > abs(dm) || flagSecant>4)
			flagSecant= 0;
			bisection = bisection + 1;
			dd = dm;
		else
			flagSecant = flagSecant + 1;
			secant = secant + 1;
			dd = ds;
		end
		
		if (abs(dd) < epsilon)
            if(dm<0)
                dd = 0.5*-1*epsilon;
            else
                dd = 0.5*epsilon;
            end
		end
		
		d = b + dd;
		fd = f(d);
		
		if (abs(fd) <= epsilon)
			b=d;
            c=d;
			fb=fd;
            fc=fd;
			break;
		end
		a=b; b=d;
		fa=fb; fb=fd;
		
		if (fb *fc>=0)
			c=a; fc=fa;
        end
        
        if(i>15 && fd>1000)
            ret = NaN;
            return ;
        end
   	end
	ret= b;
end
