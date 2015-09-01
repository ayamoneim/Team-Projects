function [ans,iter,bisection,secant]= Interface(f, maxIter,epsilon)
	[xl, xu]= randomize(f);
	if(nargin < 3) epsilon= 1e-12; end
	if(nargin < 2) maxIter= 50; end
	if(xu==1000000000)
        df = diff(sym(f));
		f= sym(f)/df;
	end
	
	[xl,xu]= randomize(f);
	if(xu==1000000000)
        ans = NaN;
		return;
	end
	
	[ans,iter,bisection,secant]= Hybrid(f,xl,xu,epsilon,maxIter);
    double(ans);
end
