SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS "mydb" DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE mydb;

DROP TABLE IF EXISTS `Users`;
CREATE TABLE "Users" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "username" varchar(50) NOT NULL,
  "email" varchar(50) NOT NULL,
  "password" varchar(50) NOT NULL,
  "birthday" date NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `Posts`;
CREATE TABLE "Posts" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "userid" int(11) NOT NULL,
  "title" varchar(100) NOT NULL,
  "body" varchar(5000) NOT NULL,
  "date" datetime NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (userid) REFERENCES Users(id)
);

DROP TABLE IF EXISTS `Comments`;
CREATE TABLE "Comments" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "postid" int(11) NOT NULL,
  "userid" int(11) NOT NULL,
  "comment" varchar(500) NOT NULL,
  "date" datetime NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (userid) REFERENCES Users(id),
  FOREIGN KEY (postid) REFERENCES Posts(id)
);

INSERT INTO `Users` (`id`, `username`, `email`, `password`, `birthday`) VALUES
(1, 'ieg', 'ieg@gmail.com', 'f447b20a7fcbf53a5d5be013ea0b15af', '1995-06-01'),
(2, 'asd', 'asd@gmail.com', 'f447b20a7fcbf53a5d5be013ea0b15af', '1995-12-04');

INSERT INTO `Posts` (`id`, `userid`, `title`, `body`, `date`) VALUES
(1, 1, 'merhaba', 'ilk yazim', '2018-11-01 05:14:14');

INSERT INTO `Comments` (`id`, `postid`, `userid`, `comment`, `date`) VALUES
(1, 1, 2, 'güzel', '2018-11-03 05:18:14');


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
