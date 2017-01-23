SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `langreader` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `langreader` ;

-- -----------------------------------------------------
-- Table `langreader`.`USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `langreader`.`USER` ;

CREATE TABLE IF NOT EXISTS `langreader`.`USER` (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NOT NULL,
  `initLevel` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userId`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `userId_UNIQUE` ON `langreader`.`USER` (`userId` ASC);


-- -----------------------------------------------------
-- Table `langreader`.`TEXT_VIEW`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `langreader`.`TEXT_VIEW` ;

CREATE TABLE IF NOT EXISTS `langreader`.`TEXT_VIEW` (
  `textViewId` INT NOT NULL AUTO_INCREMENT,
  `textId` VARCHAR(100) NOT NULL,
  `userId` INT NOT NULL,
  `dateTime` TIMESTAMP NOT NULL,
  PRIMARY KEY (`textViewId`),
  CONSTRAINT `fk_userId`
    FOREIGN KEY (`userId`)
    REFERENCES `langreader`.`USER` (`userId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `userTextId_UNIQUE` ON `langreader`.`TEXT_VIEW` (`textViewId` ASC);

CREATE INDEX `fk_userId_idx` ON `langreader`.`TEXT_VIEW` (`userId` ASC);


-- -----------------------------------------------------
-- Table `langreader`.`UNKNOWN_WORD_OCURRENCE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `langreader`.`UNKNOWN_WORD_OCURRENCE` ;

CREATE TABLE IF NOT EXISTS `langreader`.`UNKNOWN_WORD_OCURRENCE` (
  `occurenceId` INT NOT NULL AUTO_INCREMENT,
  `normalizedForm` VARCHAR(45) NOT NULL,
  `userId` INT NOT NULL,
  `textViewId` INT NOT NULL,
  PRIMARY KEY (`occurenceId`),
  CONSTRAINT `fk_userId2`
    FOREIGN KEY (`userId`)
    REFERENCES `langreader`.`USER` (`userId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_textViewId`
    FOREIGN KEY (`textViewId`)
    REFERENCES `langreader`.`TEXT_VIEW` (`textViewId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `wordId_UNIQUE` ON `langreader`.`UNKNOWN_WORD_OCURRENCE` (`occurenceId` ASC);

CREATE INDEX `fk_userId_idx` ON `langreader`.`UNKNOWN_WORD_OCURRENCE` (`userId` ASC);

CREATE INDEX `fk_textViewId_idx` ON `langreader`.`UNKNOWN_WORD_OCURRENCE` (`textViewId` ASC);


-- -----------------------------------------------------
-- Table `langreader`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `langreader`.`user` ;

CREATE TABLE IF NOT EXISTS `langreader`.`user` (
  `username` VARCHAR(16) NOT NULL,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(32) NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

CREATE USER IF NOT EXISTS 'langreader' IDENTIFIED BY 'langreader';
GRANT ALL ON langreader.* TO 'langreader';

-- Run statement below only for single use mode during development
INSERT INTO USER (userId, NAME, initLevel) VALUES (1, "HLIB", "ADVANCED");
