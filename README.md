# PROCEDURES FUNCTIONS TRIGGERS 
--------------------------------------------------------
--  File created - Monday-January-16-2017   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Function GETDAYS
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "UNI"."GETDAYS" 
(
  MON IN NUMBER 
) RETURN NUMBER AS 
days number :=0;
BEGIN
  if(MON=1) then days:=31; 
  elsif (MON=2) then days:=28;
  elsif (MON=3) then days:=31;
  elsif (MON=4) then days:=30;
  elsif (MON=5) then days:=31;
  elsif (MON=6) then days:=30;
  elsif (MON=7) then days:=31;
  elsif (MON=8) then days:=31;
  elsif (MON=9) then days:=30;
  elsif (MON=10) then days:=31;
  elsif (MON=11) then days:=30;
  elsif (MON=12) then days:=31;
  end if;
  RETURN days;
END GETDAYS;

/
--------------------------------------------------------
--  DDL for Function GETSALARY
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "UNI"."GETSALARY" 
(
  MON IN NUMBER,
  EID IN VARCHAR2,
  SALARY OUT NUMBER 
) RETURN NUMBER AS
X EMPLOYEE.ID %TYPE;
STATUS EMPLOYEE.MARRIED %TYPE;
CHILDCOUNT NUMBER :=0;
vacCount NUMBER :=0;
percentage number :=0;
s number;
d DATE;
startDay number;
endDay number;
--getting the number of bonuses for the specified month
CURSOR cur IS SELECT * FROM BONUS WHERE EMPLOYEEID=EID AND TO_CHAR(DATEOFBONUS,'mm')=MON;
CURSOR vacationCur IS SELECT * FROM VACATION WHERE EMPLOYEEID=EID AND TO_CHAR(DATEBEGIN,'mm')=MON AND TO_CHAR(DATEEND,'mm')=MON;
CURSOR vacationEnd IS SELECT * FROM VACATION WHERE EMPLOYEEID=EID AND TO_CHAR(DATEBEGIN,'mm')=MON;
BEGIN
  --getting base salary, marital status and number of children
  SELECT BASESALARY INTO X FROM EMPLOYEE WHERE ID=EID;
  SELECT MARRIED INTO STATUS FROM EMPLOYEE WHERE ID=EID;
  SELECT COUNT(*) INTO CHILDCOUNT FROM CHILD WHERE EMPLOYEEID=EID AND((MONTHS_BETWEEN(sysdate, DATEOFBIRTH) / 12)<18 OR FINISHEDSTUDIES='N');
  DBMS_OUTPUT.PUT_LINE(X);
  --calculating the number of days with unpaid vacation
  for i in vacationEnd loop
  --getting the the day that the vacation started
  startDay:=EXTRACT(DAY FROM i.datebegin);
  --getting the number of days in a month
  endDay:=GETDAYS(MON);
  --if the vacation type is 2(unpaid) calculate unpaid days
  if(i.TYPE=2) then
  --if the vacation starts in a month and end in another, stop counting at the end of the month
  if((i.dateend-i.datebegin)>(endDay-startDay)) then 
  vaccount:=vaccount + endDay-startDay;
  dbms_output.put_line(endDay-startDay);
  else 
  --else count number of days normally
  vacCount:=vacCount + (i.DATEEND-i.DATEBEGIN);
  end if;
  dbms_output.put_line(vaccount);
  end if;
  end loop;
  --adding bonuses
  for i in cur loop
  X:=X+i.amount;
  end loop;
  IF(STATUS='M') THEN 
    X:=X+1500;
    END IF;
    --adding children bonus
  IF(CHILDCOUNT>0) THEN X:=X+CHILDCOUNT*500; END IF;
  --deducting tax
  if(X>35000) THEN X:=X*0.93;
  ELSE
  X:=X*0.95;
  END IF;
  DBMS_OUTPUT.PUT_LINE(X);
  --calculating the deducted perfcentage based on the number of days with unpaid vacation
  percentage:=ROUND(vaccount*100/30);
  X:=ROUND((1-percentage/100)*X);
  DBMS_OUTPUT.PUT_LINE(X);
  
  SALARY:=X;
  return SALARY;
END GETSALARY;

/
--------------------------------------------------------
--  DDL for Function SALARYSUM
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "UNI"."SALARYSUM" 
(
MON IN NUMBER
) RETURN NUMBER AS 
--returned value the sum of all salaries
salarysum number :=0;
--the value of the salary of a single employee
val number:=0;
cursor cur is select * from employee;
BEGIN
  for i in cur loop
  --getting the salary of an employee in a given month
  val:=GETSALARY(MON, i.ID, val);
  salarysum:=salarysum + val;
  end loop;
  
  DBMS_OUTPUT.PUT_LINE('sum is: ' ||salarysum);
  RETURN salarysum;
END SALARYSUM;

/

--------------------------------------------------------
--  DDL for Procedure ADDEMPLOYEE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."ADDEMPLOYEE" 
(
  FNAME IN VARCHAR2 
, LNAME IN VARCHAR2 
, DOB IN DATE 
, MARRIED IN CHARACTER 
, DTITLE IN VARCHAR2 
, DIPLEVEL IN NUMBER 
, DDATE IN DATE 
) AS
salary number :=0;
eid EMPLOYEE.ID %TYPE;
BEGIN
  IF (DIPLEVEL=1) THEN 
    salary:=30000;
    ELSIF (DIPLEVEL=2) THEN
      salary :=50000; 
    ELSIF (DIPLEVEL=3) THEN
      salary:=80000;
  END IF;
  --INSERTING EMPLOYEE DATA
  INSERT INTO EMPLOYEE(FIRSTNAME,LASTNAME,DATEOFBIRTH,BASESALARY,MARRIED) VALUES (FNAME,LNAME,DOB,salary,MARRIED)
    RETURNING ID into eid;
  --INSERTING DIPLOMA DATA
    INSERT INTO DIPLOMA (TITLE, DLEVEL, DATEOFDIPLOMA, EMPID) VALUES(DTITLE, DIPLEVEL ,DDATE,eid);
END ADDEMPLOYEE;

/
--------------------------------------------------------
--  DDL for Procedure ADDIPLOMA
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."ADDIPLOMA" 
(
  TITLE IN VARCHAR2 
, DLEVEL IN NUMBER 
, DATEOFDIPLOMA IN DATE 
, EMPID IN VARCHAR2
) AS 
BEGIN
    INSERT INTO DIPLOMA(TITLE, DLEVEL, DATEOFDIPLOMA, EMPID) VALUES (TITLE, DLEVEL, DATEOFDIPLOMA, EMPID);
END ADDIPLOMA;

/
--------------------------------------------------------
--  DDL for Procedure ADDVACATION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."ADDVACATION" 
(
  EID IN VARCHAR2 
, BDATE IN DATE 
, EDATE IN DATE 
, BTYPE IN NUMBER 
) AS 
BEGIN
INSERT INTO VACATION(EMPLOYEEID, DATEBEGIN, DATEEND, TYPE) VALUES(EID, BDATE, EDATE, BTYPE);  
END ADDVACATION;

/
--------------------------------------------------------
--  DDL for Procedure DELETEBONUS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."DELETEBONUS" 
(
  BID IN VARCHAR2 
) AS 
BEGIN
  DELETE FROM BONUS WHERE ID = BID;
END DELETEBONUS;

/
--------------------------------------------------------
--  DDL for Procedure DELETECHILD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."DELETECHILD" 
(
  CID IN VARCHAR2 
) AS 
BEGIN
  delete from CHILD where ID = CID;
END DELETECHILD;

/
--------------------------------------------------------
--  DDL for Procedure DELETEDIPLOMA
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."DELETEDIPLOMA" 
(
  DID IN VARCHAR2 
) AS 
BEGIN
  DELETE FROM DIPLOMA WHERE ID = DID;
END DELETEDIPLOMA;

/
--------------------------------------------------------
--  DDL for Procedure DELETEMPLOYEE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."DELETEMPLOYEE" 
(
  EID IN VARCHAR2 
) AS 
BEGIN
  -- delete vacations
  delete from VACATION where EMPLOYEEID = EID;
  -- delete bonuses
  delete from BONUS where EMPLOYEEID = EID;
  -- delete diplomas
  delete from DIPLOMA where EMPID = EID;
  -- delete children 
  delete from CHILD where EMPLOYEEID = EID;
  -- delete me :) 
  delete from EMPLOYEE where ID = EID;
END DELETEMPLOYEE;

/
--------------------------------------------------------
--  DDL for Procedure DELETEVACATION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."DELETEVACATION" 
(
  VID IN VARCHAR2 
) AS 
BEGIN
  DELETE FROM VACATION WHERE ID = VID;
END DELETEVACATION;

/

--------------------------------------------------------
--  DDL for Procedure MODIFYDIPLOMA
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."MODIFYDIPLOMA" 
(
  DIPID IN VARCHAR2 
, TITLE IN VARCHAR2 
, DLEVEL IN NUMBER 
, DATEOFDIPLOMA IN DATE 
) AS 
x DIPLOMA %rowtype;
eTitle DIPLOMA.TITLE %type;
eLevel DIPLOMA.DLEVEL %type;
eDOD DIPLOMA.DATEOFDIPLOMA %type;
BEGIN
   select * into x from DIPLOMA where ID = DIPID;
  if(x.TITLE != TITLE and TITLE is not null) then
      eTitle := TITLE;
  else
    eTitle := x.TITLE;
  end if;
  if(x.DLEVEL != DLEVEL and DLEVEL is not null) then
      eLevel := DLEVEL;
  else
    eLevel := x.DLEVEL;
  end if;
  if(x.DATEOFDIPLOMA != DATEOFDIPLOMA and DATEOFDIPLOMA is not null) then
      eDOD := DATEOFDIPLOMA;
  else
    eDOD := x.DATEOFDIPLOMA;
  end if;
  -- query
  update DIPLOMA
    set TITLE = eTitle,
        DLEVEL = eLevel,
        DATEOFDIPLOMA = eDOD
        where ID = DIPID;
END MODIFYDIPLOMA;

/
--------------------------------------------------------
--  DDL for Procedure MODIFYEMPLOYEE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."MODIFYEMPLOYEE" 
(
  EMPID IN VARCHAR2 
, fNAME IN VARCHAR2 
, lNAME IN VARCHAR2 
, DOB IN DATE 
, empMarried IN VARCHAR2 
) AS 
x EMPLOYEE %rowtype;
efName EMPLOYEE.FIRSTNAME %type;
elNAME EMPLOYEE.LASTNAME %type;
eDOB EMPLOYEE.DATEOFBIRTH %type;
eMarried EMPLOYEE.MARRIED %type;
BEGIN
  select * into x from Employee where ID = EMPID;
  if(x.FIRSTNAME != fName and fName is not null) then
      efName := fName;
  else
    efName := x.FIRSTNAME;
  end if;
  if(x.LASTNAME != lNAME and lNAME is not null) then
      elNAME := lNAME;
  else
    elNAME := x.LASTNAME;
  end if;
  if(x.DATEOFBIRTH != DOB and DOB is not null) then
      eDOB := DOB;
  else
    eDOB := x.DATEOFBIRTH;
  end if;
  if(x.MARRIED != empMarried and empMarried is not null) then
      eMarried := empMarried;
  else
    eMarried := x.MARRIED;
  end if;
  -- query
  update employee
    set FIRSTNAME = efName,
        LASTNAME = elNAME,
        DATEOFBIRTH = eDOB,
        MARRIED = eMarried
        where ID = EMPID;
END MODIFYEMPLOYEE;

/
--------------------------------------------------------
--  DDL for Procedure MODIFYVACATION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UNI"."MODIFYVACATION" 
(
  VID IN VARCHAR2,
  DATEBEGIN IN DATE 
, DATEEND IN DATE 
, VTYPE IN NUMBER 
) AS 
x VACATION %rowtype;
eDATEBEGIN VACATION.DATEBEGIN %type;
eDATEEND VACATION.DATEEND %type;
eVTYPE VACATION.TYPE %type;
BEGIN
 select * into x from VACATION where ID = VID;
  if(x.DATEBEGIN != DATEBEGIN and DATEBEGIN is not null) then
      eDATEBEGIN := DATEBEGIN;
  else
    eDATEBEGIN := x.DATEBEGIN;
  end if;
  if(x.DATEEND != DATEEND and DATEEND is not null) then
      eDATEEND := DATEEND;
  else
    eDATEEND := x.DATEEND;
  end if;
  if(x.TYPE != VTYPE and VTYPE is not null) then
      eVTYPE := VTYPE;
  else
    eVTYPE := x.TYPE;
  end if;
  -- query
  update VACATION
    set DATEBEGIN = eDATEBEGIN,
        DATEEND = eDATEEND,
        TYPE = eVTYPE
        where ID = VID;
END MODIFYVACATION;

/
