DROP TABLE IF EXISTS booklist;



CREATE TABLE booklist (
  isbn VARCHAR(13) PRIMARY KEY,
  title VARCHAR(255),
  authors VARCHAR(255),
  published_date DATE,
  description VARCHAR(1000),
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);





INSERT INTO booklist (isbn, title, authors, published_date, description) VALUES
('9781234567890', 'Book 1', 'Author 1', '2022-01-01', 'Description 1'),
('9782345678901', 'Book 2', 'Author 2', '2022-02-01', 'Description 2'),
('9783456789012', 'Book 3', 'Author 3', '2022-03-01', 'Description 3'),
('9784567890123', 'Book 4', 'Author 4', '2022-04-01', 'Description 4'),
('9785678901234', 'Book 5', 'Author 5', '2022-05-01', 'Description 5'),
('9786789012345', 'Book 6', 'Author 6', '2022-06-01', 'Description 6'),
('9787890123456', 'Book 7', 'Author 7', '2022-07-01', 'Description 7'),
('9788901234567', 'Book 8', 'Author 8', '2022-08-01', 'Description 8'),
('9789012345678', 'Book 9', 'Author 9', '2022-09-01', 'Description 9'),
('9780123456789', 'Book 10', 'Author 10', '2022-10-01', 'Description 10');
