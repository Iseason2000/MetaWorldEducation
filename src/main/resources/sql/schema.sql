CREATE TABLE IF NOT EXISTS `player_info`
(
    `player_id`      int          NOT NULL AUTO_INCREMENT,
    `usr_name`       varchar(255) NOT NULL,
    `usr_pwd`        varchar(255) NOT NULL,
    `player_name`    varchar(255) NULL,
    `update_time`    datetime     NULL,
    `pos_x`          float        NULL,
    `pos_y`          float        NULL,
    `pos_z`          float        NULL,
    `roa_x`          float        NULL,
    `roa_y`          float        NULL,
    `roa_z`          float        NULL,
    `move_speed`     float        NULL,
    `rotate_speed`   float        NULL,
    `is_running`     int          NULL,
    `is_talking`     int          NULL,
    `talk_msg`       text         NULL,
    `scene_id`       int          NULL,
    `activity_id`    int          NULL,
    `identity_name`  int          NULL,
    `hair_type`      int          NULL,
    `face_type`      int          NULL,
    `hair_color`     int          NULL,
    `face_color`     int          NULL,
    `hobby`          varchar(255) NULL,
    `disposition`    varchar(255) NULL,
    `occupation`     varchar(255) NULL,
    `story`          varchar(255) NULL,
    `blouse_index`   int          NULL,
    `trousers_index` int          NULL,
    `shoe_index`     int          NULL,
    PRIMARY KEY (`player_id`),
    UNIQUE INDEX `register` (`usr_name`),
    INDEX `query` (`player_id`, `usr_name`, `scene_id`, `activity_id`)
);

CREATE TABLE IF NOT EXISTS `user_friend_info`
(
    `uf_id`     int NOT NULL AUTO_INCREMENT,
    `user_id`   int NOT NULL,
    `friend_id` int NOT NULL,
    PRIMARY KEY (`uf_id`),
    UNIQUE INDEX `friend` (`user_id`, `friend_id`)
);

CREATE TABLE IF NOT EXISTS `apply_record`
(
    `apply_id`   int      NOT NULL AUTO_INCREMENT,
    `applyer`    int      NOT NULL,
    `receiver`   int      NOT NULL,
    `apply_time` datetime NOT NULL,
    `result`     int      NOT NULL,
    PRIMARY KEY (`apply_id`)
);

CREATE TABLE IF NOT EXISTS `activity_info`
(
    `activity_id`         int          NOT NULL,
    `player_id`           int          NOT NULL,
    `scene_id`            int          NOT NULL,
    `player_name`         varchar(255) NOT NULL,
    `activity_name`       varchar(255) NOT NULL,
    `create_time`         datetime     NOT NULL,
    `start_time`          datetime     NOT NULL,
    `end_time`            datetime     NOT NULL,
    `activity_type`       int          NOT NULL,
    `activity_permission` int          NOT NULL,
    `activity_password`   varchar(255) NOT NULL,
    `current_player`      int          NULL,
    `max_player`          int          NULL,
    PRIMARY KEY (`activity_id`)
);

CREATE TABLE IF NOT EXISTS `msg_record`
(
    `msg_id`      int      NOT NULL AUTO_INCREMENT,
    `sender_id`   int      NOT NULL,
    `receive_id`  int      NOT NULL,
    `scene_id`    int      NULL,
    `activity_id` int      NULL,
    `create_time` datetime NOT NULL,
    `msg`         text     NOT NULL,
    `is_read`     int      NOT NULL,
    PRIMARY KEY (`msg_id`),
    INDEX `index_all` (`sender_id`, `receive_id`, `scene_id`, `activity_id`),
    CONSTRAINT `sender` FOREIGN KEY (`sender_id`) REFERENCES `player_info` (`player_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `receiver` FOREIGN KEY (`receive_id`) REFERENCES `player_info` (`player_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `activity` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE `activity_player`
(
    `ap_id`       int NOT NULL,
    `activity_id` int NOT NULL,
    `player_id`   int NOT NULL,
    PRIMARY KEY (`ap_id`),
    UNIQUE INDEX `all` (`ap_id`, `activity_id`, `player_id`),
    CONSTRAINT `activity` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `player` FOREIGN KEY (`player_id`) REFERENCES `player_info` (`player_id`) ON DELETE CASCADE ON UPDATE NO ACTION
);