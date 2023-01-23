CREATE TABLE `capstone`.`member` (
                                     `member_id` INT NOT NULL,
                                     `name` VARCHAR(255) NULL,
                                     `email` VARCHAR(255) NULL,
                                     `password` VARCHAR(255) NULL,
                                     PRIMARY KEY (`member_id`),
                                     UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);