function varargout = Gui_Part1(varargin)
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
    'gui_Singleton',  gui_Singleton, ...
    'gui_OpeningFcn', @Gui_Part1_OpeningFcn, ...
    'gui_OutputFcn',  @Gui_Part1_OutputFcn, ...
    'gui_LayoutFcn',  [] , ...
    'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end

function Gui_Part1_OpeningFcn(hObject, eventdata, handles, varargin)

handles.output = hObject;

% Update handles structure
guidata(hObject, handles);




% --- Outputs from this function are returned to the command line.
function varargout = Gui_Part1_OutputFcn(hObject, eventdata, handles)

varargout{1} = handles.output;


function Secant_Callback(hObject, eventdata, handles)

syms x
flag = 1;

a = str2double(get(handles.LowerBound,'String'));
b = str2double(get(handles.UpperBound,'String'));

tolerance = str2double(get(handles.Tolerance,'String'));
f = inline(get(handles.InputFn,'String'));
fplot(f,[-10,10]);
hold on;
c = b - (f(b) *(b - a) /(f(b)-f(a)));

iterations = str2double(get(handles.Iterations,'String'));
if(iterations==-1)
    iterations = 50;
end;
set(handles.table, 'columnname', {'i', 'X(i-1)', 'F(X(i-1))','X(i)' ,'F(Xi)','X(i+1)','F(X(i+1))'});

data = zeros(iterations,7);
data(flag,1) = flag;
data(flag,2) = a;
data(flag,3) = f(a);
data(flag,4) = b;
data(flag,5) = f(b);
data(flag,6) = c;
data(flag,7) = f(c);
while abs(c-b) > tolerance
    a = b;
    b = c;
    c = b - f(b) *(b - a) /(f(b)-f(a));    
    plot(c,f(c),':r*');
    hold on;
    
    flag = flag + 1;
    data(flag,1) = flag;
    data(flag,2) = a;
    data(flag,3) = f(a);
    data(flag,4) = b;
    data(flag,5) = f(b);
    data(flag,6) = c;
    data(flag,7) = f(c);
    if(flag == iterations)
        break;
    end
end
set(handles.table,'data',data);
set(handles.ans,'String',num2str(c));
hold off;


function InputFn_Callback(hObject, eventdata, handles)

function InputFn_CreateFcn(hObject, eventdata, handles)
% hObject    handle to InputFn (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.


if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes when figure1 is resized.
function figure1_ResizeFcn(hObject, eventdata, handles)
% hObject    handle to figure1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)



function LowerBound_Callback(hObject, eventdata, handles)
% hObject    handle to LowerBound (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of LowerBound as text
%        str2double(get(hObject,'String')) returns contents of LowerBound as a double


% --- Executes during object creation, after setting all properties.
function LowerBound_CreateFcn(hObject, eventdata, handles)
% hObject    handle to LowerBound (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function Tolerance_Callback(hObject, eventdata, handles)
% hObject    handle to Tolerance (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of Tolerance as text
%        str2double(get(hObject,'String')) returns contents of Tolerance as a double


% --- Executes during object creation, after setting all properties.
function Tolerance_CreateFcn(hObject, eventdata, handles)
% hObject    handle to Tolerance (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function UpperBound_Callback(hObject, eventdata, handles)

function UpperBound_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


function falsePosition_Callback(hObject, eventdata, handles)

xl = str2double(get(handles.LowerBound,'String'));

xu = str2double(get(handles.UpperBound,'String'));

es = str2double(get(handles.Tolerance,'String'));

f = inline(get(handles.InputFn,'String'));
fplot(f,[-10,10],'b-');
hold on;
iterations = str2double(get(handles.Iterations,'String'));
set(handles.table, 'columnname', {'i', 'X_Lower', 'F(X_Lower)','X_Upper' ,'F(X_Upper)','Xi','F(Xi)'});

if(iterations==-1)
    iterations = 50;
end;

data = zeros(iterations,7);



xf =0;
a= xl;
b= xu;
ya = f(a);
yb = f(b);
if (ya(1)*yb(1) >0)
   %msgbox('Error:Function with no sign change');

end
x =0;
for i=1:iterations
    xf=x;
    x = b -yb *((b-a)/ (yb-ya));
    plot(x,':r*');
    hold on;
    y = f(x);
    data(i,1) = i;
    data(i,2) = a;
    data(i,3) = f(a);
    data(i,4) = b;
    data(i,5) = f(b);
    data(i,6) = x;
    data(i,7) = y;
    if y == 0
  %  msgbox('Zero Found');
        break;
    elseif y*ya <0
        b = x;
        yb = y;
    else
        a = x;
        ya =y;
    end;
    if ((i>1) && (abs(x - xf) <es))
 %       msgbox('converged');
        break
    end;
    iter =i;  
end
if (iter>=iterations)
    %msgbox('no zero appears');
end
set(handles.table,'Data',data);
set(handles.ans,'String',num2str(x));
hold off;





function NewtonRaphson_Callback(hObject, eventdata, handles)

xo = str2double(get(handles.LowerBound,'String'));

syms x;
es = str2double(get(handles.Tolerance,'String'));
f = inline(get(handles.InputFn,'String'));
iterations = str2double(get(handles.Iterations,'String'));

fplot(f,[-10,10]);
hold on;

set(handles.table, 'columnname', {'i', 'Xi', 'F(Xi)', 'D(Xi)'});
d=diff(sym(f),1);
data = zeros(iterations,7);

for i=1:1:iterations
    sa=subs(f,xo);
    sb=subs(d,xo);
    xi=xo-(sa/sb);
    ea=abs((xi-xo)/xi);
    xo=xi;
    data(i,1) = i;
    data(i,2) = xo;
    data(i,3) = sa;
    data(i,4) = sb;
    plot(xo,':r*');
    hold on;
   
    if(ea<es)
        break;
    end
end
format short
set(handles.table,'Data',data);

set(handles.ans,'String',num2str(xo));
hold off;



function Iterations_Callback(hObject, eventdata, handles)
% hObject    handle to Iterations (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of Iterations as text
%        str2double(get(hObject,'String')) returns contents of Iterations as a double


% --- Executes during object creation, after setting all properties.
function Iterations_CreateFcn(hObject, eventdata, handles)
% hObject    handle to Iterations (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


function Bisection_Callback(hObject, eventdata, handles)


xl = str2double(get(handles.LowerBound,'String'));
xu = str2double(get(handles.UpperBound,'String'));
tolerance = str2double(get(handles.Tolerance,'String'));
f = inline(get(handles.InputFn,'String'));
fplot(f,[-10,10],'b-');
hold on;
iterations = str2double(get(handles.Iterations,'String'));
set(handles.table, 'columnname', {'i', 'X_Lower', 'F(X_Lower)','X_Upper' ,'F(X_Upper)','Xi','F(Xi)'});
data = zeros(iterations,7);
iterations = 1 + floor(log2((xu-xl)/tolerance));
    if f(xl)* f(xu) >0
        msgbox('there is no root');
    else 
                xr = xl + (xu-xl)/2;
        i = 1;
         data(i,1) = i;
                data(i,2) = xl;
                data(i,3) = f(xl);
                data(i,4) = xu;
                data(i,5) = f(xu);
                data(i,6) = xr;
                data(i,7) = f(xr);
        i = 2;
        while iterations>= i
            if f(xl)*f(xr)<0
                xu=xr;
            else 
                xl=xr;
            end
                xr = xl + (xu-xl)/2;
                data(i,1) = i;
                data(i,2) = xl;
                data(i,3) = f(xl);
                data(i,4) = xu;
                data(i,5) = f(xu);
                data(i,6) = xr;
                data(i,7) = f(xr);
            plot(xr,':r*');
            hold on;
            i = i + 1;
        end
        set(handles.table,'Data',data);

set(handles.ans,'String',num2str(xr));
    end
    
 hold off;  
    

% --- Executes on button press in FileInput.
function FileInput_Callback(hObject, eventdata, handles)

file = fopen(get(handles.fileName,'String'),'r');

line = fgetl(file);
set(handles.InputFn,'String',line);

line = fgetl(file);
set(handles.LowerBound,'String',line);

line = fgetl(file);
set(handles.UpperBound,'String',line);

line = fgetl(file);
set(handles.Tolerance,'String',line);

line = fgetl(file);
set(handles.Iterations,'String',line);

fclose(file);


% --- Executes on button press in solve.
function solve_Callback(hObject, eventdata, handles)
% hObject    handle to solve (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)



function fileName_Callback(hObject, eventdata, handles)
% hObject    handle to fileName (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of fileName as text
%        str2double(get(hObject,'String')) returns contents of fileName as a double


% --- Executes during object creation, after setting all properties.
function fileName_CreateFcn(hObject, eventdata, handles)
% hObject    handle to fileName (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
