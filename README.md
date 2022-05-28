# Library-Control-System
Pre-graduate java program
1.	Introduction
With the popularity of computers and the increase in the level of application, after examining and comparing, I decided to use my knowledge of Java to develop a small library management system to facilitate the management of books. A library management system is typically an information management system. This project uses JAVA and MySQL database to develop this library management system. The system is to solve the problems to be solved by the book management and can meet the basic requirements of book management, including functions such as adding and managing. The system can provide a quick and easy borrowing service for readers according to the needs of the users The following functions should be available in the book management system.

Staff interface
Reader interface
Reader library management
Book library management
Borrowing management
Reader information enquiry
The library management system is mainly for the operation of the library, so the system should try to meet the needs, but at the same time there should not be redundant or complicated functions, so that the operation and function of the system is confusing.
2.	Design
2.1	Follow Chart
The following base flow chart has been designed based on the project requirements.
 
Figure 1 Follow Chart
2.2	Functional analysis
No	Class Name	Function
1	Database	Connect to the database, create the relevant tables and write the relevant call methods.
2	Pop	A simple and easy to call pop-up application to alert the user of success or failure.
3	Log in 	It has four functions: login, logout, password retrieval and registration, and it determines whether an account is a member or an employee based on the account entered, and logs into different screens.
4	New Member	Enter username, password, mobile number, and email, add a new account (only member account), and is automatically given a parity ID
5	New Book	Enter the title, author, publisher, and number of books, store the book data in the database and get the ID automatically.
6	Member list	Display information from the database or all user information except passwords.
7	Book list	Display information about books in the library and staff can update and delete them, member can use search of book name.
8	Setting	All users can change their borrowing times, overtime charges and the initial number of books available for borrowing.
9	Borrowing books	Users can borrow books using their own account, and the administration can borrow books using any account.
10	Returning books	Readers' borrowing information is displayed, and the corresponding book ID is selected for return. If payment is required, only the staff side can return the book, and the staff section can also use the Renew function to refresh the borrowing hours.
11	Staff interface	Has calls for borrowing books, returning books, new users, new books, user list, book list and settings functions.
12	Member interface	Has calls for borrowing books, returning books, book lists and search functions.

3.	Preparation
3.1	Layout
The Src package contains four files

Database for storing database connection operations and methods for adding, deleting, changing, and searching
Pop for storing pop-up method calls
The icons folder is used to store the relevant interface images and button images. 
main for storing all interfaces and calls to the program
Note: main.Main is the only entry point to the program

For ease of programming all GUI interfaces in the overall project are written in fxml files and implemented in classes.
3.2	Database
First create a database named library in your local Mysql program with a username of “root” and a password of “123456”.
In the program create the statements to connect to the database and the methods to add, delete and check.

There are 4 Tables in the database.
 book: id int, title VARCHAR(200),author VARCHAR(200),publisher VARCHAR(200),number int
Used to store all books
member: id int, username VARCHAR(200), mobile VARCHAR(20),email VARCHAR(50),password VARCHAR(20),nBorrowed int, FinePerDay FLOAT
Used to store all existing member information
bookId int, memberId int, borrow_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (bookId) REFERENCES book(id), FOREIGN KEY (memberId) REFERENCES member(id)
Used to store all borrowed data and is associated with the previous book and memeber classes
middle: id int PRIMARY KEY, nBorrowed int, FinePerDay Double,BorrowedDate int 
All transit data, mainly corresponding to the setting function.
