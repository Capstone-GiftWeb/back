CREATE TABLE `capstone`.`preference` (
                                     `member_id` INT NOT NULL,
                                     `category_id` INT NOT NULL,
                                     `prev_score` INT NOT NULL,
                                     `likes` INT NOT NULL,
                                     `clicks` INT NOT NULL,
                                     `pref_score` INT NOT NULL,
                                     PRIMARY KEY (`member_id`, `category_id`)
                                     );