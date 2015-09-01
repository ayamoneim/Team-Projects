function [xl, xu]= randomize(f)
	vec= rand(1000);
	flag= 0;
	xl= vec(1);
	for(j= 0:6)
		if(j > 0)
			vec = vec/10^(j-1);
		end;
		vec= vec*10^j;
		for(i= 2:3)
          
			if(f(vec(i))*f(xl) < 0)
				xu= vec(i);
				flag= 1;
				break;
			end
			if(f(-vec(i))*f(xl) < 0)
				xu= -vec(i);
				flag= 1;
				break;
			end
		end
		if(flag==1)
			break;
		end
	end
	if(flag==0)
		xu= 1000000000;
	end
end
