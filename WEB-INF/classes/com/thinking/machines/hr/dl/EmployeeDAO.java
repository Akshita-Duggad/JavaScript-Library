package com.thinking.machines.hr.dl;
import java.sql.*;
import java.util.*;
import java.math.*;
public class EmployeeDAO
{
public List<EmployeeDTO> getAll() throws DAOException
{
List<EmployeeDTO> employees;
employees=new LinkedList<>();
try
{
Connection connection=DAOConnection.getConnection();
Statement statement;
statement=connection.createStatement();
ResultSet resultSet;
resultSet=statement.executeQuery("select employee.id,employee.name,employee.designation_code,designation.title,employee.date_of_birth,employee.gender,employee.is_indian,employee.basic_salary,employee.pan_number,employee.aadhar_card_number from employee inner join designation on employee.designation_code=designation.code order by employee.name");
EmployeeDTO employeeDTO;
int id;
String name;
int designationCode;
String title;
java.util.Date dateOfBirth;
String gender;
boolean isIndian;
BigDecimal basicSalary;
String panNumber;
String aadharCardNumber;
while(resultSet.next())
{
id=resultSet.getInt("id");
name=resultSet.getString("name").trim();
designationCode=resultSet.getInt("designation_code");
title=resultSet.getString("title").trim();
dateOfBirth=resultSet.getDate("date_of_birth");
gender=resultSet.getString("gender");
isIndian=resultSet.getBoolean("is_indian");
basicSalary=resultSet.getBigDecimal("basic_salary");
panNumber=resultSet.getString("pan_number").trim();
aadharCardNumber=resultSet.getString("aadhar_card_number").trim();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+id);
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setDesignation(title);
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setGender(gender);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);

employees.add(employeeDTO);
}
resultSet.close();
statement.close();
connection.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
return employees;
}
public void add(EmployeeDTO employeeDTO) throws DAOException
{
String name=employeeDTO.getName();
int designationCode=employeeDTO.getDesignationCode();
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
String gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
String panNumber=employeeDTO.getPANNumber();
String aadharCardNumber=employeeDTO.getAadharCardNumber();
try
{
Connection connection;
connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select id from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet;
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==true)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException(panNumber+" already exists");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select id from employee where aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==true)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException(aadharCardNumber+" already exists");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("insert into employee (name,designation_code,date_of_birth,gender,is_indian,basic_salary,pan_number,aadhar_card_number) values (?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date date=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,date);
preparedStatement.setString(4,gender);
preparedStatement.setBoolean(5,isIndian);
preparedStatement.setBigDecimal(6,basicSalary);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.executeUpdate();
resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
int id=resultSet.getInt(1);
employeeDTO.setEmployeeId("A"+id);
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqle)
{
throw new DAOException(sqle.getMessage());
}
}
public void update(EmployeeDTO employeeDTO) throws DAOException
{
try
{
String employeeId=employeeDTO.getEmployeeId();
int actualEmployeeId=0;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1));
}catch(Exception exception)
{
throw new DAOException("Invalid employee id : "+employeeId);
}
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select gender from employee where id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet;
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid employee id : "+employeeId);
}
resultSet.close();
preparedStatement.close();
String name=employeeDTO.getName();
int designationCode=employeeDTO.getDesignationCode();
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
String gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
String panNumber=employeeDTO.getPANNumber();
String aadharCardNumber=employeeDTO.getAadharCardNumber();
preparedStatement=connection.prepareStatement("select id from employee where pan_number=? and id!=?");
preparedStatement.setString(1,panNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==true)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException(panNumber+" already exists");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select id from employee where aadhar_card_number=? and id!=?");
preparedStatement.setString(1,aadharCardNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==true)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException(aadharCardNumber+" already exists");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("update employee set name=?,designation_code=?,date_of_birth=?,gender=?,is_indian=?,basic_salary=?,pan_number=?,aadhar_card_number=? where id=?");
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date date=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,date);
preparedStatement.setString(4,gender);
preparedStatement.setBoolean(5,isIndian);
preparedStatement.setBigDecimal(6,basicSalary);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.setInt(9,actualEmployeeId);

preparedStatement.executeUpdate();
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqle)
{
throw new DAOException(sqle.getMessage());
}
}
public boolean panNumberExists(String panNumber) throws DAOException
{
boolean exists=false;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet=preparedStatement.executeQuery();
exists=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return exists;
}

public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
{
boolean exists=false;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select gender from employee where aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
ResultSet resultSet=preparedStatement.executeQuery();
exists=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return exists;
}
public void deleteByEmployeeId(String employeeId) throws DAOException
{
int actualEmployeeId=0;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1));
}catch(Exception exception)
{
throw new DAOException("Invalid employee id : "+employeeId);
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select gender from employee where id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Employee Id : "+employeeId+" doesnot exists");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("delete from employee where id=?");
preparedStatement.setInt(1,actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public EmployeeDTO getByEmployeeId(String employeeId) throws DAOException
{
EmployeeDTO employeeDTO=null;
int actualEmployeeId=0;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1));
}catch(Exception exception)
{
throw new DAOException("Invalid employee id : "+employeeId);
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select employee.id,employee.name,employee.designation_code,designation.title,employee.date_of_birth,employee.gender,employee.is_indian,employee.basic_salary,employee.pan_number,employee.aadhar_card_number from employee inner join designation on employee.designation_code=designation.code and id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Employee Id : "+employeeId+" doesnot exists");
}
int id;
String name;
int designationCode;
String title;
java.util.Date dateOfBirth;
String gender;
boolean isIndian;
BigDecimal basicSalary;
String panNumber;
String aadharCardNumber;
id=resultSet.getInt("id");
name=resultSet.getString("name").trim();
designationCode=resultSet.getInt("designation_code");
title=resultSet.getString("title").trim();
dateOfBirth=resultSet.getDate("date_of_birth");
gender=resultSet.getString("gender");
isIndian=resultSet.getBoolean("is_indian");
basicSalary=resultSet.getBigDecimal("basic_salary");
panNumber=resultSet.getString("pan_number").trim();
aadharCardNumber=resultSet.getString("aadhar_card_number").trim();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+id);
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setDesignation(title);
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setGender(gender);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);


resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employeeDTO;
}

public boolean employeeIdExists(String employeeId) throws DAOException
{
boolean exists=false;
int actualEmployeeId=0;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1));
}catch(Exception exception)
{
return false;
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select gender from employee where id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
exists=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return exists;
}

public EmployeeDTO getByPANNumber(String panNumber) throws DAOException
{
EmployeeDTO employeeDTO=null;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select employee.id,employee.name,employee.designation_code,designation.title,employee.date_of_birth,employee.gender,employee.is_indian,employee.basic_salary,employee.pan_number,employee.aadhar_card_number from employee inner join designation on employee.designation_code=designation.code and pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("PAN Number : "+panNumber+" doesnot exists");
}
int id;
String name;
int designationCode;
String title;
java.util.Date dateOfBirth;
String gender;
boolean isIndian;
BigDecimal basicSalary;
String aadharCardNumber;
id=resultSet.getInt("id");
name=resultSet.getString("name").trim();
designationCode=resultSet.getInt("designation_code");
title=resultSet.getString("title").trim();
dateOfBirth=resultSet.getDate("date_of_birth");
gender=resultSet.getString("gender");
isIndian=resultSet.getBoolean("is_indian");
basicSalary=resultSet.getBigDecimal("basic_salary");
panNumber=resultSet.getString("pan_number").trim();
aadharCardNumber=resultSet.getString("aadhar_card_number").trim();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+id);
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setDesignation(title);
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setGender(gender);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);


resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employeeDTO;
}
public EmployeeDTO getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
EmployeeDTO employeeDTO=null;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select employee.id,employee.name,employee.designation_code,designation.title,employee.date_of_birth,employee.gender,employee.is_indian,employee.basic_salary,employee.pan_number,employee.aadhar_card_number from employee inner join designation on employee.designation_code=designation.code and aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Aadhar Card Number : "+aadharCardNumber+" doesnot exists");
}
int id;
String name;
int designationCode;
String title;
java.util.Date dateOfBirth;
String gender;
boolean isIndian;
BigDecimal basicSalary;
String panNumber;
id=resultSet.getInt("id");
name=resultSet.getString("name").trim();
designationCode=resultSet.getInt("designation_code");
title=resultSet.getString("title").trim();
dateOfBirth=resultSet.getDate("date_of_birth");
gender=resultSet.getString("gender");
isIndian=resultSet.getBoolean("is_indian");
basicSalary=resultSet.getBigDecimal("basic_salary");
panNumber=resultSet.getString("pan_number").trim();
aadharCardNumber=resultSet.getString("aadhar_card_number").trim();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+id);
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setDesignation(title);
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setGender(gender);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employeeDTO;
}

}